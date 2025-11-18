package dev.hyh.template.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties properties;

    private SecretKey cachedAccessKey;
    private SecretKey cachedRefreshKey;

    private SecretKey getAccessKey() {
        if (cachedAccessKey == null) {
            cachedAccessKey = Keys.hmacShaKeyFor(
                    properties.getSecretKey().getBytes(StandardCharsets.UTF_8)
            );
        }
        return cachedAccessKey;
    }

    private SecretKey getRefreshKey() {
        if (cachedRefreshKey == null) {
            cachedRefreshKey = Keys.hmacShaKeyFor(
                    properties.getRefreshSecretKey().getBytes(StandardCharsets.UTF_8)
            );
        }
        return cachedRefreshKey;
    }


    /** AccessToken 발급 */
    public String createAccessToken(String userId, Map<String, Object> claims) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + properties.getAccessTokenExpireMs());

        JwtBuilder builder = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getAccessKey());

        if (claims != null) builder.addClaims(claims);
        return builder.compact();
    }

    /** RefreshToken 발급 */
    public String createRefreshToken(String userId) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + properties.getRefreshTokenExpireMs());

        return Jwts.builder()
                .setSubject(userId)
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getRefreshKey())
                .compact();
    }


    /** AccessToken Claims 파싱 */
    public Claims getAccessClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getAccessKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /** RefreshToken Claims 파싱 */
    public Claims getRefreshClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getRefreshKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    /** AccessToken 유효성 검사 */
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getAccessKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /** RefreshToken 유효성 검사 */
    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getRefreshKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
