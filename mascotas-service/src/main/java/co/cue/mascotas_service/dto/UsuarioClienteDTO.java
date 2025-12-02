package co.cue.mascotas_service.dto;

import lombok.Data;

@Data
public class UsuarioClienteDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
}

