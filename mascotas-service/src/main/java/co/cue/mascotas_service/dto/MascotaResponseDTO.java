package co.cue.mascotas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MascotaResponseDTO {
    private Long id;
    private String nombre;
    private String especie;
    private String raza;
    private LocalDate fechaNacimiento;
    private Integer dato;
    private String sexo;
    private String color;
    private Double peso;
    private Long duenioId;
    private Boolean active;
}