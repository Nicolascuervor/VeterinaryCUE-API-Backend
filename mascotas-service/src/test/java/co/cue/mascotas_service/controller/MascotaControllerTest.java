package co.cue.mascotas_service.controller;

import co.cue.mascotas_service.model.Mascota;
import co.cue.mascotas_service.repository.MascotaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;

import co.cue.mascotas_service.dto.MascotaRequestDTO;
import static org.junit.jupiter.api.Assertions.*;

// Imports estáticos para MockMvc
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Imports estáticos para Hamcrest (usado por jsonPath)
import static org.hamcrest.Matchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

// --- (Nuevas Anotaciones) ---

/**
 * Esta es la anotación principal. Le dice a Spring Boot:
 * Olvida los mocks. Carga la APLICACIÓN COMPLETA. Escanea todos los
 * @Component, @Service, @Repository, y @Controller. Conéctate a la BD
 * de prueba (H2 en memoria).
 */
@SpringBootTest
/**
 * Esta anotación activa la herramienta 'MockMvc', que nos
 * permite simular peticiones HTTP (GET, POST, etc.) a nuestros
 * controladores.
 */
@AutoConfigureMockMvc
class MascotaControllerTest {

    // --- (Inyección de dependencias REALES) ---

    /**
     * Ya no usamos @InjectMocks. Usamos @Autowired.
     * Le pedimos a Spring que nos "inyecte" el bean real de MockMvc
     * que configuró gracias a @AutoConfigureMockMvc.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Inyectamos el Repositorio REAL.
     * ¿Por qué? Porque en nuestros tests, lo usaremos en el GIVEN
     * para insertar datos directamente en la BD de prueba.
     */
    @Autowired
    private MascotaRepository mascotaRepository;

    /**
     * Inyectamos un "Mapper" de JSON. Es una herramienta
     * de utilidad que usaremos para convertir nuestros DTO de Java
     * a un string de JSON para las peticiones POST y PUT.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Esta anotación de JUnit 5.
     * El método 'setup' se ejecutará ANTES de CADA test (@Test).
     *
     * ¿Por qué? Porque las pruebas de integración usan una BD compartida.
     * Si el 'testCrear' añade una mascota, el 'testLeer' la verá.
     * Para evitar que un test contamine a otro, BORRAMOS todo
     * de la base de datos antes de cada ejecución.
     */
    @BeforeEach
    void setup() {
        mascotaRepository.deleteAll();
    }

    // --- (Aquí empezarán nuestros métodos de prueba) ---

