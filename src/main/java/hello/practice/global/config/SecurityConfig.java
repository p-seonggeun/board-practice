package hello.practice.global.config;

import hello.practice.domain.token.repository.RefreshTokenRepository;
import hello.practice.global.auth.CustomLoginFilter;
import hello.practice.global.auth.CustomLogoutFilter;
import hello.practice.global.jwt.JwtFilter;
import hello.practice.global.jwt.JwtUtil;
import hello.practice.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
//    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisService redisService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf((configurer) -> configurer.disable());

        httpSecurity
                .formLogin((configurer) -> configurer.disable());

        httpSecurity
                .httpBasic((configurer) -> configurer.disable());

//        CustomLoginFilter customLoginFilter = new CustomLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshTokenRepository);
        CustomLoginFilter customLoginFilter = new CustomLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, redisService);
        customLoginFilter.setFilterProcessesUrl("/signin");

//        httpSecurity
//                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshTokenRepository), LogoutFilter.class);
        httpSecurity
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, redisService), LogoutFilter.class);

        httpSecurity
                .addFilterBefore(new JwtFilter(jwtUtil, redisService), CustomLoginFilter.class);

        httpSecurity
                .addFilterAt(customLoginFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity
                .authorizeHttpRequests((configurer) ->
                        configurer
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll()
                                .requestMatchers("/redis/**").permitAll()
                                .requestMatchers("/", "/signin", "/signup", "/reissue", "/signout").permitAll()
                                .requestMatchers("/user").hasRole("USER")
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .anyRequest().authenticated());

        httpSecurity
                .sessionManagement((configurer) -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }
}
