package co.cue.mascotas_service.controller;

import co.cue.mascotas_service.dto.MedicalRecordRequestDTO;
import co.cue.mascotas_service.dto.MedicalRecordResponseDTO;
import co.cue.mascotas_service.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    public ResponseEntity<MedicalRecordResponseDTO> createMedicalRecord(
             @RequestBody MedicalRecordRequestDTO requestDTO) {
        log.info("POST /api/medical-records - Crear registro médico");
        MedicalRecordResponseDTO response = medicalRecordService.createMedicalRecord(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO> getMedicalRecordById(@PathVariable Long id) {
        log.info("GET /api/medical-records/{} - Obtener registro médico", id);
        MedicalRecordResponseDTO response = medicalRecordService.getMedicalRecordById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getMedicalRecordsByPet(
            @PathVariable Long petId) {
        log.info("GET /api/medical-records/pet/{} - Obtener registros por mascota", petId);
        List<MedicalRecordResponseDTO> response = medicalRecordService.getMedicalRecordsByPet(petId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pet/{petId}/date-range")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getMedicalRecordsByDateRange(
            @PathVariable Long petId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("GET /api/medical-records/pet/{}/date-range - Obtener registros por rango de fechas", petId);
        List<MedicalRecordResponseDTO> response = medicalRecordService.getMedicalRecordsByDateRange(
                petId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO> updateMedicalRecord(
            @PathVariable Long id,
            @RequestBody MedicalRecordRequestDTO requestDTO) {
        log.info("PUT /api/medical-records/{} - Actualizar registro médico", id);
        MedicalRecordResponseDTO response = medicalRecordService.updateMedicalRecord(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        log.info("DELETE /api/medical-records/{} - Eliminar registro médico", id);
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }
}