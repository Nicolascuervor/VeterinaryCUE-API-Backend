package co.cue.agendamiento_service.models.entities.dtos;

import co.cue.agendamiento_service.models.entities.enums.TipoOcupacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OcupacionResponseDTO {

    // ID único de la ocupación (útil si el front necesita editarla o borrarla)
    private Long id;

    // ID del veterinario al que pertenece este bloqueo
    private Long veterinarioId;

    // Cuándo empieza el bloqueo (eje X inicio en el calendario)
    private LocalDateTime fechaInicio;

    // Cuándo termina el bloqueo (eje X fin en el calendario)
    private LocalDateTime fechaFin;

    // Tipo de ocupación (determina el color en el front: CITA=Azul, VACACIONES=Gris, etc.)
    private TipoOcupacion tipo;

    // ID de la cita relacionada (si es tipo CITA). Null si es un bloqueo manual.
    // Permite al front hacer clic y navegar al detalle de la cita.
    private Long referenciaExternaId;

    // Nota opcional (ej: "Almuerzo", "Reunión de personal")
    private String observacion;
}
