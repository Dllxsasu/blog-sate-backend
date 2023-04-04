package com.jeremias.dev.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults( level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class UserAuthenticationRequest {
	@Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
