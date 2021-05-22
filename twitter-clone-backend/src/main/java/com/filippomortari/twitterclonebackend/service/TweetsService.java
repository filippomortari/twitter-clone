package com.filippomortari.twitterclonebackend.service;

import com.filippomortari.twitterclonebackend.domain.Tweet;
import com.filippomortari.twitterclonebackend.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TweetsService {
    Page<Tweet> getTimelineFor(User user, Pageable pageable);

    Page<Tweet> getUserProfile(User user, Pageable pageable);
}
