package co.cue.agendamiento_service.services;

import co.cue.agendamiento_service.mapper.ServicioMapper;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.CirugiaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.ConsultaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.EsteticaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.ServicioRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.VacunacionRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos.BulkServicioResponseDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos.ServicioResponseDTO;
import co.cue.agendamiento_service.models.entities.servicios.*;
import co.cue.agendamiento_service.repository.ServicioRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service  // Marca la clase como un servicio de Spring para inyección de dependencias
@AllArgsConstructor  // Genera un constructor con todos los campos (inyección de repositorios y mapper)
@Slf4j
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

    /**
     * Crea múltiples servicios de forma masiva.
     * Procesa cada servicio individualmente, permitiendo que algunos se creen exitosamente
     * aunque otros fallen. Devuelve un resumen con los servicios creados y los errores.
     */
    @Override
    @Transactional
    public BulkServicioResponseDTO crearServiciosMasivo(List<ServicioRequestDTO> servicios) {
        log.info("Iniciando creación masiva de {} servicios", servicios.size());
        
        List<ServicioResponseDTO> serviciosCreados = new ArrayList<>();
        List<BulkServicioResponseDTO.ErrorServicioDTO> errores = new ArrayList<>();
        
        for (int i = 0; i < servicios.size(); i++) {
            ServicioRequestDTO servicioDTO = servicios.get(i);
            String nombreServicio = servicioDTO.getNombre() != null ? servicioDTO.getNombre() : "Sin nombre";
            String tipoServicio = obtenerTipoServicio(servicioDTO);
            
            try {
                ServicioResponseDTO servicioCreado = crearServicioPorTipo(servicioDTO);
                serviciosCreados.add(servicioCreado);
                log.debug("Servicio {} (índice {}) creado exitosamente: {}", nombreServicio, i, servicioCreado.getId());
            } catch (Exception e) {
                String mensajeError = e.getMessage() != null ? e.getMessage() : "Error desconocido al crear el servicio";
                errores.add(new BulkServicioResponseDTO.ErrorServicioDTO(i, nombreServicio, tipoServicio, mensajeError));
                log.error("Error al crear servicio {} (índice {}): {}", nombreServicio, i, mensajeError, e);
            }
        }
        
        int totalProcesados = servicios.size();
        int totalExitosos = serviciosCreados.size();
        int totalFallidos = errores.size();
        
        log.info("Creación masiva completada: {} procesados, {} exitosos, {} fallidos", 
                totalProcesados, totalExitosos, totalFallidos);
        
        return new BulkServicioResponseDTO(serviciosCreados, errores, totalProcesados, totalExitosos, totalFallidos);
    }

    /**
     * Crea un servicio según su tipo específico.
     */
    private ServicioResponseDTO crearServicioPorTipo(ServicioRequestDTO dto) {
        if (dto instanceof ConsultaRequestDTO consultaDTO) {
            return crearConsulta(consultaDTO);
        } else if (dto instanceof CirugiaRequestDTO cirugiaDTO) {
            return crearCirugia(cirugiaDTO);
        } else if (dto instanceof EsteticaRequestDTO esteticaDTO) {
            return crearEstetica(esteticaDTO);
        } else if (dto instanceof VacunacionRequestDTO vacunacionDTO) {
            return crearVacunacion(vacunacionDTO);
        } else {
            throw new IllegalArgumentException("Tipo de servicio no reconocido: " + dto.getClass().getSimpleName());
        }
    }

    /**
     * Obtiene el tipo de servicio como String para logging y reportes.
     */
    private String obtenerTipoServicio(ServicioRequestDTO dto) {
        if (dto instanceof ConsultaRequestDTO) {
            return "CONSULTA";
        } else if (dto instanceof CirugiaRequestDTO) {
            return "CIRUGIA";
        } else if (dto instanceof EsteticaRequestDTO) {
            return "ESTETICA";
        } else if (dto instanceof VacunacionRequestDTO) {
            return "VACUNACION";
        } else {
            return "DESCONOCIDO";
        }
    }
}