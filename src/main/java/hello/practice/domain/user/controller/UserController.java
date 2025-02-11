package hello.practice.domain.user.controller;

import hello.practice.domain.user.dto.request.UserSignUpRequestDto;
import hello.practice.domain.user.dto.response.UserSignUpResponseDto;
import hello.practice.domain.user.service.UserCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;

    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponseDto> signUp(@Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto) {

        UserSignUpResponseDto userSignUpResponseDto = userCommandService.signUp(userSignUpRequestDto);

        return ResponseEntity.ok(userSignUpResponseDto);
    }
}
