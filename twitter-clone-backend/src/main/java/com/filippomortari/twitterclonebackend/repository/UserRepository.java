package com.filippomortari.twitterclonebackend.repository;

import com.filippomortari.twitterclonebackend.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {
}
