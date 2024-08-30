package com.semillero.ecosistema.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProveedorDto {

	public enum EstadoProveedorDTO {
        REVISION_INICIAL,
        ACEPTADO,
        DENEGADO,
        REQUIERE_CAMBIOS,
        CAMBIOS_REALIZADOS
    }

    private Long id;

    @NotBlank(message = "El nombre no puede estar en blanco")
    private String nombre;

    @NotBlank(message = "La descripción no puede estar en blanco")
    @Size(max = 300, message = "La descripcion no puede tener mas de 300 caracteres")
    private String descripcion;

    @NotBlank(message = "El tipo de proveedor no puede estar en blanco")
    @Size(max=50,message="No puede tener mas de 50 caracteres")
    private String tipoProveedor;

    @NotBlank(message = "El contacto no puede estar en blanco")
    private String telefono;

    @NotBlank(message = "El email no puede estar en blanco")
    @Email
    private String email;

    private String facebook;

    private String instagram;

    @NotBlank(message = "La ciudad no puede estar en blanco")
    private String ciudad;

    private EstadoProveedorDTO estado;

    private Long paisId;

    private Long provinciaId;

    private Long categoriaId;

    private Long usuarioId;

    private String feedback;

    private boolean deleted = false;


}
