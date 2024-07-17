package com.semillero.ecosistema.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semillero.ecosistema.entidad.Publicacion;

public interface IPublicacionRepositorio extends JpaRepository<Publicacion, Long>{

	List<Publicacion> findAllByDeletedFalse();
}
