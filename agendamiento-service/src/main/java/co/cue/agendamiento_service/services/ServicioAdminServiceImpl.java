package co.cue.agendamiento_service.services;

import co.cue.agendamiento_service.mapper.ServicioMapper;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.CirugiaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.ConsultaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.EsteticaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.VacunacionRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos.ServicioResponseDTO;
import co.cue.agendamiento_service.models.entities.servicios.*;
import co.cue.agendamiento_service.repository.ServicioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service  // Marca la clase como un servicio de Spring para inyección de dependencias
@AllArgsConstructor  // Genera un constructor con todos los campos (inyección de repositorios y mapper)

public class ServicioAdminServiceImpl implements IServicioAdminService {

    private final ServicioRepository servicioRepository;  // Repositorio para acceder a la entidad Servicio
    private final ServicioMapper mapper;                 // Mapper para convertir entre entidades y DTOs


    /**
     * Lista todos los servicios activos.
     */
    @Override
    @Transactional(readOnly = true)  // Lectura transaccional (sin bloqueo de BD)
    public List<ServicioResponseDTO> listarServicios() {
        // Obtiene todos los servicios activos y los convierte a DTOs de respuesta
        return servicioRepository.findAllByActivoTrue().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }


    /**
     * Obtiene un servicio activo por su ID.
     */
    @Override
    @Transactional(readOnly = true)
    public ServicioResponseDTO getServicioById(Long id) {
        // Busca un servicio activo por ID y lo convierte a DTO de respuesta
        Servicio servicio = findServicioActivoById(id);
        return mapper.toResponseDTO(servicio);
    }



    /**
     * Crea una nueva Consulta.
     */
    @Override
    @Transactional
    public ServicioResponseDTO crearConsulta(ConsultaRequestDTO dto) {
        // Convierte el DTO en entidad, guarda en BD y devuelve DTO de respuesta
        Consulta consulta = mapper.toEntity(dto);
        Servicio servicioGuardado = servicioRepository.save(consulta);
        return mapper.toResponseDTO(servicioGuardado);
    }


    /**
     * Crea una nueva Cirugía.
     */
    @Override
    @Transactional
    public ServicioResponseDTO crearCirugia(CirugiaRequestDTO dto) {
        Cirugia cirugia = mapper.toEntity(dto); // DTO a entidad
        Servicio servicioGuardado = servicioRepository.save(cirugia);  // Guardar
        return mapper.toResponseDTO(servicioGuardado);  // Devolver DTO
    }



    /**
     * Crea un nuevo servicio de Estética.
     */
    @Override
    @Transactional
    public ServicioResponseDTO crearEstetica(EsteticaRequestDTO dto) {
        Estetica estetica = mapper.toEntity(dto);  // Convierte DTO a entidad Estética.
        Servicio servicioGuardado = servicioRepository.save(estetica); // Guarda.
        return mapper.toResponseDTO(servicioGuardado);
    }


    /**
     * Crea un nuevo servicio de Vacunación.
     */
    @Override
    @Transactional
    public ServicioResponseDTO crearVacunacion(VacunacionRequestDTO dto) {
        Vacunacion vacunacion = mapper.toEntity(dto);   // Convierte DTO a entidad Vacunación.
        Servicio servicioGuardado = servicioRepository.save(vacunacion);   // Guarda.
        return mapper.toResponseDTO(servicioGuardado);
    }




    /**
     * Actualiza una Consulta existente.
     */
    @Override
    @Transactional
    public ServicioResponseDTO actualizarConsulta(Long id, ConsultaRequestDTO dto) {
        Servicio entidad = findServicioActivoById(id); // Buscar servicio activo por ID

        if (!(entidad instanceof Consulta)) { //Valida el tipo
            throw new IllegalArgumentException("El servicio ID " + id + " no es de tipo Consulta.");
        }

        Consulta consulta = (Consulta) entidad; // Conversión de tipo

        consulta.setNombre(dto.getNombre());  // Actualiza campos.
        consulta.setDescripcion(dto.getDescripcion());
        consulta.setPrecio(dto.getPrecio());
        consulta.setDuracionPromedioMinutos(dto.getDuracionPromedioMinutos());


        Servicio actualizado = servicioRepository.save(consulta); // Guardar cambios
        return mapper.toResponseDTO(actualizado);    // Devolver DTO
    }



