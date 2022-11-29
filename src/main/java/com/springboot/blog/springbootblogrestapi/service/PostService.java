package com.springboot.blog.springbootblogrestapi.service;

// import java.util.List;

import com.springboot.blog.springbootblogrestapi.payload.PostDto;
import com.springboot.blog.springbootblogrestapi.payload.PostResponse;

public interface PostService {
	PostDto createPost(PostDto postDto);
	PostResponse getAllPosts(int pageNo, int pageSize, String sortBy,String sortDirection);
	// List<PostDto> getAllPosts(int pageNo, int pageSize);
	PostDto getPostById(long id);
	PostDto updatePost(PostDto postDto, long id);
	void  deletePost(long id);
	
}
