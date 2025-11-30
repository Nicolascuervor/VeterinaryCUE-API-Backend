package co.cue.agendamiento_service.models.entities.servicios;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity  // Marca la clase como una entidad JPA para persistencia en la base de datos
@Table(name = "servicios_consulta")   // Especifica la tabla en la base de datos asociada a esta entidad
@DiscriminatorValue("CONSULTA")    // Valor usado en la columna de discriminación para diferenciar subclases de Servicio
@NoArgsConstructor                 // Genera un constructor sin parámetros
public class Consulta extends Servicio {      // Clase que extiende Servicio, representando un servicio de consulta

    // Constructor con parámetros para inicializar la entidad
    public Consulta(String nombre, String descripcion, Integer duracion, BigDecimal precio) {
        super(nombre, descripcion, duracion, precio);   // Llama al constructor de la clase base Servicio

    }
}