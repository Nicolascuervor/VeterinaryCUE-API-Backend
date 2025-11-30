package co.cue.inventario_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Objeto de Transferencia de Datos (DTO) para la creación de productos tipo ALIMENTO.
 * Esta clase extiende de ProductoRequestDTO para heredar los atributos base (nombre, precio, stock)
 * y añade los campos específicos requeridos para catalogar comida para mascotas.
 * Su uso principal es en el endpoint de creación de alimentos, permitiendo al administrador
 * especificar detalles nutricionales o de presentación que no aplicarían a un juguete o medicina.
 */
@Getter
@Setter
public class AlimentoRequestDTO extends ProductoRequestDTO {

    /**
     * Especie objetivo del alimento (ej. "Perro", "Gato", "Hamster").
     * Este campo actúa como un filtro natural en el catálogo, permitiendo a los clientes
     * buscar rápidamente "Comida para Gatos" sin ver productos irrelevantes.
     */
    private String tipoMascota;

    /**
     * Peso neto del producto en kilogramos.
     * Dato crítico para:
     * 1. La comparación de precios (precio por kilo).
     * 2. El cálculo de costos de envío (si se implementa logística en el futuro).
     * 3. La gestión de inventario físico (espacio en almacén).
     */
    private Double pesoEnKg;
}