    /**
     * Actualiza una Cirugía existente.
     */
    @Override
    @Transactional
    public ServicioResponseDTO actualizarCirugia(Long id, CirugiaRequestDTO dto) {
        Servicio entidad = findServicioActivoById(id);

        if (!(entidad instanceof Cirugia)) {
            throw new IllegalArgumentException("El servicio ID " + id + " no es de tipo Cirugía.");
        }

        Cirugia cirugia = (Cirugia) entidad;
        // Actualiza campos generales y específicos de Cirugía
        cirugia.setNombre(dto.getNombre());
        cirugia.setDescripcion(dto.getDescripcion());
        cirugia.setPrecio(dto.getPrecio());
        cirugia.setDuracionPromedioMinutos(dto.getDuracionPromedioMinutos());
        cirugia.setRequiereQuirofano(dto.isRequiereQuirofano());
        cirugia.setNotasPreoperatorias(dto.getNotasPreoperatorias());

        Servicio actualizado = servicioRepository.save(cirugia);
        return mapper.toResponseDTO(actualizado);
    }



    /**
     * Actualiza un servicio de Estética.
     */
    @Override
    @Transactional
    public ServicioResponseDTO actualizarEstetica(Long id, EsteticaRequestDTO dto) {
        Servicio entidad = findServicioActivoById(id);

        if (!(entidad instanceof Estetica)) {
            throw new IllegalArgumentException("El servicio ID " + id + " no es de tipo Estética.");
        }

        Estetica estetica = (Estetica) entidad;
        estetica.setNombre(dto.getNombre());
        estetica.setDescripcion(dto.getDescripcion());
        estetica.setPrecio(dto.getPrecio());
        estetica.setDuracionPromedioMinutos(dto.getDuracionPromedioMinutos());


        estetica.setTipoArreglo(dto.getTipoArreglo());

        Servicio actualizado = servicioRepository.save(estetica);
        return mapper.toResponseDTO(actualizado);
    }


    /**
     * Actualiza un servicio de Vacunación.
     */
    @Override
    @Transactional
    public ServicioResponseDTO actualizarVacunacion(Long id, VacunacionRequestDTO dto) {
        Servicio entidad = findServicioActivoById(id);

        if (!(entidad instanceof Vacunacion)) {
            throw new IllegalArgumentException("El servicio ID " + id + " no es de tipo Vacunación.");
        }

        Vacunacion vacunacion = (Vacunacion) entidad;
        vacunacion.setNombre(dto.getNombre());
        vacunacion.setDescripcion(dto.getDescripcion());
        vacunacion.setPrecio(dto.getPrecio());
        vacunacion.setDuracionPromedioMinutos(dto.getDuracionPromedioMinutos());
        vacunacion.setNombreBiologico(dto.getNombreBiologico());

        Servicio actualizado = servicioRepository.save(vacunacion);
        return mapper.toResponseDTO(actualizado);
    }



    /**
     * Desactiva (Soft Delete) un servicio.
     */
    @Override
    @Transactional
    public void desactivarServicio(Long id) {
        Servicio servicio = findServicioActivoById(id); // Obtiene y verifica que esté activo.
        servicio.setActivo(false); // Soft Delete
        servicioRepository.save(servicio); // Guarda el cambio.
    }


    /**
     * Busca un servicio por ID y verifica que esté activo.
     */
    private Servicio findServicioActivoById(Long id) {
        return servicioRepository.findById(id)   // Busca por ID
                .filter(Servicio::isActivo)       // Filtra para asegurarse de que esté activo.
                .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado o inactivo con ID: " + id)); // Lanza excepción si no se encuentra.
    }
}