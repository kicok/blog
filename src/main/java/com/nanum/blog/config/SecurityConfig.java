package com.nanum.blog.config;

import com.nanum.blog.config.oauth.PrincipalOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    private final PrincipalDetailsService principalDetailsService;
//
//    public SecurityConfig(PrincipalDetailsService principalDetailsService) {
//        this.principalDetailsService = principalDetailsService;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(principalDetailsService).passwordEncoder(passwordEncoder());
//    }

    private final PrincipalOAuth2UserService principalOAuth2UserService;

    public SecurityConfig(PrincipalOAuth2UserService principalOAuth2UserService) {
        this.principalOAuth2UserService = principalOAuth2UserService;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.csrf().disable()
               .authorizeRequests()
               .antMatchers("/users/**").access("hasRole('ROLE_ADMIN')")
               .antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**")
               .permitAll()
               .anyRequest()
               .authenticated()
               .and()
               .formLogin()
               .loginPage("/auth/loginForm")
               .loginProcessingUrl("/auth/loginProc")
               .defaultSuccessUrl("/")
               .and()
               .oauth2Login()
               .userInfoEndpoint()
               .userService(principalOAuth2UserService)
               ;


    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
