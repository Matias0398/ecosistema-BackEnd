package com.semillero.ecosistema.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semillero.ecosistema.entidad.Proveedor;

public interface IProveedorRepositorio extends JpaRepository<Proveedor, Long>{

	int countByUsuarioId(Long usuarioId);
	List<Proveedor> findByNombreContainingIgnoreCase(String nombre);
	
	List<Proveedor> findByCategoriaId(Long categoriaId);
}
