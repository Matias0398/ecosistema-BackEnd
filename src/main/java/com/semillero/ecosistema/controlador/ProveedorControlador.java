package com.semillero.ecosistema.controlador;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.semillero.ecosistema.cloudinary.dto.ImageModel;
import com.semillero.ecosistema.dto.ProveedorDto;
import com.semillero.ecosistema.dto.RevisionDto;
import com.semillero.ecosistema.entidad.Proveedor;
import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.entidad.Imagen;
import com.semillero.ecosistema.servicio.ImagenServicioImpl;
import com.semillero.ecosistema.servicio.ProveedorServicio;
import com.semillero.ecosistema.servicio.UsuarioServicioImpl;

@RestController
@RequestMapping
public class ProveedorControlador {

	@Autowired
	private ProveedorServicio proveedorServicio;
	
	@Autowired
	private UsuarioServicioImpl usuarioServicio;
	
	@Autowired
	private ImagenServicioImpl imagenServicioImpl;

	//Metodo para crear un proveedor en la base de datos
	@PreAuthorize("hasRole('USUARIO')")
	@PostMapping(value="/crearProveedor/usuario/{usuarioId}",consumes = "multipart/form-data")
	public ResponseEntity<?> crearProveedor(@PathVariable Long usuarioId, @ModelAttribute @Valid ProveedorDto proveedorDto, BindingResult result, @RequestPart("imagenes") List<MultipartFile> files){
		if (result.hasErrors()) {
			List<String> errorMessages = result.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.toList());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
		}
		try {
			List<ImageModel> imageModels = imagenServicioImpl.convertirFilesAImageModels(files);
			proveedorServicio.crearProveedor(usuarioId, proveedorDto,imageModels);

			return ResponseEntity.ok("Proveedor creado con éxito");
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	//Metodo para editar un proveedor
	@PreAuthorize("hasRole('USUARIO')")
	@PutMapping(value = "/editarProveedor/usuario/{usuarioId}/proveedor/{proveedorId}", consumes = "multipart/form-data")
	public ResponseEntity<?> editarProveedor(@PathVariable Long usuarioId, @PathVariable Long proveedorId, @ModelAttribute ProveedorDto proveedorDto){
	    try {
	        Proveedor proveedorActualizado = proveedorServicio.editarProveedor(usuarioId,proveedorId, proveedorDto);
	        return ResponseEntity.ok(proveedorActualizado);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    }
	}

	//Metodo para eliminar una imagen
	@PreAuthorize("hasRole('USUARIO')")
	@DeleteMapping(value = "/eliminarImagen/{imagenId}")
	public ResponseEntity<?> eliminarImagen(@PathVariable Long imagenId) {
	    try {
	        imagenServicioImpl.eliminarImagen(imagenId);
	        return ResponseEntity.ok("Imagen eliminada correctamente");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encuentra el Id de la imagen");
	    }
	}

	//Metodo para actualizar una imagen
	@PreAuthorize("hasRole('USUARIO')")
    @PutMapping("/actualizar/{imagenId}")
    public ResponseEntity<?> actualizarImagen(@PathVariable Long imagenId, @RequestParam("file") MultipartFile file) {
        try {
            // Llamar al servicio para actualizar la imagen
            Imagen imagenActualizada = imagenServicioImpl.actualizarImagen(imagenId, file);
            return ResponseEntity.ok(imagenActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

	//Metodo para buscar un proveedor por nombre
	@GetMapping("/buscar")
	public ResponseEntity<List<Proveedor>>buscarProveedoresPorNombre(@RequestParam String query){
		try {
			List<Proveedor> proveedores=proveedorServicio.buscarPorNombre(query);
			return ResponseEntity.ok(proveedores);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	//Metodo para mostrar todos los proveedores
	@GetMapping("/mostrarTodo")
	public ResponseEntity<List<Proveedor>>mostrarTodo(){
		return ResponseEntity.ok(proveedorServicio.mostrarTodo());
	}

	//Metodo para buscar un proveedor por id
	@GetMapping("/buscarPorId/{proveedorId}")
	public ResponseEntity<Proveedor> buscarProveedorPorId(@PathVariable Long proveedorId) throws Exception {
		return ResponseEntity.ok(proveedorServicio.buscarProveedorPorId(proveedorId));
	}

	//Metodo para buscar un proveedor por categoria
	@GetMapping("/buscarPorCategoria/{categoriaId}")
    public ResponseEntity<List<Proveedor>> buscarProveedoresPorCategoria(@PathVariable Long categoriaId) {
		return ResponseEntity.ok(proveedorServicio.buscarPorCategoriaId(categoriaId));
	}

	//Metodo para mostrar los proveedores activos
	@GetMapping("/mostrarProveedorActivo")
	public ResponseEntity<List<Proveedor>> mostrarProveedorActivo(){
		return ResponseEntity.ok(proveedorServicio.mostrarProveedoresActivos());
	}

	//Metodo para mostrar los proveedores nuevos
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/nuevoProveedor")
	public ResponseEntity<List<Proveedor>>mostrarProveedorNuevo(){
		return ResponseEntity.ok(proveedorServicio.mostrarProveedorNuevo());
	}

	//Metodo para cambiar el estado de un proveedor
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/editarEstado/{id}")
	public ResponseEntity<?>editarEstado(@PathVariable Long id,@RequestBody RevisionDto revisionDto)throws Exception{
		Proveedor nuevoEstado=proveedorServicio.administrarProveedor(id, revisionDto.getEstado(), revisionDto.getFeedback());
		return ResponseEntity.ok(nuevoEstado);
	}

	//Metodo para mostrar el estado y feedback del proveedor de un usuario
	@PreAuthorize("hasRole('USUARIO')")
	@GetMapping("/misEstados/{usuarioId}")
	public ResponseEntity<?> misEstados(@PathVariable Long usuarioId) {
		Usuario usuarioCreador = usuarioServicio.buscarPorId(usuarioId);
		if (usuarioCreador != null) {
			return ResponseEntity.ok(proveedorServicio.misEstados(usuarioCreador));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ningún usuario con el id proporcionado");
		}
	}

	//Metodo para mostrar los proveedor cercanos
	@GetMapping(value = "/proveedoresCercanos")
	public List<Proveedor> obtenerProveedoresCercanos(@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lng) throws Exception{
		return proveedorServicio.obtenerProveedoresCercanos(lat, lng);
	}
}
