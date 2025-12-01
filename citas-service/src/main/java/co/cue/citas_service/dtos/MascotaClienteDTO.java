package co.cue.citas_service.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MascotaClienteDTO {
    private Long id;
    private String nombre;
    private String especie;
    private String raza;
    private Long duenioId;
}
