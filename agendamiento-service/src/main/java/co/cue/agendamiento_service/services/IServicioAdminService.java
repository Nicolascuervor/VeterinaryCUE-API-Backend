package co.cue.agendamiento_service.services;

import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.CirugiaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.ConsultaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.EsteticaRequestDTO;

public interface IServicioAdminService {
    // Métodos GET
    List<ServicioResponseDTO> listarServicios();
    ServicioResponseDTO getServicioById(Long id);

    // Métodos POST (uno por cada tipo de servicio)
    ServicioResponseDTO crearConsulta(ConsultaRequestDTO dto);
    ServicioResponseDTO crearCirugia(CirugiaRequestDTO dto);
    ServicioResponseDTO crearEstetica(EsteticaRequestDTO dto);
    // (Aquí irían los otros, ej. crearVacunacion)

    // Métodos PUT (uno por cada tipo)
    ServicioResponseDTO actualizarConsulta(Long id, ConsultaRequestDTO dto);
    // (etc.)

    // Método DELETE (Soft Delete)
    void desactivarServicio(Long id);
}
