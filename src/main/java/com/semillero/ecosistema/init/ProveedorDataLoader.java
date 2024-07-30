package com.semillero.ecosistema.init;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.semillero.ecosistema.entidad.Categoria;
import com.semillero.ecosistema.entidad.Pais;
import com.semillero.ecosistema.entidad.Proveedor;
import com.semillero.ecosistema.entidad.Proveedor.EstadoProveedor;
import com.semillero.ecosistema.entidad.Provincia;
import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.repositorio.ICategoriaRepositorio;
import com.semillero.ecosistema.repositorio.IPaisRepositorio;
import com.semillero.ecosistema.repositorio.IProveedorRepositorio;
import com.semillero.ecosistema.repositorio.IProvinciaRepositorio;
import com.semillero.ecosistema.repositorio.IUsuarioRepositorio;

@Component
@Order(5)
public class ProveedorDataLoader implements CommandLineRunner {

	@Autowired
	private IProveedorRepositorio proveedorRepositorio;
	@Autowired
	private ICategoriaRepositorio categoriaRepositorio;
	@Autowired
	private IPaisRepositorio paisRepositorio;
	@Autowired
	private IProvinciaRepositorio provinciaRepositorio;
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;

	@Override
	public void run(String... args) throws Exception {
		loadProveedorData();
	}
	
	private void loadProveedorData() {
		if (proveedorRepositorio.count() == 0) {
			
			//obtiene los datos de las tablas relacionadas para luego sacar los id correspondientes
			Categoria categoria1 = categoriaRepositorio.findByNombre("Bienestar");
			Pais pais1 = paisRepositorio.findByNombre("Argentina");
			Provincia provincia1 = provinciaRepositorio.findByNombre("Mendoza");
			Optional<Usuario> usuario1 = usuarioRepositorio.findByEmail("ecosistematt@gmail.com");
			
			Categoria categoria2 = categoriaRepositorio.findByNombre("Capacitaciones");
			Pais pais2 = paisRepositorio.findByNombre("Argentina");
			Provincia provincia2 = provinciaRepositorio.findByNombre("Jujuy");
			Optional<Usuario> usuario2 = usuarioRepositorio.findByEmail("ecosistematt@gmail.com");;
			
			Categoria categoria3 = categoriaRepositorio.findByNombre("Construcción");
			Pais pais3 = paisRepositorio.findByNombre("Argentina");
			Provincia provincia3 = provinciaRepositorio.findByNombre("Salta");
			Optional<Usuario> usuario3 = usuarioRepositorio.findByEmail("ecosistematt@gmail.com");;
			
			Categoria categoria4 = categoriaRepositorio.findByNombre("Gastronomía");
			Pais pais4 = paisRepositorio.findByNombre("Argentina");
			Provincia provincia4 = provinciaRepositorio.findByNombre("Tucumán");
			Optional<Usuario> usuario4 = usuarioRepositorio.findByEmail("ecosistematt@gmail.com");;
			
			Categoria categoria5 = categoriaRepositorio.findByNombre("Gastronomía");
			Pais pais5 = paisRepositorio.findByNombre("Argentina");
			Provincia provincia5 = provinciaRepositorio.findByNombre("Mendoza");
			Optional<Usuario> usuario5 = usuarioRepositorio.findByEmail("ecosistematt@gmail.com");
			
			//crea los proveedores
			Proveedor proveedor1 = Proveedor.builder()
					.ciudad("Godoy Cruz")
					.deleted(false)
					.descripcion("Lavanda es un proyecto familiar. Perseguimos una cosmética efectiva")
					.tipoProveedor("Cosmética natural")
					.email("lavanda@gmail.com")
					.estado(EstadoProveedor.ACEPTADO)
					.facebook("www.facebook.com/lavanda")
					.feedback(null)
					.instagram("www.instagram.com/lavanda")
					.nombre("Lavanda")
					.telefono("3885111877")
					.categoria(categoria1)
					.pais(pais1)
					.provincia(provincia1)
					.usuario(usuario1.get())
					.imagenes(null)
					.build();
			
			Proveedor proveedor2 = Proveedor.builder()
					.ciudad("San Salvador de Jujuy")
					.deleted(false)
					.descripcion("Lila es un proyecto familiar. Perseguimos una cosmética efectiva")
					.tipoProveedor("Cosmética natural")
					.email("lila@gmail.com")
					.estado(EstadoProveedor.ACEPTADO)
					.facebook("www.facebook.com/lila")
					.feedback(null)
					.instagram("www.instagram.com/lila")
					.nombre("Lila")
					.telefono("256336699")
					.categoria(categoria2)
					.pais(pais2)
					.provincia(provincia2)
					.usuario(usuario2.get())
					.imagenes(null)
					.build();
			
			Proveedor proveedor3 = Proveedor.builder()
					.ciudad("Salta")
					.deleted(false)
					.descripcion("Hilandería Guemes es un proyecto familiar. Perseguimos una cosmética efectiva")
					.tipoProveedor("Construcción")
					.email("guemes@gmail.com")
					.estado(EstadoProveedor.ACEPTADO)
					.facebook("www.facebook.com/hilanderia")
					.feedback(null)
					.instagram("www.instagram.com/hilanderia")
					.nombre("Hilanderia Guemes")
					.telefono("3875446877")
					.categoria(categoria3)
					.pais(pais3)
					.provincia(provincia3)
					.usuario(usuario3.get())
					.imagenes(null)
					.build();
			
			Proveedor proveedor4 = Proveedor.builder()
					.ciudad("San Miguel de Tucumán")
					.deleted(false)
					.descripcion("Beto Sandwiches es un proyecto familiar. Perseguimos una cosmética efectiva")
					.tipoProveedor("Cocina natural")
					.email("beto@gmail.com")
					.estado(EstadoProveedor.ACEPTADO)
					.facebook("www.facebook.com/beto")
					.feedback(null)
					.instagram("www.instagram.com/beto")
					.nombre("Beto Sandwiches")
					.telefono("3815446877")
					.categoria(categoria4)
					.pais(pais4)
					.provincia(provincia4)
					.usuario(usuario4.get())
					.imagenes(null)
					.build();
			
			Proveedor proveedor5 = Proveedor.builder()
					.ciudad("Mendoza")
					.deleted(false)
					.descripcion("Avocado es un proyecto familiar. Perseguimos una cosmética efectiva")
					.tipoProveedor("Cocina natural")
					.email("avocado@gmail.com")
					.estado(EstadoProveedor.ACEPTADO)
					.facebook("www.facebook.com/avocado")
					.feedback(null)
					.instagram("www.instagram.com/avocado")
					.nombre("Avocado")
					.telefono("3815446877")
					.categoria(categoria5)
					.pais(pais5)
					.provincia(provincia5)
					.usuario(usuario5.get())
					.imagenes(null)
					.build();
			
			 //guarda los proveedores
			 proveedorRepositorio.save(proveedor1);
			 proveedorRepositorio.save(proveedor2);
			 proveedorRepositorio.save(proveedor3);
			 proveedorRepositorio.save(proveedor4);
			 proveedorRepositorio.save(proveedor5);


		}
	}
}
