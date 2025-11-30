package co.cue.inventario_service.models.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa un producto de tipo ACCESORIO en el inventario.
 * Extiende la clase abstracta Producto, heredando atributos comunes como precio,
 * stock y nombre. Esta subclase se especializa en artículos duraderos o de uso
 * externo para las mascotas, como correas, juguetes, camas o ropa.
 * Estrategia de Persistencia:
 * Al usar la estrategia de herencia JOINED (definida en Producto), los datos
 * específicos de esta clase se almacenan en su propia tabla 'productos_accesorio',
 * la cual está vinculada por llave foránea a la tabla principal 'productos'.
 */
@Entity
@Table(name = "productos_accesorio")
@DiscriminatorValue("ACCESORIO")
@Getter
@Setter
public class Accesorio extends Producto {

    /**
     * Material principal de fabricación del accesorio.
     *
     * Ejemplo: "Cuero", "Nailon", "Plástico Atóxico".
     * Este atributo es relevante para la decisión de compra basada en durabilidad
     * o alergias de la mascota.
     */
    private String material;

    /**
     * Dimensiones, talla o tamaño del accesorio.
     *
     * Puede representar tallas estándar (S, M, L) o medidas específicas (ej. "120cm").
     * Es fundamental para asegurar que el producto se ajuste a la mascota del cliente.
     */
    private String tamanio;
}