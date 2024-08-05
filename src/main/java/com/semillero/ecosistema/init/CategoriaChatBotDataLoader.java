package com.semillero.ecosistema.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.semillero.ecosistema.entidad.CategoriaChatBot;
import com.semillero.ecosistema.repositorio.ICategoriaChatBotRepositorio;

@Component
@Order(6)
public class CategoriaChatBotDataLoader implements CommandLineRunner {

	@Autowired
	private ICategoriaChatBotRepositorio categoriaChatBotRepositorio;

	@Override
	public void run(String... args) throws Exception {
		loadCategoriaChatBotData();
	}

	private void loadCategoriaChatBotData() {
		if (categoriaChatBotRepositorio.count() == 0) {
			//crea las Categorias con su nombre
			CategoriaChatBot categoria1 = new CategoriaChatBot();
			categoria1.setNombre("Proveedores");
			CategoriaChatBot categoria2 = new CategoriaChatBot();
			categoria2.setNombre("Productos y servicios");
			//guarda las Categorias
			categoriaChatBotRepositorio.save(categoria1);
			categoriaChatBotRepositorio.save(categoria2);
		}

	}
}
