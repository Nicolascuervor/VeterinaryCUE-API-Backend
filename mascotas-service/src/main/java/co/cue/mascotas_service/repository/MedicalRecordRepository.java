package co.cue.mascotas_service.repository;

import co.cue.mascotas_service.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    List<MedicalRecord> findByPetId(Long petId);

    List<MedicalRecord> findByPetIdOrderByRecordDateDesc(Long petId);

    List<MedicalRecord> findByVeterinarianId(Long veterinarianId);

    List<MedicalRecord> findByRecordType(String recordType);

    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.petId = :petId AND mr.recordDate BETWEEN :startDate AND :endDate")
    List<MedicalRecord> findByPetIdAndDateRange(
            @Param("petId") Long petId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}