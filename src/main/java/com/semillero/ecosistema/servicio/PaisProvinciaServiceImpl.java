package com.semillero.ecosistema.servicio;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.semillero.ecosistema.dto.PaisDto;
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
	
	public List<PaisDto> mostrarTodo() {
        return paisRepositorio.findAll().stream()
                .map(this::convertirAPaisDto)
                .collect(Collectors.toList());
    }
	
	private PaisDto convertirAPaisDto(Pais pais) {
		PaisDto dto=new PaisDto();
		dto.setId(pais.getId());
		dto.setNombre(pais.getNombre());
		return dto;
	}

	public Pais obtenerPaisPorId(Long paisId) throws Exception {
	    return paisRepositorio.findById(paisId).orElseThrow(()->new Exception("País no encontrado con el ID:"+paisId));
	}

	public Provincia obtenerProvinciaPorId(Long provinciaId,Long paisId) throws Exception {
		Provincia provincia=provinciaRepositorio.findById(provinciaId).orElseThrow(()->new Exception("Provincia no encontrada con el ID:"+provinciaId));
		if(provincia!=null) {
			return provinciaRepositorio.findByIdAndPaisId(provinciaId, paisId).orElseThrow(()->new Exception("La provincia no pertenece al pais con el ID: "+paisId));
		}else {
			return provinciaRepositorio.findById(provinciaId).orElseThrow(() -> new Exception("Provincia no encontrada con el ID:" + provinciaId));
		}
	}
	
	public List<Provincia> mostrarProvinciasPorPaisId(Long paisId) throws Exception {
	    Optional<Pais> opc = paisRepositorio.findById(paisId);
	    return opc.map(Pais::getProvincias).orElseThrow(() -> new Exception("El pais con el id: "+paisId+" no existe"));
	}
}
