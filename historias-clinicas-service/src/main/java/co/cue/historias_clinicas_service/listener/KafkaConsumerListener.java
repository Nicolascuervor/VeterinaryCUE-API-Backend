package co.cue.historias_clinicas_service.listener;

import co.cue.historias_clinicas_service.events.CitaCompletadaEventDTO;
import co.cue.historias_clinicas_service.service.IHistorialClinicoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class KafkaConsumerListener {

    // Servicio encargado de crear o actualizar los historiales clínicos
    private final IHistorialClinicoService historialClinicoService;
    // Listener que escucha mensajes del tópico "citas_completadas_topic"
    @KafkaListener(
            topics = "citas_completadas_topic",
            groupId = "${spring.kafka.consumer.group-id}"// Se obtiene del application.yml
    )
    public void handleCitaCompletada(CitaCompletadaEventDTO evento) {


        // Log para indicar la recepción del evento
        log.info("Evento 'CitaCompletadaEventDTO' recibido para Cita ID: {}", evento.getCitaId());

        try {
            // Enviar los datos del evento al servicio para registrar el historial clínico
            historialClinicoService.registrarHistorialDesdeEvento(evento);

        } catch (Exception e) {
            // Manejo de errores en caso de fallos en el procesamiento del evento
            log.error("Error al procesar CitaCompletadaEventDTO para Cita ID: {}",
                    evento.getCitaId(), e);
        }
    }
}