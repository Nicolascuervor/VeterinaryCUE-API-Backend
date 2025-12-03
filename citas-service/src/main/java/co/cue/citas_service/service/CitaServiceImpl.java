package co.cue.citas_service.service;

import co.cue.agendamiento_service.models.entities.dtos.ReservaRequestDTO;
import co.cue.citas_service.client.AgendamientoServiceClient;
import co.cue.citas_service.client.AuthServiceClient;
import co.cue.citas_service.client.MascotaServiceClient;
import co.cue.citas_service.dtos.*;
import co.cue.citas_service.dtos.enums.NotificationType;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("citaServiceImpl")
@AllArgsConstructor
@Slf4j
public class CitaServiceImpl implements ICitaService {

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

    private final AuthServiceClient authClient;       // Inyectar
    private final MascotaServiceClient mascotaClient; // Inyectar

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
        cita.setEstado(EstadoCita.CONFIRMADA); // O ESPERA, según tu flujo
        cita.setMotivoConsulta(dto.getMotivoConsulta());
        cita.setEstadoGeneralMascota(dto.getEstadoGeneralMascota());

        // Guardamos primero para obtener el ID de la cita
        Cita citaGuardada = citaRepository.save(cita);

        // 4. Reservar en Agendamiento (Llamada al Microservicio)
        // Preparamos el DTO con el ID de la cita recién creada
        OcupacionRequestDTO reservaDTO = OcupacionRequestDTO.builder()
                .veterinarioId(dto.getVeterinarianId())
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .tipo("CITA") // Coincide con el Enum en el otro servicio
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
        // Actualizar campos de la cita
        mapper.updateEntityFromDTO(updateDTO, cita);

        // Guardar cambios primero para tener la cita actualizada
        Cita citaActualizada = citaRepository.save(cita);
        mapper.updateEntityFromDTO(updateDTO, citaActualizada);

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
            // Obtener datos del Dueño REAL de la mascota (no el usuario que realiza la acción)
            UsuarioClienteDTO duenio = null;
            try {
                log.info("Obteniendo datos del dueño real (Dueño ID: {})...", duenioId);
                duenio = authClient.obtenerUsuarioPorId(duenioId).block();
                if (duenio != null) {
                    log.info("Dueño obtenido: {} {} ({})", duenio.getNombre(), duenio.getApellido(), duenio.getCorreo());
                } else {
                    log.warn("No se pudo obtener datos del dueño. Dueño ID: {}", duenioId);
                }
            } catch (Exception e) {
                log.error("Error al obtener datos del dueño. Dueño ID: {}, Error: {}", duenioId, e.getMessage(), e);
            }

            // Obtener nombre de la Mascota
            String nombreMascota = "Tu Mascota";
            try {
                log.info("Obteniendo datos de la mascota (Pet ID: {})...", cita.getPetId());
                MascotaClienteDTO mascota = mascotaClient.findMascotaById(cita.getPetId()).block();
                if (mascota != null && mascota.getNombre() != null) {
                    nombreMascota = mascota.getNombre();
                    log.info("Mascota obtenida: {}", nombreMascota);
                } else {
                    log.warn("No se pudo obtener nombre de la mascota. Pet ID: {}", cita.getPetId());
                }
            } catch (Exception e) {
                log.error("Error al obtener datos de la mascota. Pet ID: {}, Error: {}", cita.getPetId(), e.getMessage(), e);
            }

            // Obtener datos del Veterinario para el mensaje
            String nombreVeterinario = "Dr. ID " + cita.getVeterinarianId();
            try {
                log.info("Obteniendo datos del veterinario (Veterinario ID: {})...", cita.getVeterinarianId());
                UsuarioClienteDTO veterinario = authClient.obtenerUsuarioPorId(cita.getVeterinarianId()).block();
                if (veterinario != null) {
                    nombreVeterinario = "Dr. " + veterinario.getNombre() + " " + veterinario.getApellido();
                    log.info("Veterinario obtenido: {}", nombreVeterinario);
                }
            } catch (Exception e) {
                log.warn("No se pudo obtener datos del veterinario. Veterinario ID: {}, Error: {}",
                        cita.getVeterinarianId(), e.getMessage());
            }

