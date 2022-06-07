package com.bosscut.config;

import com.bosscut.repository.UserRepository;
import com.bosscut.service.DomainUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration {
    private final UserRepository userRepository;

    public SecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new DomainUserDetailsService(userRepository);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/h2-console/**", "/users/**", "/login", "/register").permitAll()
                .antMatchers("/settings/**")
                .hasAuthority("Admin")
//                .hasAnyAuthority("Admin", "Editor", "Salesperson")
//                .hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
                .and()
                .rememberMe().key("AbcdEfghIjklmNopQrsTuvXyz_0123456789")
                .and()
                .logout().permitAll();

        http.headers().frameOptions().sameOrigin();

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/h2-console/**", "/img/**", "/js/**", "/css/**", "/webjars/**");
    }
}
