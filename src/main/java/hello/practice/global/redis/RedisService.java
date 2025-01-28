package hello.practice.global.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveRefreshToken(String username, String refreshToken, Long timeout) {
        String key = "refreshToken:user:" + username;
        redisTemplate.opsForValue().set(key, refreshToken, timeout, TimeUnit.MILLISECONDS);
        log.info("{}의 리프레시 토큰 저장", key);
    }

    public String getRefreshToken(String username) {
        String key = "refreshToken:user:" + username;
        String result = (String) redisTemplate.opsForValue().get(key);
        log.info("{}의 리프레시 토큰: {}", key, result);
        return result;
    }

    public void deleteRefreshToken(String username) {
        String key = "refreshToken:user:" + username;
        Boolean result = redisTemplate.delete(key);
        log.info("{}의 리프레시 토큰 삭제 결과: {}", key, result);
    }

    public boolean existRefreshToken(String username) {
        String key = "refreshToken:user:" + username;
        Boolean result = redisTemplate.hasKey(key);
        log.info("{}의 리프레시 토큰 존재 확인 결과: {}", key, result);
        return result;
    }

    public void addToBlacklist(String accessToken, Long remainingTtl) {
        String blacklistKey = "blacklist:" + accessToken;
        redisTemplate.opsForValue().set(blacklistKey, "true", remainingTtl, TimeUnit.MILLISECONDS);
        log.info("액세스 토큰 블랙리스트 추가: {}", accessToken);
    }

    public boolean isBlacklisted(String accessToken) {
        String blacklistKey = "blacklist:" + accessToken;
        Boolean result = redisTemplate.hasKey(blacklistKey);
        log.info("{} 블랙리스트 여부: {}", blacklistKey, result);
        return result != null && result; // null 체크 후 true 여부 반환
    }

}
