package com.nt.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // Chain 1 — MVC/Thymeleaf routes (checked first)
    @Bean
    @Order(1)
    public SecurityFilterChain mvcSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/login", "/signup", "/dashboard/**",
                "/css/**", "/js/**", "/images/**", "/error")
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/login", "/signup", "/css/**", "/js/**", "/images/**", "/error")
                                .permitAll().requestMatchers("/dashboard/**")
                                .authenticated()).formLogin(form ->
                        form.loginPage("/login").defaultSuccessUrl("/dashboard", true)
                                .failureUrl("/login?error")
                                .permitAll()).logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout").permitAll());
        return http.build();
    }

    //  Chain 2 — JWT REST API routes (everything else)
    @Bean
    @Order(2)
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/public/**", "/swagger-ui/**", "/v3/api-docs/**")
                                .permitAll().anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
