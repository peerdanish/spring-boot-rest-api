package com.springboot.blog.springbootblogrestapi.service;

import java.util.List;

import com.springboot.blog.springbootblogrestapi.payload.CommentDto;

public interface CommentService {
	CommentDto createComment(long postId, CommentDto comment);
	List<CommentDto> getCommentsByPostId(long postId);
	CommentDto getCommentById(long postId, long commentId);
	CommentDto updateComment(long postId, long commentId, CommentDto comment);
	void deleteComment(long postId, long commentId);
}
