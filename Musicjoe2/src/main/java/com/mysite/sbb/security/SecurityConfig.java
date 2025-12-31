package com.mysite.sbb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorize) -> authorize
                /* 静的リソースとアップロードファイルへのアクセスを許可 */
                .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/files/**")).permitAll() // 修正：保存先パスに合わせる
                
                /* 管理者権限が必要なパス */
                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/music/delete/**")).hasRole("ADMIN")
                
                /* その他のパスはすべて許可 */
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
            
            .csrf((csrf) -> csrf.disable())
            .headers((headers) -> headers.frameOptions((f) -> f.sameOrigin()))
            
            .formLogin((login) -> login
                .loginPage("/user/login")
                .defaultSuccessUrl("/music/list")) // 修正：/list から /music/list へ
            
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/music/list") // 修正：/list から /music/list へ
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID"));
        
        return http.build();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}