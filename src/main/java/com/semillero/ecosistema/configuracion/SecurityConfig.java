package com.semillero.ecosistema.configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService; // Asegúrate de que este bean esté disponible

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/usuarios","auth/login").permitAll() // Endpoint público para crear usuarios
                .requestMatchers("/usuarios/desactivar/**").hasRole("ADMIN") // Endpoint protegido para desactivar usuarios
                .anyRequest().authenticated() // Protege otros endpoints
            )
            .oauth2Login()
            .and()
            .csrf().disable()
            .addFilterBefore(new JwtAuthenticationFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}