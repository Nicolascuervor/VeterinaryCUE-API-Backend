package co.cue.agendamiento_service.models.entities.servicios;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "servicios_estetica") // Especifica la tabla asociada en la base de datos
@DiscriminatorValue("ESTETICA")     // Valor en la columna de discriminación para diferenciar subclases de Servicio
@Getter                             // Genera automáticamente los getters para todos los campos
@Setter                             // Genera automáticamente los setters para todos los campos
@NoArgsConstructor                // Genera un constructor sin parámetros
@AllArgsConstructor               // Genera un constructor con todos los campos como parámetros
public class Estetica extends Servicio {    // Clase que extiende Servicio, representando un servicio de estétic

    @Column(length = 100)                  // Especifica la columna para el tipo de arreglo con longitud máxima 100 caracteres
    private String tipoArreglo;           // Campo que indica el tipo de arreglo estético


    // Constructor con parámetros, llama al constructor de la clase base y asigna tipoArreglo
    public Estetica(String nombre, String descripcion, Integer duracion, BigDecimal precio, String tipoArreglo) {
        super(nombre, descripcion, duracion, precio);  // Inicializa campos de la clase base Servicio
        this.tipoArreglo = tipoArreglo;                // Inicializa el campo tipoArreglo
    }
}