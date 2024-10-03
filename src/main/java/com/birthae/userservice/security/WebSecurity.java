package com.birthae.userservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->{
                    request.requestMatchers(antMatcher("/users/**")).permitAll();// users에 대해서는 모두 통과
                    request.requestMatchers(antMatcher("/**")).permitAll();});
        return http.build();
    }
//
//    public class MyCustomSecurity extends AbstractHttpConfigurer<MyCustomSecurity, HttpSecurity> {
//
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//
//            AuthenticationManager authenticationManager = http.getSharedObject(
//                    AuthenticationManager.class);
//            AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, userService, environment);
//            http.addFilter(authenticationFilter);
//        }
//
//        protected void configure2(AuthenticationManagerBuilder auth) throws Exception {
//            auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
//        }
//
//    }

}

