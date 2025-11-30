package co.cue.agendamiento_service.models.entities.servicios;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity  // Marca la clase como una entidad JPA para persistencia en la base de datos
@Table(name = "servicios_vacunacion")   // Define la tabla específica para esta subclase
@DiscriminatorValue("VACUNACION")      // Valor que se usará en la columna de discriminación para identificar esta subclase
@Getter                                // Genera automáticamente los getters para todos los campos
@Setter                                // Genera automáticamente los setters para todos los campos
@NoArgsConstructor
public class Vacunacion extends Servicio {   // Subclase de Servicio especializada en vacunación
    @Column(length = 100)                     // Columna opcional con longitud máxima de 100 caracteres
    private String nombreBiologico;           // Nombre del biológico utilizado en la vacunación


    // Constructor que inicializa todos los campos, incluyendo los heredados de Servicio
    public Vacunacion(String nombre, String descripcion, Integer duracion, BigDecimal precio, String nombreBiologico) {
        super(nombre, descripcion, duracion, precio);  // Llama al constructor de la clase base
        this.nombreBiologico = nombreBiologico;        // Inicializa el campo específico de vacunación
    }
}
