package co.cue.citas_service.service;

import co.cue.citas_service.client.AgendamientoServiceClient;
import co.cue.citas_service.client.AuthServiceClient;
import co.cue.citas_service.client.MascotaServiceClient;
import co.cue.citas_service.dtos.*;
import co.cue.citas_service.dtos.enums.NotificationType;
import co.cue.citas_service.dtos.CitaConfirmacionResponseDTO;
import co.cue.citas_service.entity.Cita;
import co.cue.citas_service.entity.EstadoCita;
import co.cue.citas_service.events.CitaCompletadaEventDTO;
import co.cue.citas_service.mapper.CitaMapper;
import co.cue.citas_service.pattern.state.CitaStateFactory;
import co.cue.citas_service.pattern.state.ICitaState;
import co.cue.citas_service.repository.CitaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service("citaServiceImpl")
@AllArgsConstructor
@Slf4j
public class CitaServiceImpl implements ICitaService {

    // Constantes para strings
    private static final String TIPO_CITA = "CITA";
    private static final String DR_PREFIX = "Dr. ";
    private static final String DR_ID_PREFIX = "Dr. ID ";
    private static final String NOMBRE_MASCOTA_DEFAULT = "Tu Mascota";
    private static final String NOMBRE_MASCOTA_NA = "N/A";
    private static final String CORREO_KEY = "correo";
    private static final String NOMBRE_DUENIO_KEY = "nombreDuenio";
    private static final String NOMBRE_MASCOTA_KEY = "nombreMascota";
    private static final String FECHA_KEY = "fecha";
    private static final String MEDICO_KEY = "medico";
    private static final String TIPO_DESTINATARIO_KEY = "tipoDestinatario";
    private static final String LINK_CONFIRMACION_KEY = "linkConfirmacion";
    private static final String FECHA_ANTERIOR_KEY = "fechaAnterior";
    private static final String FECHA_NUEVA_KEY = "fechaNueva";
    private static final String MOTIVO_KEY = "motivo";
    private static final String SIN_MOTIVO_ESPECIFICADO = "Sin motivo especificado";
    private static final String CLIENTE_DEFAULT = "Cliente";
    
    // Mensajes de log
    private static final String LOG_OBTENIENDO_DATOS_MASCOTA = "Obteniendo datos de la mascota (Pet ID: {})...";
    private static final String LOG_MASCOTA_OBTENIDA = "Mascota obtenida: {}";
    private static final String LOG_ERROR_OBTENER_MASCOTA = "Error al obtener datos de la mascota. Pet ID: {}, Error: {}";
    private static final String LOG_NO_PUDO_OBTENER_MASCOTA = "No se pudo obtener nombre de la mascota. Pet ID: {}";
    private static final String LOG_OBTENIENDO_DATOS_VETERINARIO = "Obteniendo datos del veterinario (Veterinario ID: {})...";
    private static final String LOG_VETERINARIO_OBTENIDO = "Veterinario obtenido: {}";
    private static final String LOG_ERROR_OBTENER_VETERINARIO = "No se pudo obtener datos del veterinario. Veterinario ID: {}, Error: {}";
    private static final String LOG_OBTENIENDO_DATOS_DUENIO = "Obteniendo datos del dueño real (Dueño ID: {})...";
    private static final String LOG_DUENIO_OBTENIDO = "Dueño obtenido: {} {} ({})";
    private static final String LOG_NO_PUDO_OBTENER_DUENIO = "No se pudo obtener datos del dueño. Dueño ID: {}";
    private static final String LOG_ERROR_OBTENER_DUENIO = "Error al obtener datos del dueño. Dueño ID: {}, Error: {}";
    
    // Estados válidos para mensajes de error
    private static final String ESTADOS_VALIDOS = "ESPERA, CONFIRMADA, EN_PROGRESO, FINALIZADA, CANCELADA, NO_ASISTIO";

    // Repositorio de citas
    private final CitaRepository citaRepository;

    // Cliente para comunicarse con el servicio de agendamiento
    private final AgendamientoServiceClient agendamientoClient;

    // Servicio para enviar eventos a Kafka
    private final KafkaProducerService kafkaProducer;

    // Mapper para convertir entre entidades y DTOs
    private final CitaMapper mapper;

    // Factory para obtener el comportamiento de cada estado
    private final CitaStateFactory stateFactory;

    private final AuthServiceClient authClient;
    private final MascotaServiceClient mascotaClient;

    @Override
    @Transactional
    public CitaResponseDTO createCita(CitaRequestDTO dto, Long usuarioId) {
        log.info("Iniciando creación de cita para servicioId: {}", dto.getServicioId());

        ServicioClienteDTO servicio;
        try {
            servicio = agendamientoClient.getServicioById(dto.getServicioId())
                    .blockOptional()
                    .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado: " + dto.getServicioId()));
        } catch (Exception e) {
            // Capturamos si agendamiento-service falla o no encuentra el ID
            log.error("Error al consultar servicio: {}", e.getMessage());
            throw new IllegalArgumentException("No se pudo validar el servicio seleccionado. Verifique que exista y el sistema esté activo.");
        }

        LocalDateTime fechaInicio = dto.getFechaInicio();
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria.");
        }

        // Calculamos la hora fin sumando la duración del servicio
        LocalDateTime fechaFin = fechaInicio.plusMinutes(servicio.getDuracionPromedioMinutos());

