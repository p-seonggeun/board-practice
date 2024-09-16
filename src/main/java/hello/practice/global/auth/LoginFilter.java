package hello.practice.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.practice.domain.user.dto.request.CustomUserDetails;
import hello.practice.domain.user.dto.request.UserLoginDto;
import hello.practice.global.exception.BusinessException;
import hello.practice.global.exception.ErrorCode;
import hello.practice.global.exception.ErrorResponse;
import hello.practice.global.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static hello.practice.domain.common.Constants.ACCESS_TOKEN_EXPIRED_MS;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserLoginDto userLoginDto;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            userLoginDto = objectMapper.readValue(messageBody, UserLoginDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();

        if (iterator.hasNext()) {
            GrantedAuthority grantedAuthority = iterator.next();
            String role = grantedAuthority.getAuthority();
            String accessToken = jwtUtil.createJwt(username, role, ACCESS_TOKEN_EXPIRED_MS);

            response.addHeader("Authorization", "Bearer " + accessToken);
            log.info("로그인 성공: {}", username);
            log.info("액세스 토큰 발급: {}", accessToken);
        }

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.UNAUTHORIZED.getCode(), List.of("아이디 또는 비밀번호가 일치하지 않습니다."));
        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        log.error("로그인 실패: 아이디 또는 비밀번호가 일치하지 않습니다.", failed);
//        throw new BusinessException(ErrorCode.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다.");
    }
}
