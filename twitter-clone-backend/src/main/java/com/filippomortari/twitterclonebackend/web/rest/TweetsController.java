package com.filippomortari.twitterclonebackend.web.rest;

import com.filippomortari.twitterclonebackend.domain.entity.Tweet;
import com.filippomortari.twitterclonebackend.domain.TweetRequest;
import com.filippomortari.twitterclonebackend.service.TweetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetsController {

    private final TweetsService tweetsService;

    @GetMapping(value = "/timeline")
    public Page<Tweet> timeline(Pageable pageable) {
        final String username = findUsername();
        return tweetsService.getTimelineFor(username, pageable);
    }

    @GetMapping(value = "/profile")
    public Page<Tweet> profile(Pageable pageable) {
        final String username = findUsername();
        return tweetsService.getUserProfile(username, pageable);
    }

    @PostMapping
    public Tweet post(@Valid @RequestBody TweetRequest tweetRequest) {
        final String username = findUsername();
        return tweetsService.create(username, tweetRequest);
    }

    @DeleteMapping
    public ResponseEntity deleteAll() {
        tweetsService.deleteAll();
        return ResponseEntity.ok().build();
    }

    private String findUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
