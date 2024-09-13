package hello.practice.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignUpRequestDto {

    @NotBlank(message = "아이디는 공백일 수 없습니다.")
    @Size(min = 4, max = 16, message = "아이디는 4글자 이상, 16글자 이하여야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 8글자 이상, 16글자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).+$", message = "영문 대소문자, 숫자, 특수문자를 포함하여야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 공백일 수 없습니다.")
    @Size(min = 2, max = 8, message = "닉네임은 2글자 이상, 8글자 이하여야 합니다.")
    private String nickname;

    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;

}
