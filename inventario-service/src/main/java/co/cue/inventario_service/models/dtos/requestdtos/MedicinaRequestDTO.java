package co.cue.inventario_service.models.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Objeto de Transferencia de Datos (DTO) para la creación de productos tipo MEDICINA.
 * Esta clase es una especialización de ProductoRequestDTO diseñada para capturar
 * la información farmacológica de los productos.
 * Se utiliza en el endpoint de registro de medicamentos, permitiendo almacenar
 * datos críticos de salud pública (componentes y dosificación) que no aplican
 * a otros tipos de productos del inventario.
 */
@Getter
@Setter
public class MedicinaRequestDTO extends ProductoRequestDTO {

    /**
     * Descripción detallada de los componentes activos del medicamento.
     *
     * Este campo es fundamental para el control médico y para evitar alergias
     * o interacciones negativas en los pacientes (mascotas).
     * Ejemplo: "Amoxicilina 500mg, Excipiente c.b.p.".
     */
    private String composicion;

    /**
     * Instrucciones estándar para la administración del fármaco.
     *
     * Proporciona una guía de referencia rápida para el veterinario o el dueño
     * al momento de consultar el catálogo o generar una receta.
     * Ejemplo: "1 tableta cada 12 horas por 5 días".
     */
    private String dosisRecomendada;
}
