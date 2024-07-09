package com.semillero.ecosistema.servicio;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.semillero.ecosistema.entidad.Pais;
import com.semillero.ecosistema.entidad.Provincia;
import com.semillero.ecosistema.repositorio.IPaisRepositorio;
import com.semillero.ecosistema.repositorio.IProvinciaRepositorio;

@Service
public class PaisProvinciaServiceImpl {

	@Autowired
	private IPaisRepositorio paisRepositorio;
	
	@Autowired
	private IProvinciaRepositorio provinciaRepositorio;
	
	public List<Pais>mostrarTodo(){
		return paisRepositorio.findAll();
	}
	
	public List<Provincia> mostrarProvinciasPorPaisId(Long paisId) {
	    Optional<Pais> opc = paisRepositorio.findById(paisId);
	    return opc.map(Pais::getProvincias).orElse(Collections.emptyList());
	}
}
