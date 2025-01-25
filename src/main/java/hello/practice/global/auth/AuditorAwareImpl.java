package hello.practice.global.auth;

import hello.practice.domain.user.dto.request.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return Optional.of(userDetails.getUsername());  // 사용자 이름이나 ID를 반환
    }
}
