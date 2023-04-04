package com.jeremias.dev.security;

import org.springframework.stereotype.Component;

import com.jeremias.dev.exception.InvalidRequestException;
@Component
public class TokenExtractor {
	public String extractToken(final String authorizationHeader) {
        if (!authorizationHeader.startsWith("Token ")) {
            throw new InvalidRequestException("Authorization Header", "has no `Token` prefix");
        }
        return authorizationHeader.substring("Token ".length());
    }
}
