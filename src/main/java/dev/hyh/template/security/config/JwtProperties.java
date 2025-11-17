package dev.hyh.template.security.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secretKey;
    private long accessTokenExpireMs;
    private long refreshTokenExpireMs;

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setAccessTokenExpireMs(long accessTokenExpireMs) {
        this.accessTokenExpireMs = accessTokenExpireMs;
    }

    public void setRefreshTokenExpireMs(long refreshTokenExpireMs) {
        this.refreshTokenExpireMs = refreshTokenExpireMs;
    }
}
