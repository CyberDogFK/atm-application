package com.antp.atmapplication.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService detailsService;

    public SecurityConfiguration(PasswordEncoder passwordEncoder,
                                 UserDetailsService detailsService) {
        this.passwordEncoder = passwordEncoder;
        this.detailsService = detailsService;
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((authz) ->
                        authz
                                .requestMatchers(HttpMethod.POST,"/register").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .anyRequest().permitAll()
                )
                .formLogin().permitAll()
                .and()
                .httpBasic(Customizer.withDefaults())
                .csrf().disable()
                .headers().frameOptions().disable().and()
                .build();
    }
}
