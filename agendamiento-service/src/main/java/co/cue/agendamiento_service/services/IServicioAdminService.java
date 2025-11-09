package co.cue.agendamiento_service.services;

import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.CirugiaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.ConsultaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.EsteticaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.VacunacionRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos.ServicioResponseDTO;

import java.util.List;

public interface IServicioAdminService {

    List<ServicioResponseDTO> listarServicios();
    ServicioResponseDTO getServicioById(Long id);

    ServicioResponseDTO crearConsulta(ConsultaRequestDTO dto);
    ServicioResponseDTO crearCirugia(CirugiaRequestDTO dto);
    ServicioResponseDTO crearEstetica(EsteticaRequestDTO dto);
    ServicioResponseDTO crearVacunacion(VacunacionRequestDTO dto);

    ServicioResponseDTO actualizarConsulta(Long id, ConsultaRequestDTO dto);
    ServicioResponseDTO actualizarCirugia(Long id, CirugiaRequestDTO dto);
    ServicioResponseDTO actualizarEstetica(Long id, EsteticaRequestDTO dto);
    ServicioResponseDTO actualizarVacunacion(Long id, VacunacionRequestDTO dto);

    void desactivarServicio(Long id);
}
