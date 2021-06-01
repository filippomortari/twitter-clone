package com.filippomortari.twitterclonebackend.service;

import com.filippomortari.twitterclonebackend.domain.TweetRequest;
import com.filippomortari.twitterclonebackend.domain.entity.Tweet;
import com.filippomortari.twitterclonebackend.domain.entity.User;
import com.filippomortari.twitterclonebackend.repository.TweetRepository;
import com.filippomortari.twitterclonebackend.repository.UserRepository;
import com.filippomortari.twitterclonebackend.service.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TweetsServiceImplTest {
    private TweetsService underTest;
    @Mock
    private TweetRepository tweetRepository;
    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setUnderTest() {
        underTest = new TweetsServiceImpl(tweetRepository, userRepository);
    }

    @Test
    void shouldProduceTimelineForUser() {
        // GIVEN
        Page<Tweet> result = new PageImpl<>(Collections.emptyList());
        given(tweetRepository.getTimelineFor(any(), any())).willReturn(result);

        // WHEN
        final String username = "username";
        final Page<Tweet> timelineFor = underTest.getTimelineFor(username, null);

        // THEN
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(tweetRepository, times(1)).getTimelineFor(captor.capture(), any());
        assertThat(captor.getValue().getUsername(), is(username));
        assertThat(timelineFor, is(equalTo(result)));
    }

    @Test
    void shouldProduceProfileForUser() {
        // GIVEN
        Page<Tweet> result = new PageImpl<>(Collections.emptyList());
        given(tweetRepository.getUserProfile(any(), any())).willReturn(result);

        // WHEN
        final String username = "username";
        final Page<Tweet> timelineFor = underTest.getUserProfile(username, null);

        // THEN
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(tweetRepository, times(1)).getUserProfile(captor.capture(), any());
        assertThat(captor.getValue().getUsername(), is(username));
        assertThat(timelineFor, is(equalTo(result)));
    }

    @Test
    void shouldCreateTweet() {
        // GIVEN
        final User user = User.builder().build();
        given(userRepository.findById(anyString())).willReturn(Optional.of(user));
        doAnswer(returnsFirstArg()).when(tweetRepository).save(any());

        // WHEN
        final String username = "username";
        final TweetRequest tweetRequest = TweetRequest.builder().content("lorem ipsum dolor sit amet").build();
        final Tweet tweet = underTest.create(username, tweetRequest);

        // THEN
        assertThat(tweet.getAuthor(), is(user));
        assertThat(tweet.getContent(), is(tweetRequest.getContent()));
        verify(tweetRepository, times(1)).save(any());
        verify(userRepository, times(1)).findById(eq(username));

    }

    @Test
    void shouldThrowWhenUserNotFound() {
        // GIVEN
        given(userRepository.findById(anyString())).willReturn(Optional.empty());

        // WHEN
        final String username = "username";
        final TweetRequest tweetRequest = TweetRequest.builder().content("lorem ipsum dolor sit amet").build();

        // THEN
        Exception exception = assertThrows(UserNotFoundException.class, () ->
                underTest.create(username, tweetRequest));

        assertThat(exception.getMessage(), is("Unable to retrieve user username"));
    }

    @Test
    void shouldDeleteAll() {
        // GIVEN, WHEN
        underTest.deleteAll();

        // THEN
        verify(tweetRepository, times(1)).deleteAll();
    }
}