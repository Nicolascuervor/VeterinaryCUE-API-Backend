package co.cue.agendamiento_service.mapper;

import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.CirugiaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.ConsultaRequestDTO;

import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos.ServicioResponseDTO;
import co.cue.agendamiento_service.models.entities.servicios.Cirugia;
import co.cue.agendamiento_service.models.entities.servicios.Consulta;
import co.cue.agendamiento_service.models.entities.servicios.Servicio;
import org.springframework.stereotype.Component;

@Component
public class ServicioMapper {


    public ServicioResponseDTO toResponseDTO(Servicio entity) {
        if (entity instanceof Consulta) {
            return toConsultaResponseDTO((Consulta) entity);
        }
        if (entity instanceof Cirugia) {
            return toCirugiaResponseDTO((Cirugia) entity);
        }
        // (Agregar "instanceof" para Estetica, Vacunacion, etc.)

        throw new IllegalArgumentException("Tipo de Servicio no mapeado: " + entity.getClass().getName());
    }

    private ServicioResponseDTO toConsultaResponseDTO(Consulta entity) {
        ServicioResponseDTO dto = new ServicioResponseDTO();
        mapBaseEntityToResponse(entity, dto);
        dto.setTipoServicio("CONSULTA");
        return dto;
    }

    private ServicioResponseDTO toCirugiaResponseDTO(Cirugia entity) {
        ServicioResponseDTO dto = new ServicioResponseDTO();
        mapBaseEntityToResponse(entity, dto);
        dto.setTipoServicio("CIRUGIA");

        // Mapear campos específicos
        dto.getDetalles().put("requiereQuirofano", entity.isRequiereQuirofano());
        dto.getDetalles().put("notasPreoperatorias", entity.getNotasPreoperatorias());
        return dto;
    }

    // Helper base
    private void mapBaseEntityToResponse(Servicio entity, ServicioResponseDTO dto) {
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setDuracionPromedioMinutos(entity.getDuracionPromedioMinutos());
        dto.setPrecio(entity.getPrecio());
        dto.setActivo(entity.isActivo());
    }


    public Consulta toEntity(ConsultaRequestDTO dto) {
        return new Consulta(
                dto.getNombre(),
                dto.getDescripcion(),
                dto.getDuracionPromedioMinutos(),
                dto.getPrecio()
        );
    }

    public Cirugia toEntity(CirugiaRequestDTO dto) {
        return new Cirugia(
                dto.getNombre(),
                dto.getDescripcion(),
                dto.getDuracionPromedioMinutos(),
                dto.getPrecio(),
                dto.isRequiereQuirofano(),
                dto.getNotasPreoperatorias()
        );
    }


}
