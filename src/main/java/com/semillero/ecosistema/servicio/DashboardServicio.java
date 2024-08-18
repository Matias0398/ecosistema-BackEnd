package com.semillero.ecosistema.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.semillero.ecosistema.entidad.Categoria;
import com.semillero.ecosistema.entidad.Proveedor.EstadoProveedor;
import com.semillero.ecosistema.repositorio.IProveedorRepositorio;

@Service
public class DashboardServicio {
	
	@Autowired
	private IProveedorRepositorio proveedorRepositorio;
	
	public Long proveedoresAceptados() {
		return proveedorRepositorio.countByEstado(EstadoProveedor.ACEPTADO);
	}
	
	public Long proveedoresEnEspera() {
		return proveedorRepositorio.countByEstado(EstadoProveedor.REVISION_INICIAL);
	}
	
	public Long proveedoresDenegador() {
		return proveedorRepositorio.countByEstado(EstadoProveedor.DENEGADO);
	}

	public Long contarProveedoresPorCategoria(Categoria categoria) {
		return proveedorRepositorio.countByCategoria(categoria);
	}
}
