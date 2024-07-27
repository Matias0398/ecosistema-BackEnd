package com.semillero.ecosistema.configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and() // Habilitar CORS usando la configuración proporcionada por CorsConfig
            .csrf().disable()
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/login","/auth/registro").permitAll() // Permitir acceso libre a estos endpoints
                .requestMatchers("/usuarios/**").hasRole("ADMIN") // Solo ADMIN puede acceder a desactivar usuarios
                .requestMatchers("/publicar/**", "/editar-publicacion/**", "/borrar-publicacion/**","/publicaciones").hasRole("ADMIN") // Solo ADMIN puede publicar, editar y borrar publicaciones
                .requestMatchers("/publicaciones/**", "/buscar/**").permitAll() // Permitir acceso a obtener publicaciones y buscar por ID a todos
                .requestMatchers("/crearProveedor/**", "/editarProveedor/**").hasRole("USUARIO")
                .requestMatchers("/buscarPorCategoria/**").permitAll()
                .requestMatchers("/categorias/**","/ubicacion/**").permitAll()
                .anyRequest().authenticated() // Asegura que todas las demás solicitudes estén autenticadas
            )
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(new JwtAuthenticationFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}