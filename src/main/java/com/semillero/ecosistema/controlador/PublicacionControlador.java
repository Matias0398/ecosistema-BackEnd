package com.semillero.ecosistema.controlador;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.semillero.ecosistema.entidad.Publicacion;
import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.repositorio.IUsuarioRepositorio;
import com.semillero.ecosistema.servicio.PublicacionServicioImpl;

import jakarta.validation.Valid;

@RestController
public class PublicacionControlador {

	@Autowired
	private PublicacionServicioImpl publicacionServicioImpl;
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;
	
	@PostMapping(value="/publicar/{userId}")
	public ResponseEntity<String> crearPublicacion(@PathVariable Long userId, @Valid @RequestBody Publicacion publicacion) {
		Optional<Usuario> user = usuarioRepositorio.findById(userId);
		   if(user.isPresent()){
		        publicacion.setUsuarioCreador(user.get());
		        publicacionServicioImpl.crearPublicacion(publicacion);
		        return ResponseEntity.ok("Publicación creada con éxito");
		    } 
		   return ResponseEntity.badRequest().body("No se encontró ningún usuario con el id proporcionado");
	}
	
	@PutMapping(value="/editar-publicacion/{id}")
	public ResponseEntity<String> editarPublicacion(@PathVariable Long id, @Valid @RequestBody Publicacion publicacion) {
		boolean success = publicacionServicioImpl.editarPublicacion(id, publicacion);
		
		if (success) {
			 return ResponseEntity.ok("La publicacion se actualizó con éxito");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró una publicación con el id proporcionado");
		}
	}
	
	@DeleteMapping(value="/borrar-publicacion/{id}")
	public ResponseEntity<String> borrarPublicacion(@PathVariable Long id) {
		boolean success = publicacionServicioImpl.cambiarEstado(id);

        if (success) {
            return ResponseEntity.ok("La publicacion se borró con éxito");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró una publicación con el id proporcionado");
        }
	}
	
	@GetMapping(value="/publicaciones")
	public ResponseEntity<List<Publicacion>> obtenerPublicaciones() {
		return ResponseEntity.ok(publicacionServicioImpl.obtenerPublicaciones());
	}
	
	@GetMapping(value="/publicaciones/activas")
	public ResponseEntity<List<Publicacion>> obtenerPublicacionesActivas() {
		List<Publicacion> publicacionesActivas = publicacionServicioImpl.obtenerPublicacionesActivas();
		for(Publicacion publicacion : publicacionesActivas) {
			publicacionServicioImpl.incrementarVisualizaciones(publicacion);
		}
		return ResponseEntity.ok(publicacionesActivas);
	}
	
	
	@GetMapping(value="buscar/{idPublicacion}")
	public ResponseEntity<?> buscarPorId(@PathVariable Long idPublicacion) {
		Optional<Publicacion> publicacion = publicacionServicioImpl.buscarPublicacionPorId(idPublicacion);
		   if(publicacion.isPresent()){
		        return ResponseEntity.ok(publicacion.get());
		    } 
		   return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna publicación con el id proporcionado");

	}
	
	@PutMapping("/cambiar-estado/{id}")
	public ResponseEntity<String> cambiarEstado(@PathVariable Long id){
		boolean success = publicacionServicioImpl.cambiarEstado(id);

        if (success==true) {
            return ResponseEntity.ok("El estado se cambio con éxito");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró una publicación con el id proporcionado");
        }
	}
	

	
}
