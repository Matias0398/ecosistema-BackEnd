package com.semillero.ecosistema.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.semillero.ecosistema.dto.PaisDto;
import com.semillero.ecosistema.entidad.Categoria;
import com.semillero.ecosistema.entidad.Pais;
import com.semillero.ecosistema.entidad.Proveedor;
import com.semillero.ecosistema.entidad.Proveedor.EstadoProveedor;
import com.semillero.ecosistema.entidad.Provincia;
import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.repositorio.IProveedorRepositorio;

@Service
public class ProveedorServicio {

	@Autowired
	private IProveedorRepositorio proveedorRepositorio;
	
	@Autowired
	private PaisProvinciaServiceImpl paisProvinciaServiceImpl;
	
	@Autowired
	private UsuarioServicioImpl usuarioServicioImpl;
	
	@Autowired
	private CategoriaServicioImpl categoriaServicioImpl;
	
	private static final int maxProveedores=3;
	
	public Proveedor crearProveedor(Long usuarioId, Proveedor proveedor, Long paisId, Long provinciaId, Long categoriaId) throws Exception {
		int cantidadProveedores=proveedorRepositorio.countByUsuarioId(usuarioId);
		if(cantidadProveedores>=maxProveedores) {
			throw new Exception("El usuario ya tiene el maximo de proveedores");
		}
		
		
		Usuario usuario = usuarioServicioImpl.buscarPorId(usuarioId);
		
		if(usuario==null) {
			throw new Exception("El usuario no existe");
		}
		
		PaisDto paisDto = paisProvinciaServiceImpl.obtenerPaisDtoPorId(paisId);
		
		Provincia provincia=paisProvinciaServiceImpl.mostrarProvinciaPorId(paisId, provinciaId);
		
		if (provincia==null) {
            throw new Exception("La provincia no pertenece al país especificado.");
        }
		
		Categoria categoria=categoriaServicioImpl.buscarPorId(categoriaId);
		
		Pais pais = new Pais(paisDto.getId(), paisDto.getNombre());
		
		proveedor.setCategoria(categoria);
		proveedor.setUsuario(usuario);
		proveedor.setPais(pais);
		proveedor.setProvincia(provincia);
		proveedor.setEstado(EstadoProveedor.REVISION_INICIAL);
		
		return proveedorRepositorio.save(proveedor);
	}
	
	public Proveedor editarProveedor(Long usuarioId, Long proveedorId, Proveedor proveedorDetalles, Long paisId, Long provinciaId, Long categoriaId) throws Exception {
        Proveedor proveedor = proveedorRepositorio.findById(proveedorId)
                .orElseThrow(() -> new Exception("Proveedor no encontrado"));

        if (!proveedor.getUsuario().getId().equals(usuarioId)) {
            throw new Exception("No tiene permiso para editar este proveedor.");
        }

        PaisDto paisDto = paisProvinciaServiceImpl.obtenerPaisDtoPorId(paisId);
        Provincia provincia=paisProvinciaServiceImpl.mostrarProvinciaPorId(paisId, provinciaId);
        Categoria categoria=categoriaServicioImpl.buscarPorId(categoriaId);

        Pais pais = new Pais(paisDto.getId(), paisDto.getNombre());

        proveedor.setNombre(proveedorDetalles.getNombre());
        proveedor.setDescripcion(proveedorDetalles.getDescripcion());
        proveedor.setTelefono(proveedorDetalles.getTelefono());
        proveedor.setEmail(proveedorDetalles.getEmail());
        proveedor.setFacebook(proveedorDetalles.getFacebook());
        proveedor.setInstagram(proveedorDetalles.getInstagram());
        proveedor.setPais(pais);
        proveedor.setProvincia(provincia);
        proveedor.setCiudad(proveedorDetalles.getCiudad());
        proveedor.setCategoria(categoria);
        proveedor.setFeedback(proveedorDetalles.getFeedback());
        proveedor.setImagenes(proveedorDetalles.getImagenes());
        proveedor.setEstado(EstadoProveedor.CAMBIOS_REALIZADOS);

        return proveedorRepositorio.save(proveedor);
    }
	
	public List<Proveedor>buscarPorNombre(String query){
		return proveedorRepositorio.findByNombreContainingIgnoreCase(query);
	}
	
	public List<Proveedor> buscarPorCategoriaId(Long categoriaId) {
        return proveedorRepositorio.findByCategoriaId(categoriaId);
    }
}
