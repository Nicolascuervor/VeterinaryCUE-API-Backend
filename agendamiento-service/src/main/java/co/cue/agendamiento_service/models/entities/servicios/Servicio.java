package co.cue.agendamiento_service.models.entities.servicios;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;


@Entity
@Table(name = "servicios")  // Define la tabla asociada en la base de datos
@Inheritance(strategy = InheritanceType.JOINED) // Especifica la estrategia de herencia JOINED para que cada subclase tenga su propia tabla unida a la tabla base
@DiscriminatorColumn(name = "tipo_servicio")   // Columna de discriminación para identificar qué subclase de Servicio representa cada fila
@Getter                                        // Genera automáticamente los getters para todos los campos
@Setter                                        // Genera automáticamente los setters para todos los campos
@NoArgsConstructor                            // Genera un constructor sin parámetros
public abstract class Servicio {             // Clase abstracta que sirve como base para todos los tipos de servicios

    @Id                                                       // Marca este campo como clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Estrategia de generación automática del ID (autoincrement)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    // Columna no nula, única y con longitud máxima de 150 caracteres
    private String nombre;

    @Column(length = 1000) // Columna opcional con longitud máxima de 1000 caracteres
    private String descripcion;

    @Column(nullable = false) // Columna no nula
    private Integer duracionPromedioMinutos;  // Duración promedio del servicio en minutos

    @Column(nullable = false, precision = 10, scale = 2)
    // Columna decimal no nula con precisión 10 y escala 2
    private BigDecimal precio;  // Precio del servicio

    @Column(nullable = false) // Columna no nula
    private boolean activo = true;   // Indica si el servicio está activo, por defecto true


    // Constructor protegido para inicializar campos de la clase base
    protected Servicio(String nombre, String descripcion, Integer duracionPromedioMinutos, BigDecimal precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracionPromedioMinutos = duracionPromedioMinutos;
        this.precio = precio;
        this.activo = true;  // Siempre inicializa activo como true
    }
}
