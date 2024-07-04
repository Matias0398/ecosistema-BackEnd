package com.semillero.ecosistema.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.semillero.ecosistema.entidad.Categoria;
import com.semillero.ecosistema.servicio.CategoriaServicio;

@RestController
public class CategoriaControlador {
	@Autowired
	CategoriaServicio categoriaServicio;
	
	@GetMapping(value="/categorias")
	public List<Categoria> getListaCategorias(){
		return categoriaServicio.getCategorias();
	}
}
