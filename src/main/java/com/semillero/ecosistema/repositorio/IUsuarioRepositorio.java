package com.semillero.ecosistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;


import com.semillero.ecosistema.entidad.Usuario;

public interface IUsuarioRepositorio extends JpaRepository<Usuario, Long> {

}
