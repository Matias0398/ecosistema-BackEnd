package com.semillero.ecosistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semillero.ecosistema.entidad.Publicacion;

public interface IPublicacionRepositorio extends JpaRepository<Publicacion, Long>{

}
