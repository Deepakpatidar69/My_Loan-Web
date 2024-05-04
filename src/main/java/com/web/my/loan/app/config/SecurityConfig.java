package com.web.my.loan.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService detailsService() {

        return new CustomUserDetailServices();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(this.detailsService());
        authenticationProvider.setPasswordEncoder(this.passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain getFilterChain(HttpSecurity security) throws Exception {
        security
                .csrf((t)
                        -> t.disable()
                )
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/admin/**").authenticated()
                .requestMatchers("/user/**").authenticated()
                .anyRequest().permitAll())
                .formLogin(login_config -> {
                    login_config.loginPage("/loan/signin").loginProcessingUrl("/loan/do_signin")
                            .defaultSuccessUrl("/user/dash");
                })
                .logout((l) -> {

                    l.logoutUrl("/logout")
                            .logoutSuccessUrl("/loan/login?logout")
                            .permitAll();
                });

        return security.build();

    }
}
