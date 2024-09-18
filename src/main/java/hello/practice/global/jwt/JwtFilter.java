package hello.practice.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.practice.domain.user.dto.request.CustomUserDetails;
import hello.practice.domain.user.dto.request.UserDto;
import hello.practice.domain.user.entity.Role;
import hello.practice.domain.user.entity.User;
import hello.practice.global.exception.ErrorCode;
import hello.practice.global.exception.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        // 토큰이 없거나 Bearer로 시작하지 않으면 에러 처리
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            handleErrorResponse(response, ErrorCode.ACCESS_TOKEN_NOT_FOUND, "토큰이 존재하지 않습니다.");
            return;
        }

        String accessToken = authorization.split(" ")[1];

        // 토큰이 만료되었으면 에러 처리
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            handleErrorResponse(response, ErrorCode.ACCESS_TOKEN_EXPIRED, "토큰이 만료되었습니다.");
            return;
        }

        // 토큰으로부터 사용자명과 역할 가져오기
        String username = jwtUtil.getUsername(accessToken);
        Role role = Role.fromKey(jwtUtil.getRole(accessToken));
        log.info("{}, {}", username, role);

        // 사용자 정보 설정
        UserDto userDto = new UserDto(username, "tempPassword", role);
        CustomUserDetails customUserDetails = new CustomUserDetails(userDto);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.info("세션에 {}가 등록 되었습니다.", authToken);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // 특정 경로를 제외시킴
        String path = request.getRequestURI();
        return path.equals("/signin") || path.equals("/signup") || path.equals("/");
    }

    private void handleErrorResponse(HttpServletResponse response, ErrorCode errorCode, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), List.of(message));
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        log.error(message);
    }
}

