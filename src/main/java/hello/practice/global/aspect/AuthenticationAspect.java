package hello.practice.global.aspect;

import hello.practice.domain.board.service.BoardQueryService;
import hello.practice.domain.user.dto.request.CustomUserDetails;
import hello.practice.global.exception.BusinessException;
import hello.practice.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticationAspect {

    private final BoardQueryService boardQueryService;

    @Pointcut("execution(* hello.practice.domain.board.controller.BoardController.updateBoardById(..)) || " +
            "execution(* hello.practice.domain.board.controller.BoardController.deleteBoardById(..))")
    private void boardAuthentication() {}

    @Around("boardAuthentication()")
    public Object doAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        
        // 메서드 파라미터 추출
        Object[] args = joinPoint.getArgs();
        Long boardId = null;

        // boardId 추출
        for (Object arg : args) {
            if (arg instanceof Long) {
                boardId = (Long) arg;
                break;
            }
        }
        // 권한 검증 로직
        if (!boardQueryService.findBoardById(boardId).getWriter().equals(customUserDetails.getNickname())) {
            throw new BusinessException(ErrorCode.BOARD_FORBIDDEN, "게시물에 권한이 없습니다");
        }

        return joinPoint.proceed();
    }
}
