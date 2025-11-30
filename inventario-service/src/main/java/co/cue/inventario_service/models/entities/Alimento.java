package co.cue.inventario_service.models.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa un producto de tipo ALIMENTO en el inventario.
 *
 * Esta clase extiende de la entidad base Producto y se especializa en artículos
 * consumibles nutricionales para mascotas.
 *
 * Estrategia de Persistencia:
 * Bajo el esquema de herencia JOINED, los atributos definidos en esta clase
 * se almacenan en una tabla dedicada ('productos_alimento') que mantiene una
 * relación uno a uno con la tabla padre 'productos' mediante la clave primaria.
 *
 * El valor discriminador "ALIMENTO" permite a JPA instanciar esta clase concreta
 * automáticamente cuando se recuperan registros polimórficos.
 */
@Entity
@Table(name = "productos_alimento")
@DiscriminatorValue("ALIMENTO")
@Getter
@Setter
public class Alimento extends Producto {

    /**
     * Especie objetivo para la cual está formulado el alimento.
     *
     * Ejemplo: "Perro", "Gato", "Ave".
     * Este atributo es fundamental para la segmentación del catálogo, permitiendo
     * a los clientes filtrar rápidamente los productos relevantes para su mascota.
     */
    private String tipoMascota;

    /**
     * Peso neto del producto expresado en kilogramos.
     *
     * Este dato cumple múltiples funciones de negocio:
     * 1. Permite al cliente comparar la relación precio/cantidad.
     * 2. Es un factor clave para el cálculo de costos de envío en el servicio de Pedidos.
     * 3. Ayuda en la gestión logística y de almacenamiento físico.
     */
    private Double pesoEnKg;
}
