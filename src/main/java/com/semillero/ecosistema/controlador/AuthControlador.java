package com.semillero.ecosistema.controlador;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.servicio.GoogleTokenVerifier;
import com.semillero.ecosistema.servicio.UsuarioServicioImpl;

@RestController
@RequestMapping("/auth")
public class AuthControlador {

    @Autowired
    private GoogleTokenVerifier googleTokenVerifier;

    @Autowired
    private UsuarioServicioImpl usuarioServicio;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateGoogleUser(@RequestBody String idToken) {
        try {
            Payload payload = googleTokenVerifier.verifyToken(idToken);
            String email = payload.getEmail();
            Usuario usuario = usuarioServicio.authenticateUser(email);
            // Genera un JWT para el usuario autenticado si es necesario
            return ResponseEntity.ok(usuario);
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(401).body("Invalid ID token");
        }
    }
}