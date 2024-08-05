package com.semillero.ecosistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semillero.ecosistema.entidad.CategoriaChatBot;

public interface ICategoriaChatBotRepositorio extends JpaRepository<CategoriaChatBot,Long> {

	CategoriaChatBot findByNombre(String string);

}
