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

@Service
@AllArgsConstructor
public class ServicioAdminServiceImpl implements IServicioAdminService {

    private final ServicioRepository servicioRepository;
    private final ServicioMapper mapper;

    // --- Métodos GET ---

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponseDTO> listarServicios() {
        return servicioRepository.findAllByActivoTrue().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioResponseDTO getServicioById(Long id) {
        Servicio servicio = findServicioActivoById(id);
        return mapper.toResponseDTO(servicio);
    }

    // --- Métodos POST (Creación) ---

    @Override
    @Transactional
    public ServicioResponseDTO crearConsulta(ConsultaRequestDTO dto) {
        Consulta consulta = mapper.toEntity(dto);
        Servicio servicioGuardado = servicioRepository.save(consulta);
        return mapper.toResponseDTO(servicioGuardado);
    }

    @Override
    @Transactional
    public ServicioResponseDTO crearCirugia(CirugiaRequestDTO dto) {
        Cirugia cirugia = mapper.toEntity(dto);
        Servicio servicioGuardado = servicioRepository.save(cirugia);
        return mapper.toResponseDTO(servicioGuardado);
    }

    /**
     * (Implementación Faltante - AÑADIDA)
     */
    @Override
    @Transactional
    public ServicioResponseDTO crearEstetica(EsteticaRequestDTO dto) {
        Estetica estetica = mapper.toEntity(dto);
        Servicio servicioGuardado = servicioRepository.save(estetica);
        return mapper.toResponseDTO(servicioGuardado);
    }

    /**
     * (Implementación Faltante - AÑADIDA)
     */
    @Override
    @Transactional
    public ServicioResponseDTO crearVacunacion(VacunacionRequestDTO dto) {
        Vacunacion vacunacion = mapper.toEntity(dto);
        Servicio servicioGuardado = servicioRepository.save(vacunacion);
        return mapper.toResponseDTO(servicioGuardado);
    }


    // --- Métodos PUT (Actualización) ---

    /**
     * (Mentor): La lógica de actualización siempre sigue 3 pasos:
     * 1. Obtener la entidad existente.
     * 2. Validar que sea del tipo correcto (ej. "Consulta").
     * 3. Mapear los campos del DTO a la entidad y guardar.
     */
    @Override
    @Transactional
    public ServicioResponseDTO actualizarConsulta(Long id, ConsultaRequestDTO dto) {
        Servicio entidad = findServicioActivoById(id);

        if (!(entidad instanceof Consulta)) {
            throw new IllegalArgumentException("El servicio ID " + id + " no es de tipo Consulta.");
        }

        Consulta consulta = (Consulta) entidad;

        // Mapeamos los campos base
        consulta.setNombre(dto.getNombre());
        consulta.setDescripcion(dto.getDescripcion());
        consulta.setPrecio(dto.getPrecio());
        consulta.setDuracionPromedioMinutos(dto.getDuracionPromedioMinutos());

        // (No tiene campos específicos)

        Servicio actualizado = servicioRepository.save(consulta);
        return mapper.toResponseDTO(actualizado);
    }

    /**
     * (Implementación Faltante - AÑADIDA)
     */
    @Override
    @Transactional
    public ServicioResponseDTO actualizarCirugia(Long id, CirugiaRequestDTO dto) {
        Servicio entidad = findServicioActivoById(id);

        if (!(entidad instanceof Cirugia)) {
            throw new IllegalArgumentException("El servicio ID " + id + " no es de tipo Cirugía.");
        }

        Cirugia cirugia = (Cirugia) entidad;

        // Mapeamos los campos base
        cirugia.setNombre(dto.getNombre());
        cirugia.setDescripcion(dto.getDescripcion());
        cirugia.setPrecio(dto.getPrecio());
        cirugia.setDuracionPromedioMinutos(dto.getDuracionPromedioMinutos());

        // Mapeamos los campos específicos de Cirugia
        cirugia.setRequiereQuirofano(dto.isRequiereQuirofano());
        cirugia.setNotasPreoperatorias(dto.getNotasPreoperatorias());

        Servicio actualizado = servicioRepository.save(cirugia);
        return mapper.toResponseDTO(actualizado);
    }

    /**
     * (Implementación Faltante - AÑADIDA)
     */
    @Override
    @Transactional
    public ServicioResponseDTO actualizarEstetica(Long id, EsteticaRequestDTO dto) {
        Servicio entidad = findServicioActivoById(id);

        if (!(entidad instanceof Estetica)) {
            throw new IllegalArgumentException("El servicio ID " + id + " no es de tipo Estética.");
        }

        Estetica estetica = (Estetica) entidad;

        // Mapeamos los campos base
        estetica.setNombre(dto.getNombre());
        estetica.setDescripcion(dto.getDescripcion());
        estetica.setPrecio(dto.getPrecio());
        estetica.setDuracionPromedioMinutos(dto.getDuracionPromedioMinutos());

        // Mapeamos los campos específicos de Estetica
        estetica.setTipoArreglo(dto.getTipoArreglo());

        Servicio actualizado = servicioRepository.save(estetica);
        return mapper.toResponseDTO(actualizado);
    }

    @Override
    @Transactional
    public ServicioResponseDTO actualizarVacunacion(Long id, VacunacionRequestDTO dto) {
        Servicio entidad = findServicioActivoById(id);

        if (!(entidad instanceof Vacunacion)) {
            throw new IllegalArgumentException("El servicio ID " + id + " no es de tipo Vacunación.");
        }

        Vacunacion vacunacion = (Vacunacion) entidad;

        // Mapeamos los campos base
        vacunacion.setNombre(dto.getNombre());
        vacunacion.setDescripcion(dto.getDescripcion());
        vacunacion.setPrecio(dto.getPrecio());
        vacunacion.setDuracionPromedioMinutos(dto.getDuracionPromedioMinutos());

        // Mapeamos los campos específicos de Vacunacion
        vacunacion.setNombreBiologico(dto.getNombreBiologico());

        Servicio actualizado = servicioRepository.save(vacunacion);
        return mapper.toResponseDTO(actualizado);
    }


    @Override
    @Transactional
    public void desactivarServicio(Long id) {
        Servicio servicio = findServicioActivoById(id);
        servicio.setActivo(false); // Soft Delete
        servicioRepository.save(servicio);
    }

    // --- Método Privado de Búsqueda ---

    private Servicio findServicioActivoById(Long id) {
        return servicioRepository.findById(id)
                .filter(Servicio::isActivo) // Filtramos que esté activo
                .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado o inactivo con ID: " + id));
    }
}