package co.cue.mascotas_service.repository;

import co.cue.mascotas_service.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByOwnerId(Long ownerId);

    List<Pet> findByOwnerIdAndActiveTrue(Long ownerId);

    List<Pet> findBySpecies(String species);

    List<Pet> findByActiveTrue();

    Optional<Pet> findByMicrochipNumber(String microchipNumber);

    @Query("SELECT p FROM Pet p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Pet> searchByName(@Param("name") String name);

    boolean existsByMicrochipNumber(String microchipNumber);
}