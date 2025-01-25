package hello.practice.domain.user.service;

import hello.practice.domain.user.dto.request.UserDto;
import hello.practice.domain.user.dto.response.UserSignUpResponseDto;
import hello.practice.domain.user.entity.User;

public class UserConverter {

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getUsername(), user.getPassword(), user.getNickname(), user.getRole());
    }

    public static UserSignUpResponseDto toSignUpResponseDto(User user) {
        return UserSignUpResponseDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .role(user.getRole().getValue())
                .build();
    }
}
