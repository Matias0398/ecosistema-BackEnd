package com.semillero.ecosistema.configuracion;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
	
	 @Bean
	 public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Permitir todos los orígenes
	        configuration.setAllowedMethods(Arrays.asList("*")); // Permitir todos los métodos
	        configuration.setAllowedHeaders(Arrays.asList("*")); // Permitir todas las cabeceras
	        configuration.setAllowCredentials(true); // Permitir credenciales

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    }
	}