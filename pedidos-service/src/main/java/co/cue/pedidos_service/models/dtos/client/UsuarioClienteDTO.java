package co.cue.pedidos_service.models.dtos.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UsuarioClienteDTO {
    private String nombre;
    private String apellido;
    private String correo;
}
