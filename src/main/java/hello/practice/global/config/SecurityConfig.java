package hello.practice.global.config;

import hello.practice.global.auth.LoginFilter;
import hello.practice.global.jwt.JwtFilter;
import hello.practice.global.jwt.JwtUtil;
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

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;

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

        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil);
        loginFilter.setFilterProcessesUrl("/signin");

        httpSecurity
                .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

        httpSecurity
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity
                .authorizeHttpRequests((configurer) ->
                        configurer.requestMatchers("/", "/signin", "/signup").permitAll()
                                .requestMatchers("/user").hasRole("USER")
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .anyRequest().authenticated());

        httpSecurity
                .sessionManagement((configurer) -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }
}
