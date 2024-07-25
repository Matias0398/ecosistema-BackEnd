package com.semillero.ecosistema.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.semillero.ecosistema.entidad.Proveedor;
import com.semillero.ecosistema.servicio.ProveedorServicio;

@RestController
@RequestMapping("proveedores")
public class ProveedorControlador {

	@Autowired
	private ProveedorServicio proveedorServicio;
	
	@PreAuthorize("hasRole('USUARIO')")
	@PostMapping("/usuario/{usuarioId}/pais/{paisId}/provincia/{provinciaId}/categoria/{categoriaId}")
	public ResponseEntity<?> crearProveedor(@PathVariable Long usuarioId,@PathVariable Long paisId,@PathVariable Long provinciaId,@PathVariable Long categoriaId,@RequestBody Proveedor proveedor) {
		try {
			Proveedor nuevoProveedor = proveedorServicio.crearProveedor(usuarioId, proveedor, paisId, provinciaId,categoriaId);
            return ResponseEntity.ok(nuevoProveedor);	
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@PreAuthorize("hasRole('USUARIO')")
	@PutMapping("/usuario/{usuarioId}/proveedor/{proveedorId}/pais/{paisId}/provincia/{provinciaId}/categoria/{categoriaId}")
	public ResponseEntity<?> editarProveedor(@PathVariable Long usuarioId,@PathVariable Long proveedorId,@PathVariable Long paisId,@PathVariable Long provinciaId,@PathVariable Long categoriaId, @RequestBody Proveedor proveedorDetalles) {
		try {
			Proveedor proveedorActualizado = proveedorServicio.editarProveedor(usuarioId, proveedorId, proveedorDetalles, paisId, provinciaId,categoriaId);
            return ResponseEntity.ok(proveedorActualizado);
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/buscar")
	public ResponseEntity<List<Proveedor>>buscarProveedoresPorNombre(@RequestParam String query){
		try {
			List<Proveedor> proveedores=proveedorServicio.buscarPorNombre(query);
			return ResponseEntity.ok(proveedores);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/buscarPorCategoria/{categoriaId}")
    public ResponseEntity<List<Proveedor>> buscarProveedoresPorCategoria(@PathVariable Long categoriaId) {
        try {
            List<Proveedor> proveedores = proveedorServicio.buscarPorCategoriaId(categoriaId);
            if (proveedores.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(proveedores);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
