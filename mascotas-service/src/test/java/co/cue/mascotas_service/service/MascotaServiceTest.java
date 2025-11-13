package co.cue.mascotas_service.service;

import co.cue.mascotas_service.dto.MascotaRequestDTO;
import co.cue.mascotas_service.dto.MascotaResponseDTO;
import co.cue.mascotas_service.exceptions.MascotaNotFoundException;
import co.cue.mascotas_service.model.Mascota;
import co.cue.mascotas_service.repository.MascotaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
class MascotaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @InjectMocks
    private MascotaService mascotaService;


    @Test
    @DisplayName("Prueba que getMascotaById retorna un DTO cuando la mascota existe")
    void testGetMascotaById_WhenMascotaExists_ShouldReturnDTO() {

        long mascotaId = 1L;
        Mascota mascotaSimulada = new Mascota();
        mascotaSimulada.setId(mascotaId);
        mascotaSimulada.setNombre("Firulais");
        mascotaSimulada.setEspecie("Perro");
        mascotaSimulada.setFechaNacimiento(LocalDate.now().minusYears(2)); // Nació hace 2 años

        when(mascotaRepository.findById(mascotaId)).thenReturn(Optional.of(mascotaSimulada));


        MascotaResponseDTO response = mascotaService.getMascotaById(mascotaId);


        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals("Firulais", response.getNombre(), "El nombre en el DTO no coincide");
        assertEquals(mascotaId, response.getId(), "El ID en el DTO no coincide");


        assertEquals(2, response.getDato(), "La edad (dato) no se calculó correctamente");


        verify(mascotaRepository, times(1)).findById(mascotaId);
    }

    @Test
    @DisplayName("Prueba que getMascotaById lanza Excepción si la mascota no existe")
    void testGetMascotaById_WhenNotFound_ShouldThrowException() {

        long mascotaId = 99L;

        when(mascotaRepository.findById(mascotaId)).thenReturn(Optional.empty());


        MascotaNotFoundException exception = assertThrows(
                MascotaNotFoundException.class,  // la excepción que ESPERAMOS
                () -> {
                    mascotaService.getMascotaById(mascotaId); //
                }
        );

        assertEquals("Mascota no encontrada con ID: " + mascotaId, exception.getMessage());

        verify(mascotaRepository, times(1)).findById(mascotaId);
    }

    @Test
    @DisplayName("Prueba que createMascota guarda la entidad y retorna un DTO")
    void testCreateMascota_ShouldSaveAndReturnDTO() {
        MascotaRequestDTO requestDTO = new MascotaRequestDTO();
        requestDTO.setNombre("Neo");
        requestDTO.setEspecie("Gato");
        requestDTO.setRaza("Siames");
        requestDTO.setDuenioId(123L);
        requestDTO.setFechaNacimiento(LocalDate.now().minusYears(1)); // Nació hace 1 año

        Mascota mascotaGuardadaSimulada = new Mascota();
        mascotaGuardadaSimulada.setId(1L);
        mascotaGuardadaSimulada.setNombre("Neo");
        mascotaGuardadaSimulada.setEspecie("Gato");
        mascotaGuardadaSimulada.setRaza("Siames");
        mascotaGuardadaSimulada.setDuenoId(123L);
        mascotaGuardadaSimulada.setFechaNacimiento(LocalDate.now().minusYears(1));
        mascotaGuardadaSimulada.setActive(true);

        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaGuardadaSimulada);


        MascotaResponseDTO response = mascotaService.createMascota(requestDTO);



        assertNotNull(response);
        assertEquals(1L, response.getId(), "El ID del DTO de respuesta no coincide");
        assertEquals("Neo", response.getNombre(), "El nombre del DTO de respuesta no coincide");
        assertEquals(1, response.getDato(), "La edad (dato) no se calculó correctamente"); // [cite: 725, 728]


        ArgumentCaptor<Mascota> mascotaCaptor = ArgumentCaptor.forClass(Mascota.class);

        verify(mascotaRepository, times(1)).save(mascotaCaptor.capture());

        Mascota mascotaEnviadaARepo = mascotaCaptor.getValue();
        assertEquals("Neo", mascotaEnviadaARepo.getNombre());
        assertEquals(123L, mascotaEnviadaARepo.getDuenoId());
        assertTrue(mascotaEnviadaARepo.getActive(), "La mascota debió crearse como 'activa'");
        assertNull(mascotaEnviadaARepo.getId(), "El ID debe ser nulo antes de que el repo lo guarde");
    }

    @Test
    @DisplayName("Prueba que updateMascota actualiza la entidad y retorna el DTO modificado")
    void testUpdateMascota_WhenExists_ShouldUpdateAndReturnDTO() {

        long mascotaId = 1L;

        Mascota mascotaOriginal = new Mascota();
        mascotaOriginal.setId(mascotaId);
        mascotaOriginal.setNombre("Firulais");
        mascotaOriginal.setEspecie("Perro");
        mascotaOriginal.setRaza("Mestizo");
        mascotaOriginal.setFechaNacimiento(LocalDate.now().minusYears(3)); // 3 años

        MascotaRequestDTO requestDTO = new MascotaRequestDTO();
        requestDTO.setNombre("Garfield");
        requestDTO.setEspecie("Gato");
        requestDTO.setRaza("Persa");
        requestDTO.setDuenioId(456L);
        requestDTO.setFechaNacimiento(LocalDate.now().minusYears(5)); // Fecha corregida a 5 años

        when(mascotaRepository.findById(mascotaId)).thenReturn(Optional.of(mascotaOriginal));

        ArgumentCaptor<Mascota> mascotaCaptor = ArgumentCaptor.forClass(Mascota.class);


        when(mascotaRepository.save(any(Mascota.class))).thenAnswer(invocation -> invocation.getArgument(0));


        MascotaResponseDTO responseDTO = mascotaService.updateMascota(mascotaId, requestDTO);


        assertNotNull(responseDTO);
        assertEquals("Garfield", responseDTO.getNombre(), "El nombre en el DTO no se actualizó");
        assertEquals("Persa", responseDTO.getRaza(), "La raza en el DTO no se actualizó");
        assertEquals(456L, responseDTO.getDuenioId(), "El dueño en el DTO no se actualizó");
        assertEquals(5, responseDTO.getDato(), "La edad (dato) no se actualizó correctamente");

        verify(mascotaRepository, times(1)).findById(mascotaId);

        verify(mascotaRepository, times(1)).save(mascotaCaptor.capture());

        Mascota mascotaGuardada = mascotaCaptor.getValue();


        assertEquals("Garfield", mascotaGuardada.getNombre(), "El nombre de la entidad no se actualizó");
        assertEquals("Persa", mascotaGuardada.getRaza(), "La raza de la entidad no se actualizó");
        assertEquals(456L, mascotaGuardada.getDuenoId(), "El dueño de la entidad no se actualizó");
        assertEquals(1L, mascotaGuardada.getId(), "El ID no debe cambiar durante una actualización");
    }

    @Test
    @DisplayName("Prueba que updateMascota lanza Excepción si la mascota no existe")
    void testUpdateMascota_WhenNotFound_ShouldThrowException() {

        long mascotaId = 99L;

        MascotaRequestDTO requestDTO = new MascotaRequestDTO();
        requestDTO.setNombre("Fantasma");

        when(mascotaRepository.findById(mascotaId)).thenReturn(Optional.empty());


        MascotaNotFoundException exception = assertThrows(
                MascotaNotFoundException.class,
                () -> {

                    mascotaService.updateMascota(mascotaId, requestDTO);
                }
        );

        assertEquals("Mascota no encontrada con ID: " + mascotaId, exception.getMessage());


        verify(mascotaRepository, times(1)).findById(mascotaId);

        verify(mascotaRepository, never()).save(any(Mascota.class));
    }


    @Test
    @DisplayName("Prueba que deactivateMascota establece 'active' a false y guarda")
    void testDeactivateMascota_WhenExists_ShouldSetActiveFalseAndSave() {
        long mascotaId = 1L;

        // 1a. Simula la mascota que existe en la BD (aún activa)
        Mascota mascotaSimulada = new Mascota();
        mascotaSimulada.setId(mascotaId);
        mascotaSimulada.setNombre("Firulais");
        mascotaSimulada.setActive(true); // <-- Estado inicial: activa

        // 1b. Programamos los Mocks

        // Stub 1: Cuando el servicio busque la mascota, la encontrará.
        when(mascotaRepository.findById(mascotaId)).thenReturn(Optional.of(mascotaSimulada));

        // (Mentor): No necesitamos un .thenReturn() para save()
        // porque el método 'deactivateMascota' no usa el objeto retornado.
        // Solo necesitamos "escuchar" la llamada.

        // Preparamos el captor para "atrapar" la mascota que se intenta guardar
        ArgumentCaptor<Mascota> mascotaCaptor = ArgumentCaptor.forClass(Mascota.class);


        // --- 2. WHEN (Cuando) ---
        // (Ejecutamos el método a probar, que devuelve void)
        mascotaService.deactivateMascota(mascotaId);


        // --- 3. THEN (Entonces) ---
        // (Verificamos las interacciones, ya que no hay respuesta)

        // 3a. Verificar que se buscó la mascota correcta
        verify(mascotaRepository, times(1)).findById(mascotaId);

        // 3b. Verificar que 'save()' se llamó y capturar el objeto
        verify(mascotaRepository, times(1)).save(mascotaCaptor.capture());

        // 3c. Obtener el objeto capturado
        Mascota mascotaGuardada = mascotaCaptor.getValue();

        // (Mentor): ¡Esta es la aserción más importante del test!
        // Verificamos que la lógica de negocio (mascota.setActive(false))
        // se ejecutó correctamente sobre el objeto.
        assertFalse(mascotaGuardada.getActive(), "La mascota guardada debería estar inactiva (active=false)");

        // (Mentor): También verificamos que no modificamos el ID por accidente
        assertEquals(mascotaId, mascotaGuardada.getId(), "El ID de la mascota no debe cambiar");
    }

    @Test
    @DisplayName("Prueba que deactivateMascota lanza Excepción si la mascota no existe")
    void testDeactivateMascota_WhenNotFound_ShouldThrowException() {
        // --- 1. GIVEN (Dado) ---
        long mascotaId = 99L; // Un ID que no existe

        // Simulamos que el repositorio NO encuentra nada
        when(mascotaRepository.findById(mascotaId)).thenReturn(Optional.empty());

        // --- 2. WHEN & 3. THEN (Cuando y Entonces) ---
        // Verificamos que se lanza la excepción esperada

        assertThrows(
                MascotaNotFoundException.class, // La excepción que esperamos
                () -> {
                    // CUANDO intentamos desactivar la mascota
                    mascotaService.deactivateMascota(mascotaId);
                }
        );

        // --- 4. VERIFY (Verificación) ---
        // Verificamos que findById() SÍ se llamó...
        verify(mascotaRepository, times(1)).findById(mascotaId);

        // ...pero que save() NUNCA se llamó.
        verify(mascotaRepository, never()).save(any(Mascota.class));
    }


    @Test
    @DisplayName("Prueba que getAllMascotas retorna una lista de DTOs cuando existen mascotas")
    void testGetAllMascotas_WhenMascotasExist_ShouldReturnDTOList() {
        // --- 1. GIVEN (Dado) ---
        // (Preparamos el escenario)

        // 1a. Simula la lista de entidades que devuelve la BD
        Mascota mascota1 = new Mascota();
        mascota1.setId(1L);
        mascota1.setNombre("Firulais");
        mascota1.setFechaNacimiento(LocalDate.now().minusYears(2));

        Mascota mascota2 = new Mascota();
        mascota2.setId(2L);
        mascota2.setNombre("Garfield");
        mascota2.setFechaNacimiento(LocalDate.now().minusYears(5));

        List<Mascota> listaSimulada = List.of(mascota1, mascota2);

        // 1b. Programamos el Mock
        // "CUANDO se llame a findAll(), ENTONCES retorna nuestra listaSimulada"
        when(mascotaRepository.findAll()).thenReturn(listaSimulada);

        // --- 2. WHEN (Cuando) ---
        // (Ejecutamos el método a probar)
        List<MascotaResponseDTO> response = mascotaService.getAllMascotas();

        // --- 3. THEN (Entonces) ---
        // (Verificamos el resultado)
        assertNotNull(response, "La lista de respuesta no debe ser nula");
        assertEquals(2, response.size(), "El tamaño de la lista de DTOs no coincide");

        // Verificamos que la transformación (mapToResponseDTO) funcionó
        assertEquals("Firulais", response.get(0).getNombre());
        assertEquals(5, response.get(1).getDato()); // Verificamos la edad de Garfield

        // Verificamos la interacción
        verify(mascotaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Prueba que getAllMascotas retorna una lista vacía si no hay mascotas")
    void testGetAllMascotas_WhenNoMascotas_ShouldReturnEmptyList() {
        // --- 1. GIVEN (Dado) ---
        // (Mentor): Este es el "Caso Borde" o "Empty Path".
        // Programamos el mock para que devuelva una lista vacía.
        when(mascotaRepository.findAll()).thenReturn(List.of()); // List.of() crea una lista vacía

        // --- 2. WHEN (Cuando) ---
        List<MascotaResponseDTO> response = mascotaService.getAllMascotas();

        // --- 3. THEN (Entonces) ---
        assertNotNull(response, "La respuesta nunca debe ser nula, debe ser una lista vacía");
        assertTrue(response.isEmpty(), "La lista debería estar vacía");

        // Verificamos que, aunque no había datos, la consulta SÍ se hizo
        verify(mascotaRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("Prueba que getAllMascotas retorna una lista de DTOs cuando existen mascotas")
    void testGetAllActiveMascotas_WhenMascotasExist_ShouldReturnDTOList() {
        // --- 1. GIVEN (Dado) ---
        // (Preparamos el escenario)

        // 1a. Simula la lista de entidades que devuelve la BD
        Mascota mascota1 = new Mascota();
        mascota1.setId(1L);
        mascota1.setNombre("Firulais");
        mascota1.setFechaNacimiento(LocalDate.now().minusYears(2));

        Mascota mascota2 = new Mascota();
        mascota2.setId(2L);
        mascota2.setNombre("Garfield");
        mascota2.setFechaNacimiento(LocalDate.now().minusYears(5));

        List<Mascota> listaSimulada = List.of(mascota1, mascota2);

        // 1b. Programamos el Mock
        // "CUANDO se llame a findAll(), ENTONCES retorna nuestra listaSimulada"
        when(mascotaRepository.findByActiveTrue()).thenReturn(listaSimulada);

        // --- 2. WHEN (Cuando) ---
        // (Ejecutamos el método a probar)
        List<MascotaResponseDTO> response = mascotaService.getActiveMascotas(); // <-- CORRECCIÓN 2
        // --- 3. THEN (Entonces) ---
        // (Verificamos el resultado)
        assertNotNull(response, "La lista de respuesta no debe ser nula");
        assertEquals(2, response.size(), "El tamaño de la lista de DTOs no coincide");

        // Verificamos que la transformación (mapToResponseDTO) funcionó
        assertEquals("Firulais", response.get(0).getNombre());
        assertEquals(5, response.get(1).getDato()); // Verificamos la edad de Garfield

        // Verificamos la interacción
        verify(mascotaRepository, times(1)).findByActiveTrue(); // <-- CORRECCIÓN 3

    }

    @Test
    @DisplayName("Prueba que getAllMascotas retorna una lista vacía si no hay mascotas")
    void testGetAlActiveMascotas_WhenNoMascotas_ShouldReturnEmptyList() {
        // --- 1. GIVEN (Dado) ---
        // (Mentor): Este es el "Caso Borde" o "Empty Path".
        // Programamos el mock para que devuelva una lista vacía.
        when(mascotaRepository.findByActiveTrue()).thenReturn(List.of()); // List.of() crea una lista vacía

        // --- 2. WHEN (Cuando) ---
        List<MascotaResponseDTO> response = mascotaService.getActiveMascotas(); // <-- CORRECCIÓN 2
        // --- 3. THEN (Entonces) ---
        assertNotNull(response, "La respuesta nunca debe ser nula, debe ser una lista vacía");
        assertTrue(response.isEmpty(), "La lista debería estar vacía");

        // Verificamos que, aunque no había datos, la consulta SÍ se hizo
        verify(mascotaRepository, times(1)).findByActiveTrue(); // <-- CORRECCIÓN 3

    }


    @Test
    @DisplayName("Prueba que getMascotasByOwner retorna DTOs cuando el dueño tiene mascotas")
    void testGetMascotasByOwner_WhenOwnerHasMascotas_ShouldReturnDTOList() {
        // --- 1. GIVEN (Dado) ---
        long ownerId = 123L;

        // 1a. Simula la lista de mascotas para ese dueño
        Mascota mascota1 = new Mascota();
        mascota1.setId(1L);
        mascota1.setNombre("Firulais");
        mascota1.setDuenoId(ownerId);
        mascota1.setFechaNacimiento(LocalDate.now().minusYears(2));

        Mascota mascota2 = new Mascota();
        mascota2.setId(2L);
        mascota2.setNombre("Garfield");
        mascota2.setDuenoId(ownerId);
        mascota2.setFechaNacimiento(LocalDate.now().minusYears(5));

        List<Mascota> listaSimulada = List.of(mascota1, mascota2);

        // 1b. Programamos el Mock
        // "CUANDO se llame a findByDuenoId() con 123L, ENTONCES retorna nuestra listaSimulada"
        when(mascotaRepository.findByDuenoId(ownerId)).thenReturn(listaSimulada);

        // --- 2. WHEN (Cuando) ---
        // (Ejecutamos el método a probar)
        List<MascotaResponseDTO> response = mascotaService.getMascotasByOwner(ownerId);

        // --- 3. THEN (Entonces) ---
        // (Verificamos el resultado)
        assertNotNull(response, "La lista de respuesta no debe ser nula");
        assertEquals(2, response.size(), "El tamaño de la lista de DTOs no coincide");
        assertEquals("Firulais", response.get(0).getNombre(), "El nombre de la mascota no coincide");
        assertEquals(ownerId, response.get(0).getDuenioId(), "El ID del dueño no coincide");

        // Verificamos la interacción
        verify(mascotaRepository, times(1)).findByDuenoId(ownerId);
    }

    @Test
    @DisplayName("Prueba que getMascotasByOwner retorna lista vacía si el dueño no tiene mascotas")
    void testGetMascotasByOwner_WhenOwnerHasNoMascotas_ShouldReturnEmptyList() {
        // --- 1. GIVEN (Dado) ---
        long ownerId = 404L; // Un dueño que no tiene mascotas

        // Programamos el mock para que devuelva una lista vacía.
        when(mascotaRepository.findByDuenoId(ownerId)).thenReturn(List.of());

        // --- 2. WHEN (Cuando) ---
        List<MascotaResponseDTO> response = mascotaService.getMascotasByOwner(ownerId);

        // --- 3. THEN (Entonces) ---
        assertNotNull(response, "La respuesta nunca debe ser nula, debe ser una lista vacía");
        assertTrue(response.isEmpty(), "La lista debería estar vacía");

        // Verificamos que la consulta SÍ se hizo
        verify(mascotaRepository, times(1)).findByDuenoId(ownerId);
    }






}
