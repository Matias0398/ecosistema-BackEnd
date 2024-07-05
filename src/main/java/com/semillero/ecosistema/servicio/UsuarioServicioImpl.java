package com.semillero.ecosistema.servicio;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.repositorio.IUsuarioRepositorio;

@Service
public class UsuarioServicioImpl  {
	
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;

	@Transactional
	public Usuario guardar(Usuario usuario) {
		return usuarioRepositorio.save(usuario);
	}

	@Transactional
	public boolean desactivarUsuario(Integer id) {
		 Usuario usuario = buscarPorId(id);
	        if (usuario != null) {
	            usuario.setDeleted(true);
	            usuarioRepositorio.save(usuario);
	            return true;
	        }
	        return false;
	}
	
	@Transactional
	public Usuario buscarPorId(Integer id) {
		
		Optional<Usuario> opc=usuarioRepositorio.findById(id);
		return  opc.orElse(null);
	}
}