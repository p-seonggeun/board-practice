package hello.practice.domain.user.dto.request;

import lombok.Data;

@Data
public class UserLoginDto {

    private String username;
    private String password;

}
