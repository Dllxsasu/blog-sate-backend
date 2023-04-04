package com.jeremias.dev.services.user;

import org.springframework.stereotype.Service;

import com.jeremias.dev.dto.request.UpdateUserRequest;
import com.jeremias.dev.dto.request.UserAuthenticationRequest;
import com.jeremias.dev.dto.request.UserRegistrationRequest;
import com.jeremias.dev.dto.view.ProfileView;
import com.jeremias.dev.dto.view.UserView;
import com.jeremias.dev.persistence.entity.User;
import com.jeremias.dev.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
	 private final SecuredUserService securedUserService;
	 private final UserRepository userRepository;
	 
	 
	 public Mono<ProfileView> getProfile(final String profileUserName, final User viewUser){
		 return userRepository.findByUsernameOrFail(profileUserName)
				 .map(user -> ProfileView.convertToProfileViewByViewerUser(user,viewUser));
	 }
	 
	 public Mono<ProfileView> getProfile(final String profileUsername){
		 return userRepository.findByUsernameOrFail(profileUsername)
				 .map(ProfileView::toUnfollowedProfileView);
	 }
	 

	    public Mono<UserView> signup(final Mono<UserRegistrationRequest> request) {
	        return request.flatMap(r -> securedUserService.signup(r));
	    }

	    public Mono<UserView> login(final Mono<UserAuthenticationRequest> request) {
	        return securedUserService.login(request);
	    }

	    public Mono<UserView> updateUser(final Mono<UpdateUserRequest> request, final UserSessionProvider.UserSession userSession) {
	        return Mono.defer(() ->securedUserService.updateUser(request, userSession.user()))
	                .map(it -> UserView.fromUserAndToken(it, userSession.token()));
	    }

	    public Mono<ProfileView> follow(final String profileUserName, final User follower) {
	        return userRepository.findByUsernameOrFail(profileUserName)
	                .flatMap(userToFollow -> {
	                    follower.follow(userToFollow);
	                    return userRepository.save(follower).thenReturn(userToFollow);
	                })
	                .map(ProfileView::toFollowedProfileView);
	    }

	    public Mono<ProfileView> unfollow(final String profileUserName, final User follower) {
	        return userRepository.findByUsernameOrFail(profileUserName)
	                .flatMap(userToUnfollow -> {
	                    follower.unfollow(userToUnfollow);
	                    return userRepository.save(follower).thenReturn(userToUnfollow);
	                })
	                .map(ProfileView::toUnfollowedProfileView);
	    }
	 
}
