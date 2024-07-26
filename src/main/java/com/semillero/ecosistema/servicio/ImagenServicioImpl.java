package com.semillero.ecosistema.servicio;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.semillero.ecosistema.cloudinary.dto.ImageModel;
import com.semillero.ecosistema.entidad.Imagen;
import com.semillero.ecosistema.repositorio.IImagenRepositorio;

@Service
public class ImagenServicioImpl {
	
	@Autowired
	private CloudinaryServicioImpl cloudinaryServicio;
	
	@Autowired
	private IImagenRepositorio imagenRepositorio;
	
	public Imagen crearImagen(ImageModel imageModel) { 
		try {
            Imagen imagen = new Imagen();
            imagen.setNombre(imageModel.getNombre());
            imagen.setUrl(cloudinaryServicio.cargarImagen(imageModel.getFile()));
            imagenRepositorio.save(imagen);
            return imagen;
	} catch (Exception e) {
		e.printStackTrace();
        return null;
	}
}
}
