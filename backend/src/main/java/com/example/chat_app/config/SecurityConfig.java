package com.example.chat_app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.chat_app.security.CustomUserDetails;
import com.example.chat_app.service.CustomUserDetailsService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig{
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .formLogin(
            login -> login
            // 指定したURLがリクエストされるとログイン認証を行う。
            .loginProcessingUrl("/login") 
            // ログイン時のURLの指定
            // .loginPage("/api/login")  
            // 認証成功後にリダイレクトする場所の指定
            .defaultSuccessUrl("/chat-websocket")  
            // ログインに失敗した時のURL
            .failureUrl("/login?error")  
            //アクセス権限の有無（permitAllは全てのユーザーがアクセス可能)
            .permitAll()                  
        )
        .logout(
            logout -> logout
            .logoutSuccessUrl("/")
        )
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())  // CSRFを無効にする（必要に応じて有効に）
        .authorizeHttpRequests(requests -> requests
            .requestMatchers("/api/login", "/api/register", "/chat-websocket/**", "/test").permitAll()  // 認証なしでアクセス可能なエンドポイント
            .anyRequest().authenticated()
        )
        // .anyRequest().permitAll()  // すべてのリクエストを許可
        .sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .sessionManagement(session -> session
        .maximumSessions(1) // 1つのセッションのみを許可
        .expiredUrl("/login?expired") // セッション切れ時のリダイレクトURL
        );  // その他は認証が必要

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);

            authenticationManagerBuilder
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);  // 認証情報（Cookieなど）を許可

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        logger.info("CORS設定を適用しました: {}", configuration.getAllowedOrigins());
        return source;

    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000");
            }
        };
    }

    public int getCurrentUserId() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
        Object principal = authentication.getPrincipal();
        logger.debug("認証情報を確認しました: " + authentication);
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            return userDetails.getUserId();
        } else {
            logger.error("Expected CustomUserDetails but got " + principal.getClass());
        }
    }else {
        logger.warn("認証されていないユーザーです");
    }
    return 0; // 認証されていない場合
}
}