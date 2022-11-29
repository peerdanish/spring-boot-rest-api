package com.springboot.blog.springbootblogrestapi.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


import lombok.Data;

@Data
public class CommentDto {
	private long id;
	@NotEmpty(message = "name should not be empty")
	private String name;
	@NotEmpty(message = "email should not be empty")
	@Email(message = "email should be valid")
	private String email;
	@NotEmpty(message = "comment should not be empty")
	@Size(min = 10, message = "comment should have atleast 10 characters")
	private String body;
	
}
