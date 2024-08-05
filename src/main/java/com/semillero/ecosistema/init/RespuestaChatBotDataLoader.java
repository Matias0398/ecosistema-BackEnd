package com.semillero.ecosistema.init;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.semillero.ecosistema.entidad.PreguntaChatBot;
import com.semillero.ecosistema.entidad.RespuestaChatBot;
import com.semillero.ecosistema.repositorio.IPreguntaChatBotRepositorio;
import com.semillero.ecosistema.repositorio.IRespuestaChatBotRepositorio;

@Component
@Order(8)
public class RespuestaChatBotDataLoader implements CommandLineRunner {

	@Autowired
	private IRespuestaChatBotRepositorio respuestaChatBotRepositorio;
	@Autowired IPreguntaChatBotRepositorio preguntaChatBotRepositorio;
	
	@Override
	public void run(String... args) throws Exception {
		loadRespuestaChatBotData();
	}
	
	private void loadRespuestaChatBotData() {
		if (respuestaChatBotRepositorio.count() == 0) {
			
			//Obtiene los datos de la tabla relacionada para luego sacar el id correspondiente
			Optional<PreguntaChatBot> pregunta1 = preguntaChatBotRepositorio.findById(1L);
			Optional<PreguntaChatBot> pregunta2 = preguntaChatBotRepositorio.findById(2L);
			Optional<PreguntaChatBot> pregunta3 = preguntaChatBotRepositorio.findById(3L);
			Optional<PreguntaChatBot> pregunta4 = preguntaChatBotRepositorio.findById(4L);
			Optional<PreguntaChatBot> pregunta5 = preguntaChatBotRepositorio.findById(5L);
			Optional<PreguntaChatBot> pregunta6 = preguntaChatBotRepositorio.findById(6L);
			Optional<PreguntaChatBot> pregunta7 = preguntaChatBotRepositorio.findById(7L);
			
			//crea las Respuestas con sus datos
			RespuestaChatBot respuesta1 = new RespuestaChatBot();
			respuesta1.setTexto("No, no tiene ningún costo ser proveedor.");
			respuesta1.setPregunta(pregunta1.get());
			
			RespuestaChatBot respuesta2 = new RespuestaChatBot();
			respuesta2.setTexto("La categoría la decidís al momento de cargar tu producto o servicio.");
			respuesta2.setPregunta(pregunta2.get());
			
			RespuestaChatBot respuesta3 = new RespuestaChatBot();
			respuesta3.setTexto("No, no apareces inmediatamente. Debes pasar por un proceso de revisión por parte del administrador para ser aceptado. En caso de que el administrador tenga alguna duda, colocará tu servicio en revisión para que puedas responder o aclarar las inquietudes del administrador.");
			respuesta3.setPregunta(pregunta3.get());
			
			RespuestaChatBot respuesta4 = new RespuestaChatBot();
			respuesta4.setTexto("Si, siempre podés editar lo que subís. Tené en cuenta que vuelves a un proceso de revisión en caso de editar tu postulación.");
			respuesta4.setPregunta(pregunta4.get());
			
			RespuestaChatBot respuesta5 = new RespuestaChatBot();
			respuesta5.setTexto("No, no puedes cargar publicaciones. Las publicaciones las carga el administrador.");
			respuesta5.setPregunta(pregunta5.get());
			
			RespuestaChatBot respuesta6 = new RespuestaChatBot();
			respuesta6.setTexto("Actualmente no es posible abonar un servicio “Premium”. Los proveedores le aparecen al visitante por cercanía en caso de que hayan activado su ubicación. Así será más fácil que los usuarios cercanos a tu emprendimiento te encuentren.");
			respuesta6.setPregunta(pregunta6.get());
			
			RespuestaChatBot respuesta7 = new RespuestaChatBot();
			respuesta7.setTexto("Si! Podes cargar mas de un producto o servicio. Siempre por separado.");
			respuesta7.setPregunta(pregunta7.get());
			
			//guarda las Respuestas
			respuestaChatBotRepositorio.save(respuesta1);
			respuestaChatBotRepositorio.save(respuesta2);
			respuestaChatBotRepositorio.save(respuesta3);
			respuestaChatBotRepositorio.save(respuesta4);
			respuestaChatBotRepositorio.save(respuesta5);
			respuestaChatBotRepositorio.save(respuesta6);
			respuestaChatBotRepositorio.save(respuesta7);
			
		}
	}
}
