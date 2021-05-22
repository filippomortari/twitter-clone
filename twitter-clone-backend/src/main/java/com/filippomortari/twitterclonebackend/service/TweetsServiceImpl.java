package com.filippomortari.twitterclonebackend.service;

import com.filippomortari.twitterclonebackend.domain.Tweet;
import com.filippomortari.twitterclonebackend.domain.User;
import com.filippomortari.twitterclonebackend.repository.TweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TweetsServiceImpl implements TweetsService {
    private final TweetRepository tweetRepository;
    @Override
    public Page<Tweet> getTimelineFor(User user, Pageable pageable) {
        return tweetRepository.getTimelineFor(user, pageable);
    }

    @Override
    public Page<Tweet> getUserProfile(User user, Pageable pageable) {
        return tweetRepository.getUserProfile(user, pageable);
    }
}
