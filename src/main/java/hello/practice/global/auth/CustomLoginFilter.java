package hello.practice.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.practice.domain.token.entity.RefreshToken;
import hello.practice.domain.token.repository.RefreshTokenRepository;
import hello.practice.domain.user.dto.request.CustomUserDetails;
import hello.practice.domain.user.dto.request.UserLoginDto;
import hello.practice.global.exception.ErrorCode;
import hello.practice.global.exception.ErrorResponse;
import hello.practice.global.jwt.JwtUtil;
import hello.practice.global.redis.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static hello.practice.domain.common.Constants.*;

@RequiredArgsConstructor
@Slf4j
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
//    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisService redisService;

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
        String nickname = customUserDetails.getNickname();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();

        if (iterator.hasNext()) {
            GrantedAuthority grantedAuthority = iterator.next();
            String role = grantedAuthority.getAuthority();
            String accessToken = jwtUtil.createJwt("Access", username, nickname, role, ACCESS_TOKEN_EXPIRED_MS);
            String refreshToken = jwtUtil.createJwt("Refresh", username, nickname, role, REFRESH_TOKEN_EXPIRED_MS);

//            saveRefreshToken(username, refreshToken, REFRESH_TOKEN_EXPIRED_MS);
            redisService.saveRefreshToken(username, refreshToken, REFRESH_TOKEN_EXPIRED_MS);

            response.setHeader("Authorization", "Bearer " + accessToken);
            response.addCookie(createCookie("RefreshToken", refreshToken));
            response.setStatus(HttpStatus.OK.value());
            log.info("로그인 성공: Id: {}, Nickname: {}", username, nickname);
            log.info("액세스 토큰 발급: {}", accessToken);
            log.info("리프레시 토큰 발급: {}", refreshToken);
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

//    private void saveRefreshToken(String username, String refresh, Long expiredMs) {
//        Date date = new Date(System.currentTimeMillis() + expiredMs);
//        RefreshToken refreshToken = new RefreshToken(username, refresh, date.toString());
//
//        log.info("{}의 리프레시 토큰 저장: {}", username, refreshToken);
//        refreshTokenRepository.save(refreshToken);
//    }

    private Cookie createCookie(String cookieName, String value) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setHttpOnly(true);

        return cookie;
    }
}
