package com.semillero.ecosistema.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AuthResponse {

	private String jwtToken;
	private String nombre;
	private String email;
	private String rol;
}
