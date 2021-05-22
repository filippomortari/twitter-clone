package com.filippomortari.twitterclonebackend.web.rest;

import com.filippomortari.twitterclonebackend.domain.Tweet;
import com.filippomortari.twitterclonebackend.domain.User;
import com.filippomortari.twitterclonebackend.service.TweetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetsController {

    private final TweetsService tweetsService;

    @GetMapping(value = "/timeline")
    public Page<Tweet> timeline(String user, Pageable pageable) {
        User.builder().username("user1").build();
        return tweetsService.getTimelineFor(User.builder().username("user1").build(), pageable);
    }

    @GetMapping(value = "/profile")
    public Page<Tweet> profile(String user, Pageable pageable) {
        User.builder().username("user1").build();
        return tweetsService.getUserProfile(User.builder().username("user1").build(), pageable);
    }
}
