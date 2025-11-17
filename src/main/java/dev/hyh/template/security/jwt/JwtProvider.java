package dev.hyh.template.security.jwt;

import dev.hyh.template.security.config.JwtProperties;
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

    private SecretKey secretKeyCache;

    /**
     * 🔹 SecretKey 캐싱 (매 요청마다 key 변환 안 함 → 성능 최적화)
     */
    private SecretKey getSigningKey() {
        if (secretKeyCache == null) {
            secretKeyCache = Keys.hmacShaKeyFor(
                    properties.getSecretKey().getBytes(StandardCharsets.UTF_8)
            );
        }
        return secretKeyCache;
    }

    /**
     * 🔹 AccessToken 생성
     */
    public String createAccessToken(String userId, Map<String, Object> extraClaims) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + properties.getAccessTokenExpireMs());

        JwtBuilder builder = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey());

        if (extraClaims != null) {
            builder.addClaims(extraClaims);
        }

        return builder.compact();
    }

    /**
     * 🔹 RefreshToken 생성
     */
    public String createRefreshToken(String userId) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + properties.getRefreshTokenExpireMs());

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("type", "refresh")
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 🔹 JWT 파싱 후 Claims 추출
     */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 🔹 userId 가져오기
     */
    public String getUserIdFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * 🔹 토큰 유효성 검증
     */
    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;

        } catch (ExpiredJwtException e) {
            // 만료됨 → RefreshToken 사용해야 함
            return false;

        } catch (JwtException e) {
            // 서명 불일치, 구조 이상 등 모든 JWT 예외
            return false;
        }
    }
}
