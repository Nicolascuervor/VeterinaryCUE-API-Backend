package co.cue.historias_clinicas_service.service;

import co.cue.historias_clinicas_service.client.MascotaClienteDTO;
import co.cue.historias_clinicas_service.client.MascotaServiceClient;
import co.cue.historias_clinicas_service.dto.HistorialClinicoRequestDTO;
import co.cue.historias_clinicas_service.dto.HistorialClinicoResponseDTO;
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

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class HistorialClinicoServiceImpl implements IHistorialClinicoService {
    private final HistorialClinicoRepository historialClinicoRepository;
    private final HistorialClinicoMapper mapper;
    private final MascotaServiceClient mascotaClient;
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
        
        historialClinicoRepository.save(nuevoHistorial);
        log.info("Historial clínico creado exitosamente para Cita ID: {}", event.getCitaId());
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
        HistorialClinico nuevoHistorial = mapper.mapRequestToEntity(requestDTO);
        nuevoHistorial.setVeterinarianId(veterinarioId); // Asignamos el ID del Vet que crea
        HistorialClinico guardado = historialClinicoRepository.save(nuevoHistorial);
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
}