package com.example.organization.config;


import com.example.organization.jwt.JwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebSecurity
public class Security {

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    @Autowired
    private  JwtConverter jwtConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
               auth.requestMatchers("/v1/organization/*").hasAnyRole(ADMIN, USER)
                       .anyRequest().authenticated());
        http.sessionManagement(sess -> sess.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)));
        return http.build();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((authz) ->
//                authz.requestMatchers(GET, "/api/hello").permitAll()
//                        .requestMatchers(GET, "/api/admin/**").hasRole(ADMIN)
//                        .requestMatchers(GET, "/api/user/**").hasRole(USER)
//                        .requestMatchers(GET, "/api/admin-and-user/**").hasAnyRole(ADMIN, USER)
//                        .anyRequest().authenticated());
//        http.sessionManagement(sess -> sess.sessionCreationPolicy(
//                SessionCreationPolicy.STATELESS));
//        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)));
//        return http.build();
//    }
}