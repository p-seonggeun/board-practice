package hello.practice.global.redis;

import hello.practice.global.redis.dto.RedisSaveRequestDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
public class RedisController {

    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping("/save")
    public String save(@RequestBody RedisSaveRequestDto redisSaveRequestDto) {
        redisService.save(redisSaveRequestDto);
        return "Saved successfully!";
    }

    @GetMapping("/get")
    public String get(@RequestParam String key) {
        return redisService.get(key);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String key) {
        redisService.delete(key);
        return "Deleted successfully!";
    }
}
