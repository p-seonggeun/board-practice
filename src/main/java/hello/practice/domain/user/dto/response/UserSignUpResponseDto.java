package hello.practice.domain.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSignUpResponseDto {

    private String username;
    private String nickname;
    private String email;
    private String role;

}
