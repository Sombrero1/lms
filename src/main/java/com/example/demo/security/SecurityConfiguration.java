package com.example.demo.security;

import com.example.demo.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)

public class SecurityConfiguration {

    public SecurityConfiguration(UserAuthService userAuthService) {
        this.userDetailService = userAuthService;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private final UserDetailsService userDetailService;



    @Autowired
    public void authConfigure(AuthenticationManagerBuilder authenticationManagerBuilder, PasswordEncoder passwordEncoder) throws Exception{
        authenticationManagerBuilder.inMemoryAuthentication()
                .withUser("student")
                .password(passwordEncoder.encode("123"))
                .roles("STUDENT")
                .and()
                .withUser("admin")
                .password(passwordEncoder.encode("123"))
                .roles("ADMIN");
        authenticationManagerBuilder.userDetailsService(userDetailService);
    }

    @Configuration
    public static class UiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/**").permitAll()
                    .and()
                    .formLogin()
                    .defaultSuccessUrl("/course")
                    .and()
                    .exceptionHandling()
                    .accessDeniedPage("/access_denied");
        }
    }
}
