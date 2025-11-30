package co.cue.inventario_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Objeto de Transferencia de Datos (DTO) para la creación de productos tipo ACCESORIO.
 * Esta clase es una especialización de ProductoRequestDTO. Se utiliza para capturar
 * la información necesaria cuando un administrador desea registrar un nuevo accesorio
 * (ej. correas, juguetes, camas, ropa) en el inventario.
 * Diseño:
 * Extiende la clase base para reutilizar los campos comunes (nombre, precio, stock)
 * y añade únicamente los atributos exclusivos de este tipo de producto, evitando
 * tener una clase "Producto" gigante con muchos campos nulos.
 */
@Getter
@Setter
public class AccesorioRequestDTO extends ProductoRequestDTO {

    /**
     * Material principal del accesorio (ej. "Cuero", "Plástico", "Algodón").
     * Dato relevante para la decisión de compra del cliente.
     */
    private String material;

    /**
     * Dimensiones o talla del accesorio (ej. "S", "M", "L", "150x50cm").
     * Fundamental para asegurar que el producto sea adecuado para la mascota.
     */
    private String tamanio;
}