package com.filippomortari.twitterclonebackend.service;

import com.filippomortari.twitterclonebackend.domain.entity.Tweet;
import com.filippomortari.twitterclonebackend.domain.TweetRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TweetsService {
    Page<Tweet> getTimelineFor(String username, Pageable pageable);

    Page<Tweet> getUserProfile(String username, Pageable pageable);

    Tweet create(String username, TweetRequest tweetRequest);

    void deleteAll();
}
