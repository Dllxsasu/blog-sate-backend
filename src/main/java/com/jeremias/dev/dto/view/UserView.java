package com.jeremias.dev.dto.view;

import com.jeremias.dev.persistence.entity.User;
import com.jeremias.dev.services.user.UserSessionProvider.UserSession;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
//@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class UserView {
	private String email;

    private String token;

    private String username;

    private String bio;

    private String image;
    
    public static UserView fromUserAndToken(final UserSession userssion) {
    	return fromUserAndToken(userssion.user(), userssion.token());
    }
    
    public static UserView fromUserAndToken(final User user, final String token) {
    	return UserView.builder()
    			.email(user.getEmail())
    			.username(user.getUsername())
    			.bio(user.getBio())
    			.token(token).build();
    }
    
}
