package com.semillero.ecosistema.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank(message = "El nombre no puede estar en blanco")
	private String nombre;
	@NotBlank(message = "El apellido no puede estar en blanco")
	private String apellido;
	@NotBlank(message = "El email no puede estar en blanco")
	@Email
	private String email;
	@NotNull
	@Column(columnDefinition = "boolean default false")
	private boolean deleted = false;
	private String contrasena;
	@NotBlank(message = "El rol no puede estar en blanco")
	private String rol;
	@NotBlank(message = "El telefono no puede estar en blanco")
	private int telefono;
}
