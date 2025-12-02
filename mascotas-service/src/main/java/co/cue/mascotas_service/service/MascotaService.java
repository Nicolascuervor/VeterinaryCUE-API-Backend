package co.cue.mascotas_service.service;

import co.cue.mascotas_service.client.AuthServiceClient;
import co.cue.mascotas_service.dto.MascotaRequestDTO;
import co.cue.mascotas_service.dto.MascotaResponseDTO;
import co.cue.mascotas_service.dto.NotificationRequestDTO;
import co.cue.mascotas_service.dto.NotificationType;
import co.cue.mascotas_service.exceptions.MascotaNotFoundException;
import co.cue.mascotas_service.model.Mascota;
import co.cue.mascotas_service.repository.MascotaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final KafkaProducerService kafkaProducer;
    private final AuthServiceClient authClient;
    // ------------------------------------------------------------
    //                      CREATE
    // ------------------------------------------------------------
    @Transactional
    public MascotaResponseDTO createMascota(MascotaRequestDTO requestDTO) {
        log.info("Creando nueva mascota: {}", requestDTO.getNombre());

        Mascota mascota = Mascota.builder()
                .nombre(requestDTO.getNombre())
                .especie(requestDTO.getEspecie())
                .raza(requestDTO.getRaza())
                .fechaNacimiento(requestDTO.getFechaNacimiento())
                .sexo(requestDTO.getSexo())
                .color(requestDTO.getColor())
                .peso(requestDTO.getPeso())
                .duenoId(requestDTO.getDuenioId())
                .active(true)
                .build();

        Mascota savedMascota = mascotaRepository.save(mascota);
        log.info("Mascota creada exitosamente con ID: {}", savedMascota.getId());

        // Enviar notificación al cliente
        try {
            enviarNotificacionMascotaCreada(savedMascota, requestDTO.getDuenioId());
        } catch (Exception e) {
            log.warn("La mascota se creó pero falló el envío de notificación: {}", e.getMessage());
        }

        return mapToResponseDTO(savedMascota);
    }

    private void enviarNotificacionMascotaCreada(Mascota mascota, Long duenioId) {
        try {
            // Obtener datos del dueño
            var usuario = authClient.obtenerUsuarioPorId(duenioId).block();
            
            if (usuario != null && usuario.getCorreo() != null) {
                // Construir payload para el email
                Map<String, String> payload = new HashMap<>();
                payload.put("correo", usuario.getCorreo());
                payload.put("nombreDuenio", usuario.getNombre() + " " + (usuario.getApellido() != null ? usuario.getApellido() : ""));
                payload.put("nombreMascota", mascota.getNombre());
                payload.put("especie", mascota.getEspecie());
                payload.put("raza", mascota.getRaza() != null ? mascota.getRaza() : "");

                // Enviar evento
                NotificationRequestDTO notificacion = new NotificationRequestDTO(
                        NotificationType.MASCOTA_CREADA,
                        payload
                );

                kafkaProducer.enviarNotificacion(notificacion);
                log.info("Solicitud de notificación enviada para Mascota ID: {}", mascota.getId());
            } else {
                log.warn("No se pudo obtener información del usuario para enviar notificación de mascota creada");
            }
        } catch (Exception e) {
            log.error("Error al enviar notificación de mascota creada: {}", e.getMessage(), e);
        }
    }
    // ------------------------------------------------------------
    //                      GET BY ID
    // ------------------------------------------------------------
    public MascotaResponseDTO getMascotaById(Long id) {
        log.info("Buscando mascota con ID: {}", id);
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new MascotaNotFoundException(id));
        return mapToResponseDTO(mascota);
    }
    // ------------------------------------------------------------
    //                    GET ALL
    // ------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<MascotaResponseDTO> getAllMascotas() {
        log.info("Obteniendo todas las mascotas");
        return mascotaRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }
    // ------------------------------------------------------------
    //                  GET ACTIVE ONLY
    // ------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<MascotaResponseDTO> getActiveMascotas() {
        log.info("Obteniendo mascotas activas");
        return mascotaRepository.findByActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }
    // ------------------------------------------------------------
    //               GET BY OWNER (DUENIO)
    // ------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<MascotaResponseDTO> getMascotasByOwner(Long ownerId) {
        log.info("Obteniendo mascotas del dueño con ID: {}", ownerId);
        return mascotaRepository.findByDuenoId(ownerId).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // ------------------------------------------------------------
    //                SEARCH BY NAME
    // ------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<MascotaResponseDTO> searchMascotasByName(String name) {
        log.info("Buscando mascotas con nombre: {}", name);
        return mascotaRepository.searchByName(name).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }
    // ------------------------------------------------------------
    //                     UPDATE
    // ------------------------------------------------------------
    @Transactional
    public MascotaResponseDTO updateMascota(Long id, MascotaRequestDTO requestDTO) {
        log.info("Actualizando mascota con ID: {}", id);

        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new MascotaNotFoundException(id));


        mascota.setNombre(requestDTO.getNombre());
        mascota.setEspecie(requestDTO.getEspecie());
        mascota.setRaza(requestDTO.getRaza());
        mascota.setFechaNacimiento(requestDTO.getFechaNacimiento());
        mascota.setSexo(requestDTO.getSexo());
        mascota.setColor(requestDTO.getColor());
        mascota.setPeso(requestDTO.getPeso());
        mascota.setDuenoId(requestDTO.getDuenioId());

        Mascota updatedMascota = mascotaRepository.save(mascota);
        log.info("Mascota actualizada exitosamente, nuevo dueño ID: {}", requestDTO.getDuenioId());

        return mapToResponseDTO(updatedMascota);
    }
    // ------------------------------------------------------------
    //               LOGICAL DELETE (ACTIVE = FALSE)
    // ------------------------------------------------------------
    @Transactional
    public void deactivateMascota(Long id) {
        log.info("Desactivando mascota con ID: {}", id);
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new MascotaNotFoundException(id));
        mascota.setActive(false);
        mascotaRepository.save(mascota);
        log.info("Mascota desactivada exitosamente");
    }
    // ------------------------------------------------------------
    //                   HARD DELETE
    // ------------------------------------------------------------
    @Transactional
    public void deleteMascota(Long id) {
        log.info("Eliminando mascota con ID: {}", id);
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new MascotaNotFoundException(id));
        mascotaRepository.delete(mascota);
        log.info("Mascota eliminada exitosamente");
    }

    // ------------------------------------------------------------
    //                 MAPPER (ENTITY → DTO)
    // ------------------------------------------------------------
    private MascotaResponseDTO mapToResponseDTO(Mascota mascota) {
        return MascotaResponseDTO.builder()
                .id(mascota.getId())
                .nombre(mascota.getNombre())
                .especie(mascota.getEspecie())
                .raza(mascota.getRaza())
                .fechaNacimiento(mascota.getFechaNacimiento())
                .dato(calculateAge(mascota.getFechaNacimiento()))
                .sexo(mascota.getSexo())
                .color(mascota.getColor())
                .peso(mascota.getPeso())
                .duenioId(mascota.getDuenoId())
                .active(mascota.getActive())
                .build();

    }

    // ------------------------------------------------------------
    //                     AGE CALCULATOR
    // ------------------------------------------------------------
    private Integer calculateAge(LocalDate birthDate) {
        if (birthDate == null) return null;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}