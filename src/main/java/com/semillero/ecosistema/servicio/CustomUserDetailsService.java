package com.semillero.ecosistema.servicio;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.repositorio.IUsuarioRepositorio;

@Service
public class CustomUserDetailsService {

    private final IUsuarioRepositorio usuarioRepositorio;

    public CustomUserDetailsService(IUsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Usuario loadUsuarioByEmail(String email) {
        Optional<Usuario> optionalUser = usuarioRepositorio.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("No hay usuario con este email: " + email);
        }
        return optionalUser.get();
    }
}