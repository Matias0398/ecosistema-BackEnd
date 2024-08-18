package com.semillero.ecosistema.servicio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.semillero.ecosistema.cloudinary.dto.ImageModel;
import com.semillero.ecosistema.dto.ProveedorDto;
import com.semillero.ecosistema.dto.StatusDto;
import com.semillero.ecosistema.entidad.Categoria;
import com.semillero.ecosistema.entidad.Imagen;
import com.semillero.ecosistema.entidad.Pais;
import com.semillero.ecosistema.entidad.Proveedor;
import com.semillero.ecosistema.entidad.Proveedor.EstadoProveedor;
import com.semillero.ecosistema.entidad.Provincia;
import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.entidad.Usuario.RolDeUsuario;
import com.semillero.ecosistema.repositorio.ICategoriaRepositorio;
import com.semillero.ecosistema.repositorio.IPaisRepositorio;
import com.semillero.ecosistema.repositorio.IProveedorRepositorio;
import com.semillero.ecosistema.repositorio.IProvinciaRepositorio;
import com.semillero.ecosistema.repositorio.IUsuarioRepositorio;

@Service
public class ProveedorServicio {

	@Autowired
	private IProveedorRepositorio proveedorRepositorio;
	
	@Autowired
	private UsuarioServicioImpl usuarioServicioImpl;
	
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private ImagenServicioImpl imagenServicioImpl;

	@Autowired
	private ICategoriaRepositorio categoriaRepository;

	@Autowired
	private IPaisRepositorio paisRepository;

	@Autowired
	private IProvinciaRepositorio provinciaRepository;
	
	@Autowired
	private EmailCuerpoServicio emailCuerpoServicio;
	    
	private static final int maxProveedores=3;
	
	public Proveedor crearProveedor(Long usuarioId, ProveedorDto proveedorDto, List<ImageModel> imageModels) throws Exception {
        int cantidadProveedores = proveedorRepositorio.countByUsuarioId(usuarioId);
        if (cantidadProveedores >= maxProveedores) {
            throw new Exception("El usuario ya tiene el máximo de proveedores");
        }

        Usuario usuario = usuarioServicioImpl.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new Exception("El usuario no existe");
        }

        Proveedor proveedornuevo = new Proveedor();

        // Buscar y asignar Categoria
        if (proveedorDto.getCategoriaId() != null) {
            Optional<Categoria> categoriaOptional = categoriaRepository.findById(proveedorDto.getCategoriaId());
            if (categoriaOptional.isPresent()) {
                proveedornuevo.setCategoria(categoriaOptional.get());
            } else {
                throw new Exception("No se encontró una categoría con el ID: " + proveedorDto.getCategoriaId());
            }
        }

        // Buscar y asignar Pais
        if (proveedorDto.getPaisId() != null) {
            Optional<Pais> paisOptional = paisRepository.findById(proveedorDto.getPaisId());
            if (paisOptional.isPresent()) {
                proveedornuevo.setPais(paisOptional.get());
            } else {
                throw new Exception("No se encontró un país con el ID: " + proveedorDto.getPaisId());
            }
        }

        // Buscar y asignar Provincia
        if (proveedorDto.getProvinciaId() != null) {
            Optional<Provincia> provinciaOptional = provinciaRepository.findById(proveedorDto.getProvinciaId());
            if (provinciaOptional.isPresent()) {
                proveedornuevo.setProvincia(provinciaOptional.get());
            } else {
                throw new Exception("No se encontró una provincia con el ID: " + proveedorDto.getProvinciaId());
            }
        }

        proveedornuevo.setUsuario(usuario);
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
        proveedornuevo.setFechaCreacion(LocalDateTime.now()	);

        List<Imagen> listaimagenes = new ArrayList<>();
        for (ImageModel imageModel : imageModels) {
            if (imageModel.getFile() != null && !imageModel.getFile().isEmpty()) {
                Imagen imagen = imagenServicioImpl.crearImagen(imageModel);
                if (imagen != null) {
                    imagen.setProveedor(proveedornuevo);
                    listaimagenes.add(imagen);
                }
            }
        }
        proveedornuevo.setImagenes(listaimagenes);

