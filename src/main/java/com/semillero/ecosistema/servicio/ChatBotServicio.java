package com.semillero.ecosistema.servicio;

import org.springframework.beans.factory.annotation.Autowired;

import com.semillero.ecosistema.entidad.Respuesta;
import com.semillero.ecosistema.repositorio.IPreguntaRepositorio;
import com.semillero.ecosistema.repositorio.IRespuestaRepositorio;

public class ChatBotServicio {
	
	@Autowired
	private IPreguntaRepositorio preguntaRepositorio;
	@Autowired
	private IRespuestaRepositorio respuestaRepositorio;
	
	public Respuesta mostrarRespuesta() {
		
		return null;
	}

}
