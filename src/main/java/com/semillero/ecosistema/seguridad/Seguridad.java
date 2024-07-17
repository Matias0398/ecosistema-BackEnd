package com.semillero.ecosistema.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Seguridad {

	@Bean
	public DefaultSecurityFilterChain SecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
		 
				httpSecurity
				.authorizeHttpRequests((requests) -> requests
		                .requestMatchers("/public/**").permitAll() // Permitir acceso a endpoints públicos
		                .anyRequest().authenticated() // Requerir autenticación para cualquier otra solicitud
		            )
		            .oauth2Login(Customizer.withDefaults()); // Configurar OAuth2 Login con valores predeterminados

		        return httpSecurity.build();
	}
    
}
