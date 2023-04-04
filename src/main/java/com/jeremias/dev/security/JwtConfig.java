package com.jeremias.dev.security;

import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.util.CollectionUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import reactor.core.publisher.Mono;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

    @Bean
    /* default */ ServerAuthenticationConverter jwtServerAuthenticationConverter(final TokenExtractor tokenExtractor) {
        return ex -> Mono.justOrEmpty(ex).flatMap(exchange -> {
            final List<String> headers = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
            if (CollectionUtils.isEmpty(headers)) {
                return Mono.empty();
            }
            final var authHeader = headers.get(0);
            final var token = tokenExtractor.extractToken(authHeader);
            return Mono.just(new UsernamePasswordAuthenticationToken(token, token));
        });
    }

    @Bean
    /* default */ ReactiveAuthenticationManager jwtAuthenticationManager(final TokenProvider tokenService) {
        return authentication -> Mono.justOrEmpty(authentication).map(auth -> {
            final String token = (String) auth.getCredentials();
            final Jws<Claims> jws = tokenService.validate(token);
            final String userId = jws.getBody().getSubject();
            final TokenPrincipal tokenPrincipal = new TokenPrincipal(userId, token);
            return new UsernamePasswordAuthenticationToken(
                    tokenPrincipal,
                    token,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        });
    }

    @Bean
    /* default */ AuthenticationWebFilter authenticationFilter(final ReactiveAuthenticationManager manager,
                                                               final ServerAuthenticationConverter converter) {
        final AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(manager);
        authenticationWebFilter.setServerAuthenticationConverter(converter);
        return authenticationWebFilter;
    }
}
