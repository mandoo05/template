package dev.hyh.template.security.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final StringRedisTemplate redisTemplate;

    private final String KEY_PREFIX = "REFRESH:";

    public void save(String userId, String refreshToken, long ttlMs) {
        redisTemplate.opsForValue().set(
                KEY_PREFIX + userId,
                refreshToken,
                ttlMs,
                TimeUnit.MILLISECONDS
        );
    }

    public String find(String userId) {
        return redisTemplate.opsForValue().get(KEY_PREFIX + userId);
    }

    public void delete(String userId) {
        redisTemplate.delete(KEY_PREFIX + userId);
    }
}
