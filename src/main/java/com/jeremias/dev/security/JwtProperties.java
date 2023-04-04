package com.jeremias.dev.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Value;

@Value
@ConfigurationProperties(prefix = "jwt")
@ConstructorBinding
public class JwtProperties {
    int sessionTime;
}