        return proveedorRepositorio.save(proveedornuevo);
    }
	
	public Proveedor editarProveedor(Long usuarioId, Long proveedorId, ProveedorDto proveedorDetalles) throws Exception {
	    // Buscar el proveedor por ID
	    Proveedor proveedor = proveedorRepositorio.findById(proveedorId)
	            .orElseThrow(() -> new Exception("Proveedor no encontrado"));

	    // Verificar permisos del usuario
	    if (!proveedor.getUsuario().getId().equals(usuarioId)) {
	        throw new Exception("No tiene permiso para editar este proveedor.");
	    }

	    // Actualizar datos del proveedor solo si están presentes en el DTO
	    if (proveedorDetalles.getNombre() != null) proveedor.setNombre(proveedorDetalles.getNombre());
	    if (proveedorDetalles.getTipoProveedor() != null) proveedor.setTipoProveedor(proveedorDetalles.getTipoProveedor());
	    if (proveedorDetalles.getDescripcion() != null) proveedor.setDescripcion(proveedorDetalles.getDescripcion());
	    if (proveedorDetalles.getTelefono() != null) proveedor.setTelefono(proveedorDetalles.getTelefono());
	    if (proveedorDetalles.getEmail() != null) proveedor.setEmail(proveedorDetalles.getEmail());
	    if (proveedorDetalles.getFacebook() != null) proveedor.setFacebook(proveedorDetalles.getFacebook());
	    if (proveedorDetalles.getInstagram() != null) proveedor.setInstagram(proveedorDetalles.getInstagram());
	    if (proveedorDetalles.getCiudad() != null) proveedor.setCiudad(proveedorDetalles.getCiudad());
	    if (proveedorDetalles.getFeedback() != null) proveedor.setFeedback(proveedorDetalles.getFeedback());
	    proveedor.setEstado(EstadoProveedor.REVISION_INICIAL);
	    
	    // Actualizar solo si se proporciona una nueva categoría, país o provincia
	    if (proveedorDetalles.getCategoriaId() != null) {
	        Optional<Categoria> categoriaOptional = categoriaRepository.findById(proveedorDetalles.getCategoriaId());
	        if (categoriaOptional.isPresent()) {
	            proveedor.setCategoria(categoriaOptional.get());
	        } else {
	            throw new Exception("No se encontró una categoría con el ID: " + proveedorDetalles.getCategoriaId());
	        }
	    }

	    if (proveedorDetalles.getPaisId() != null) {
	        Optional<Pais> paisOptional = paisRepository.findById(proveedorDetalles.getPaisId());
	        if (paisOptional.isPresent()) {
	            proveedor.setPais(paisOptional.get());
	        } else {
	            throw new Exception("No se encontró un país con el ID: " + proveedorDetalles.getPaisId());
	        }
	    }

	    if (proveedorDetalles.getProvinciaId() != null) {
	        Optional<Provincia> provinciaOptional = provinciaRepository.findById(proveedorDetalles.getProvinciaId());
	        if (provinciaOptional.isPresent()) {
	            proveedor.setProvincia(provinciaOptional.get());
	        } else {
	            throw new Exception("No se encontró una provincia con el ID: " + proveedorDetalles.getProvinciaId());
	        }
	    }

	    return proveedorRepositorio.save(proveedor);
	}
	public List<Proveedor>buscarPorNombre(String query){
		return proveedorRepositorio.findByNombreContainingIgnoreCase(query);
	}

	public Proveedor buscarProveedorPorId(Long proveedorId) throws Exception {
		// Buscar proveedor por ID
		Proveedor proveedor = proveedorRepositorio.findById(proveedorId)
				.orElseThrow(() -> new Exception("Proveedor no encontrado"));

		// Verificar el estado del proveedor y si está eliminado
		if (!proveedor.isDeleted()) {
			return proveedor;
		} else {
			throw new Exception("Proveedor no encontrado o no está activo.");
		}
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
	

	public List<Proveedor>mostrarProveedorNuevo(){
		List<Proveedor>proveedorNuevo=proveedorRepositorio.findAll();
		return proveedorNuevo.stream().filter(proveedor -> EstadoProveedor.REVISION_INICIAL.equals(proveedor.getEstado()) || EstadoProveedor.REQUIERE_CAMBIOS.equals(proveedor.getEstado()) && !proveedor.isDeleted())
									  .collect(Collectors.toList());
	}
	public List<Proveedor>mostrarTodo(){
		List<Proveedor> proveedor=proveedorRepositorio.findAll();
		return proveedor;
	}
	

	
	public Proveedor administrarProveedor(Long id,Proveedor.EstadoProveedor estado,String feedback) {
		Proveedor proveedor=proveedorRepositorio.findById(id).orElseThrow(()-> new RuntimeException("Proveedor no encontrado"));
		if(estado.equals(EstadoProveedor.ACEPTADO)) {			
			proveedor.setFechaCreacion(LocalDateTime.now());
		}
		proveedor.setEstado(estado);
		proveedor.setFeedback(feedback);
		return proveedorRepositorio.save(proveedor);
	}
	
	public List<StatusDto> misEstados (Usuario usuarioCreador) {
		List <Proveedor> proveedores = proveedorRepositorio.findByUsuario(usuarioCreador);
		List<StatusDto> misEstados = new ArrayList<StatusDto>();
		
		for (Proveedor proveedor : proveedores){
			StatusDto proveedorStatus = new StatusDto();
			proveedorStatus.setEstado(proveedor.getEstado());
			proveedorStatus.setFeedback(proveedor.getFeedback());
			proveedorStatus.setId(proveedor.getId());
			misEstados.add(proveedorStatus);
		}
		return misEstados;
	}
	
	public List<Proveedor> obtenerProveedoresAceptadosUltimaSemana() {
        LocalDateTime unaSemanaAtras = LocalDateTime.now().minusWeeks(1);
        return proveedorRepositorio.findByEstadoAndDeletedFalseAndFechaCreacionAfter(
                Proveedor.EstadoProveedor.ACEPTADO, 
                unaSemanaAtras);
    }
	
	public void enviarReporteSemanal() {
		List<Proveedor> proveedoresNuevos=obtenerProveedoresAceptadosUltimaSemana();
		
		if(!proveedoresNuevos.isEmpty()) {
			List<Usuario> todosLosUsuarios = usuarioRepositorio.findAll();
			String cuerpoEmail=emailCuerpoServicio.generarCuerpoEmail(proveedoresNuevos);
			for (Usuario usuario : todosLosUsuarios) {
				if(usuario.getRol().equals(RolDeUsuario.USUARIO))
				emailCuerpoServicio.enviarCorreo(usuario.getEmail(), "Nuevos Proveedores Aceptados de la Semana", cuerpoEmail);
			}
		}
	}
}
