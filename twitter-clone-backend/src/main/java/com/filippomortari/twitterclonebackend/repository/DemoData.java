package com.filippomortari.twitterclonebackend.repository;

import com.filippomortari.twitterclonebackend.domain.entity.Tweet;
import com.filippomortari.twitterclonebackend.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@Component
@RequiredArgsConstructor
public class DemoData {

    @Autowired
    private final TweetRepository tweetRepository;
    @Autowired
    private final UserRepository userRepository;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
//        User user1 = User
//                .builder()
//                .username("user1")
//                .firstName("Albert")
//                .lastName("Einstein")
//                .build();
//
//        User user2 = User
//                .builder()
//                .username("user2")
//                .firstName("Marie")
//                .lastName("Curie")
//                .build();
//
//        User user3 = User
//                .builder()
//                .username("user3")
//                .firstName("Sigmund")
//                .lastName("Freud")
//                .build();
//
//        User user4 = User
//                .builder()
//                .username("user4")
//                .firstName("Max")
//                .lastName("Planck")
//                .build();
//
//        User user5 = User
//                .builder()
//                .username("user5")
//                .firstName("Niels")
//                .lastName("Bohr")
//                .build();
//
//        userRepository.save(user1);
//        userRepository.save(user2);
//        userRepository.save(user3);
//        userRepository.save(user4);
//        userRepository.save(user5);

        Optional<User> user1 = userRepository.findById("user1");
        Optional<User> user2 = userRepository.findById("user2");

        Tweet tweet = Tweet
                .builder()
                .author(user1.get())
                .content("Lorem ipsum dolor sit amet")
                .build();
        tweetRepository.save(tweet);


        Tweet tweet3 = Tweet
                .builder()
                .author(user1.get())
                .content("In ritardoooooooo")
                .build();
        tweetRepository.save(tweet3);


        Tweet tweet2 = Tweet
                .builder()
                .author(user2.get())
                .content("djfkah fdkjj dfahjkda fksdj ahsdk ")
                .build();

        tweetRepository.save(tweet2);

        User genUser = User
                .builder()
                .username("user1")
                .build();

        System.out.println(tweetRepository.getUserProfile(genUser, null).getContent());
        System.out.println(tweetRepository.getTimelineFor(genUser, null).getContent());
    }
}
