package hello.practice.domain.user.service;

import hello.practice.domain.user.dto.request.UserSignUpRequestDto;
import hello.practice.domain.user.dto.response.UserSignUpResponseDto;
import hello.practice.domain.user.entity.Role;
import hello.practice.domain.user.entity.User;
import hello.practice.domain.user.repository.UserRepository;
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
        boolean existsByEmail = userRepository.existsByEmail(userSignUpRequestDto.getEmail());
        boolean existsByNickname = userRepository.existsByNickname(userSignUpRequestDto.getNickname());


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