    /**
     * (Mentor): Nota que añadimos 'throws Exception' porque
     * mockMvc.perform() puede lanzar excepciones (ej. al parsear JSON).
     */
    @Test
    @DisplayName("Prueba GET /api/mascotas/{id} - Happy Path (200 OK)")
    void testGetMascotaById_WhenExists_ShouldReturn200OK() throws Exception {

        // --- 1. GIVEN (Dado) ---
        // (Insertamos datos REALES en la BD de prueba H2)

        // (Mentor): ¡Ya no usamos 'new Mascota()'! Usamos el builder
        // de la entidad real para que @PrePersist funcione.
        Mascota mascotaGuardada = Mascota.builder()
                .nombre("Firulais")
                .especie("Perro")
                .raza("Mestizo")
                .fechaNacimiento(LocalDate.now().minusYears(2))
                .duenoId(1L)
                .active(true)
                .build();

        // (Mentor): Usamos el repositorio REAL inyectado para guardar
        // la entidad en la BD H2.
        Mascota mascotaEnDB = mascotaRepository.save(mascotaGuardada);
        Long idReal = mascotaEnDB.getId(); // Capturamos el ID real generado por la BD

        // --- 2. WHEN & 3. THEN (Cuando y Entonces) ---
        // (Mentor): MockMvc combina el 'When' (perform) y el 'Then' (andExpect)

        mockMvc.perform(
                        // (WHEN) Simula una petición GET a esta URL
                        get("/api/mascotas/" + idReal)
                                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_DUEÑO")))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                // (THEN) Verificamos los resultados de la RESPUESTA HTTP
                .andExpect(
                        // 1. Verificar el código de estado HTTP
                        status().isOk() // Esperamos un 200 OK
                )
                .andExpect(
                        // 2. Verificar el tipo de contenido
                        content().contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        // 3. Verificar el payload JSON (lo más importante)
                        // (Mentor): Usamos "jsonPath" para "consultar" el JSON de respuesta.
                        // '$' es la raíz del JSON.
                        jsonPath("$.id", is(idReal.intValue()))
                )
                .andExpect(
                        jsonPath("$.nombre", is("Firulais"))
                )
                .andExpect(
                        jsonPath("$.especie", is("Perro"))
                )
                .andExpect(
                        // Probamos la lógica de negocio (calculateAge)
                        jsonPath("$.dato", is(2))
                )
                .andExpect(
                        jsonPath("$.active", is(true))
                );
    }

    @Test
    @DisplayName("Prueba GET /api/mascotas/{id} - Sad Path (404 Not Found)")
    void testGetMascotaById_WhenNotExists_ShouldReturn404NotFound() throws Exception {

        // --- 1. GIVEN (Dado) ---
        // (Mentor): No necesitamos insertar nada.
        // El @BeforeEach (con mascotaRepository.deleteAll()) se aseguró
        // de que la BD esté vacía. Simplemente usamos un ID que no existe.
        long idInexistente = 99L;

        // --- 2. WHEN & 3. THEN (Cuando y Entonces) ---

        mockMvc.perform(
                        // (WHEN) Hacemos un GET a un ID que sabemos que no existe
                        get("/api/mascotas/" + idInexistente)
                                // (Mentor): Aún debemos estar autenticados.
                                // La seguridad (401) se revisa ANTES que la lógica (404).
                                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_DUEÑO")))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                // (THEN) Verificamos la respuesta HTTP
                .andExpect(
                        // ¡Esperamos un 404 Not Found!
                        status().isNotFound()
                );
    }

    @Test
    @DisplayName("Prueba POST /api/mascotas - Happy Path (201 Faltancreated)")
    void testCreateMascota_WhenValidRequest_ShouldReturn201Created() throws Exception {

        // --- 1. GIVEN (Dado) ---
        // (Preparamos el DTO que simulamos enviar en el JSON)
        MascotaRequestDTO requestDTO = new MascotaRequestDTO();
        requestDTO.setNombre("Garfield");
        requestDTO.setEspecie("Gato");
        requestDTO.setRaza("Persa");
        requestDTO.setFechaNacimiento(LocalDate.now().minusYears(5));
        requestDTO.setDuenioId(123L);

        // (Mentor): Convertimos el objeto Java (DTO) a un String JSON
        // Para eso inyectamos el 'ObjectMapper' en nuestra clase.
        String requestBodyJson = objectMapper.writeValueAsString(requestDTO);

        // --- 2. WHEN & 3. THEN (Cuando y Entonces) ---

        mockMvc.perform(
                        // (WHEN) Simula una petición POST a la URL raíz
                        post("/api/mascotas")
                                // (Mentor): ¡NUEVA SEGURIDAD!
                                // Tu SecurityConfig  dice que POST es solo para ADMINS.
                                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                                .contentType(MediaType.APPLICATION_JSON)
                                // (Mentor): ¡NUEVO! Aquí es donde enviamos el body
                                .content(requestBodyJson)
                )
                // (THEN) Verificamos la respuesta HTTP
                .andExpect(
                        // 1. Esperamos un 201 CREATED
                        status().isCreated()
                )
                .andExpect(
                        // 2. Verificamos que el JSON de respuesta tenga los datos
                        jsonPath("$.nombre", is("Garfield"))
                )
                .andExpect(
                        jsonPath("$.especie", is("Gato"))
                )
                .andExpect(
                        // 3. Verificamos que la lógica de negocio (edad) funcionó
                        jsonPath("$.dato", is(5))
                )
                .andExpect(
                        // 4. Verificamos que el ID fue generado (no es nulo)
                        jsonPath("$.id", notNullValue())
                );

        // --- 4. VERIFICACIÓN EN BD (El paso extra) ---
        // (Mentor): ¿Cómo sabemos que REALMENTE se guardó?
        // ¡Consultamos el repositorio real!

        List<Mascota> mascotasEnDB = mascotaRepository.findAll();

        // Verificamos que la BD (que estaba vacía por el @BeforeEach) ahora tiene 1 mascota
        assertEquals(1, mascotasEnDB.size(), "La base de datos debería tener 1 mascota");

        // Verificamos que los datos en la BD son correctos
        assertEquals("Garfield", mascotasEnDB.get(0).getNombre(), "El nombre en la BD no coincide");
    }

    @Test
    @DisplayName("Prueba POST /api/mascotas - Sad Path (400 Bad Request) por validación")
    void testCreateMascota_WhenInvalidRequest_ShouldReturn400BadRequest() throws Exception {

        // --- 1. GIVEN (Dado) ---
        // (Preparamos un DTO inválido)
        MascotaRequestDTO requestDTO = new MascotaRequestDTO();
        requestDTO.setNombre(null); // <-- ¡Hacemos que el DTO sea inválido!
        requestDTO.setEspecie("Gato"); // Los otros campos están bien
        requestDTO.setFechaNacimiento(LocalDate.now().minusMonths(6));
        requestDTO.setDuenioId(123L);

        // Convertimos el DTO (inválido) a JSON
        String requestBodyJson = objectMapper.writeValueAsString(requestDTO);

        // --- 2. WHEN & 3. THEN (Cuando y Entonces) ---

        mockMvc.perform(
                        // (WHEN) Enviamos el JSON inválido
                        post("/api/mascotas")
                                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyJson)
                )
                // (THEN) Verificamos la respuesta HTTP
                .andExpect(
                        // ¡Esperamos un 400 Bad Request!
                        status().isBadRequest()
                );

        // --- 4. VERIFICACIÓN EN BD ---
        // (Mentor): Verificamos que NADA se guardó en la BD,
        // ya que la validación debió fallar ANTES de llamar al servicio.
        assertEquals(0, mascotaRepository.count(), "La BD debe estar vacía si la validación falla");
    }

    @Test
    @DisplayName("Prueba PUT /api/mascotas/{id} - Happy Path (200 OK)")
    void testUpdateMascota_WhenExistsAndValidRequest_ShouldReturn200OK() throws Exception {

        // --- 1. GIVEN (Dado) ---
        // (Insertamos la mascota ORIGINAL en la BD de prueba H2)
        Mascota mascotaOriginal = Mascota.builder()
                .nombre("Firulais")
                .especie("Perro")
                .raza("Mestizo")
                .fechaNacimiento(LocalDate.now().minusYears(3))
                .duenoId(1L)
                .active(true)
                .build();

        Mascota mascotaEnDB = mascotaRepository.save(mascotaOriginal);
        Long idReal = mascotaEnDB.getId(); // Capturamos el ID real

        // (Preparamos el DTO con los datos NUEVOS)
        MascotaRequestDTO requestDTO = new MascotaRequestDTO();
        requestDTO.setNombre("Garfield"); // <-- Nombre actualizado
        requestDTO.setEspecie("Gato");      // <-- Especie actualizada
        requestDTO.setRaza("Persa");
        requestDTO.setFechaNacimiento(LocalDate.now().minusYears(5));
        requestDTO.setDuenioId(456L);

        // Convertimos el DTO (nuevo) a JSON
        String requestBodyJson = objectMapper.writeValueAsString(requestDTO);


        // --- 2. WHEN & 3. THEN (Cuando y Entonces) ---

        mockMvc.perform(
                        // (WHEN) Simula una petición PUT al ID existente
                        put("/api/mascotas/" + idReal)
                                // (Mentor): Verificamos la seguridad (ROLE_ADMIN para PUT)
                                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyJson) // Enviamos el JSON con los datos nuevos
                )
                // (THEN) Verificamos la respuesta HTTP
                .andExpect(
                        // 1. Esperamos un 200 OK (PUT devuelve 200)
                        status().isOk()
                )
                .andExpect(
                        // 2. Verificamos que el JSON de respuesta tenga los datos NUEVOS
                        jsonPath("$.nombre", is("Garfield"))
                )
                .andExpect(
                        jsonPath("$.especie", is("Gato"))
                )
                .andExpect(
                        // 3. Verificamos que la lógica de negocio (edad) se recalculó
                        jsonPath("$.dato", is(5))
                )
                .andExpect(
                        jsonPath("$.id", is(idReal.intValue()))
                );

        // --- 4. VERIFICACIÓN EN BD (El paso extra) ---
        // (Mentor): Verificamos que el cambio SÍ persistió en la BD H2

        Mascota mascotaActualizadaEnDB = mascotaRepository.findById(idReal).get();

        assertEquals("Garfield", mascotaActualizadaEnDB.getNombre(), "El nombre en la BD no se actualizó");
        assertEquals("Gato", mascotaActualizadaEnDB.getEspecie(), "La especie en la BD no se actualizó");
    }

