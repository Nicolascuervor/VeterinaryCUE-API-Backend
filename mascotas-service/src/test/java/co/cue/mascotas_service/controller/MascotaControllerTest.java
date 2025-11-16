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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@SpringBootTest

@AutoConfigureMockMvc
class MascotaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setup() {
        mascotaRepository.deleteAll();
    }


    @Test
    @DisplayName("Prueba GET /api/mascotas/{id} - Happy Path (200 OK)")
    void testGetMascotaById_WhenExists_ShouldReturn200OK() throws Exception {

        Mascota mascotaGuardada = Mascota.builder()
                .nombre("Firulais")
                .especie("Perro")
                .raza("Mestizo")
                .fechaNacimiento(LocalDate.now().minusYears(2))
                .duenoId(1L)
                .active(true)
                .build();

        Mascota mascotaEnDB = mascotaRepository.save(mascotaGuardada);
        Long idReal = mascotaEnDB.getId();

        mockMvc.perform(

                        get("/api/mascotas/" + idReal)
                                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_DUEÑO")))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(

                        content().contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(

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

        long idInexistente = 99L;


        mockMvc.perform(

                        get("/api/mascotas/" + idInexistente)
                                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_DUEÑO")))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isNotFound()
                );
    }

    @Test
    @DisplayName("Prueba POST /api/mascotas - Happy Path (201 Faltancreated)")
    void testCreateMascota_WhenValidRequest_ShouldReturn201Created() throws Exception {


        MascotaRequestDTO requestDTO = new MascotaRequestDTO();
        requestDTO.setNombre("Garfield");
        requestDTO.setEspecie("Gato");
        requestDTO.setRaza("Persa");
        requestDTO.setFechaNacimiento(LocalDate.now().minusYears(5));
        requestDTO.setDuenioId(123L);


        String requestBodyJson = objectMapper.writeValueAsString(requestDTO);


        mockMvc.perform(
                        post("/api/mascotas")

                                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyJson)
                )
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        jsonPath("$.nombre", is("Garfield"))
                )
                .andExpect(
                        jsonPath("$.especie", is("Gato"))
                )
                .andExpect(
                        jsonPath("$.dato", is(5))
                )
                .andExpect(
                        jsonPath("$.id", notNullValue())
                );


        List<Mascota> mascotasEnDB = mascotaRepository.findAll();

        assertEquals(1, mascotasEnDB.size(), "La base de datos debería tener 1 mascota");

        assertEquals("Garfield", mascotasEnDB.get(0).getNombre(), "El nombre en la BD no coincide");
    }

    @Test
    @DisplayName("Prueba POST /api/mascotas - Sad Path (400 Bad Request) por validación")
    void testCreateMascota_WhenInvalidRequest_ShouldReturn400BadRequest() throws Exception {

        MascotaRequestDTO requestDTO = new MascotaRequestDTO();
        requestDTO.setNombre(null);
        requestDTO.setEspecie("Gato");
        requestDTO.setFechaNacimiento(LocalDate.now().minusMonths(6));
        requestDTO.setDuenioId(123L);

        String requestBodyJson = objectMapper.writeValueAsString(requestDTO);


        mockMvc.perform(
                        post("/api/mascotas")
                                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyJson)
                )
                .andExpect(
                        status().isBadRequest()
                );
        assertEquals(0, mascotaRepository.count(), "La BD debe estar vacía si la validación falla");
    }

    @Test
    @DisplayName("Prueba PUT /api/mascotas/{id} - Happy Path (200 OK)")
    void testUpdateMascota_WhenExistsAndValidRequest_ShouldReturn200OK() throws Exception {
        Mascota mascotaOriginal = Mascota.builder()
                .nombre("Firulais")
                .especie("Perro")
                .raza("Mestizo")
                .fechaNacimiento(LocalDate.now().minusYears(3))
                .duenoId(1L)
                .active(true)
                .build();

        Mascota mascotaEnDB = mascotaRepository.save(mascotaOriginal);
        Long idReal = mascotaEnDB.getId();

        MascotaRequestDTO requestDTO = new MascotaRequestDTO();
        requestDTO.setNombre("Garfield");
        requestDTO.setEspecie("Gato");
        requestDTO.setRaza("Persa");
        requestDTO.setFechaNacimiento(LocalDate.now().minusYears(5));
        requestDTO.setDuenioId(456L);

        String requestBodyJson = objectMapper.writeValueAsString(requestDTO);



        mockMvc.perform(

                        put("/api/mascotas/" + idReal)
                                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyJson)
                )

                .andExpect(

                        status().isOk()
                )
                .andExpect(

                        jsonPath("$.nombre", is("Garfield"))
                )
                .andExpect(
                        jsonPath("$.especie", is("Gato"))
                )
                .andExpect(

                        jsonPath("$.dato", is(5))
                )
                .andExpect(
                        jsonPath("$.id", is(idReal.intValue()))
                );


        Mascota mascotaActualizadaEnDB = mascotaRepository.findById(idReal).get();

        assertEquals("Garfield", mascotaActualizadaEnDB.getNombre(), "El nombre en la BD no se actualizó");
        assertEquals("Gato", mascotaActualizadaEnDB.getEspecie(), "La especie en la BD no se actualizó");
    }

    @Test
    @DisplayName("Prueba PUT /api/mascotas/{id} - Sad Path (404 Not Found)")
    void testUpdateMascota_WhenNotExists_ShouldReturn404NotFound() throws Exception {


        long idInexistente = 99L;


        MascotaRequestDTO requestDTO = new MascotaRequestDTO();
        requestDTO.setNombre("Fantasma");
        requestDTO.setEspecie("Desconocido");
        requestDTO.setFechaNacimiento(LocalDate.now().minusYears(1));
        requestDTO.setDuenioId(1L);

        String requestBodyJson = objectMapper.writeValueAsString(requestDTO);



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