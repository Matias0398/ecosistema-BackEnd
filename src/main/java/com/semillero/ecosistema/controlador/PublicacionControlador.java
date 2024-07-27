package com.semillero.ecosistema.controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.semillero.ecosistema.cloudinary.dto.ImageModel;
import com.semillero.ecosistema.dto.PublicacionDto;
import com.semillero.ecosistema.entidad.Publicacion;
import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.entidad.Usuario.RolDeUsuario;
import com.semillero.ecosistema.repositorio.IUsuarioRepositorio;
import com.semillero.ecosistema.servicio.PublicacionServicioImpl;

import jakarta.validation.Valid;

@RestController
public class PublicacionControlador {

	@Autowired
	private PublicacionServicioImpl publicacionServicioImpl;
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;
	


	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/publicar/{userId}", consumes = "multipart/form-data")
	public ResponseEntity<String> crearPublicacion(@PathVariable Long userId,@Valid @ModelAttribute PublicacionDto publicacionDto,@RequestPart("imagen") List<MultipartFile> files) throws IOException {

	    Optional<Usuario> user = usuarioRepositorio.findById(userId);
	    
	    if (user.isPresent() && user.get().getRol() == RolDeUsuario.ADMIN) {
	        List<ImageModel> imageModels = new ArrayList<>();
	        
	        // Crear ImageModel para cada archivo
	        for (MultipartFile file : files) {
	            String nombreArchivo = file.getOriginalFilename();
	            
	            ImageModel imageModel = new ImageModel();
	            imageModel.setFile(file);
	            imageModel.setNombre(nombreArchivo);
	            
	            imageModels.add(imageModel);
	        }

	        // Pasar la lista de ImageModel al servicio de publicaciones
	        publicacionDto.setUsuarioCreador(user.get());
	        publicacionServicioImpl.crearPublicacion(publicacionDto, imageModels);
	        return ResponseEntity.ok("Publicación creada con éxito");
	    }
	    
	    return ResponseEntity.badRequest().body("No se encontró ningún usuario con el id proporcionado o con los permisos requeridos");
	}


	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value="/editar-publicacion/{id}")
	public ResponseEntity<String> editarPublicacion(@PathVariable Long id, @Valid @RequestBody Publicacion publicacion) {
		boolean success = publicacionServicioImpl.editarPublicacion(id, publicacion);
		
		if (success) {
			 return ResponseEntity.ok("La publicacion se actualizó con éxito");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró una publicación con el id proporcionado");
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value="/borrar-publicacion/{id}")
	public ResponseEntity<String> borrarPublicacion(@PathVariable Long id) {
		boolean success = publicacionServicioImpl.borrarPublicacion(id);
		
		if (success) {
			return ResponseEntity.ok("La publicación se borró con exito");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró una publicación con el id proporcionado");
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
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
	public ResponseEntity<String> cambiarEstado(@PathVariable Long id) {
		boolean success = publicacionServicioImpl.cambiarEstado(id);

        if (success==true) {
            return ResponseEntity.ok("El estado se cambio con éxito");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró una publicación con el id proporcionado");
        }
	}
	

	
}
