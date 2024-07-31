package com.semillero.ecosistema.servicio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.semillero.ecosistema.cloudinary.dto.ImageModel;
import com.semillero.ecosistema.dto.PaisDto;
import com.semillero.ecosistema.dto.ProveedorDto;
import com.semillero.ecosistema.entidad.Categoria;
import com.semillero.ecosistema.entidad.Imagen;
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
	
	@Autowired
	private ImagenServicioImpl imagenServicioImpl;
	
	private static final int maxProveedores=3;
	
	public Proveedor crearProveedor(Long usuarioId, ProveedorDto proveedorDto, Long paisId, Long provinciaId, Long categoriaId,List<ImageModel>imageModels) throws Exception {
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
		Proveedor proveedornuevo=new Proveedor();
		proveedornuevo.setCategoria(categoria);
		proveedornuevo.setUsuario(usuario);
		proveedornuevo.setPais(pais);
		proveedornuevo.setProvincia(provincia);
		proveedornuevo.setEstado(EstadoProveedor.REVISION_INICIAL);
		proveedornuevo.setNombre(proveedorDto.getNombre());
		proveedornuevo.setTipoProveedor(proveedorDto.getTipoProveedor());
		proveedornuevo.setCiudad(proveedorDto.getCiudad());
		proveedornuevo.setDescripcion(proveedorDto.getDescripcion());
		proveedornuevo.setEmail(proveedorDto.getEmail());
		proveedornuevo.setTelefono(proveedorDto.getTelefono());
		proveedornuevo.setFeedback("Proveedor en revisión");
		proveedornuevo.setFacebook(proveedorDto.getFacebook());
		proveedornuevo.setInstagram(proveedorDto.getInstagram());
		List<Imagen>listaimagenes=new ArrayList<>();
		
		for(ImageModel imageModel : imageModels) {
			if(imageModel.getFile()!=null && !imageModel.getFile().isEmpty()) {
				Imagen imagen=imagenServicioImpl.crearImagen(imageModel);
				if(imagen!=null) {
					imagen.setProveedor(proveedornuevo);
					listaimagenes.add(imagen);
				}
			}
		}
		proveedornuevo.setImagenes(listaimagenes);
		return proveedorRepositorio.save(proveedornuevo);
	}
	
	public Proveedor editarProveedor(Long usuarioId, Long proveedorId, ProveedorDto proveedorDetalles, Long paisId, Long provinciaId, Long categoriaId, List<MultipartFile> files) throws Exception {
	    Proveedor proveedor = proveedorRepositorio.findById(proveedorId)
	            .orElseThrow(() -> new Exception("Proveedor no encontrado"));

	    if (!proveedor.getUsuario().getId().equals(usuarioId)) {
	        throw new Exception("No tiene permiso para editar este proveedor.");
	    }

	    PaisDto paisDto = paisProvinciaServiceImpl.obtenerPaisDtoPorId(paisId);
	    Provincia provincia = paisProvinciaServiceImpl.mostrarProvinciaPorId(paisId, provinciaId);
	    Categoria categoria = categoriaServicioImpl.buscarPorId(categoriaId);

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
	    proveedor.setEstado(EstadoProveedor.REVISION_INICIAL);

	    // Manejar las nuevas imágenes
	    List<Imagen> listaImagenes = new ArrayList<>();

	    for (MultipartFile file : files) {
	        if (file != null && !file.isEmpty()) {
	            ImageModel imageModel = new ImageModel();
	            imageModel.setFile(file);
	            imageModel.setNombre(file.getOriginalFilename());
	            
	            Imagen imagen = imagenServicioImpl.crearImagen(imageModel);
	            if (imagen != null) {
	                imagen.setProveedor(proveedor);
	                listaImagenes.add(imagen);
	            }
	        }
	    }

	    // Actualizar las imágenes del proveedor
	    proveedor.setImagenes(listaImagenes);

	    return proveedorRepositorio.save(proveedor);
	}
	
	public List<Proveedor>buscarPorNombre(String query){
		return proveedorRepositorio.findByNombreContainingIgnoreCase(query);
	}
	
	public List<Proveedor> buscarPorCategoriaId(Long categoriaId) {
		
		List<Proveedor> proveedorActivo=proveedorRepositorio.findAll();
		
		return proveedorActivo.stream().filter(proveedor -> EstadoProveedor.ACEPTADO.equals(proveedor.getEstado()) && !proveedor.isDeleted() && categoriaId.equals(proveedor.getCategoria().getId()))
									   .collect(Collectors.toList());
    }
	
	public List<Proveedor>mostrarProveedoresActivos(){
		
		List<Proveedor> proveedorActivo=proveedorRepositorio.findAll();
		
		return proveedorActivo.stream().filter(proveedor -> EstadoProveedor.ACEPTADO.equals(proveedor.getEstado()) && !proveedor.isDeleted())
									   .collect(Collectors.toList());
			
	}
	
	
}
