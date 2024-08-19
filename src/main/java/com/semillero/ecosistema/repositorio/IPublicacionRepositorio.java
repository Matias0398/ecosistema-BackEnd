package com.semillero.ecosistema.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.semillero.ecosistema.entidad.Publicacion;

public interface IPublicacionRepositorio extends JpaRepository<Publicacion, Long>{

	List<Publicacion> findAllByDeletedFalse();
	
	@Query("SELECT p.id, p.cantidadDeVisualizaciones FROM Publicacion p")
    List<Object[]> findAllVisualizaciones();
}
