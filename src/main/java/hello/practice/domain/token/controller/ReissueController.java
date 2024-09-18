package hello.practice.domain.token.controller;

import hello.practice.domain.token.entity.RefreshToken;
import hello.practice.domain.token.repository.RefreshTokenRepository;
import hello.practice.global.exception.BusinessException;
import hello.practice.global.exception.ErrorCode;
import hello.practice.global.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;

import static hello.practice.domain.common.Constants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("RefreshToken")) {

                refreshToken = cookie.getValue();
                log.info("리프레시 토큰: {}", refreshToken);
                break;
            }
        }

        if (refreshToken == null) {
            log.error("리프레시 토큰을 찾을 수 없습니다.");
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND, "리프레시 토큰을 찾을 수 없습니다");
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            log.error("리프레시 토큰이 만료되었습니다.");
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_EXPIRED, "리프레시 토큰이 만료되었습니다");
        }

        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("Refresh")) {
            log.error("유효하지 않은 리프레시 토큰입니다.");
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN, "유효하지 않은 리프레시 토큰입니다");
        }

        Boolean isExist = refreshTokenRepository.existsByRefreshToken(refreshToken);
        if(!isExist) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN, "유효하지 않은 리프레시 토큰입니다");
        }

        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        String newAccessToken = jwtUtil.createJwt("Access", username, role, ACCESS_TOKEN_EXPIRED_MS);
        String newRefreshToken = jwtUtil.createJwt("Refresh", username, role, REFRESH_TOKEN_EXPIRED_MS);

        refreshTokenRepository.deleteByRefreshToken(refreshToken);
        log.info("{}의 기존 리프레시 토큰[{}]이 삭제되었습니다.", username, refreshToken);
        saveRefreshToken(username, newRefreshToken, REFRESH_TOKEN_EXPIRED_MS);

        response.setHeader("Authorization", "Bearer " + newAccessToken);
        response.addCookie(createCookie("RefreshToken", newRefreshToken));
        log.info("액세스 토큰 재발급 완료: {}", newAccessToken);
        log.info("리프레시 토큰 재발급 완료: {}", newRefreshToken);

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("액세스 토큰과 리프레시 토큰이 재발급 되었습니다.");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void saveRefreshToken(String username, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        RefreshToken refreshToken = new RefreshToken(username, refresh, date.toString());

        log.info("{}의 리프레시 토큰 저장: {}", username, refreshToken);
        refreshTokenRepository.save(refreshToken);
    }

    private Cookie createCookie(String cookieName, String value) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setHttpOnly(true);

        return cookie;
    }
}
