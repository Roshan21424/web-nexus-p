package com.personal.nexus.repository;

import com.personal.nexus.entity.feed.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}
