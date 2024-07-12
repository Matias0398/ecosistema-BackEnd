package com.semillero.ecosistema.servicio;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.semillero.ecosistema.entidad.Publicacion;
import com.semillero.ecosistema.repositorio.IPublicacionRepositorio;

@Service
@Transactional
public class PublicacionServicioImpl {

	@Autowired
	private IPublicacionRepositorio publicacionRepositorio;
	
	public Publicacion crearPublicacion(Publicacion publicacion) {
		publicacion.setFechaDeCreacion(new Date());
		return publicacionRepositorio.save(publicacion);
	}
	
	public List<Publicacion> obtenerPublicaciones() {
		return publicacionRepositorio.findAll();
	}
	
	public List<Publicacion> obtenerPublicacionesActivas(){
		return publicacionRepositorio.findAllByDeletedFalse();
	}
	
	public Optional<Publicacion> buscarPublicacionPorId(Long id) {
		return publicacionRepositorio.findById(id);
	}
	
	public void incrementarVisualizaciones(Publicacion publicacion) {
			publicacion.setCantidadDeVisualizaciones(publicacion.getCantidadDeVisualizaciones() + 1);
			publicacionRepositorio.save(publicacion);
	}
	
	public boolean cambiarEstado(Long id) {
		Optional <Publicacion> opcPublicacion = buscarPublicacionPorId(id);
		if (opcPublicacion.isPresent()) {
			Publicacion publicacion = opcPublicacion.get();
			publicacion.setDeleted(!publicacion.isDeleted());
			publicacionRepositorio.save(publicacion);
			
			return true;
		} 
		return false;
	}
	
	public boolean editarPublicacion(Long id, Publicacion publicacion) {
		Optional <Publicacion> opcPublicacion = buscarPublicacionPorId(id);
		if(opcPublicacion.isPresent()) {
			Publicacion publicacionBD = opcPublicacion.get();
			publicacionBD.setTitulo(publicacion.getTitulo());
			publicacionBD.setDescripcion(publicacion.getDescripcion());
			publicacionBD.setDeleted(publicacion.isDeleted());
			publicacionBD.setFechaDeCreacion(publicacion.getFechaDeCreacion());
			publicacionBD.setImagenes(publicacion.getImagenes());
			publicacionBD.setUsuarioCreador(publicacion.getUsuarioCreador());
			publicacionBD.setCantidadDeVisualizaciones(publicacion.getCantidadDeVisualizaciones());
			
			publicacionRepositorio.save(publicacionBD);
			
			return true;
		}
		return false;
	}
	

	


	
	
	
	
}
