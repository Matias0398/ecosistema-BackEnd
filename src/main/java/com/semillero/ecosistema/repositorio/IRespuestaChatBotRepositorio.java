package com.semillero.ecosistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.semillero.ecosistema.entidad.RespuestaChatBot;

public interface IRespuestaChatBotRepositorio extends JpaRepository<RespuestaChatBot,Long> {

}
