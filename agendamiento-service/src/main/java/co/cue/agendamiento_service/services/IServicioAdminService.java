package co.cue.agendamiento_service.services;

import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.CirugiaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.ConsultaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.EsteticaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.VacunacionRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos.ServicioResponseDTO;

import java.util.List;

public interface IServicioAdminService {
    // Devuelve una lista de todos los servicios activos como DTOs de respuesta
    List<ServicioResponseDTO> listarServicios();
    // Obtiene un servicio por su ID y lo devuelve como DTO de respuesta
    ServicioResponseDTO getServicioById(Long id);

    // Crea un nuevo servicio de tipo Consulta a partir de un DTO de solicitud
    // Devuelve el servicio creado como DTO de respuesta
    ServicioResponseDTO crearConsulta(ConsultaRequestDTO dto);

    // Crea un nuevo servicio de tipo Cirugía a partir de un DTO de solicitud
    // Devuelve el servicio creado como DTO de respuesta
    ServicioResponseDTO crearCirugia(CirugiaRequestDTO dto);

    // Crea un nuevo servicio de tipo Estética a partir de un DTO de solicitud
    // Devuelve el servicio creado como DTO de respuesta
    ServicioResponseDTO crearEstetica(EsteticaRequestDTO dto);

    // Crea un nuevo servicio de tipo Vacunación a partir de un DTO de solicitud
    // Devuelve el servicio creado como DTO de respuesta
    ServicioResponseDTO crearVacunacion(VacunacionRequestDTO dto);


    // Actualiza un servicio existente de tipo Consulta por su ID usando un DTO de solicitud
    // Devuelve el servicio actualizado como DTO de respuesta
    ServicioResponseDTO actualizarConsulta(Long id, ConsultaRequestDTO dto);

    // Actualiza un servicio existente de tipo Cirugía por su ID usando un DTO de solicitud
    // Devuelve el servicio actualizado como DTO de respuesta
    ServicioResponseDTO actualizarCirugia(Long id, CirugiaRequestDTO dto);

    // Actualiza un servicio existente de tipo Estética por su ID usando un DTO de solicitud
    // Devuelve el servicio actualizado como DTO de respuesta
    ServicioResponseDTO actualizarEstetica(Long id, EsteticaRequestDTO dto);

    // Actualiza un servicio existente de tipo Vacunación por su ID usando un DTO de solicitud
    // Devuelve el servicio actualizado como DTO de respuesta
    ServicioResponseDTO actualizarVacunacion(Long id, VacunacionRequestDTO dto);



    // Desactiva un servicio existente por su ID
    // El servicio deja de aparecer en listados activos y no puede ser usado
    void desactivarServicio(Long id);
}
