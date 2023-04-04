package com.jeremias.dev.api;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.jeremias.dev.dto.ProfileWrapper;
import com.jeremias.dev.dto.UserWrapper.UpdateUserRequestWrapper;
import com.jeremias.dev.dto.UserWrapper.UserAuthenticationRequestWrapper;
import com.jeremias.dev.dto.UserWrapper.UserRegistrationRequestWrapper;
import com.jeremias.dev.dto.UserWrapper.UserViewWrapper;
import com.jeremias.dev.dto.view.UserView;
import com.jeremias.dev.services.user.UserService;
import com.jeremias.dev.services.user.UserSessionProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Component
@Log4j2
@RequiredArgsConstructor
public class UserHandler {
	private final UserService userService;
	private final UserSessionProvider userSessionProvider;

	public Mono<ServerResponse> login(final ServerRequest req) {
		final Mono<UserAuthenticationRequestWrapper> in = req.bodyToMono(UserAuthenticationRequestWrapper.class);
		return ServerResponse.ok().body(userService.login(in.map(e -> e.getContent())).map(UserViewWrapper::new),
				UserViewWrapper.class);
	}

	public Mono<ServerResponse> signup(final ServerRequest req) {
		final Mono<UserRegistrationRequestWrapper> in = req.bodyToMono(UserRegistrationRequestWrapper.class);
		log.info("llego aqui");
		return ServerResponse.ok().body(userService.signup(in.map(e -> e.getContent())).map(UserViewWrapper::new),
				UserViewWrapper.class);
	}

	public Mono<ServerResponse> currentUser(final ServerRequest req) {
		return ServerResponse.ok().body(userSessionProvider.getCurrentUserSessionOrEmpty()
				.map(UserView::fromUserAndToken).map(UserViewWrapper::new), UserViewWrapper.class);
	}

	public Mono<ServerResponse> updateUser(final ServerRequest req) {
		final Mono<UpdateUserRequestWrapper> in = req.bodyToMono(UpdateUserRequestWrapper.class);
		return ServerResponse.ok().body(userSessionProvider.getCurrentUserSessionOrEmpty()
				.flatMap(it -> userService.updateUser(in.map(e -> e.getContent()), it)).map(UserViewWrapper::new),
				UserViewWrapper.class);
	}

	public Mono<ServerResponse> getProfile(final ServerRequest req) {
		final String profileUserName = req.pathVariable("username");
		return ServerResponse.ok().body(userSessionProvider.getCurrentUserOrEmpty()
				.flatMap(currentUser -> userService.getProfile(profileUserName, currentUser))
				.switchIfEmpty(Mono.defer(() -> userService.getProfile(profileUserName))).map(ProfileWrapper::new),
				ProfileWrapper.class);
	}

	public Mono<ServerResponse> follow(final ServerRequest req) {
		final String profileUserName = req.pathVariable("username");
		return ServerResponse.ok().body(userSessionProvider.getCurrentUserOrEmpty()
				.flatMap(currentUser -> userService.follow(profileUserName, currentUser)).map(ProfileWrapper::new),
				ProfileWrapper.class);
	}

	public Mono<ServerResponse> unfollow(final ServerRequest req) {
		final String profileUserName = req.pathVariable("username");
		return ServerResponse.ok().body(userSessionProvider.getCurrentUserOrEmpty()
				.flatMap(currentUser -> userService.unfollow(profileUserName, currentUser)).map(ProfileWrapper::new),
				ProfileWrapper.class);
	}
}
