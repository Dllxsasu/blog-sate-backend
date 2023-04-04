package com.jeremias.dev.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeremias.dev.dto.request.UpdateUserRequest;
import com.jeremias.dev.dto.request.UserAuthenticationRequest;
import com.jeremias.dev.dto.request.UserRegistrationRequest;
import com.jeremias.dev.dto.view.UserView;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserWrapper<T> {
	@JsonProperty("user")
	private T content;
	
	 @NoArgsConstructor
	    public static class UserViewWrapper extends UserWrapper<UserView> {
	        public UserViewWrapper(final UserView userView) {
	            super(userView);
	        }
	    }

	    @NoArgsConstructor
	    public static class UserRegistrationRequestWrapper extends UserWrapper<UserRegistrationRequest> {
	        public UserRegistrationRequestWrapper(final UserRegistrationRequest user) {
	            super(user);
	        }
	    }

	    @NoArgsConstructor
	    public static class UserAuthenticationRequestWrapper extends UserWrapper<UserAuthenticationRequest> {
	        public UserAuthenticationRequestWrapper(final UserAuthenticationRequest user) {
	            super(user);
	        }
	    }

	    @NoArgsConstructor
	    public static class UpdateUserRequestWrapper extends UserWrapper<UpdateUserRequest> {
	        public UpdateUserRequestWrapper(final UpdateUserRequest user) {
	            super(user);
	        }
	    }
}
