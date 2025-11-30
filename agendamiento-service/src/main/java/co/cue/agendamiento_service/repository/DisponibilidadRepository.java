package co.cue.agendamiento_service.repository;

import co.cue.agendamiento_service.models.entities.Disponibilidad;
import co.cue.agendamiento_service.models.entities.enums.EstadoDisponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository  // Marca la interfaz como un repositorio Spring, habilitando inyección y manejo de persistencia
public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {
// Extiende JpaRepository para proveer operaciones CRUD para la entidad Disponibilidad
    // La entidad principal es Disponibilidad y la clave primaria es Long


    /**
     * Busca todas las disponibilidades de un veterinario en un rango de fechas específico
     * y que estén en un estado determinado (por ejemplo, DISPONIBLE).
     *
     * @param veterinarioId ID del veterinario
     * @param fechaInicio   Fecha y hora de inicio del rango
     * @param fechaFin      Fecha y hora de fin del rango
     * @param estado        Estado de la disponibilidad a filtrar
     * @return Lista de disponibilidades que cumplen los criterios
     */
    List<Disponibilidad> findByVeterinarioIdAndFechaHoraInicioBetweenAndEstado(
            Long veterinarioId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            EstadoDisponibilidad estado
    );


    /**
     * Busca todas las disponibilidades asociadas a una cita específica.
     *
     * @param citaId ID de la cita
     * @return Lista de disponibilidades vinculadas a la cita
     */
    List<Disponibilidad> findByCitaId(Long citaId);


    /**
     * Actualiza masivamente el estado y la citaId de un conjunto de disponibilidades,
     * únicamente si actualmente están DISPONIBLES.
     *
     * @param ids         Lista de IDs de disponibilidades a actualizar
     * @param citaId      ID de la cita a asignar
     * @param nuevoEstado Nuevo estado que se quiere establecer
     * @return Número de registros actualizados
     */

    @Modifying  // Indica que la consulta modifica datos (UPDATE)
    @Query("UPDATE Disponibilidad d SET d.estado = :nuevoEstado, d.citaId = :citaId " +
            "WHERE d.id IN :ids AND d.estado = 'DISPONIBLE'")
    int reservarSlotsMasivo(
            @Param("ids") List<Long> ids,
            @Param("citaId") Long citaId,
            @Param("nuevoEstado") EstadoDisponibilidad nuevoEstado
    );
}