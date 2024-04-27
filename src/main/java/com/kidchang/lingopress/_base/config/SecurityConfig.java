package com.kidchang.lingopress._base.config;

import com.kidchang.lingopress.jwt.JwtExceptionFilter;
import com.kidchang.lingopress.jwt.JwtFilter;
import com.kidchang.lingopress.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
                                .permitAll()
                                .requestMatchers(
                                        "/api/v1/users/oauth2/**",
                                        "/api/v1/users/reissue",
                                        "/api/v1/users/status",
                                        "/api/v1/press/**",
                                        "/actuator/**",
                                        "/api/v1/users/error-occur"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtFilter(jwtTokenUtil),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtFilter.class);

        return http.build();
    }

}
