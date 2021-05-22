package com.filippomortari.twitterclonebackend.repository;

import com.filippomortari.twitterclonebackend.domain.entity.Tweet;
import com.filippomortari.twitterclonebackend.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TweetRepository extends PagingAndSortingRepository<Tweet, UUID> {

    @Query(
            value = "SELECT t FROM tweet t INNER JOIN fetch t.author a WHERE t.author <> :user ORDER BY t.createdDate DESC",
            countQuery = "SELECT count(*) FROM tweet t WHERE t.author <> :user"
    )
    Page<Tweet> getTimelineFor(User user, Pageable pageable);

    @Query(value = "SELECT t FROM tweet t INNER JOIN fetch t.author a WHERE t.author = :user ORDER BY t.createdDate DESC",
            countQuery = "SELECT count(*) FROM tweet t WHERE t.author = :user")
    Page<Tweet> getUserProfile(User user, Pageable pageable);

}
