package co.cue.historias_clinicas_service.service;

import co.cue.historias_clinicas_service.client.AuthServiceClient;
import co.cue.historias_clinicas_service.client.MascotaClienteDTO;
import co.cue.historias_clinicas_service.client.MascotaServiceClient;
import co.cue.historias_clinicas_service.client.UsuarioClienteDTO;
import co.cue.historias_clinicas_service.dto.HistorialClinicoRequestDTO;
import co.cue.historias_clinicas_service.dto.HistorialClinicoResponseDTO;
import co.cue.historias_clinicas_service.dto.NotificationRequestDTO;
import co.cue.historias_clinicas_service.dto.NotificationType;
import co.cue.historias_clinicas_service.entity.HistorialClinico;
import co.cue.historias_clinicas_service.events.CitaCompletadaEventDTO;
import co.cue.historias_clinicas_service.mapper.HistorialClinicoMapper;
import co.cue.historias_clinicas_service.repository.HistorialClinicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class HistorialClinicoServiceImpl implements IHistorialClinicoService {
    private final HistorialClinicoRepository historialClinicoRepository;
    private final HistorialClinicoMapper mapper;
    private final MascotaServiceClient mascotaClient;
    private final AuthServiceClient authServiceClient;
    private final KafkaProducerService kafkaProducerService;
    @Override
    @Transactional
    public void registrarHistorialDesdeEvento(CitaCompletadaEventDTO event) {
        if (historialClinicoRepository.existsByCitaId(event.getCitaId())) {
            log.warn("Evento de Cita ID: {} recibido, pero ya existe un historial. Ignorando (Idempotencia).", event.getCitaId());
            return;
        }
        
        // Validar campos requeridos antes de crear el historial
        if (event.getPetId() == null) {
            log.error("Error: El evento de Cita ID: {} no tiene petId. No se puede crear el historial.", event.getCitaId());
            return;
        }
        if (event.getVeterinarianId() == null) {
            log.error("Error: El evento de Cita ID: {} no tiene veterinarianId. No se puede crear el historial.", event.getCitaId());
            return;
        }
        if (event.getFecha() == null) {
            log.error("Error: El evento de Cita ID: {} no tiene fecha. No se puede crear el historial.", event.getCitaId());
            return;
        }
        if (event.getDiagnostico() == null || event.getDiagnostico().trim().isEmpty()) {
            log.warn("Advertencia: El evento de Cita ID: {} no tiene diagnóstico. Se establecerá un valor por defecto.", event.getCitaId());
            // El mapper manejará esto estableciendo un valor por defecto
        }
        
        log.info("Procesando evento para Cita ID: {}. Creando nuevo registro de historial.", event.getCitaId());
        HistorialClinico nuevoHistorial = mapper.mapEventToEntity(event);
        
        // Asegurar que el diagnóstico no sea null (requerido por la BD)
        if (nuevoHistorial.getDiagnostico() == null || nuevoHistorial.getDiagnostico().trim().isEmpty()) {
            nuevoHistorial.setDiagnostico("Sin diagnóstico registrado");
            log.info("Se estableció diagnóstico por defecto para Cita ID: {}", event.getCitaId());
        }
        
        HistorialClinico guardado = historialClinicoRepository.save(nuevoHistorial);
        log.info("Historial clínico creado exitosamente para Cita ID: {}", event.getCitaId());
        
        // Enviar notificación al dueño de la mascota (fuera de la transacción para no afectar el guardado)
        try {
            enviarNotificacionHistorialCreado(guardado);
        } catch (Exception e) {
            log.error("Error al enviar notificación de historial clínico creado. El historial fue guardado correctamente. Cita ID: {}", 
                    event.getCitaId(), e);
            // No lanzamos la excepción para que la creación del historial no falle si la notificación falla
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<HistorialClinicoResponseDTO> findMedicalRecordsByPetId(Long petId, Long usuarioId) {

        validarPropiedadMascota(petId, usuarioId);

        log.info("Usuario {} autorizado. Buscando historiales para mascota {}", usuarioId, petId);

        return historialClinicoRepository.findByPetId(petId) //
                .stream()
                .map(mapper::mapEntityToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HistorialClinicoResponseDTO findMedicalRecordById(Long historialId, Long usuarioId) {
        HistorialClinico historial = findEntidadById(historialId);
        validarPropiedadMascota(historial.getPetId(), usuarioId);
        log.info("Usuario {} autorizado para ver historial ID {}", usuarioId, historialId);
        return mapper.mapEntityToResponseDTO(historial);
    }

    @Override
    @Transactional
    public HistorialClinicoResponseDTO createHistorialMedico(HistorialClinicoRequestDTO requestDTO, Long veterinarioId) {
        log.info("Veterinario {} creando historial manual para mascota {}", veterinarioId, requestDTO.getPetId());
        
        // Validar campos requeridos
        if (requestDTO.getPetId() == null) {
            throw new IllegalArgumentException("El ID de la mascota es obligatorio");
        }
        if (requestDTO.getFecha() == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        
        HistorialClinico nuevoHistorial = mapper.mapRequestToEntity(requestDTO);
        nuevoHistorial.setVeterinarianId(veterinarioId); // Asignamos el ID del Vet que crea
        
        // Asegurar que el diagnóstico no sea null (requerido por la BD)
        if (nuevoHistorial.getDiagnostico() == null || nuevoHistorial.getDiagnostico().trim().isEmpty()) {
            nuevoHistorial.setDiagnostico("Sin diagnóstico registrado");
            log.info("Se estableció diagnóstico por defecto para mascota ID: {}", requestDTO.getPetId());
        }
        
        HistorialClinico guardado = historialClinicoRepository.save(nuevoHistorial);
        log.info("Historial clínico creado exitosamente con ID: {}", guardado.getId());
        
        // Enviar notificación al dueño de la mascota (fuera de la transacción para no afectar el guardado)
        try {
            enviarNotificacionHistorialCreado(guardado);
        } catch (Exception e) {
            log.error("Error al enviar notificación de historial clínico creado. El historial fue guardado correctamente. ID: {}", 
                    guardado.getId(), e);
            // No lanzamos la excepción para que la creación del historial no falle si la notificación falla
        }
        
        return mapper.mapEntityToResponseDTO(guardado);
    }

    @Override
    @Transactional
    public HistorialClinicoResponseDTO updateHistorialMedico(Long historialId, HistorialClinicoRequestDTO requestDTO, Long veterinarioId) {
        log.info("Veterinario {} actualizando historial ID {}", veterinarioId, historialId);

        HistorialClinico historialExistente = findEntidadById(historialId);

        historialExistente.setDiagnostico(requestDTO.getDiagnostico());
        historialExistente.setTratamiento(requestDTO.getTratamiento());
        historialExistente.setObservaciones(requestDTO.getObservaciones());
        historialExistente.setPeso(requestDTO.getPeso());
        historialExistente.setTemperatura(requestDTO.getTemperatura());
        historialExistente.setFrecuenciaCardiaca(requestDTO.getFrecuenciaCardiaca());
        historialExistente.setFrecuenciaRespiratoria(requestDTO.getFrecuenciaRespiratoria());
        historialExistente.setEstadoGeneral(requestDTO.getEstadoGeneral());
        historialExistente.setExamenesRealizados(requestDTO.getExamenesRealizados());
        historialExistente.setMedicamentosRecetados(requestDTO.getMedicamentosRecetados());
        historialExistente.setProximaCita(requestDTO.getProximaCita());

        HistorialClinico guardado = historialClinicoRepository.save(historialExistente);
        return mapper.mapEntityToResponseDTO(guardado);
    }

    @Override
    @Transactional
    public void deleteHistorialMedico(Long historialId, Long usuarioId) {
        log.warn("Usuario {} intentando desactivar historial ID {}", usuarioId, historialId);
        HistorialClinico historial = findEntidadById(historialId);
        validarPropiedadMascota(historial.getPetId(), usuarioId);

        historial.setActivo(false);
        historialClinicoRepository.save(historial);
        log.info("Historial ID {} desactivado exitosamente.", historialId);
    }


    private HistorialClinico findEntidadById(Long id) {
        return historialClinicoRepository.findById(id)
                .filter(HistorialClinico::getActivo) // Solo buscar activos
                .orElseThrow(() -> new EntityNotFoundException("Historial Clínico no encontrado con ID: " + id));
    }

    private void validarPropiedadMascota(Long petId, Long usuarioId) {
        log.debug("Validando propiedad de mascota {} para usuario {}", petId, usuarioId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdminOrVet = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_VETERINARIO"));
        if (isAdminOrVet) {
            log.info("Acceso autorizado por ROL (ADMIN/VETERINARIO) para usuario {}", usuarioId);
            return;
        }
        MascotaClienteDTO mascota = mascotaClient.findMascotaById(petId)
                .blockOptional()
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada: " + petId));

        if (!mascota.getDuenioId().equals(usuarioId)) {
            log.warn("¡Acceso Denegado! Usuario {} intentó acceder a datos de mascota {} que pertenece a dueño {}",
                    usuarioId, petId, mascota.getDuenioId());
            throw new AccessDeniedException("No tiene permiso para ver este historial clínico.");
        }

        log.info("Acceso autorizado por PROPIEDAD (DUEÑO) para usuario {}", usuarioId);
    }

    /**
     * Envía una notificación por correo electrónico al dueño de la mascota
     * cuando se crea un nuevo historial clínico.
     */
    private void enviarNotificacionHistorialCreado(HistorialClinico historial) {
        try {
            log.info("Preparando notificación de historial clínico creado para mascota ID: {}", historial.getPetId());
            
            // Obtener información de la mascota
            MascotaClienteDTO mascota = mascotaClient.findMascotaById(historial.getPetId())
                    .blockOptional()
                    .orElse(null);
            
            if (mascota == null) {
                log.warn("No se pudo obtener información de la mascota ID: {}. No se enviará notificación.", historial.getPetId());
                return;
            }
            
            if (mascota.getDuenioId() == null) {
                log.warn("La mascota ID: {} no tiene dueño asignado. No se enviará notificación.", historial.getPetId());
                return;
            }
            
            // Obtener información del dueño
            UsuarioClienteDTO usuario = authServiceClient.obtenerUsuarioPorId(mascota.getDuenioId())
                    .blockOptional()
                    .orElse(null);
            
            if (usuario == null || usuario.getCorreo() == null) {
                log.warn("No se pudo obtener información del usuario ID: {} o no tiene correo. No se enviará notificación.", mascota.getDuenioId());
                return;
            }
            
            // Preparar el payload de la notificación
            Map<String, String> payload = new HashMap<>();
            payload.put("correo", usuario.getCorreo());
            payload.put("nombreDuenio", (usuario.getNombre() != null ? usuario.getNombre() : "") + 
                    (usuario.getApellido() != null ? " " + usuario.getApellido() : "").trim());
            payload.put("nombreMascota", mascota.getNombre() != null ? mascota.getNombre() : "Tu mascota");
            payload.put("fecha", historial.getFecha() != null ? 
                    historial.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A");
            payload.put("diagnostico", historial.getDiagnostico() != null ? historial.getDiagnostico() : "Sin diagnóstico registrado");
            
            // Crear y enviar la notificación
            NotificationRequestDTO notificationRequest = new NotificationRequestDTO(
                    NotificationType.HISTORIAL_CLINICO_CREADO,
                    payload
            );
            
            kafkaProducerService.enviarNotificacion(notificationRequest);
            log.info("Notificación de historial clínico creado enviada exitosamente para mascota ID: {}", historial.getPetId());
            
        } catch (Exception e) {
            log.error("Error al enviar notificación de historial clínico creado para historial ID: {}", 
                    historial.getId(), e);
            // No lanzamos la excepción para que la creación del historial no falle si la notificación falla
        }
    }
}