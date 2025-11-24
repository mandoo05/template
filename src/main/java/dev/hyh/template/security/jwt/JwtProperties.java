package dev.hyh.template.security.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secretKey;
    private String refreshSecretKey;
    private long accessTokenExpireMs;
    private long refreshTokenExpireMs;

    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public void setRefreshSecretKey(String refreshSecretKey) { this.refreshSecretKey = refreshSecretKey; }
    public void setAccessTokenExpireMs(long accessTokenExpireMs) { this.accessTokenExpireMs = accessTokenExpireMs; }
    public void setRefreshTokenExpireMs(long refreshTokenExpireMs) { this.refreshTokenExpireMs = refreshTokenExpireMs; }
}
