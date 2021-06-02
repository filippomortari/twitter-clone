package com.filippomortari.twitterclonebackend.integration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.filippomortari.twitterclonebackend.domain.TweetRequest;
import com.filippomortari.twitterclonebackend.domain.entity.Tweet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import java.net.URI;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Sql(scripts = {"classpath:timeline.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TweetsControllerIT {

    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.5")
            .withDatabaseName("postgres")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        r.add("spring.datasource.username", postgreSQLContainer::getUsername);
        r.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    void postTweet_typical() {
        // GIVEN, WHEN
        String username = "user1";
        String content = "Lorem ipsum dolor sit amet";
        URI uri = URI.create(format("http://localhost:%d/tweets", serverPort));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", generateJwtForUser(username));

        Tweet tweet = restTemplate
                .exchange(uri,
                        HttpMethod.POST,
                        new HttpEntity<>(TweetRequest
                                .builder()
                                .content(content)
                                .build(),
                                headers
                        ),
                        new ParameterizedTypeReference<Tweet>() {
                        }
                ).getBody();

        // THEN
        assertThat(tweet, is(not(nullValue())));
        assertThat(tweet.getCreatedDate(), is(not(nullValue())));
        assertThat(tweet.getAuthor(), is(not(nullValue())));
        assertThat(tweet.getAuthor().getUsername(), is("user1"));
        assertThat(tweet.getAuthor().getFirstName(), is("Albert"));
        assertThat(tweet.getAuthor().getLastName(), is("Einstein"));
        assertThat(tweet.getContent(), is(content));
    }

    @Test
    void postTweet_length_greater_than_160() {
        // GIVEN, WHEN
        String username = "user1";
        String content = RandomStringUtils.randomAlphanumeric(180);
        URI uri = URI.create(format("http://localhost:%d/tweets", serverPort));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", generateJwtForUser(username));

        ResponseEntity<String> exchange = restTemplate
                .exchange(uri,
                        HttpMethod.POST,
                        new HttpEntity<>(TweetRequest
                                .builder()
                                .content(content)
                                .build(),
                                headers
                        ),
                        new ParameterizedTypeReference<>() {}
                );

        // THEN
        assertThat(exchange.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(exchange.getBody(), containsString("A tweet is 160 characters max"));
    }

    @Test
    void postTweet_user_not_found() {
        // GIVEN, WHEN
        String username = "user1000";
        String content = "Lorem ipsum dolor sit amet";
        URI uri = URI.create(format("http://localhost:%d/tweets", serverPort));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", generateJwtForUser(username));

        ResponseEntity<String> exchange = restTemplate
                .exchange(uri,
                        HttpMethod.POST,
                        new HttpEntity<>(TweetRequest
                                .builder()
                                .content(content)
                                .build(),
                                headers
                        ),
                        new ParameterizedTypeReference<>() {}
                );

        // THEN
        assertThat(exchange.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }


    @Test
    void getTimeline_for_user1() {
        // GIVEN, WHEN
        String username = "user1";
        URI uri = URI.create(format("http://localhost:%d/tweets/timeline", serverPort));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", generateJwtForUser(username));

        ResponseEntity<HelperPage<Tweet>> exchange = restTemplate
                .exchange(uri,
                        HttpMethod.GET,
                        new HttpEntity<>(
                                null,
                                headers
                        ),
                        new ParameterizedTypeReference<>() {}
                );

        // THEN
        assertThat(exchange.getStatusCode(), is(HttpStatus.OK));
        assertThat(exchange.getBody(), is(not(nullValue())));
        assertThat(exchange.getBody().getNumberOfElements(), is(4));
        // SORTED BY DATE DESC (latest first - see timeline.sql)
        assertThat(exchange.getBody().getContent().get(0).getAuthor().getUsername(), is("user5"));
        assertThat(exchange.getBody().getContent().get(1).getAuthor().getUsername(), is("user4"));
        assertThat(exchange.getBody().getContent().get(2).getAuthor().getUsername(), is("user3"));
        assertThat(exchange.getBody().getContent().get(3).getAuthor().getUsername(), is("user2"));
    }

    @Test
    void getProfile_for_user1() {
        // GIVEN, WHEN
        String username = "user1";
        URI uri = URI.create(format("http://localhost:%d/tweets/profile", serverPort));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", generateJwtForUser(username));

        ResponseEntity<HelperPage<Tweet>> exchange = restTemplate
                .exchange(uri,
                        HttpMethod.GET,
                        new HttpEntity<>(
                                null,
                                headers
                        ),
                        new ParameterizedTypeReference<>() {}
                );

        // THEN
        assertThat(exchange.getStatusCode(), is(HttpStatus.OK));
        assertThat(exchange.getBody(), is(not(nullValue())));
        assertThat(exchange.getBody().getNumberOfElements(), is(1));
        assertThat(exchange.getBody().getContent().get(0).getAuthor().getUsername(), is("user1"));
        assertThat(exchange.getBody().getContent().get(0).getContent(), is("Lorem ipsum dolor sit amet, consectetur adipiscing elit."));
    }

    @ParameterizedTest
    @ValueSource(strings = {"POST,/", "GET,/profile", "GET,/timeline"})
    void unauthanticated_user(String httpCall) {
        // GIVEN, WHEN
        String[] httpInputs = httpCall.split(",");
        URI uri = URI.create(format("http://localhost:%d/tweets%s", serverPort, httpInputs[1]));
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<String> exchange = restTemplate
                .exchange(uri,
                        HttpMethod.valueOf(httpInputs[0]),
                        new HttpEntity<>(
                                null,
                                headers
                        ),
                        new ParameterizedTypeReference<>() {}
                );

        // THEN
        assertThat(exchange.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @ParameterizedTest
    @ValueSource(strings = {"POST,/", "GET,/profile", "GET,/timeline"})
    void unparsable_token(String httpCall) {
        // GIVEN, WHEN
        String[] httpInputs = httpCall.split(",");
        URI uri = URI.create(format("http://localhost:%d/tweets%s", serverPort, httpInputs[1]));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer foo.bar");

        ResponseEntity<String> exchange = restTemplate
                .exchange(uri,
                        HttpMethod.valueOf(httpInputs[0]),
                        new HttpEntity<>(
                                null,
                                headers
                        ),
                        new ParameterizedTypeReference<>() {}
                );

        // THEN
        assertThat(exchange.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    private String generateJwtForUser(String username) {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        return String.format("Bearer %s",
                JWT.create()
                        .withIssuer("auth0")
                        .withClaim("username", username)
                        .sign(algorithm))
                ;
    }
}
