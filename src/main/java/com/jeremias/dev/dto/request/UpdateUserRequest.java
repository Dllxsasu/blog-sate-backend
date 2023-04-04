package com.jeremias.dev.dto.request;

import javax.validation.constraints.Email;

import com.jeremias.dev.validation.NotBlankOrNull;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UpdateUserRequest {
	@Email
	@NotBlankOrNull
	private String email;

	@NotBlankOrNull
	private String username;

	@NotBlankOrNull
	private String password;

	private String image;

	private String bio;
}
