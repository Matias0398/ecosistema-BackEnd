package com.semillero.ecosistema.servicio;

import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import jakarta.annotation.Resource;

@Service
public class CloudinaryServicioImpl {
	@Resource
	private Cloudinary cloudinary;
	
	public String cargarImagen(MultipartFile file) { //método para subir la imagen a cloudinary y obtener la url
		try {
			Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), null);
			String publicId = (String) uploadedFile.get("public_id");
	        return cloudinary.url().secure(true).generate(publicId);
		} catch (IOException e) {
			 e.printStackTrace();
			 return null;
		}
		
	}
	

}
