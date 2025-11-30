package co.cue.agendamiento_service.models.entities.servicios;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity   // Marca la clase como una entidad JPA para persistencia en base de datos
@Table(name = "servicios_cirugia")   // Especifica la tabla en la base de datos asociada a esta entidad
@DiscriminatorValue("CIRUGIA")       // Valor usado en la columna de discriminación para diferenciar subclases de Servicio
@Getter                              // Genera automáticamente getters para todos los campos
@Setter                             // Genera automáticamente setters para todos los campos
@NoArgsConstructor                  // Genera un constructor sin parámetros
public class Cirugia extends Servicio {    // Clase que extiende Servicio, representando un servicio de cirugía

    @Column(nullable = false)    // La columna no puede ser nula
    private boolean requiereQuirofano;     // Indica si la cirugía necesita quirófano

    @Column(length = 500)        // Define la longitud máxima de la columna en la base de datos
    private String notasPreoperatorias;   // Notas o instrucciones preoperatorias para la cirugía


    // Constructor con todos los campos necesarios para inicializar la entidad
    public Cirugia(String nombre, String descripcion, Integer duracion, BigDecimal precio, boolean requiereQuirofano, String notasPreoperatorias) {
        super(nombre, descripcion, duracion, precio);  // Llama al constructor de la clase base Servicio
        this.requiereQuirofano = requiereQuirofano;    // Asigna el valor de requiereQuirofano
        this.notasPreoperatorias = notasPreoperatorias;   // Asigna las notas preoperatorias
    }
}