package com.semillero.ecosistema.controlador;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.semillero.ecosistema.dto.AuthResponse;
import com.semillero.ecosistema.dto.GoogleTokenRequest;
import com.semillero.ecosistema.entidad.Usuario;

import com.semillero.ecosistema.servicio.CustomUserDetailsService;
import com.semillero.ecosistema.util.GoogleVerifier;
import com.semillero.ecosistema.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class LoginControlador {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	 @PostMapping("/google")
	    public AuthResponse authenticateWithGoogle(@RequestBody GoogleTokenRequest googleTokenRequest) 
	            throws GeneralSecurityException, IOException {
	        String googleToken = googleTokenRequest.getToken();
	        GoogleIdToken.Payload payload = GoogleVerifier.verifyToken(googleToken);

	        if (payload == null) {
	            throw new RuntimeException("Invalid Google token");
	        }

	        String email = payload.getEmail();
	        Usuario user = customUserDetailsService.loadUsuarioByEmail(email);


	        // Generar el token JWT
	        String jwtToken = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
	                user.getEmail(), 
	                user.getContrasena(), 
	                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRol().name()))
	        ));

	        return new AuthResponse(jwtToken, user.getRol().name());
	    }
	}

