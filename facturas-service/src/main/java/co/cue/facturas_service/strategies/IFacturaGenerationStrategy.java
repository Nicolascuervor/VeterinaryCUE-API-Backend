package co.cue.facturas_service.strategies;

import co.cue.facturas_service.models.enums.TipoFactura;

public interface IFacturaGenerationStrategy {
    // Método principal que cada estrategia de facturación debe implementar.
    // Recibe un evento genérico (generalmente un DTO de Kafka) y ejecuta
    // la lógica específica para generar una factura según el tipo.
    void generarFactura(Object eventPayload);


    // Retorna el tipo de factura que maneja esta estrategia.
    // Esto permite que el orquestador seleccione dinámicamente la estrategia correcta.
    TipoFactura getTipo();
}
