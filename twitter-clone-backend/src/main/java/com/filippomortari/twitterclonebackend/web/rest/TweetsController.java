package com.filippomortari.twitterclonebackend.web.rest;

import com.filippomortari.twitterclonebackend.domain.entity.Tweet;
import com.filippomortari.twitterclonebackend.domain.entity.TweetRequest;
import com.filippomortari.twitterclonebackend.service.TweetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetsController {

    private final TweetsService tweetsService;

    @GetMapping(value = "/timeline")
    public Page<Tweet> timeline(Pageable pageable) {
        String user1 = "user1";
        return tweetsService.getTimelineFor(user1, pageable);
    }

    @GetMapping(value = "/profile")
    public Page<Tweet> profile(Pageable pageable) {
        String user1 = "user1";
        return tweetsService.getUserProfile(user1, pageable);
    }

    @PostMapping
    public Tweet post(@Valid @RequestBody TweetRequest tweetRequest) {
        String user1 = "user1";
        return tweetsService.create(user1, tweetRequest);
    }

}
