package com.springboot.blog.springbootblogrestapi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.blog.springbootblogrestapi.exception.BlogAPIException;
import com.springboot.blog.springbootblogrestapi.exception.ResourceNotFoundException;
import com.springboot.blog.springbootblogrestapi.model.Comment;
import com.springboot.blog.springbootblogrestapi.model.Post;
import com.springboot.blog.springbootblogrestapi.payload.CommentDto;
import com.springboot.blog.springbootblogrestapi.repository.CommentRepository;
import com.springboot.blog.springbootblogrestapi.repository.PostRepository;
import com.springboot.blog.springbootblogrestapi.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	private CommentRepository commentRepository;
	private PostRepository postRepository;
	private ModelMapper mapper;


	public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,ModelMapper mapper ) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.mapper = mapper;
	}
	@Override
	public CommentDto createComment(long postId, CommentDto commentDto) {
		Comment comment = mapToEntity(commentDto);

		//retieve post entity by id
		Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id", postId));
		//set post to comment entity
		comment.setPost(post);

		//save comment to db
		Comment savedComment = commentRepository.save(comment);
		return mapToDto(savedComment);
	}

	//convert entity into dto using modelmapper library
	private CommentDto mapToDto(Comment comment){
		CommentDto commentDto = mapper.map(comment, CommentDto.class);
		return commentDto;
	}
	//convert dto into entity using modelmapper library
	private Comment mapToEntity(CommentDto commentDto){
		Comment comment = mapper.map(commentDto, Comment.class);
		return comment;
	}
	@Override
	public List<CommentDto> getCommentsByPostId(long postId) {
		//retrieve comments  by id
		List<Comment> comments = commentRepository.findByPostId(postId);
		//convert list of comment entities to list of comment dtos
		
		return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
	}
	@Override
	public CommentDto getCommentById(long postId, long commentId) {
		// retrieve post by id
		Post post = postRepository.findById(postId).orElseThrow(
			() -> new ResourceNotFoundException("Post","id", postId));
		
		// retrive comment by id
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","id", commentId));

		if(!comment.getPost().getId().equals(post.getId()))
		{
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
		}

		return mapToDto(comment);
	}

	//update comment
	@Override
	public CommentDto updateComment(long postId, long commentId, CommentDto commentRequest) {

		// retrieve post by id
		Post post = postRepository.findById(postId).orElseThrow(
			() -> new ResourceNotFoundException("Post","id", postId));

		// retrive comment by id
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","id", commentId));

		if(!comment.getPost().getId().equals(post.getId()))
		{
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
		}

		comment.setName(commentRequest.getName());
		comment.setEmail(commentRequest.getEmail());
		comment.setBody(commentRequest.getBody());

		Comment updatedComment = commentRepository.save(comment);

		return mapToDto(updatedComment);
	}

	//delete comment by id
	@Override
	public void deleteComment(long postId, long commentId) {
		Post post = postRepository.findById(postId).orElseThrow(
			() -> new ResourceNotFoundException("Post","id", postId));

		// retrive comment by id
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","id", commentId));

		if(!comment.getPost().getId().equals(post.getId()))
		{
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
		}
		
		commentRepository.delete(comment);
	}
	
}
