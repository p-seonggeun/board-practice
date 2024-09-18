package hello.practice.domain.user.service;

import hello.practice.domain.user.dto.request.CustomUserDetails;
import hello.practice.domain.user.dto.request.UserDto;
import hello.practice.domain.user.entity.User;
import hello.practice.domain.user.repository.UserRepository;
import hello.practice.global.exception.BusinessException;
import hello.practice.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, "계정을 찾을 수 없습니다"));
        UserDto userDto = user.toUserDto();
        return new CustomUserDetails(userDto);
    }
}
