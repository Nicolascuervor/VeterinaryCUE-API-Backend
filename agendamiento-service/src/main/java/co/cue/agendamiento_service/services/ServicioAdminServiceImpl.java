package co.cue.agendamiento_service.services;

import co.cue.agendamiento_service.mapper.ServicioMapper;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.CirugiaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.ConsultaRequestDTO;
import co.cue.agendamiento_service.models.entities.servicios.Cirugia;
import co.cue.agendamiento_service.models.entities.servicios.Consulta;
import co.cue.agendamiento_service.models.entities.servicios.Servicio;
import co.cue.agendamiento_service.repository.ServicioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());
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

    // (Implementaciones de crearEstetica, crearVacunacion...)

    // --- Métodos PUT (Actualización) ---

    @Override
    @Transactional
    public ServicioResponseDTO actualizarConsulta(Long id, ConsultaRequestDTO dto) {
        // (Colega Senior): Buscamos la entidad existente
        Servicio entidad = findServicioActivoById(id);

        // Validamos que sea del tipo correcto
        if (!(entidad instanceof Consulta)) {
            throw new IllegalArgumentException("El servicio ID " + id + " no es de tipo Consulta.");
        }

        Consulta consulta = (Consulta) entidad;

        // Actualizamos los campos
        consulta.setNombre(dto.getNombre());
        consulta.setDescripcion(dto.getDescripcion());
        consulta.setPrecio(dto.getPrecio());
        consulta.setDuracionPromedioMinutos(dto.getDuracionPromedioMinutos());

        Servicio actualizado = servicioRepository.save(consulta);
        return mapper.toResponseDTO(actualizado);
    }

    // (Implementaciones de actualizarCirugia, etc.)

    // --- Método DELETE ---

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
