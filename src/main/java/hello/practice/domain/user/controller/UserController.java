package hello.practice.domain.user.controller;

import hello.practice.domain.user.dto.request.UserSignUpRequestDto;
import hello.practice.domain.user.dto.response.UserSignUpResponseDto;
import hello.practice.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponseDto> signUp(@Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto) {

        UserSignUpResponseDto userSignUpResponseDto = userService.signUp(userSignUpRequestDto);

        return ResponseEntity.ok(userSignUpResponseDto);
    }
}
