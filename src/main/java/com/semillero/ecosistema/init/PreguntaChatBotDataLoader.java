package com.semillero.ecosistema.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.semillero.ecosistema.entidad.CategoriaChatBot;
import com.semillero.ecosistema.entidad.PreguntaChatBot;
import com.semillero.ecosistema.repositorio.ICategoriaChatBotRepositorio;
import com.semillero.ecosistema.repositorio.IPreguntaChatBotRepositorio;

@Component
@Order(7)
public class PreguntaChatBotDataLoader implements CommandLineRunner {
	@Autowired
	private IPreguntaChatBotRepositorio preguntaChatBotRepositorio;
	@Autowired
	private ICategoriaChatBotRepositorio categoriaChatBotRepositorio;

	@Override
	public void run(String... args) throws Exception {
		loadPreguntaChatBotData();
	}

	private void loadPreguntaChatBotData() {
		if (preguntaChatBotRepositorio.count() == 0) {
			//obtiene los datos de las tablas relacionadas para luego sacar la categoria correspondiente
			CategoriaChatBot categoria1 = categoriaChatBotRepositorio.findByNombre("Proveedores");
			CategoriaChatBot categoria2 = categoriaChatBotRepositorio.findByNombre("Productos y servicios");
			
			//crea las Preguntas con sus datos
			PreguntaChatBot pregunta1 = new PreguntaChatBot();
			pregunta1.setId(1L);
			pregunta1.setTexto("¿Tiene algún costo aparecer como proveedor en ECOS?");
			pregunta1.setCategoria(categoria1);
			
			PreguntaChatBot pregunta2 = new PreguntaChatBot();
			pregunta2.setId(2L);
			pregunta2.setTexto("¿Cómo se decide qué categoría le corresponde al servicio/producto que brindo?");
			pregunta2.setCategoria(categoria2);
			
			PreguntaChatBot pregunta3 = new PreguntaChatBot();
			pregunta3.setId(3L);
			pregunta3.setTexto("¿Una vez que cargo mi producto/servicio aparezco inmediatamente en la plataforma?");
			pregunta3.setCategoria(categoria2);
			
			PreguntaChatBot pregunta4 = new PreguntaChatBot();
			pregunta4.setId(4L);
			pregunta4.setTexto("¿Una vez que cargue mi producto/servicio puedo editar lo que subí?");
			pregunta4.setCategoria(categoria1);
			
			PreguntaChatBot pregunta5 = new PreguntaChatBot();
			pregunta5.setId(5L);
			pregunta5.setTexto("Siendo proveedor, puedo cargar publicaciones acerca de mi servicio/producto?");
			pregunta5.setCategoria(categoria1);
			
			PreguntaChatBot pregunta6 = new PreguntaChatBot();
			pregunta6.setId(6L);
			pregunta6.setTexto("¿Puedo pagar para aparecer primero a todos los usuarios que visiten la plataforma?");
			pregunta6.setCategoria(categoria1);
			
			PreguntaChatBot pregunta7 = new PreguntaChatBot();
			pregunta7.setId(7L);
			pregunta7.setTexto("¿Puedo cargar más de un producto o servicio?");
			pregunta7.setCategoria(categoria1);

			//guarda las Preguntas
			preguntaChatBotRepositorio.save(pregunta1);
			preguntaChatBotRepositorio.save(pregunta2);
			preguntaChatBotRepositorio.save(pregunta3);
			preguntaChatBotRepositorio.save(pregunta4);
			preguntaChatBotRepositorio.save(pregunta5);
			preguntaChatBotRepositorio.save(pregunta6);
			preguntaChatBotRepositorio.save(pregunta7);
		}

	}
}

