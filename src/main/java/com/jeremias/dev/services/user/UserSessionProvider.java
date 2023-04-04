package com.jeremias.dev.services.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;

import com.jeremias.dev.persistence.entity.User;
import com.jeremias.dev.persistence.repository.UserRepository;
import com.jeremias.dev.security.TokenPrincipal;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserSessionProvider {
	private final transient UserRepository userRepository;
	
					  	
	public Mono<User> getCurrentUserOrEmpty(){
		  return getCurrentUserSessionOrEmpty().map(UserSession::user);
	}
	public Mono<UserSession> getCurrentUserSessionOrEmpty(){
		return ReactiveSecurityContextHolder.getContext()
				.flatMap(context -> {
					final Authentication authentication = context.getAuthentication();
					if(authentication == null) {
						return Mono.empty();
					}
					
					final var tokenPrincipal = (TokenPrincipal) authentication.getPrincipal();
					return userRepository.findById(tokenPrincipal.userId())
							.map(user -> new UserSession(user, tokenPrincipal.token()));
				});
	}
	
	 public record UserSession(User user, String token) {
    }
}
