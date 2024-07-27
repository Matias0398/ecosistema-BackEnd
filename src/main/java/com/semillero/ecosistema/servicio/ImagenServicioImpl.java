package com.semillero.ecosistema.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.semillero.ecosistema.cloudinary.dto.ImageModel;
import com.semillero.ecosistema.dto.ImageUploadResult;
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
            ImageUploadResult uploadResult = cloudinaryServicio.cargarImagen(imageModel.getFile());
            if (uploadResult != null) {
                Imagen imagen = new Imagen();
                imagen.setNombre(imageModel.getNombre()); // Establecer nombre del archivo
                imagen.setUrl(uploadResult.getUrl());
                imagen.setPublicId(uploadResult.getPublicId());
                // No es necesario guardar la imagen aquí ya que se guardará al guardar la Publicacion
                return imagen;
            } else {
                // Manejar el caso en que la subida falle
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
