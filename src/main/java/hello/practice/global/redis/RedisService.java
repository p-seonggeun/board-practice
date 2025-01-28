package hello.practice.global.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveRefreshToken(String username, String refreshToken, Long timeout) {
        redisTemplate.opsForValue().set(username, refreshToken, timeout);
        log.info("{}의 리프레시 토큰 저장", username);
    }

    public String getRefreshToken(String username) {
        String result = (String) redisTemplate.opsForValue().get(username);
        log.info("{}의 리프레시 토큰: {}", username, result);
        return result;
    }

    public void deleteRefreshToken(String username) {
        Boolean result = redisTemplate.delete(username);
        log.info("{}의 리프레시 토큰 삭제 결과: {}", username, result);
    }

    public boolean existRefreshToken(String username) {
        Boolean result = redisTemplate.hasKey(username);
        log.info("{}의 리프레시 토큰 존재 확인 결과: {}", username, result);
        return result;
    }
}