        // 2.5. Obtener el dueño de la mascota (NO el usuario que crea la cita)
        // Esto es importante porque cuando un veterinario agenda una cita, el dueño debe ser el dueño de la mascota, no el veterinario
        Long duenioIdReal = null;
        try {
            log.info("Obteniendo datos de la mascota para determinar el dueño real. Pet ID: {}", dto.getPetId());
            MascotaClienteDTO mascota = mascotaClient.findMascotaById(dto.getPetId()).block();
            if (mascota != null && mascota.getDuenioId() != null) {
                duenioIdReal = mascota.getDuenioId();
                log.info("Dueño real de la mascota obtenido: Duenio ID: {}", duenioIdReal);
            } else {
                log.warn("No se pudo obtener el dueño de la mascota. Usando usuarioId del header como fallback. Pet ID: {}", dto.getPetId());
                duenioIdReal = usuarioId; // Fallback: usar el usuarioId si no se puede obtener el dueño
            }
        } catch (Exception e) {
            log.error("Error al obtener el dueño de la mascota. Usando usuarioId del header como fallback. Pet ID: {}, Error: {}", dto.getPetId(), e.getMessage(), e);
            duenioIdReal = usuarioId; // Fallback: usar el usuarioId si hay error
        }

        // 3. Crear entidad Cita (Pre-guardado para tener ID)
        Cita cita = new Cita();
        cita.setDuenioId(duenioIdReal); // Usar el dueño real de la mascota, no el usuario que crea la cita
        cita.setPetId(dto.getPetId());
        cita.setVeterinarianId(dto.getVeterinarianId());
        cita.setServicioId(servicio.getId());
        cita.setNombreServicio(servicio.getNombre());
        cita.setPrecioServicio(servicio.getPrecio());
        cita.setFechaHoraInicio(fechaInicio);
        cita.setFechaHoraFin(fechaFin);
        cita.setEstado(EstadoCita.ESPERA); // La cita inicia en ESPERA hasta que el usuario la confirme
        cita.setMotivoConsulta(dto.getMotivoConsulta());
        cita.setEstadoGeneralMascota(dto.getEstadoGeneralMascota());
        
        // Generar token único para confirmación por correo
        String tokenConfirmacion = UUID.randomUUID().toString();
        cita.setTokenConfirmacion(tokenConfirmacion);
        log.info("Token de confirmación generado para Cita: {}", tokenConfirmacion);

        // Guardamos primero para obtener el ID de la cita
        Cita citaGuardada = citaRepository.save(cita);

        // 4. Reservar en Agendamiento (Llamada al Microservicio)
        // Preparamos el DTO con el ID de la cita recién creada
        OcupacionRequestDTO reservaDTO = OcupacionRequestDTO.builder()
                .veterinarioId(dto.getVeterinarianId())
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .tipo(TIPO_CITA)
                .referenciaExternaId(citaGuardada.getId()) // ¡Clave para poder cancelar luego!
                .observacion("Cita para mascota ID: " + dto.getPetId())
                .build();

        try {
            // Llamada síncrona (block) para asegurar que la reserva sea exitosa antes de confirmar
            agendamientoClient.reservarEspacio(reservaDTO).block();
            log.info("Espacio reservado exitosamente en agendamiento para Cita ID: {}", citaGuardada.getId());
        } catch (Exception e) {
            log.error("Error al reservar espacio en agenda. Revirtiendo creación de cita.", e);
            // Si falla la reserva (ej: horario ocupado), borramos la cita local para mantener consistencia (Compensación simple)
            citaRepository.deleteById(citaGuardada.getId());
            throw new IllegalStateException("El horario seleccionado ya no está disponible o no es válido.", e);
        }

        try {
            // Usar el duenioIdReal (dueño de la mascota) para las notificaciones, no el usuarioId del header
            log.info("Iniciando envío de notificaciones para Cita ID: {}, Dueño Real ID: {} (Usuario que creó la cita: {})",
                    citaGuardada.getId(), duenioIdReal, usuarioId);
            enviarNotificacionConfirmacion(citaGuardada, duenioIdReal); // Usar el dueño real, no el usuarioId
            log.info("Notificaciones enviadas exitosamente para Cita ID: {}", citaGuardada.getId());
        } catch (Exception e) {
            log.error("La cita se creó pero falló el envío de notificación. Cita ID: {}, Error: {}", citaGuardada.getId(), e.getMessage(), e);
        }

