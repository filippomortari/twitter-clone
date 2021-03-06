package com.filippomortari.twitterclonebackend.domain;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TweetRequest {

    @NotEmpty
    @Size(message = "A tweet is 160 characters max", max = 160)
    private String content;
}
