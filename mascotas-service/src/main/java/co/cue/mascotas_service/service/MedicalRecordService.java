package co.cue.mascotas_service.service;

import co.cue.mascotas_service.dto.MedicalRecordRequestDTO;
import co.cue.mascotas_service.dto.MedicalRecordResponseDTO;
import co.cue.mascotas_service.model.MedicalRecord;
import co.cue.mascotas_service.model.Pet;
import co.cue.mascotas_service.repository.MedicalRecordRepository;
import co.cue.mascotas_service.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PetRepository petRepository;

    @Transactional
    public MedicalRecordResponseDTO createMedicalRecord(MedicalRecordRequestDTO requestDTO) {
        log.info("Creando registro médico para mascota ID: {}", requestDTO.getPetId());

        Pet pet = petRepository.findById(requestDTO.getPetId())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + requestDTO.getPetId()));

        MedicalRecord record = MedicalRecord.builder()
                .petId(requestDTO.getPetId())
                .recordDate(requestDTO.getRecordDate())
                .recordType(requestDTO.getRecordType())
                .description(requestDTO.getDescription())
                .diagnosis(requestDTO.getDiagnosis())
                .treatment(requestDTO.getTreatment())
                .veterinarianId(requestDTO.getVeterinarianId())
                .veterinarianName(requestDTO.getVeterinarianName())
                .cost(requestDTO.getCost())
                .build();

        MedicalRecord savedRecord = medicalRecordRepository.save(record);
        log.info("Registro médico creado exitosamente con ID: {}", savedRecord.getId());

        return mapToResponseDTO(savedRecord, pet.getName());
    }

    @Transactional(readOnly = true)
    public MedicalRecordResponseDTO getMedicalRecordById(Long id) {
        log.info("Buscando registro médico con ID: {}", id);
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro médico no encontrado con ID: " + id));

        Pet pet = petRepository.findById(record.getPetId())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        return mapToResponseDTO(record, pet.getName());
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordResponseDTO> getMedicalRecordsByPet(Long petId) {
        log.info("Obteniendo registros médicos de la mascota ID: {}", petId);

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + petId));

        return medicalRecordRepository.findByPetIdOrderByRecordDateDesc(petId).stream()
                .map(record -> mapToResponseDTO(record, pet.getName()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordResponseDTO> getMedicalRecordsByDateRange(
            Long petId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Obteniendo registros médicos por rango de fechas");

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + petId));

        return medicalRecordRepository.findByPetIdAndDateRange(petId, startDate, endDate).stream()
                .map(record -> mapToResponseDTO(record, pet.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public MedicalRecordResponseDTO updateMedicalRecord(Long id, MedicalRecordRequestDTO requestDTO) {
        log.info("Actualizando registro médico con ID: {}", id);

        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro médico no encontrado con ID: " + id));

        Pet pet = petRepository.findById(requestDTO.getPetId())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        record.setRecordDate(requestDTO.getRecordDate());
        record.setRecordType(requestDTO.getRecordType());
        record.setDescription(requestDTO.getDescription());
        record.setDiagnosis(requestDTO.getDiagnosis());
        record.setTreatment(requestDTO.getTreatment());
        record.setVeterinarianId(requestDTO.getVeterinarianId());
        record.setVeterinarianName(requestDTO.getVeterinarianName());
        record.setCost(requestDTO.getCost());

        MedicalRecord updatedRecord = medicalRecordRepository.save(record);
        log.info("Registro médico actualizado exitosamente");

        return mapToResponseDTO(updatedRecord, pet.getName());
    }

    @Transactional
    public void deleteMedicalRecord(Long id) {
        log.info("Eliminando registro médico con ID: {}", id);
        if (!medicalRecordRepository.existsById(id)) {
            throw new RuntimeException("Registro médico no encontrado con ID: " + id);
        }
        medicalRecordRepository.deleteById(id);
        log.info("Registro médico eliminado exitosamente");
    }

    private MedicalRecordResponseDTO mapToResponseDTO(MedicalRecord record, String petName) {
        return MedicalRecordResponseDTO.builder()
                .id(record.getId())
                .petId(record.getPetId())
                .petName(petName)
                .recordDate(record.getRecordDate())
                .recordType(record.getRecordType())
                .description(record.getDescription())
                .diagnosis(record.getDiagnosis())
                .treatment(record.getTreatment())
                .veterinarianId(record.getVeterinarianId())
                .veterinarianName(record.getVeterinarianName())
                .cost(record.getCost())
                .createdAt(record.getCreatedAt())
                .build();
    }
}