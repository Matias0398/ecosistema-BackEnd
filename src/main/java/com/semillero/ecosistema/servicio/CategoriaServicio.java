package com.semillero.ecosistema.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.semillero.ecosistema.entidad.Categoria;
import com.semillero.ecosistema.repositorio.ICategoriaRepositorio;

@Service
public class CategoriaServicio {
	@Autowired
	ICategoriaRepositorio categoriaRepositorio;
	
	public List<Categoria> getCategorias(){
		return categoriaRepositorio.findAll();
	}
}
