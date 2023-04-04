package com.jeremias.dev.security;



import java.security.KeyPair;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenProvider {

	private final KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
    private final JwtParser jwtParser = Jwts.parserBuilder()
            .setSigningKey(keyPair.getPublic())
            .build();
    private final JwtProperties jwtProperties;

    public Jws<Claims> validate(final String jwt) {
        return jwtParser.parseClaimsJws(jwt);
    }

    public String generateToken(final String userId) {
        return Jwts.builder()
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .setSubject(userId)
                .setExpiration(expirationDate())
                .compact();
    }

    private Date expirationDate() {
        final var expirationDate = System.currentTimeMillis() + getSessionTime();
        return new Date(expirationDate);
    }

    private long getSessionTime() {
        return jwtProperties.getSessionTime() * 1000L;
    }
    
    
}