            if (duenio != null && duenio.getCorreo() != null && !duenio.getCorreo().isEmpty()) {
                Map<String, String> payload = new HashMap<>();
                payload.put("correo", duenio.getCorreo());
                String nombreCompletoDuenio = ((duenio.getNombre() != null ? duenio.getNombre() : "") + " " +
                        (duenio.getApellido() != null ? duenio.getApellido() : "")).trim();
                payload.put("nombreDuenio", nombreCompletoDuenio);
                payload.put("nombreMascota", nombreMascota);
                payload.put("fecha", cita.getFechaHoraInicio().toString().replace("T", " "));
                payload.put("medico", nombreVeterinario);

                NotificationRequestDTO notificacion = null;
                NotificationType tipoNotificacion = null;

                // Determinar el tipo de notificación según el nuevo estado
                switch (cita.getEstado()) {
                    case CANCELADA -> {
                        tipoNotificacion = NotificationType.CITA_CANCELADA;
                        payload.put("motivo", cita.getObservaciones() != null ? cita.getObservaciones() : "Sin motivo especificado");
                    }
                    case EN_PROGRESO -> {
                        tipoNotificacion = NotificationType.CITA_EN_PROGRESO;
                    }
                    case FINALIZADA -> {
                        tipoNotificacion = NotificationType.CITA_FINALIZADA;
                    }
                    case NO_ASISTIO -> {
                        tipoNotificacion = NotificationType.CITA_NO_ASISTIO;
                    }
                    default -> {
                        log.debug("No se envía notificación para el estado: {}", cita.getEstado());
                        return;
                    }
                }

                notificacion = new NotificationRequestDTO(tipoNotificacion, payload);

                log.info("Enviando notificación de cambio de estado al dueño: {}", duenio.getCorreo());
                kafkaProducer.enviarNotificacion(notificacion);
                log.info("✅ Notificación de cambio de estado enviada para Cita ID: {}, Estado: {}", cita.getId(), cita.getEstado());
            } else {
                log.warn("No se puede enviar notificación. Dueño es null o no tiene correo. Cita ID: {}, Dueño ID: {}",
                        cita.getId(), duenioId);
            }
        } catch (Exception e) {
            log.error("❌ Error al enviar notificación de cambio de estado. Cita ID: {}, Error: {}",
                    cita.getId(), e.getMessage(), e);
        }

        log.info("=== FINALIZANDO ENVÍO DE NOTIFICACIÓN DE CAMBIO DE ESTADO ===");
    }

    private void enviarNotificacionConfirmacion(Cita cita, Long usuarioId) {
        log.info("=== INICIANDO ENVÍO DE NOTIFICACIONES ===");
        log.info("Cita ID: {}, Usuario ID: {}, Veterinario ID: {}, Pet ID: {}",
                cita.getId(), usuarioId, cita.getVeterinarianId(), cita.getPetId());

        // 1. Obtener datos del Dueño (Síncrono/Bloqueante para simplificar el flujo)
        UsuarioClienteDTO duenio = null;
        try {
            log.info("Obteniendo datos del dueño (Usuario ID: {})...", usuarioId);
            duenio = authClient.obtenerUsuarioPorId(usuarioId).block();
            if (duenio != null) {
                log.info("Dueño obtenido: {} {} ({})", duenio.getNombre(), duenio.getApellido(), duenio.getCorreo());
            } else {
                log.warn("No se pudo obtener datos del dueño. Usuario ID: {}", usuarioId);
            }
        } catch (Exception e) {
            log.error("Error al obtener datos del dueño. Usuario ID: {}, Error: {}", usuarioId, e.getMessage(), e);
        }

        // 2. Obtener datos del Veterinario
        UsuarioClienteDTO veterinario = null;
        try {
            log.info("Obteniendo datos del veterinario (Veterinario ID: {})...", cita.getVeterinarianId());
            veterinario = authClient.obtenerUsuarioPorId(cita.getVeterinarianId()).block();
            if (veterinario != null) {
                log.info("Veterinario obtenido: {} {} ({})", veterinario.getNombre(), veterinario.getApellido(), veterinario.getCorreo());
            } else {
                log.warn("No se pudo obtener datos del veterinario. Veterinario ID: {}", cita.getVeterinarianId());
            }
        } catch (Exception e) {
            log.error("Error al obtener datos del veterinario. Veterinario ID: {}, Error: {}", cita.getVeterinarianId(), e.getMessage(), e);
        }

        // 3. Obtener nombre de la Mascota (Best effort)
        String nombreMascota = "Tu Mascota";
        try {
            log.info("Obteniendo datos de la mascota (Pet ID: {})...", cita.getPetId());
            MascotaClienteDTO mascota = mascotaClient.findMascotaById(cita.getPetId()).block();
            if (mascota != null && mascota.getNombre() != null) {
                nombreMascota = mascota.getNombre(); // Usar el nombre real de la mascota
                log.info("Mascota obtenida: {}", nombreMascota);
            } else {
                log.warn("No se pudo obtener nombre de la mascota. Pet ID: {}", cita.getPetId());
            }
        } catch (Exception e) {
            log.error("Error al obtener datos de la mascota. Pet ID: {}, Error: {}", cita.getPetId(), e.getMessage(), e);
        }

        // Verificar si el dueño y el veterinario son la misma persona
        boolean mismoUsuario = duenio != null && veterinario != null &&
                usuarioId.equals(cita.getVeterinarianId());

        if (mismoUsuario) {
            log.info("⚠️ Dueño y veterinario son la misma persona (ID: {}). Enviando solo un correo combinado.", usuarioId);
        }

        // 4. Enviar correo al Dueño (solo si es diferente del veterinario)
        if (!mismoUsuario && duenio != null && duenio.getCorreo() != null && !duenio.getCorreo().isEmpty()) {
            try {
                Map<String, String> payloadDuenio = new HashMap<>();
                payloadDuenio.put("correo", duenio.getCorreo());
                String nombreCompletoDuenio = ((duenio.getNombre() != null ? duenio.getNombre() : "") + " " +
                        (duenio.getApellido() != null ? duenio.getApellido() : "")).trim();
                payloadDuenio.put("nombreDuenio", nombreCompletoDuenio);
                payloadDuenio.put("nombreMascota", nombreMascota);
                payloadDuenio.put("fecha", cita.getFechaHoraInicio().toString().replace("T", " "));
                payloadDuenio.put("medico", veterinario != null ?
                        "Dr. " + veterinario.getNombre() + " " + veterinario.getApellido() :
                        "Dr. ID " + cita.getVeterinarianId());
                payloadDuenio.put("tipoDestinatario", "DUENIO");

                NotificationRequestDTO notificacionDuenio = new NotificationRequestDTO(
                        NotificationType.CITA_CONFIRMACION,
                        payloadDuenio
                );

                log.info("Enviando notificación al dueño: {}", duenio.getCorreo());
                kafkaProducer.enviarNotificacion(notificacionDuenio);
                log.info("✅ Solicitud de notificación enviada al dueño para Cita ID: {}", cita.getId());
            } catch (Exception e) {
                log.error("Error al enviar notificación al dueño. Cita ID: {}, Error: {}", cita.getId(), e.getMessage(), e);
            }
        } else if (mismoUsuario) {
            log.info("⏭️ Omitiendo correo al dueño (mismo que veterinario)");
        } else {
            log.warn("No se puede enviar correo al dueño. Dueño es null o no tiene correo. Cita ID: {}", cita.getId());
        }

        // 5. Enviar correo al Veterinario (siempre, pero con mensaje especial si es el mismo que el dueño)
        if (veterinario != null && veterinario.getCorreo() != null && !veterinario.getCorreo().isEmpty()) {
            try {
                Map<String, String> payloadVeterinario = new HashMap<>();
                payloadVeterinario.put("correo", veterinario.getCorreo());
                String nombreCompletoCliente = duenio != null ?
                        ((duenio.getNombre() != null ? duenio.getNombre() : "") + " " +
                                (duenio.getApellido() != null ? duenio.getApellido() : "")).trim() : "Cliente";
                payloadVeterinario.put("nombreDuenio", nombreCompletoCliente);
                payloadVeterinario.put("nombreMascota", nombreMascota);
                payloadVeterinario.put("fecha", cita.getFechaHoraInicio().toString().replace("T", " "));
                payloadVeterinario.put("medico", "Dr. " + veterinario.getNombre() + " " + veterinario.getApellido());
                payloadVeterinario.put("tipoDestinatario", "VETERINARIO");
                // Agregar flag si es el mismo usuario
                if (mismoUsuario) {
                    payloadVeterinario.put("esMismoUsuario", "true");
                    log.info("📧 Enviando correo combinado (dueño y veterinario son la misma persona)");
                }

                NotificationRequestDTO notificacionVeterinario = new NotificationRequestDTO(
                        NotificationType.CITA_CONFIRMACION,
                        payloadVeterinario
                );

                log.info("Enviando notificación al veterinario: {}", veterinario.getCorreo());
                kafkaProducer.enviarNotificacion(notificacionVeterinario);
                log.info("✅ Solicitud de notificación enviada al veterinario para Cita ID: {}", cita.getId());
            } catch (Exception e) {
                log.error("Error al enviar notificación al veterinario. Cita ID: {}, Error: {}", cita.getId(), e.getMessage(), e);
            }
        } else {
            log.warn("No se puede enviar correo al veterinario. Veterinario es null o no tiene correo. Cita ID: {}", cita.getId());
        }

        log.info("=== FINALIZANDO ENVÍO DE NOTIFICACIONES ===");
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
}