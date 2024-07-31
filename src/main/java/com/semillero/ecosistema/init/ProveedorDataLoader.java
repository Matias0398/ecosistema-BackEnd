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
			Proveedor proveedor1 = new Proveedor();
			proveedor1.setCiudad("Godoy Cruz");
			proveedor1.setDeleted(false);
			proveedor1.setDescripcion("Lavanda es un proyecto familiar. Perseguimos una cosmética efectiva");
			proveedor1.setTipoProveedor("Cosmética natural");
			proveedor1.setEmail("lavanda@gmail.com");
			proveedor1.setEstado(EstadoProveedor.ACEPTADO);
			proveedor1.setFacebook("www.facebook.com/lavanda");
			proveedor1.setFeedback(null);
			proveedor1.setInstagram("www.instagram.com/lavanda");
			proveedor1.setNombre("Lavanda");
			proveedor1.setTelefono("3885111877");
			proveedor1.setCategoria(categoria1);
			proveedor1.setPais(pais1);
			proveedor1.setProvincia(provincia1);
			proveedor1.setUsuario(usuario1.get());
			proveedor1.setImagenes(null);
			
			
			Proveedor proveedor2 = new Proveedor();
			proveedor2.setCiudad("San Salvador de Jujuy");
			proveedor2.setDeleted(false);
			proveedor2.setDescripcion("Lila es un proyecto familiar. Perseguimos una cosmética efectiva");
			proveedor2.setTipoProveedor("Cosmética natural");
			proveedor2.setEmail("lila@gmail.com");
			proveedor2.setEstado(EstadoProveedor.ACEPTADO);
			proveedor2.setFacebook("www.facebook.com/lila");
			proveedor2.setFeedback(null);
			proveedor2.setInstagram("www.instagram.com/lila");
			proveedor2.setNombre("Lila");
			proveedor2.setTelefono("256336699");
			proveedor2.setCategoria(categoria2);
			proveedor2.setPais(pais2);
			proveedor2.setProvincia(provincia2);
			proveedor2.setUsuario(usuario2.get());
			proveedor2.setImagenes(null);
			
			

			Proveedor proveedor3 = new Proveedor();
			proveedor3.setCiudad("Salta");
			proveedor3.setDeleted(false);
			proveedor3.setDescripcion("Hilandería Guemes es un proyecto familiar. Perseguimos una cosmética efectiva");
			proveedor3.setTipoProveedor("Construcción");
			proveedor3.setEmail("guemes@gmail.com");
			proveedor3.setEstado(EstadoProveedor.ACEPTADO);
			proveedor3.setFacebook("ww.facebook.com/hilanderia");
			proveedor3.setFeedback(null);
			proveedor3.setInstagram("www.instagram.com/hilanderia");
			proveedor3.setNombre("Hilanderia Guemes");
			proveedor3.setTelefono("3875446877");
			proveedor3.setCategoria(categoria3);
			proveedor3.setPais(pais3);
			proveedor3.setProvincia(provincia3);
			proveedor3.setUsuario(usuario3.get());
			proveedor3.setImagenes(null);
			
			
			Proveedor proveedor4 = new Proveedor();
			proveedor4.setCiudad("San Miguel de Tucumán");
			proveedor4.setDeleted(false);
			proveedor4.setDescripcion("Beto Sandwiches es un proyecto familiar. Perseguimos una cosmética efectiva");
			proveedor4.setTipoProveedor("Cocina natural");
			proveedor4.setEmail("beto@gmail.com");
			proveedor4.setEstado(EstadoProveedor.ACEPTADO);
			proveedor4.setFacebook("www.facebook.com/beto");
			proveedor4.setFeedback(null);
			proveedor4.setInstagram("www.instagram.com/beto");
			proveedor4.setNombre("Beto Sandwiches");
			proveedor4.setTelefono("3815446877");
			proveedor4.setCategoria(categoria4);
			proveedor4.setPais(pais4);
			proveedor4.setProvincia(provincia4);
			proveedor4.setUsuario(usuario4.get());
			proveedor4.setImagenes(null);
			
			
			Proveedor proveedor5 = new Proveedor();
			proveedor5.setCiudad("Mendoza");
			proveedor5.setDeleted(false);
			proveedor5.setDescripcion("Avocado es un proyecto familiar. Perseguimos una cosmética efectiva");
			proveedor5.setTipoProveedor("Cocina natural");
			proveedor5.setEmail("avocado@gmail.com");
			proveedor5.setEstado(EstadoProveedor.ACEPTADO);
			proveedor5.setFacebook("www.facebook.com/avocado");
			proveedor5.setFeedback(null);
			proveedor5.setInstagram("www.instagram.com/avocado");
			proveedor5.setNombre("Avocado");
			proveedor5.setTelefono("3815446877");
			proveedor5.setCategoria(categoria5);
			proveedor5.setPais(pais5);
			proveedor5.setProvincia(provincia5);
			proveedor5.setUsuario(usuario5.get());
			proveedor5.setImagenes(null);
			
			 //guarda los proveedores
			 proveedorRepositorio.save(proveedor1);
			 proveedorRepositorio.save(proveedor2);
			 proveedorRepositorio.save(proveedor3);
			 proveedorRepositorio.save(proveedor4);
			 proveedorRepositorio.save(proveedor5);


		}
	}
}
