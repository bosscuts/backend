package com.bosscut.config;

import com.bosscut.repository.UserRepository;
import com.bosscut.service.DomainUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration implements WebMvcConfigurer {

    private final DataSource dataSource;

    private final UserRepository userRepository;

    public SecurityConfiguration(DataSource dataSource, UserRepository userRepository) {
        this.dataSource = dataSource;
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//          .userDetailsService(authorizedUserDetailsService())
//          .passwordEncoder(passwordEncoder());
//    }

    @Configuration
    @Order(1)
    public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

        private final PersistentTokenRepository persistentTokenRepository;

        public AdminSecurityConfig(PersistentTokenRepository persistentTokenRepository) {
            this.persistentTokenRepository = persistentTokenRepository;
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring()
                    .antMatchers("/admin/assets/**")
                    .antMatchers("/backend/invoice")
                    .antMatchers("/admin/images/**")
                    .antMatchers("/admin/sweetalert/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.requiresChannel()
                    .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
                    .requiresSecure();
            http.antMatcher("/admin_/**")
                    .authorizeRequests()
                    .antMatchers("/admin_/**").hasAnyRole("ADMIN", "EDITOR", "AUTHOR")
                    .and()
                    .formLogin()
                    .loginPage("/admin_/login").permitAll()
                    .loginProcessingUrl("/admin_/login")
                    .defaultSuccessUrl("/admin_")
                    .failureUrl("/admin_/login?fail")
                    .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/admin_/logout", "GET"))
                    .logoutSuccessUrl("/admin_/login")
                    .and()
                    .rememberMe()
                    .tokenRepository(persistentTokenRepository)
                    .and()
                    .headers()
                    .frameOptions().disable()
                    .cacheControl().disable()
                    .httpStrictTransportSecurity().disable()
                    .and()
                    .csrf()
                    .disable()
                    .exceptionHandling()
                    .accessDeniedPage("/admin_/login");
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth)
                throws Exception {
            auth
                    .userDetailsService(authorizedUserDetailsService())
                    .passwordEncoder(passwordEncoder());
        }
    }

    @Configuration
    @Order(2)
    public class GuestSecurityConfig extends WebSecurityConfigurerAdapter {

        private final PersistentTokenRepository persistentTokenRepository;

        public GuestSecurityConfig(PersistentTokenRepository persistentTokenRepository) {
            this.persistentTokenRepository = persistentTokenRepository;
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring()
                    .antMatchers("/blog/css/**")
                    .antMatchers("/blog/js/**")
                    .antMatchers("/blog/fonts/**")
                    .antMatchers("/blog/img/**")
                    .antMatchers("/blog/scss/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.requiresChannel()
                    .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
                    .requiresSecure();
            http.antMatcher("/**")
                    .authorizeRequests()
                    .antMatchers("/settings/**").hasRole("VIEWER")
                    .antMatchers("/**/viet-bai").hasAnyRole("ADMIN", "EDITOR", "AUTHOR", "VIEWER")
                    .antMatchers("/comments/**").hasRole("VIEWER")
                    .and()
                    .formLogin()
                    .loginPage("/login").permitAll()
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/")
                    .failureUrl("/?fail")
                    .permitAll()
                    .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .logoutSuccessUrl("/?logout")
                    .deleteCookies("my-remember-me-cookie")
                    .permitAll()
                    .and()
                    .rememberMe()
                    .tokenRepository(persistentTokenRepository)
                    .and()
                    .headers()
                    .frameOptions().disable()
                    .cacheControl().disable()
                    .httpStrictTransportSecurity().disable()
                    .and()
                    .csrf()
                    .disable()
                    .exceptionHandling()
                    .accessDeniedPage("/login");
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth)
                throws Exception {
            auth
                    .userDetailsService(authorizedUserDetailsService())
                    .passwordEncoder(passwordEncoder());
        }
    }

    @Bean
    public UserDetailsService authorizedUserDetailsService() {
        return new DomainUserDetailsService(userRepository);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        return repository;
    }

}

