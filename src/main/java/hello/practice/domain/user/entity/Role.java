package hello.practice.domain.user.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Role {

    USER("ROLE_USER", "일반 사용자"), ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String value;

    Role(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static Role fromKey(String key) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getKey().equalsIgnoreCase(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 키: " + key));
    }
}
