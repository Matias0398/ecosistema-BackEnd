package com.semillero.ecosistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semillero.ecosistema.entidad.Proveedor;

public interface IProveedorRepositorio extends JpaRepository<Proveedor, Long>{

	int countByUsuarioId(Long usuarioId);
}
