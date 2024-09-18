package hello.practice.domain.common;

public final class Constants {
    private Constants() {}

    public static final Long ACCESS_TOKEN_EXPIRED_MS = 60 * 10 * 1000L; // 10분
    public static final Long REFRESH_TOKEN_EXPIRED_MS = 24 * 60 * 60 * 1000L;
    public static final int COOKIE_MAX_AGE = 24 * 60 * 60;
}
