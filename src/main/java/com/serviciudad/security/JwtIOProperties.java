package com.serviciudad.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "jms.jwt")
public class JwtIOProperties {
    private Security security;
    private String timezone;
    private String issuer;
    private Token token;
    private Excluded excluded;

    @Data
    public static class Security {
        private boolean enabled;
    }

    @Data
    public static class Token {
        private Auth auth;
        private String secret;
        private int expiresInt;
    }

    @Data
    public static class Auth {
        private String path;
    }

    public static class Excluded {
        private String path;
    }
}