        return mapper.mapToResponseDTO(citaGuardada);
    }

    // Actualizar cita
    @Override
    @Transactional
    public CitaUpdateDTO updateCita(Long id, CitaUpdateDTO updateDTO) {
        log.info("Actualizando cita ID: {}", id);
        Cita cita = findCitaByIdPrivado(id);

        // Guardar el estado anterior para comparar
        EstadoCita estadoAnterior = cita.getEstado();
        
        // Guardar el horario anterior para detectar cambios
        LocalDateTime fechaHoraInicioAnterior = cita.getFechaHoraInicio();
        LocalDateTime fechaHoraFinAnterior = cita.getFechaHoraFin();
        boolean horarioCambio = false;

        // Manejar transición de estado usando patrón State
        if (updateDTO.getEstado() != null && updateDTO.getEstado() != cita.getEstado()) {
            log.debug("Intentando transición de estado: {} -> {}", cita.getEstado(), updateDTO.getEstado());
            ICitaState comportamientoEstadoActual = stateFactory.getState(cita.getEstado());
            switch (updateDTO.getEstado()) {
                case CONFIRMADA -> comportamientoEstadoActual.confirmar(cita);
                case EN_PROGRESO -> comportamientoEstadoActual.iniciar(cita);
                case FINALIZADA -> comportamientoEstadoActual.finalizar(cita);
                case CANCELADA -> comportamientoEstadoActual.cancelar(cita);
                case NO_ASISTIO -> comportamientoEstadoActual.noAsistio(cita);
                default -> throw new IllegalArgumentException("Transición no soportada hacia: " + updateDTO.getEstado());
            }
        }
        
        // Manejar actualización de horario si se proporciona
        if (updateDTO.getFechaHoraInicio() != null && !updateDTO.getFechaHoraInicio().equals(fechaHoraInicioAnterior)) {
            log.info("Detectado cambio de horario para Cita ID: {}. Horario anterior: {}, Nuevo horario: {}", 
                    id, fechaHoraInicioAnterior, updateDTO.getFechaHoraInicio());
            horarioCambio = true;
            actualizarHorarioCita(cita, updateDTO, fechaHoraInicioAnterior, fechaHoraFinAnterior);
        }
        
        // Actualizar campos de la cita
        mapper.updateEntityFromDTO(updateDTO, cita);

        // Guardar cambios
        Cita citaActualizada = citaRepository.save(cita);

        // Enviar evento si la cita se finalizó (para crear historial clínico)
        if (citaActualizada.getEstado() == EstadoCita.FINALIZADA) {
            log.info("Cita {} finalizada. Enviando evento a Kafka.", id);
            CitaCompletadaEventDTO evento = mapper.mapToCitaCompletadaEvent(citaActualizada);
            kafkaProducer.enviarCitaCompletada(evento);
        }

        // Enviar notificación al cliente si cambió el estado
        // Usar el duenioId de la cita (dueño real de la mascota), no el usuario que realiza la acción
        if (estadoAnterior != citaActualizada.getEstado()) {
            try {
                Long duenioIdReal = citaActualizada.getDuenioId();
                log.info("Estado de cita cambió de {} a {}. Enviando notificación al dueño real (ID: {})",
                        estadoAnterior, citaActualizada.getEstado(), duenioIdReal);
                enviarNotificacionCambioEstado(citaActualizada, estadoAnterior, duenioIdReal);
            } catch (Exception e) {
                log.error("La cita se actualizó pero falló el envío de notificación. Cita ID: {}, Error: {}",
                        citaActualizada.getId(), e.getMessage(), e);
            }
        }
        
        // Enviar notificación si cambió el horario
        if (horarioCambio) {
            try {
                Long duenioIdReal = citaActualizada.getDuenioId();
                log.info("Horario de cita cambió. Enviando notificación de reasignación al dueño real (ID: {})", duenioIdReal);
                enviarNotificacionCambioHorario(citaActualizada, fechaHoraInicioAnterior, duenioIdReal);
            } catch (Exception e) {
                log.error("La cita se actualizó pero falló el envío de notificación de cambio de horario. Cita ID: {}, Error: {}",
                        citaActualizada.getId(), e.getMessage(), e);
            }
        }

        return updateDTO;
    }

    @Override
    @Transactional
    public void deleteCita(Long id) {
        log.warn("Iniciando cancelación de Cita ID: {}", id);

        // 1. Buscamos la cita localmente
        Cita cita = findCitaByIdPrivado(id);

        // 2. Cambiamos su estado a CANCELADA (No la borramos físicamente para historial)
        cita.setEstado(EstadoCita.CANCELADA);
        citaRepository.save(cita);

        // 3. Liberar el espacio en el microservicio de Agendamiento
        // Esto borrará la "cajita de color" (OcupacionAgenda) del calendario del veterinario.
        try {
            // Llamamos al nuevo endpoint '/interno/liberar/{referenciaId}'
            agendamientoClient.liberarEspacio(cita.getId()).block();
            log.info("Agenda liberada exitosamente para la Cita cancelada ID: {}", id);

        } catch (Exception e) {
            // Manejo de consistencia eventual:
            // Si el servicio de agendamiento falla, logueamos el error grave.
            // (En un sistema ideal, esto enviaría un evento a una cola "Dead Letter" para reintentar luego)
            log.error("¡ALERTA DE CONSISTENCIA! La cita {} se canceló localmente, pero falló la liberación de agenda en el servicio remoto. Error: {}", id, e.getMessage());

        }
    }

    // Buscar cita por ID y mapear a DTO
    @Override
    @Transactional(readOnly = true)
    public CitaResponseDTO findCitaById(Long id) {
        return citaRepository.findCitaById(id)
                .map(mapper::mapToResponseDTO) // <-- ¡Usando el Mapper!
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada con ID: " + id));
    }

    // Buscar cita por ID (privado, devuelve entidad)
    private Cita findCitaByIdPrivado(Long id) {
        return citaRepository.findCitaById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada con ID: " + id));
    }

    // Buscar citas por estado
    @Override
    @Transactional(readOnly = true)
    public List<Cita> findCitaByEstado(String estado) {
        EstadoCita estadoEnum = EstadoCita.valueOf(estado.toUpperCase());
        return citaRepository.findAllByEstado(estadoEnum);
    }

    // Buscar citas de un día específico
    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDTO> findCitasDelDia(LocalDate fecha) {
        log.info("Buscando citas para la fecha: {}", fecha);

        LocalDateTime inicioDelDia = fecha.atStartOfDay();

        LocalDateTime finDelDia = fecha.atTime(LocalTime.MAX);

        List<Cita> citasDelDia = citaRepository.findAllByFechaHoraInicioBetween(
                inicioDelDia,
                finDelDia
        );
        return citasDelDia.stream()
                .map(mapper::mapToResponseDTO)
                .toList();
    }


    private void enviarNotificacionCambioEstado(Cita cita, EstadoCita estadoAnterior, Long duenioId) {
        log.info("=== INICIANDO ENVÍO DE NOTIFICACIÓN DE CAMBIO DE ESTADO ===");
        log.info("Cita ID: {}, Estado anterior: {}, Estado nuevo: {}, Dueño ID: {}",
                cita.getId(), estadoAnterior, cita.getEstado(), duenioId);

        // Solo enviar notificación si el estado cambió y no es la primera confirmación
        if (estadoAnterior == EstadoCita.CONFIRMADA && cita.getEstado() == EstadoCita.CONFIRMADA) {
            log.info("⏭️ Omitiendo notificación: ya se envió notificación de confirmación en createCita");
            return;
        }

        try {
            UsuarioClienteDTO duenio = obtenerDatosDuenio(duenioId);
            String nombreMascota = obtenerNombreMascota(cita.getPetId());
            String nombreVeterinario = obtenerNombreVeterinario(cita.getVeterinarianId());

            if (duenio == null || duenio.getCorreo() == null || duenio.getCorreo().isEmpty()) {
                log.warn("No se puede enviar notificación. Dueño es null o no tiene correo. Cita ID: {}, Dueño ID: {}",
                        cita.getId(), duenioId);
                return;
            }

            NotificationType tipoNotificacion = determinarTipoNotificacion(cita.getEstado());
            if (tipoNotificacion == null) {
                log.debug("No se envía notificación para el estado: {}", cita.getEstado());
                return;
            }

            Map<String, String> payload = construirPayloadBasico(duenio, nombreMascota, 
                    cita.getFechaHoraInicio(), nombreVeterinario);
            
            if (cita.getEstado() == EstadoCita.CANCELADA) {
                payload.put(MOTIVO_KEY, cita.getObservaciones() != null ? cita.getObservaciones() : SIN_MOTIVO_ESPECIFICADO);
            }

            NotificationRequestDTO notificacion = new NotificationRequestDTO(tipoNotificacion, payload);
            log.info("Enviando notificación de cambio de estado al dueño: {}", duenio.getCorreo());
            kafkaProducer.enviarNotificacion(notificacion);
            log.info("✅ Notificación de cambio de estado enviada para Cita ID: {}, Estado: {}", cita.getId(), cita.getEstado());
        } catch (Exception e) {
            log.error("❌ Error al enviar notificación de cambio de estado. Cita ID: {}, Error: {}",
                    cita.getId(), e.getMessage(), e);
        }

        log.info("=== FINALIZANDO ENVÍO DE NOTIFICACIÓN DE CAMBIO DE ESTADO ===");
    }

    private void enviarNotificacionCambioHorario(Cita cita, LocalDateTime horarioAnterior, Long duenioId) {
        log.info("=== INICIANDO ENVÍO DE NOTIFICACIÓN DE CAMBIO DE HORARIO ===");
        log.info("Cita ID: {}, Horario anterior: {}, Horario nuevo: {}, Dueño ID: {}",
                cita.getId(), horarioAnterior, cita.getFechaHoraInicio(), duenioId);

        try {
            UsuarioClienteDTO duenio = obtenerDatosDuenio(duenioId);
            String nombreMascota = obtenerNombreMascota(cita.getPetId());
            String nombreVeterinario = obtenerNombreVeterinario(cita.getVeterinarianId());

            if (duenio == null || duenio.getCorreo() == null || duenio.getCorreo().isEmpty()) {
                log.warn("No se puede enviar notificación. Dueño es null o no tiene correo. Cita ID: {}, Dueño ID: {}",
                        cita.getId(), duenioId);
                return;
            }

            Map<String, String> payload = construirPayloadBasico(duenio, nombreMascota, 
                    cita.getFechaHoraInicio(), nombreVeterinario);
            payload.put(FECHA_ANTERIOR_KEY, horarioAnterior.toString().replace("T", " "));
            payload.put(FECHA_NUEVA_KEY, cita.getFechaHoraInicio().toString().replace("T", " "));

            NotificationRequestDTO notificacion = new NotificationRequestDTO(
                    NotificationType.CITA_HORARIO_REASIGNADO,
                    payload
            );

            log.info("Enviando notificación de cambio de horario al dueño: {}", duenio.getCorreo());
            kafkaProducer.enviarNotificacion(notificacion);
            log.info("✅ Notificación de cambio de horario enviada para Cita ID: {}", cita.getId());
        } catch (Exception e) {
            log.error("❌ Error al enviar notificación de cambio de horario. Cita ID: {}, Error: {}",
                    cita.getId(), e.getMessage(), e);
        }

        log.info("=== FINALIZANDO ENVÍO DE NOTIFICACIÓN DE CAMBIO DE HORARIO ===");
    }

    private void enviarNotificacionConfirmacion(Cita cita, Long usuarioId) {
        log.info("=== INICIANDO ENVÍO DE NOTIFICACIONES ===");
        log.info("Cita ID: {}, Usuario ID: {}, Veterinario ID: {}, Pet ID: {}",
                cita.getId(), usuarioId, cita.getVeterinarianId(), cita.getPetId());

        UsuarioClienteDTO duenio = obtenerDatosDuenio(usuarioId);
        UsuarioClienteDTO veterinario = obtenerDatosVeterinario(cita.getVeterinarianId());
        String nombreMascota = obtenerNombreMascota(cita.getPetId());

        boolean mismoUsuario = duenio != null && veterinario != null &&
                usuarioId.equals(cita.getVeterinarianId());

        if (mismoUsuario) {
            log.info("⚠️ Dueño y veterinario son la misma persona (ID: {}). Enviando solo un correo combinado.", usuarioId);
        }

        enviarNotificacionAlDuenio(cita, duenio, veterinario, nombreMascota, mismoUsuario);
        enviarNotificacionAlVeterinario(cita, duenio, veterinario, nombreMascota, mismoUsuario);

        log.info("=== FINALIZANDO ENVÍO DE NOTIFICACIONES ===");
    }
    
    /**
     * Envía notificación de confirmación de cita al dueño.
     */
    private void enviarNotificacionAlDuenio(Cita cita, UsuarioClienteDTO duenio, UsuarioClienteDTO veterinario,
                                           String nombreMascota, boolean mismoUsuario) {
        if (mismoUsuario) {
            log.info("⏭️ Omitiendo correo al dueño (mismo que veterinario)");
            return;
        }
        
        if (duenio == null || duenio.getCorreo() == null || duenio.getCorreo().isEmpty()) {
            log.warn("No se puede enviar correo al dueño. Dueño es null o no tiene correo. Cita ID: {}", cita.getId());
            return;
        }

        try {
            String nombreVeterinario = veterinario != null ?
                    DR_PREFIX + veterinario.getNombre() + " " + veterinario.getApellido() :
                    DR_ID_PREFIX + cita.getVeterinarianId();
            
            Map<String, String> payload = construirPayloadBasico(duenio, nombreMascota, 
                    cita.getFechaHoraInicio(), nombreVeterinario);
            payload.put(TIPO_DESTINATARIO_KEY, "DUENIO");
            
            if (cita.getTokenConfirmacion() != null) {
                payload.put(LINK_CONFIRMACION_KEY, cita.getTokenConfirmacion());
            }

            NotificationRequestDTO notificacion = new NotificationRequestDTO(
                    NotificationType.CITA_CONFIRMACION,
                    payload
            );

            log.info("Enviando notificación al dueño: {}", duenio.getCorreo());
            kafkaProducer.enviarNotificacion(notificacion);
            log.info("✅ Solicitud de notificación enviada al dueño para Cita ID: {}", cita.getId());
        } catch (Exception e) {
            log.error("Error al enviar notificación al dueño. Cita ID: {}, Error: {}", cita.getId(), e.getMessage(), e);
        }
    }
    
    /**
     * Envía notificación de confirmación de cita al veterinario.
     */
    private void enviarNotificacionAlVeterinario(Cita cita, UsuarioClienteDTO duenio, UsuarioClienteDTO veterinario,
                                                 String nombreMascota, boolean mismoUsuario) {
        if (veterinario == null || veterinario.getCorreo() == null || veterinario.getCorreo().isEmpty()) {
            log.warn("No se puede enviar correo al veterinario. Veterinario es null o no tiene correo. Cita ID: {}", cita.getId());
            return;
        }

        try {
            String nombreVeterinario = DR_PREFIX + veterinario.getNombre() + " " + veterinario.getApellido();
            Map<String, String> payload = new HashMap<>();
            payload.put(CORREO_KEY, veterinario.getCorreo());
            payload.put(NOMBRE_DUENIO_KEY, construirNombreCompletoDuenio(duenio));
            payload.put(NOMBRE_MASCOTA_KEY, nombreMascota);
            payload.put(FECHA_KEY, cita.getFechaHoraInicio().toString().replace("T", " "));
            payload.put(MEDICO_KEY, nombreVeterinario);
            payload.put(TIPO_DESTINATARIO_KEY, "VETERINARIO");
            
            if (mismoUsuario) {
                payload.put("esMismoUsuario", "true");
                log.info("📧 Enviando correo combinado (dueño y veterinario son la misma persona)");
            }

            NotificationRequestDTO notificacion = new NotificationRequestDTO(
                    NotificationType.CITA_CONFIRMACION,
                    payload
            );

            log.info("Enviando notificación al veterinario: {}", veterinario.getCorreo());
            kafkaProducer.enviarNotificacion(notificacion);
            log.info("✅ Solicitud de notificación enviada al veterinario para Cita ID: {}", cita.getId());
        } catch (Exception e) {
            log.error("Error al enviar notificación al veterinario. Cita ID: {}, Error: {}", cita.getId(), e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene los datos de un veterinario por su ID.
     * Retorna null si no se puede obtener.
     */
    private UsuarioClienteDTO obtenerDatosVeterinario(Long veterinarioId) {
        try {
            log.info(LOG_OBTENIENDO_DATOS_VETERINARIO, veterinarioId);
            UsuarioClienteDTO veterinario = authClient.obtenerUsuarioPorId(veterinarioId).block();
            if (veterinario != null) {
                log.info("Veterinario obtenido: {} {} ({})", veterinario.getNombre(), veterinario.getApellido(), veterinario.getCorreo());
                return veterinario;
            }
            log.warn("No se pudo obtener datos del veterinario. Veterinario ID: {}", veterinarioId);
        } catch (Exception e) {
            log.error("Error al obtener datos del veterinario. Veterinario ID: {}, Error: {}", veterinarioId, e.getMessage(), e);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDTO> getAllCitas() {
        log.info("Consultando todas las citas registradas en el sistema");
        return citaRepository.findAll().stream()
                .map(mapper::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CitaDetailDTO getCitaDetailById(Long id) {
        Cita cita = findCitaByIdPrivado(id); // Reutilizamos tu método privado

        // Usamos un nuevo método en el mapper para este DTO
        return mapper.mapToDetailDTO(cita);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaDetailDTO> getAllCitasDetails() {
        log.info("Consultando el reporte detallado de todas las citas...");
        return citaRepository.findAll().stream()
                // Usamos el método 'mapToDetailDTO' que escribiste manualmente en el Mapper
                .map(mapper::mapToDetailDTO)
                .toList();
    }

    @Override
    @Transactional
    public CitaConfirmacionResponseDTO confirmarCitaPorToken(String token) {
        log.info("Confirmando cita con token: {}", token);
        
        // Buscar la cita por token
        Cita cita = citaRepository.findByTokenConfirmacion(token)
                .orElseThrow(() -> new EntityNotFoundException("Token de confirmación inválido o cita no encontrada"));
        
        // Validar que la cita esté en estado ESPERA
        if (cita.getEstado() != EstadoCita.ESPERA) {
            throw new IllegalStateException("La cita ya ha sido confirmada o no está en estado de espera. Estado actual: " + cita.getEstado());
        }
        
        // Usar el patrón State para confirmar la cita
        ICitaState estadoEspera = stateFactory.getState(EstadoCita.ESPERA);
        estadoEspera.confirmar(cita);
        
        // Guardar la cita confirmada
        Cita citaConfirmada = citaRepository.save(cita);
        
        log.info("Cita ID: {} confirmada exitosamente mediante token", citaConfirmada.getId());
        
        // Obtener información completa para la respuesta
        return construirRespuestaConfirmacion(citaConfirmada, "Cita confirmada exitosamente");
    }
    
    @Override
    @Transactional(readOnly = true)
    public CitaConfirmacionResponseDTO obtenerInformacionCitaPorToken(String token) {
        log.info("Obteniendo información de cita con token: {}", token);
        
        // Buscar la cita por token
        Cita cita = citaRepository.findByTokenConfirmacion(token)
                .orElseThrow(() -> new EntityNotFoundException("Token de confirmación inválido o cita no encontrada"));
        
        // Construir respuesta con información de la cita (sin confirmarla)
        return construirRespuestaConfirmacion(cita, "Información de la cita");
    }
    
    /**
     * Construye la respuesta de confirmación con información completa de la cita.
     * Obtiene datos de mascota y veterinario desde otros servicios.
     */
    private CitaConfirmacionResponseDTO construirRespuestaConfirmacion(Cita cita, String mensaje) {
        String nombreMascota = obtenerNombreMascotaPublico(cita.getPetId());
        String nombreVeterinario = obtenerNombreVeterinarioPublico(cita.getVeterinarianId());
        
        return CitaConfirmacionResponseDTO.builder()
                .id(cita.getId())
                .estado(cita.getEstado())
                .mensaje(mensaje)
                .nombreMascota(nombreMascota)
                .petId(cita.getPetId())
                .nombreVeterinario(nombreVeterinario)
                .veterinarianId(cita.getVeterinarianId())
                .fechaHoraInicio(cita.getFechaHoraInicio())
                .fechaHoraFin(cita.getFechaHoraFin())
                .nombreServicio(cita.getNombreServicio())
                .motivoConsulta(cita.getMotivoConsulta())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDTO> obtenerCitasFuturasPorVeterinario(Long veterinarioId) {
        log.info("Obteniendo citas futuras/pendientes para veterinario ID: {}", veterinarioId);
        
        // Fecha y hora actual para filtrar solo citas futuras
        LocalDateTime fechaActual = LocalDateTime.now();
        
        // Estados considerados como pendientes/futuros
        List<EstadoCita> estadosPendientes = List.of(
                EstadoCita.ESPERA,
                EstadoCita.CONFIRMADA,
                EstadoCita.EN_PROGRESO
        );
        
        // Buscar citas del veterinario que sean futuras y estén en estados pendientes
        List<Cita> citas = citaRepository.findByVeterinarianIdAndFechaHoraInicioGreaterThanEqualAndEstadoIn(
                veterinarioId,
                fechaActual,
                estadosPendientes
        );
        
        log.info("Se encontraron {} citas futuras/pendientes para veterinario ID: {}", citas.size(), veterinarioId);
        
        // Mapear a DTOs y ordenar por fecha de inicio (más próximas primero)
        return citas.stream()
                .sorted((c1, c2) -> c1.getFechaHoraInicio().compareTo(c2.getFechaHoraInicio()))
                .map(mapper::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDTO> obtenerTodasLasCitasPorVeterinario(Long veterinarioId) {
        log.info("Obteniendo todas las citas (pasadas y futuras) para veterinario ID: {}", veterinarioId);
        
        List<Cita> citas = citaRepository.findByVeterinarianId(veterinarioId);
        
        log.info("Se encontraron {} citas para veterinario ID: {}", citas.size(), veterinarioId);
        
        // Mapear a DTOs y ordenar por fecha de inicio (más recientes primero)
        return citas.stream()
                .sorted((c1, c2) -> c2.getFechaHoraInicio().compareTo(c1.getFechaHoraInicio()))
                .map(mapper::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDTO> obtenerCitasPorVeterinarioYEstado(Long veterinarioId, String estado) {
        log.info("Obteniendo citas para veterinario ID: {} con estado: {}", veterinarioId, estado);
        
        // Convertir string a enum
        EstadoCita estadoEnum;
        try {
            estadoEnum = EstadoCita.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado de cita inválido: " + estado + 
                    ". Estados válidos: " + ESTADOS_VALIDOS);
        }
        
        List<Cita> citas = citaRepository.findByVeterinarianIdAndEstado(veterinarioId, estadoEnum);
        
        log.info("Se encontraron {} citas con estado {} para veterinario ID: {}", 
                citas.size(), estado, veterinarioId);
        
        // Mapear a DTOs y ordenar por fecha de inicio (más recientes primero)
        return citas.stream()
                .sorted((c1, c2) -> c2.getFechaHoraInicio().compareTo(c1.getFechaHoraInicio()))
                .map(mapper::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDTO> obtenerCitasFuturasPorVeterinarioYEstado(Long veterinarioId, String estado) {
        log.info("Obteniendo citas futuras para veterinario ID: {} con estado: {}", veterinarioId, estado);
        
        // Convertir string a enum
        EstadoCita estadoEnum;
        try {
            estadoEnum = EstadoCita.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado de cita inválido: " + estado + 
                    ". Estados válidos: " + ESTADOS_VALIDOS);
        }
        
        // Fecha y hora actual para filtrar solo citas futuras
        LocalDateTime fechaActual = LocalDateTime.now();
        
        List<Cita> citas = citaRepository.findByVeterinarianIdAndEstadoAndFechaHoraInicioGreaterThanEqual(
                veterinarioId, estadoEnum, fechaActual);
        
        log.info("Se encontraron {} citas futuras con estado {} para veterinario ID: {}", 
                citas.size(), estado, veterinarioId);
        
        // Mapear a DTOs y ordenar por fecha de inicio (más próximas primero)
        return citas.stream()
                .sorted((c1, c2) -> c1.getFechaHoraInicio().compareTo(c2.getFechaHoraInicio()))
                .map(mapper::mapToResponseDTO)
                .toList();
    }
    
    // ========== MÉTODOS AUXILIARES PARA REDUCIR COMPLEJIDAD ==========
    
    /**
     * Obtiene el nombre de una mascota por su ID.
     * Retorna un valor por defecto si no se puede obtener.
     */
    private String obtenerNombreMascota(Long petId) {
        try {
            log.info(LOG_OBTENIENDO_DATOS_MASCOTA, petId);
            MascotaClienteDTO mascota = mascotaClient.findMascotaById(petId).block();
            if (mascota != null && mascota.getNombre() != null) {
                log.info(LOG_MASCOTA_OBTENIDA, mascota.getNombre());
                return mascota.getNombre();
            }
            log.warn(LOG_NO_PUDO_OBTENER_MASCOTA, petId);
        } catch (Exception e) {
            log.error(LOG_ERROR_OBTENER_MASCOTA, petId, e.getMessage(), e);
        }
        return NOMBRE_MASCOTA_DEFAULT;
    }
    
    /**
     * Obtiene el nombre de una mascota por su ID (versión pública para confirmación).
     * Retorna "N/A" si no se puede obtener.
     */
    private String obtenerNombreMascotaPublico(Long petId) {
        try {
            log.info(LOG_OBTENIENDO_DATOS_MASCOTA, petId);
            MascotaClienteDTO mascota = mascotaClient.findMascotaByIdPublico(petId).block();
            if (mascota != null && mascota.getNombre() != null) {
                log.info(LOG_MASCOTA_OBTENIDA, mascota.getNombre());
                return mascota.getNombre();
            }
        } catch (Exception e) {
            log.warn(LOG_NO_PUDO_OBTENER_MASCOTA + ". Error: {}", petId, e.getMessage());
        }
        return NOMBRE_MASCOTA_NA;
    }
    
    /**
     * Obtiene el nombre completo de un veterinario por su ID.
     * Retorna un valor por defecto si no se puede obtener.
     */
    private String obtenerNombreVeterinario(Long veterinarioId) {
        try {
            log.info(LOG_OBTENIENDO_DATOS_VETERINARIO, veterinarioId);
            UsuarioClienteDTO veterinario = authClient.obtenerUsuarioPorId(veterinarioId).block();
            if (veterinario != null) {
                String nombreCompleto = DR_PREFIX + veterinario.getNombre() + " " + veterinario.getApellido();
                log.info(LOG_VETERINARIO_OBTENIDO, nombreCompleto);
                return nombreCompleto;
            }
        } catch (Exception e) {
            log.warn(LOG_ERROR_OBTENER_VETERINARIO, veterinarioId, e.getMessage());
        }
        return DR_ID_PREFIX + veterinarioId;
    }
    
    /**
     * Obtiene el nombre completo de un veterinario por su ID (versión pública para confirmación).
     * Retorna "N/A" si no se puede obtener.
     */
    private String obtenerNombreVeterinarioPublico(Long veterinarioId) {
        try {
            log.info(LOG_OBTENIENDO_DATOS_VETERINARIO, veterinarioId);
            UsuarioClienteDTO veterinario = authClient.obtenerUsuarioPorIdPublico(veterinarioId).block();
            if (veterinario != null) {
                String nombreCompleto = DR_PREFIX + veterinario.getNombre() + " " + veterinario.getApellido();
                log.info(LOG_VETERINARIO_OBTENIDO, nombreCompleto);
                return nombreCompleto;
            }
        } catch (Exception e) {
            log.warn(LOG_ERROR_OBTENER_VETERINARIO, veterinarioId, e.getMessage());
        }
        return NOMBRE_MASCOTA_NA;
    }
    
    /**
     * Obtiene los datos de un dueño por su ID.
     * Retorna null si no se puede obtener.
     */
    private UsuarioClienteDTO obtenerDatosDuenio(Long duenioId) {
        try {
            log.info(LOG_OBTENIENDO_DATOS_DUENIO, duenioId);
            UsuarioClienteDTO duenio = authClient.obtenerUsuarioPorId(duenioId).block();
            if (duenio != null) {
                log.info(LOG_DUENIO_OBTENIDO, duenio.getNombre(), duenio.getApellido(), duenio.getCorreo());
                return duenio;
            }
            log.warn(LOG_NO_PUDO_OBTENER_DUENIO, duenioId);
        } catch (Exception e) {
            log.error(LOG_ERROR_OBTENER_DUENIO, duenioId, e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Construye el nombre completo de un dueño a partir de sus datos.
     */
    private String construirNombreCompletoDuenio(UsuarioClienteDTO duenio) {
        if (duenio == null) {
            return CLIENTE_DEFAULT;
        }
        String nombre = duenio.getNombre() != null ? duenio.getNombre() : "";
        String apellido = duenio.getApellido() != null ? duenio.getApellido() : "";
        return (nombre + " " + apellido).trim();
    }
    
    /**
     * Construye un payload básico para notificaciones de citas.
     */
    private Map<String, String> construirPayloadBasico(UsuarioClienteDTO duenio, String nombreMascota, 
                                                       LocalDateTime fechaHoraInicio, String nombreVeterinario) {
        Map<String, String> payload = new HashMap<>();
        payload.put(CORREO_KEY, duenio.getCorreo());
        payload.put(NOMBRE_DUENIO_KEY, construirNombreCompletoDuenio(duenio));
        payload.put(NOMBRE_MASCOTA_KEY, nombreMascota);
        payload.put(FECHA_KEY, fechaHoraInicio.toString().replace("T", " "));
        payload.put(MEDICO_KEY, nombreVeterinario);
        return payload;
    }
    
    /**
     * Determina el tipo de notificación según el estado de la cita.
     */
    private NotificationType determinarTipoNotificacion(EstadoCita estado) {
        return switch (estado) {
            case CANCELADA -> NotificationType.CITA_CANCELADA;
            case EN_PROGRESO -> NotificationType.CITA_EN_PROGRESO;
            case FINALIZADA -> NotificationType.CITA_FINALIZADA;
            case NO_ASISTIO -> NotificationType.CITA_NO_ASISTIO;
            default -> null;
        };
    }
    
    /**
     * Actualiza el horario de una cita, liberando el espacio anterior y reservando el nuevo.
     */
    private void actualizarHorarioCita(Cita cita, CitaUpdateDTO updateDTO, 
                                      LocalDateTime fechaHoraInicioAnterior, LocalDateTime fechaHoraFinAnterior) {
        LocalDateTime nuevaFechaHoraFin = calcularNuevaFechaFin(cita, updateDTO, fechaHoraInicioAnterior, fechaHoraFinAnterior);
        updateDTO.setFechaHoraFin(nuevaFechaHoraFin);
        
        liberarEspacioAnterior(cita.getId());
        reservarNuevoEspacio(cita, updateDTO, nuevaFechaHoraFin, fechaHoraInicioAnterior, fechaHoraFinAnterior);
    }
    
    /**
     * Calcula la nueva fecha de fin basándose en la duración del servicio o la duración anterior.
     */
    private LocalDateTime calcularNuevaFechaFin(Cita cita, CitaUpdateDTO updateDTO,
                                               LocalDateTime fechaHoraInicioAnterior, LocalDateTime fechaHoraFinAnterior) {
        if (updateDTO.getFechaHoraFin() != null) {
            return updateDTO.getFechaHoraFin();
        }
        
        try {
            ServicioClienteDTO servicio = agendamientoClient.getServicioById(cita.getServicioId())
                    .blockOptional()
                    .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado: " + cita.getServicioId()));
            return updateDTO.getFechaHoraInicio().plusMinutes(servicio.getDuracionPromedioMinutos());
        } catch (Exception e) {
            log.error("Error al obtener duración del servicio. Usando duración del horario anterior.", e);
            long duracionMinutos = java.time.Duration.between(fechaHoraInicioAnterior, fechaHoraFinAnterior).toMinutes();
            return updateDTO.getFechaHoraInicio().plusMinutes(duracionMinutos);
        }
    }
    
    /**
     * Libera el espacio anterior en el servicio de agendamiento.
     */
    private void liberarEspacioAnterior(Long citaId) {
        try {
            log.info("Liberando espacio anterior en agendamiento para Cita ID: {}", citaId);
            agendamientoClient.liberarEspacio(citaId).block();
            log.info("Espacio anterior liberado exitosamente");
        } catch (Exception e) {
            log.error("Error al liberar espacio anterior. Continuando con la actualización. Error: {}", e.getMessage());
        }
    }
    
    /**
     * Reserva el nuevo espacio en el servicio de agendamiento.
     * Si falla, intenta restaurar el espacio anterior.
     */
    private void reservarNuevoEspacio(Cita cita, CitaUpdateDTO updateDTO, LocalDateTime nuevaFechaHoraFin,
                                     LocalDateTime fechaHoraInicioAnterior, LocalDateTime fechaHoraFinAnterior) {
        try {
            OcupacionRequestDTO nuevaReservaDTO = OcupacionRequestDTO.builder()
                    .veterinarioId(cita.getVeterinarianId())
                    .fechaInicio(updateDTO.getFechaHoraInicio())
                    .fechaFin(nuevaFechaHoraFin)
                    .tipo(TIPO_CITA)
                    .referenciaExternaId(cita.getId())
                    .observacion("Cita reasignada para mascota ID: " + cita.getPetId())
                    .build();
            
            log.info("Reservando nuevo espacio en agendamiento para Cita ID: {}", cita.getId());
            agendamientoClient.reservarEspacio(nuevaReservaDTO).block();
            log.info("Nuevo espacio reservado exitosamente");
        } catch (Exception e) {
            log.error("Error al reservar nuevo espacio. Revirtiendo liberación del espacio anterior.", e);
            restaurarEspacioAnterior(cita, fechaHoraInicioAnterior, fechaHoraFinAnterior);
            throw new IllegalStateException("El nuevo horario seleccionado ya no está disponible o no es válido.", e);
        }
    }
    
    /**
     * Intenta restaurar el espacio anterior cuando falla la reserva del nuevo espacio.
     */
    private void restaurarEspacioAnterior(Cita cita, LocalDateTime fechaHoraInicioAnterior, LocalDateTime fechaHoraFinAnterior) {
        try {
            OcupacionRequestDTO reservaAnteriorDTO = OcupacionRequestDTO.builder()
                    .veterinarioId(cita.getVeterinarianId())
                    .fechaInicio(fechaHoraInicioAnterior)
                    .fechaFin(fechaHoraFinAnterior)
                    .tipo(TIPO_CITA)
                    .referenciaExternaId(cita.getId())
                    .observacion("Restauración de cita para mascota ID: " + cita.getPetId())
                    .build();
            agendamientoClient.reservarEspacio(reservaAnteriorDTO).block();
            log.info("Espacio anterior restaurado");
        } catch (Exception restoreException) {
            log.error("Error crítico: No se pudo restaurar el espacio anterior. Cita ID: {}", cita.getId(), restoreException);
        }
    }
}