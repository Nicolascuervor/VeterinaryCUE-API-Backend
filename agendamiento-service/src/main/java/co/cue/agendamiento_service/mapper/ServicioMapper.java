package co.cue.agendamiento_service.mapper;

import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.CirugiaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.ConsultaRequestDTO;

import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.EsteticaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.VacunacionRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos.*;

import co.cue.agendamiento_service.models.entities.servicios.*;
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
        if (entity instanceof Estetica) {
            return toEsteticaResponseDTO((Estetica) entity);
        }
        if (entity instanceof Vacunacion) {
            return toVacunacionResponseDTO((Vacunacion) entity);
        }
        throw new IllegalArgumentException("Tipo de Servicio no mapeado: " + entity.getClass().getName());
    }

    private ConsultaResponseDTO toConsultaResponseDTO(Consulta entity) {
        ConsultaResponseDTO dto = new ConsultaResponseDTO();
        mapBaseEntityToResponse(entity, dto);
        return dto;
    }

    private CirugiaResponseDTO toCirugiaResponseDTO(Cirugia entity) {
        CirugiaResponseDTO dto = new CirugiaResponseDTO();
        mapBaseEntityToResponse(entity, dto);
        dto.setRequiereQuirofano(entity.isRequiereQuirofano());
        dto.setNotasPreoperatorias(entity.getNotasPreoperatorias());
        return dto;
    }

    private EsteticaResponseDTO toEsteticaResponseDTO(Estetica entity) {
        EsteticaResponseDTO dto = new EsteticaResponseDTO();
        mapBaseEntityToResponse(entity, dto);
        dto.setTipoArreglo(entity.getTipoArreglo());
        return dto;
    }

    private VacunacionResponseDTO toVacunacionResponseDTO(Vacunacion entity) {
        VacunacionResponseDTO dto = new VacunacionResponseDTO();
        mapBaseEntityToResponse(entity, dto);
        dto.setNombreBiologico(entity.getNombreBiologico());
        return dto;
    }

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
                dto.getNombre(), dto.getDescripcion(),
                dto.getDuracionPromedioMinutos(), dto.getPrecio()
        );
    }

    public Cirugia toEntity(CirugiaRequestDTO dto) {
        return new Cirugia(
                dto.getNombre(), dto.getDescripcion(),
                dto.getDuracionPromedioMinutos(), dto.getPrecio(),
                dto.isRequiereQuirofano(), dto.getNotasPreoperatorias()
        );
    }

    public Estetica toEntity(EsteticaRequestDTO dto) {
        return new Estetica(
                dto.getNombre(), dto.getDescripcion(),
                dto.getDuracionPromedioMinutos(), dto.getPrecio(),
                dto.getTipoArreglo()
        );
    }

    public Vacunacion toEntity(VacunacionRequestDTO dto) {
        return new Vacunacion(
                dto.getNombre(), dto.getDescripcion(),
                dto.getDuracionPromedioMinutos(), dto.getPrecio(),
                dto.getNombreBiologico()
        );
    }
}