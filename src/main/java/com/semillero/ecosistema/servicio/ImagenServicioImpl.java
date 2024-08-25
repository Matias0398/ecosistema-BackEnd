package com.semillero.ecosistema.servicio;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

    public Imagen crearImagen(ImageModel imageModel)throws Exception {
            ImageUploadResult uploadResult = cloudinaryServicio.cargarImagen(imageModel.getFile());
            if (uploadResult != null) {
                Imagen imagen = new Imagen();
                imagen.setNombre(imageModel.getNombre()); // Establecer nombre del archivo
                imagen.setUrl(uploadResult.getUrl());
                imagen.setPublicId(uploadResult.getPublicId());
                return imagen;
            } else{
                throw new Exception("No se pudo cargar la nueva imagen.");
            }
    }
    
    public Imagen actualizarImagen(Long imagenId, MultipartFile file) throws Exception {
        // Buscar la imagen existente
        Imagen imagenExistente = imagenRepositorio.findById(imagenId)
                .orElseThrow(() -> new Exception("Imagen no encontrada"));
        // Eliminar la imagen antigua de Cloudinary
        cloudinaryServicio.eliminarImagen(imagenExistente.getPublicId());
        // Subir la nueva imagen a Cloudinary
        ImageUploadResult uploadResult = cloudinaryServicio.cargarImagen(file);
        if (uploadResult != null) {
            // Actualizar los datos de la imagen en la base de datos
            imagenExistente.setNombre(file.getOriginalFilename()); // Establecer el nombre del archivo
            imagenExistente.setUrl(uploadResult.getUrl()); // URL de la nueva imagen
            imagenExistente.setPublicId(uploadResult.getPublicId()); // ID público de la nueva imagen
            // Guardar los cambios en la base de datos
            return imagenRepositorio.save(imagenExistente);
        } else {
            throw new Exception("No se pudo cargar la nueva imagen.");
        }
    }

    public void eliminarImagen(Long imagenId) throws Exception {
        if (imagenId == null) {
            throw new IllegalArgumentException("El ID de la imagen no puede ser nulo.");
        }

        // Verificar si la imagen existe antes de intentar eliminarla
        Imagen imagen = imagenRepositorio.findById(imagenId)
                .orElseThrow(() -> new NoSuchElementException("Imagen con ID " + imagenId + " no encontrada."));

        // Eliminar la imagen de Cloudinary
        cloudinaryServicio.eliminarImagen(imagen.getPublicId());
        
        // Eliminar la imagen de la base de datos
        imagenRepositorio.delete(imagen);
    }

    public List<ImageModel>convertirFilesAImageModels(List<MultipartFile>files){
        List<ImageModel> imageModels = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String nombreArchivo = file.getOriginalFilename();
                // Validar el tamaño y tipo de archivo si es necesario
                ImageModel imageModel = new ImageModel();
                imageModel.setFile(file);
                imageModel.setNombre(nombreArchivo);
                imageModels.add(imageModel);
            }
        }
        return imageModels;
    }
}