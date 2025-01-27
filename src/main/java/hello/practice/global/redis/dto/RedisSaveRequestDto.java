package hello.practice.global.redis.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RedisSaveRequestDto {

    @NotBlank(message = "Key는 비워둘 수 없습니다.")
    private String key;

    @NotBlank(message = "Value는 비워둘 수 없습니다.")
    private String value;

    @Min(value = 1, message = "Timeout은 최소 1이상이어야 합니다.")
    private long timeout;
}
