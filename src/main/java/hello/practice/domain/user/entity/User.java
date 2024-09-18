package hello.practice.domain.user.entity;

import hello.practice.domain.common.BaseTimeEntity;
import hello.practice.domain.user.dto.request.UserDto;
import hello.practice.domain.user.dto.response.UserSignUpResponseDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "users")
@Entity
@Getter
@ToString(exclude = {})
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String username, String password, String nickname, String email, Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }

    public UserDto toUserDto() {
        return new UserDto(username, password, role);
    }

    public UserSignUpResponseDto toSignUpResponseDto() {
        return UserSignUpResponseDto.builder()
                .username(username)
                .nickname(nickname)
                .email(email)
                .role(role.getValue())
                .build();
    }
}