    @Test
    @DisplayName("Prueba PUT /api/mascotas/{id} - Sad Path (404 Not Found)")
    void testUpdateMascota_WhenNotExists_ShouldReturn404NotFound() throws Exception {

        // --- 1. GIVEN (Dado) ---
        long idInexistente = 99L;

        // Preparamos un DTO VÁLIDO
        MascotaRequestDTO requestDTO = new MascotaRequestDTO();
        requestDTO.setNombre("Fantasma");
        requestDTO.setEspecie("Desconocido");
        // (Mentor): ¡LA CORRECCIÓN! Usamos una fecha que SÍ está en el pasado.
        requestDTO.setFechaNacimiento(LocalDate.now().minusYears(1));
        requestDTO.setDuenioId(1L);

        String requestBodyJson = objectMapper.writeValueAsString(requestDTO);

        // --- 2. WHEN & 3. THEN (Cuando y Entonces) ---

        mockMvc.perform(
                        put("/api/mascotas/" + idInexistente)
                                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyJson) // Ahora enviamos un body VÁLIDO
                )
                // (THEN) Ahora sí esperamos el 404
                .andExpect(
                        status().isNotFound()
                );

        // --- 4. VERIFICACIÓN EN BD ---
        assertEquals(0, mascotaRepository.count(), "La BD debe seguir vacía");
    }

    @Test
    @DisplayName("Prueba DELETE /api/mascotas/{id} - Happy Path (204 No Content)")
    void testDeleteMascota_WhenExists_ShouldReturn204NoContent() throws Exception {

        // --- 1. GIVEN (Dado) ---
        // (Insertamos una mascota en la BD de prueba para poder borrarla)
        Mascota mascotaParaBorrar = Mascota.builder()
                .nombre("Mascota a Borrar")
                .especie("Gato")
                .fechaNacimiento(LocalDate.now().minusYears(1))
                .duenoId(1L)
                .active(true)
                .build();

        // Guardamos y obtenemos el ID real
        Mascota mascotaEnDB = mascotaRepository.save(mascotaParaBorrar);
        Long idReal = mascotaEnDB.getId();

        // Verificación previa: Nos aseguramos de que la BD tiene 1 mascota
        assertEquals(1, mascotaRepository.count());

        // --- 2. WHEN & 3. THEN (Cuando y Entonces) ---

        mockMvc.perform(
                        // (WHEN) Simula una petición DELETE al ID existente
                        delete("/api/mascotas/" + idReal)
                                // (Mentor): Verificamos la seguridad. Tu SecurityConfig
                                // exige un ADMIN para cualquier método DELETE.
                                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                )
                // (THEN) Verificamos la respuesta HTTP
                .andExpect(
                        // ¡Esperamos un 204 No Content!
                        status().isNoContent()
                );

        // --- 4. VERIFICACIÓN EN BD (El paso final) ---
        // (Mentor): Verificamos que la mascota SÍ fue borrada de la BD H2

        assertEquals(0, mascotaRepository.count(), "La mascota no fue borrada de la BD");
        assertFalse(mascotaRepository.findById(idReal).isPresent(), "La mascota todavía existe en la BD");
    }

    @Test
    @DisplayName("Prueba DELETE /api/mascotas/{id} - Sad Path (404 Not Found)")
    void testDeleteMascota_WhenNotExists_ShouldReturn404NotFound() throws Exception {

        // --- 1. GIVEN (Dado) ---
        // (La BD está vacía gracias a @BeforeEach)
        long idInexistente = 99L;

        // --- 2. WHEN & 3. THEN (Cuando y Entonces) ---

        mockMvc.perform(
                        // (WHEN) Hacemos un DELETE a un ID que no existe
                        delete("/api/mascotas/" + idInexistente)
                                // (Mentor): Aún debemos ser un ADMIN para que la
                                // petición pase el filtro de seguridad.
                                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                )
                // (THEN) Verificamos la respuesta HTTP
                .andExpect(
                        // ¡Esperamos un 404 Not Found!
                        status().isNotFound()
                );

        // --- 4. VERIFICACIÓN EN BD ---
        // Verificamos que la BD sigue vacía
        assertEquals(0, mascotaRepository.count(), "La BD debe seguir vacía");
    }

}