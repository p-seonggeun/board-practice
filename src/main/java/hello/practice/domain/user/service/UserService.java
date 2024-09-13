package hello.practice.domain.user.service;

import hello.practice.domain.user.dto.request.UserSignUpRequestDto;
import hello.practice.domain.user.dto.response.UserSignUpResponseDto;
import hello.practice.domain.user.entity.Role;
import hello.practice.domain.user.entity.User;
import hello.practice.domain.user.repository.UserRepository;
import hello.practice.global.exception.BusinessException;
import hello.practice.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 앞 단에서 중복확인 모두 마친 상태
     */
    @Transactional
    public UserSignUpResponseDto signUp(UserSignUpRequestDto userSignUpRequestDto) {

        boolean existsByUsername = userRepository.existsByUsername(userSignUpRequestDto.getUsername());
        if (existsByUsername) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_USERNAME, "이미 존재하는 아이디입니다.");
        }

        boolean existsByEmail = userRepository.existsByEmail(userSignUpRequestDto.getEmail());
        if (existsByEmail) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_EMAIL, "이미 존재하는 이메일입니다.");
        }

        boolean existsByNickname = userRepository.existsByNickname(userSignUpRequestDto.getNickname());
        if (existsByNickname) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_NICKNAME, "이미 존재하는 닉네임입니다.");
        }


        log.info("--------enter UserService signUp--------");
        log.info(userSignUpRequestDto.toString());
        User user = User.builder()
                .username(userSignUpRequestDto.getUsername())
                .password(bCryptPasswordEncoder.encode(userSignUpRequestDto.getPassword()))
                .email(userSignUpRequestDto.getEmail())
                .nickname(userSignUpRequestDto.getNickname())
                .role(Role.USER)
                .build();

        userRepository.save(user);
        log.info("--------exit UserService signUp--------");
        return user.toSignUpResponseDto();
    }
}

