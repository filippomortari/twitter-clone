package com.filippomortari.twitterclonebackend.service;

import com.filippomortari.twitterclonebackend.domain.TweetRequest;
import com.filippomortari.twitterclonebackend.domain.entity.Tweet;
import com.filippomortari.twitterclonebackend.domain.entity.User;
import com.filippomortari.twitterclonebackend.repository.TweetRepository;
import com.filippomortari.twitterclonebackend.repository.UserRepository;
import com.filippomortari.twitterclonebackend.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TweetsServiceImpl implements TweetsService {
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Override
    public Page<Tweet> getTimelineFor(String username, Pageable pageable) {
        final User user = User.builder().username(username).build();
        return tweetRepository.getTimelineFor(user, pageable);
    }

    @Override
    public Page<Tweet> getUserProfile(String username, Pageable pageable) {
        final User user = User.builder().username(username).build();
        return tweetRepository.getUserProfile(user, pageable);
    }

    @Override
    public Tweet create(String username, TweetRequest tweetRequest) {
        Optional<User> optionalUser = userRepository.findById(username);
        return optionalUser
                .map(user -> {
                    final Tweet tweet = Tweet
                            .builder()
                            .author(user)
                            .content(tweetRequest.getContent())
                            .build();
                    return tweetRepository.save(tweet);
                })
                .orElseThrow(() ->
                        new UserNotFoundException(String.format("Unable to retrieve user %s", username))
        );
    }

    @Override
    public void deleteAll() {
        tweetRepository.deleteAll();
    }
}
