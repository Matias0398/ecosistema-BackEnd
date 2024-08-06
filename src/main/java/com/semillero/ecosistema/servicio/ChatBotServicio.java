package com.semillero.ecosistema.servicio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.semillero.ecosistema.entidad.Pregunta;
import com.semillero.ecosistema.entidad.Respuesta;
import com.semillero.ecosistema.repositorio.IPreguntaRepositorio;
import com.semillero.ecosistema.repositorio.IRespuestaRepositorio;

@Service
public class ChatBotServicio {
	
	@Autowired
	private IPreguntaRepositorio preguntaRepositorio;
	@Autowired
	private IRespuestaRepositorio respuestaRepositorio;
	
	public Respuesta mostrarRespuesta(Long preguntaId) {
		Optional<Respuesta>respuesta= respuestaRepositorio.findById(preguntaId);
		return respuesta.orElse(null);
	}
	
	public Pregunta guardarPregunta(Pregunta pregunta) {
		return preguntaRepositorio.save(pregunta);	
	}

}
