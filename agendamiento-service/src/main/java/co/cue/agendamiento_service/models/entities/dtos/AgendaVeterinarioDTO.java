package co.cue.agendamiento_service.models.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgendaVeterinarioDTO {
    // El "Lienzo": Horarios de trabajo (Lunes 8-12, Martes 8-12, etc.)
    // El front usa esto para pintar el fondo (zonas habilitadas en blanco, deshabilitadas en gris)
    private List<JornadaLaboralResponseDTO> jornadasConfiguradas;

    // Las "Pinceladas": Bloques ocupados encima del lienzo
    // El front pinta cajitas de colores sobre las zonas blancas
    private List<OcupacionResponseDTO> ocupaciones;
}
