package com.springboot.blog.springbootblogrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.blog.springbootblogrestapi.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	
}

