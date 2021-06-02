package com.filippomortari.twitterclonebackend.repository;

import com.filippomortari.twitterclonebackend.domain.entity.Tweet;
import com.filippomortari.twitterclonebackend.domain.entity.User;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TweetRepositoryIT {

    @LocalServerPort
    private int serverPort;

    @Autowired
    private TweetRepository underTest;
    @Autowired
    private UserRepository userRepository;

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
    @Sql(scripts = {"classpath:profile.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUserProfile() {
        // GIVEN
        Optional<User> optionalUser = userRepository.findById("user1");
        assertThat(optionalUser.isPresent(), is(true));

        final User user1 = optionalUser.get();

        // WHEN
        Page<Tweet> userProfile = underTest.getUserProfile(user1, null);

        // THEN
        assertThat(userProfile.getNumberOfElements(), is(5));
        assertThat(userProfile.get().allMatch(tweet -> tweet.getAuthor().equals(user1)), is(true));


        List<Tweet> sortedByDateDesc = userProfile
                .get()
                .sorted(Comparator.comparing(Tweet::getCreatedDate).reversed())
                .collect(Collectors.toList());

        // TWEETS ARE ORDERED BY POSTING DATE DESC
        assertThat(userProfile.getContent(), is(equalTo(sortedByDateDesc)));
    }

    @Test
    @Sql(scripts = {"classpath:profile.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUserProfile_equality_based_on_entity_identity() {
        // GIVEN
        final User user1 = User.builder().username("user1").build();

        // WHEN
        Page<Tweet> userProfile = underTest.getUserProfile(user1, null);

        // THEN
        assertThat(userProfile.getNumberOfElements(), is(5));
    }

    @Test
    @Sql(scripts = {"classpath:timeline.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUserTimeline() {
        // GIVEN
        Optional<User> optionalUser = userRepository.findById("user1");
        assertThat(optionalUser.isPresent(), is(true));

        final User user1 = optionalUser.get();

        // WHEN
        Page<Tweet> userTimeline = underTest.getTimelineFor(user1, null);

        // THEN
        assertThat(userTimeline.getNumberOfElements(), is(4));

        assertThat(userTimeline.getContent().get(0).getAuthor().getUsername(), is("user5"));
        assertThat(userTimeline.getContent().get(1).getAuthor().getUsername(), is("user4"));
        assertThat(userTimeline.getContent().get(2).getAuthor().getUsername(), is("user3"));
        assertThat(userTimeline.getContent().get(3).getAuthor().getUsername(), is("user2"));

        List<Tweet> sortedByDateDesc = userTimeline
                .get()
                .sorted(Comparator.comparing(Tweet::getCreatedDate).reversed())
                .collect(Collectors.toList());

        // TWEETS ARE ORDERED BY POSTING DATE DESC
        assertThat(userTimeline.getContent(), is(equalTo(sortedByDateDesc)));
    }
}