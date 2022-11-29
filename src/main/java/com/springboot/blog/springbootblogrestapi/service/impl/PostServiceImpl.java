package com.springboot.blog.springbootblogrestapi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.blog.springbootblogrestapi.exception.ResourceNotFoundException;
import com.springboot.blog.springbootblogrestapi.model.Post;
import com.springboot.blog.springbootblogrestapi.payload.PostDto;
import com.springboot.blog.springbootblogrestapi.payload.PostResponse;
import com.springboot.blog.springbootblogrestapi.repository.PostRepository;
import com.springboot.blog.springbootblogrestapi.service.PostService;



@Service
public class PostServiceImpl implements PostService {

	private PostRepository postRepository;
	
	private ModelMapper mapper;
	

	public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {
		this.postRepository = postRepository;	
		this.mapper = mapper;
	}

	

	@Override
	public PostDto createPost(PostDto postDto) {
		//convert dto to entity
		Post post = mapToEntity(postDto);
		Post newPost = postRepository.save(post);
		//convert entity to dto
		PostDto postResponse = mapToDto(newPost);
		return postResponse;
	}

	@Override
	public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        // get content for page object
        List<Post> listOfPosts = posts.getContent();

        List<PostDto> content= listOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
	}


	

	@Override
	public PostDto getPostById(long id) {
		Post post = postRepository.findById(id).orElseThrow(() ->
		 new ResourceNotFoundException("Post", "id", id));

		return mapToDto(post);
	}

	//convert entity into dto using modelmapper library
	private PostDto mapToDto(Post post){
		PostDto postDto = mapper.map(post, PostDto.class);
		return postDto;
	}
	//convert dto into entity using modelmapper library
	private Post mapToEntity(PostDto postDto){
		Post post = mapper.map(postDto, Post.class);
		return post;
	}


	//update post by id
	@Override
	public PostDto updatePost(PostDto postDto, long id) {
		//get post by id from db
		Post post = postRepository.findById(id).orElseThrow(() ->
		 new ResourceNotFoundException("Post", "id", id));
		 post.setTitle(postDto.getTitle());
		 post.setDescription(postDto.getDescription());
		 post.setContent(postDto.getContent());

		 Post updatedPost = postRepository.save(post);
		return mapToDto(updatedPost);
	}

	//delete post by id
	@Override
	public void deletePost(long id) {
		Post post = postRepository.findById(id).orElseThrow(() ->
		 new ResourceNotFoundException("Post", "id", id));

		 postRepository.delete(post);
		// return null;
	}
	
}
