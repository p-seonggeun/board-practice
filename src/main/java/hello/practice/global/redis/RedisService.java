package hello.practice.global.redis;

import hello.practice.global.redis.dto.RedisSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void save(RedisSaveRequestDto redisSaveRequestDto) {
        redisTemplate.opsForValue().set(redisSaveRequestDto.getKey(), redisSaveRequestDto.getValue(), Duration.ofSeconds(redisSaveRequestDto.getTimeout()));
    }

    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
