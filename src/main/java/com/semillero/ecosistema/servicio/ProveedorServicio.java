package com.semillero.ecosistema.servicio;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.opencagedata.jopencage.model.JOpenCageLatLng;
import com.opencagedata.jopencage.model.JOpenCageResponse;
import com.semillero.ecosistema.cloudinary.dto.ImageModel;
import com.semillero.ecosistema.dto.CoordenadaDto;
import com.semillero.ecosistema.dto.DistanciaDto;
import com.semillero.ecosistema.dto.ProveedorDto;
import com.semillero.ecosistema.dto.StatusDto;
import com.semillero.ecosistema.dto.UbicacionDto;
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
	private PaisProvinciaServiceImpl paisProvinciaServicio;

	@Autowired
	private UsuarioServicioImpl usuarioServicioImpl;

	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;

	@Autowired
	private ImagenServicioImpl imagenServicioImpl;

	@Autowired
	private CategoriaServicioImpl categoriaServicio;

	@Autowired
	private ICategoriaRepositorio categoriaRepository;

	@Autowired
	private IPaisRepositorio paisRepository;

	@Autowired
	private IProvinciaRepositorio provinciaRepository;
	
	@Autowired
	private GeocodingService geocodingService;
	
	@Autowired
	private EmailCuerpoServicio emailCuerpoServicio;
	    
	private static final int maxProveedores=3;
	
	public void crearProveedor(Long usuarioId, @Valid ProveedorDto proveedorDto, List<ImageModel> imageModels)throws Exception {
        int cantidadProveedores = proveedorRepositorio.countByUsuarioId(usuarioId);
        if (cantidadProveedores >= maxProveedores) {
			throw new Exception("El usuario no puede tener más de 3 proveedores");
        }
        Usuario usuario = usuarioServicioImpl.buscarPorId(usuarioId);
        Proveedor proveedorNuevo = new Proveedor();
        // Buscar y asignar Categoria
        proveedorNuevo.setCategoria(categoriaServicio.buscarPorId(proveedorDto.getCategoriaId()));
        // Buscar y asignar Pais
        proveedorNuevo.setPais(paisProvinciaServicio.obtenerPaisPorId(proveedorDto.getPaisId()));
        // Buscar y asignar Provincia
        proveedorNuevo.setProvincia(paisProvinciaServicio.obtenerProvinciaPorId(proveedorDto.getProvinciaId(),proveedorDto.getPaisId()));
        proveedorNuevo.setUsuario(usuario);
        proveedorNuevo.setEstado(EstadoProveedor.REVISION_INICIAL);
        proveedorNuevo.setNombre(proveedorDto.getNombre());
        proveedorNuevo.setTipoProveedor(proveedorDto.getTipoProveedor());
        proveedorNuevo.setCiudad(proveedorDto.getCiudad());
        proveedorNuevo.setDescripcion(proveedorDto.getDescripcion());
        proveedorNuevo.setEmail(proveedorDto.getEmail());
        proveedorNuevo.setTelefono(proveedorDto.getTelefono());
        proveedorNuevo.setFeedback("Proveedor en revisión");
        proveedorNuevo.setFacebook(proveedorDto.getFacebook());
        proveedorNuevo.setInstagram(proveedorDto.getInstagram());
        proveedorNuevo.setFechaCreacion(LocalDateTime.now());
        List<Imagen> listaImagenes = new ArrayList<>();
        for (ImageModel imageModel : imageModels) {
            if (imageModel.getFile() != null && !imageModel.getFile().isEmpty()) {
                Imagen imagen = imagenServicioImpl.crearImagen(imageModel);
                if (imagen != null) {
                    imagen.setProveedor(proveedorNuevo);
                    listaImagenes.add(imagen);
                }
            }
        }
        proveedorNuevo.setImagenes(listaImagenes);
		proveedorRepositorio.save(proveedorNuevo);
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
		Proveedor proveedor = proveedorRepositorio.findById(proveedorId)
				.orElseThrow(() -> new Exception("Proveedor no encontrado"));
		if (!proveedor.isDeleted()) {
			return proveedor;
		} else {
			throw new Exception("Proveedor no encontrado o no está activo.");
		}
	}

	public List<Proveedor> buscarPorCategoriaId(Long categoriaId) {
		List<Proveedor> proveedorActivo = proveedorRepositorio.findAll();
		return proveedorActivo
				.stream().filter(proveedor -> EstadoProveedor.ACEPTADO.equals(proveedor.getEstado())
						&& !proveedor.isDeleted() && categoriaId.equals(proveedor.getCategoria().getId()))
				.collect(Collectors.toList());
	}

	public List<Proveedor> mostrarProveedoresActivos() {
		List<Proveedor> proveedorActivo = proveedorRepositorio.findAll();
		return proveedorActivo.stream()
				.filter(proveedor -> EstadoProveedor.ACEPTADO.equals(proveedor.getEstado()) && !proveedor.isDeleted())
				.collect(Collectors.toList());
	}

	public List<Proveedor> mostrarProveedorNuevo() {
		List<Proveedor> proveedorNuevo = proveedorRepositorio.findAll();
		return proveedorNuevo.stream()
				.filter(proveedor -> EstadoProveedor.REVISION_INICIAL.equals(proveedor.getEstado())
						|| EstadoProveedor.REQUIERE_CAMBIOS.equals(proveedor.getEstado()) && !proveedor.isDeleted())
				.collect(Collectors.toList());
	}

	public List<Proveedor> mostrarTodo() {
        return proveedorRepositorio.findAll();
	}

	public Proveedor administrarProveedor(Long id, Proveedor.EstadoProveedor estado, String feedback)throws Exception {
		Proveedor proveedor = proveedorRepositorio.findById(id)
				.orElseThrow(() -> new Exception("Proveedor no encontrado"));
		proveedor.setEstado(estado);
		if (estado.equals(Proveedor.EstadoProveedor.ACEPTADO)) {
			proveedor.setFechaCreacion(LocalDateTime.now());
		}
		proveedor.setFeedback(feedback);
		return proveedorRepositorio.save(proveedor);
	}

	public List<StatusDto> misEstados(Usuario usuarioCreador) {
		List<Proveedor> proveedores = proveedorRepositorio.findByUsuario(usuarioCreador);
		List<StatusDto> misEstados = new ArrayList<>();
		for (Proveedor proveedor : proveedores) {
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
		List<Proveedor> proveedoresNuevos = obtenerProveedoresAceptadosUltimaSemana();
		if (!proveedoresNuevos.isEmpty()) {
			List<Usuario> todosLosUsuarios = usuarioRepositorio.findAll();
			String cuerpoEmail = emailCuerpoServicio.generarCuerpoEmail(proveedoresNuevos);
			for (Usuario usuario : todosLosUsuarios) {
				if (usuario.getRol().equals(RolDeUsuario.USUARIO)) {
					emailCuerpoServicio.enviarCorreo(usuario.getEmail(), "Nuevos Proveedores Aceptados de la Semana", cuerpoEmail);
				}
			}
		}
	}

	/*Método para obtener una lista de ubicaciones a partir de una lista de 
	todos los proveedores existentes en la BD*/
	public List<UbicacionDto> listarUbicaciones() {
		//llama al método que devuelve una lista de todos los proveedores existentes
		List<Proveedor> listaProveedores = mostrarTodo();
		
		//crea una lista vacía de ubicaciones
		List<UbicacionDto> listaUbicaciones = new ArrayList<>();
		
		for(Proveedor proveedor : listaProveedores) {
			//crea una variable de Ubicacion
			UbicacionDto proveedorUbicacion = new UbicacionDto();
			
			/*
			Recorre la lista de ubicaciones y por cada elemento obtiene él, id, la ciudad, provincia y pais
			asigna los valores de, id, ciudad etc. a la variable de Ubicacion
			*/
            proveedorUbicacion.setId(proveedor.getId());
			proveedorUbicacion.setCiudad(proveedor.getCiudad());
			proveedorUbicacion.setProvincia(proveedor.getProvincia());
			proveedorUbicacion.setPais(proveedor.getPais());
			
			//agrega la variable de ubicacion a la lista de ubicaciones
			listaUbicaciones.add(proveedorUbicacion);
		}
		
		return listaUbicaciones;
	}
	
	//Pasar las ubicaciones a coordenadas
	public List<CoordenadaDto> listarCoordenadas(List<UbicacionDto> listaUbicaciones) throws Exception {
		//Crea una variable para guardar la lista de coordinadas 
		List<CoordenadaDto> listaCoordenadas = new ArrayList<>();
		//recorre la lista de ubicaciones
		for(UbicacionDto ubicacion : listaUbicaciones) {
			//crea una variable de coordenada
			CoordenadaDto coordenada = new CoordenadaDto();
			//construye la query con los datos de la ubicación actual
			String query = ubicacion.getCiudad() + ", " + ubicacion.getProvincia().getNombre() + ", " + ubicacion.getPais().getNombre();
			//crea una variable en la cual se guarda un ID de la ubicación actual
			long idProveedor = ubicacion.getId();
			//llama al servicio de geocoding y le pasa la query creada anteriormente
			JOpenCageResponse response = geocodingService.doForwardRequest(query);
			//verifica que la response del servicio de geocoding no esté vacía
			if (CollectionUtils.isNotEmpty(response.getResults())) {
				/* obtiene los datos de geometry(acá se guarda la latitud y la longitud, obtenidos a partir de la query)
				de la response y los asigna a una variable */
				JOpenCageLatLng geometry =  response.getResults().get(0).getGeometry();
				
				//setea el ID de la coordenada, asignándole el valor del ID del proveedor
				coordenada.setId(idProveedor);
				//setea la latitud de coordenada, asignándole la latitud obtenida de geometry
				coordenada.setLatitud(geometry.getLat());
				//setea la longitud de coordenada, asignándole la longitud de geometry
				coordenada.setLongitud(geometry.getLng());
				
				//agrega la variable coordenada con sus datos a la lista de coordenadas
				listaCoordenadas.add(coordenada);
			} else {
				//si la response del servicio de geocoding es vacía, entonces devuelve un mensaje de error
				throw new Exception("No se encontraron coordenadas para la ubicación del proveedor con id " + idProveedor);
			}
		}
		return listaCoordenadas;
	}
	
	//método para calcular la distancia entre dos coordenadas
	private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
		int earthRadius = 6371;
	    double lat1Rad = Math.toRadians(lat1);
	    double lat2Rad = Math.toRadians(lat2);
	    double lon1Rad = Math.toRadians(lon1);
	    double lon2Rad = Math.toRadians(lon2);
	    double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
	    double y = (lat2Rad - lat1Rad);
        return Math.sqrt(x * x + y * y) * earthRadius;
	}

	private void calcularProveedoresCercanos(List<Proveedor> proveedoresCercanos, List<CoordenadaDto> listaCoordenadas, Double lat, Double lng) {
		List<DistanciaDto> listaDistancias = listaCoordenadas.stream()
				.map(coordenada -> {
					DistanciaDto distancia = new DistanciaDto();
					double latProveedor = coordenada.getLatitud();
					double lngProveedor = coordenada.getLongitud();
					double calculoDistancia = calcularDistancia(lat, lng, latProveedor, lngProveedor);
					distancia.setIdProveedor(coordenada.getId());
					distancia.setDistancia(calculoDistancia);
					return distancia;
				})
				.sorted(Comparator.comparingDouble(DistanciaDto::getDistancia))
				.limit(4)
				.toList();

		proveedoresCercanos.addAll(listaDistancias.stream()
				.map(distanciaDto -> proveedorRepositorio.findById(distanciaDto.getIdProveedor()))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.toList());
	}
	
	//método para obtener los proveedores más cercanos a partir del cálculo de distancia
	public List<Proveedor> obtenerProveedoresCercanos(Double lat, Double lng) throws Exception {
		if (Objects.nonNull(lat) && Objects.nonNull(lng)) {
			List<UbicacionDto> listaDeUbicaciones = listarUbicaciones();
			List<CoordenadaDto> listaDeCoordenadas = listarCoordenadas(listaDeUbicaciones);
			List<Proveedor> proveedoresCercanos = new ArrayList<>();
			calcularProveedoresCercanos( proveedoresCercanos, listaDeCoordenadas, lat, lng);
			return proveedoresCercanos;
		} else {
			List<Proveedor> proveedores = mostrarTodo();
			return proveedores.subList(0, 4);
		}
		 
	}
	
}
