package co.cue.agendamiento_service.mapper;

import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.CirugiaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.ConsultaRequestDTO;

import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.EsteticaRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.requestdtos.VacunacionRequestDTO;
import co.cue.agendamiento_service.models.entities.dtos.serviciosdtos.responsedtos.*;

import co.cue.agendamiento_service.models.entities.servicios.*;
import org.springframework.stereotype.Component;

@Component // Marca la clase como un componente Spring para que pueda ser inyectada en otros beans.
public class ServicioMapper {

//MAPEO DE ENTIDAD A DTO
    public ServicioResponseDTO toResponseDTO(Servicio entity) {
        // Verifica el tipo de servicio y delega al método correspondiente
        if (entity instanceof Consulta consulta) {
            return toConsultaResponseDTO(consulta); // Mapea una Consulta
        }
        if (entity instanceof Cirugia cirugia) {
            return toCirugiaResponseDTO(cirugia);    // Mapea una Cirugía
        }
        if (entity instanceof Estetica estetica) {
            return toEsteticaResponseDTO(estetica);  // Mapea un Servicio de Estética
        }
        if (entity instanceof Vacunacion vacunacion) {
            return toVacunacionResponseDTO(vacunacion); // Mapea un Servicio de Vacunación
        }
        // Si el tipo no está contemplado, lanza excepción
        throw new IllegalArgumentException("Tipo de Servicio no mapeado: " + entity.getClass().getName());
    }


    //MÉTODOS PRIVADOS PARA CADA TIPO
    private ConsultaResponseDTO toConsultaResponseDTO(Consulta entity) {
        ConsultaResponseDTO dto = new ConsultaResponseDTO();  // Crea un DTO específico para Consulta
        mapBaseEntityToResponse(entity, dto);   // Mapea los campos comunes de Servicio
        return dto;     // Retorna el DTO mapeado
    }

    private CirugiaResponseDTO toCirugiaResponseDTO(Cirugia entity) {
        CirugiaResponseDTO dto = new CirugiaResponseDTO();    // Crea DTO específico para Cirugía
        mapBaseEntityToResponse(entity, dto);    // Mapea los campos comunes de Servicio
        dto.setRequiereQuirofano(entity.isRequiereQuirofano());  // Mapea si requiere quirófano
        dto.setNotasPreoperatorias(entity.getNotasPreoperatorias());   // Mapea las notas preoperatorias
        return dto;
    }

    private EsteticaResponseDTO toEsteticaResponseDTO(Estetica entity) {
        EsteticaResponseDTO dto = new EsteticaResponseDTO();  // DTO para Estética
        mapBaseEntityToResponse(entity, dto);                  // Campos comunes
        dto.setTipoArreglo(entity.getTipoArreglo());           // Mapea el tipo de arreglo estético
        return dto;
    }

    private VacunacionResponseDTO toVacunacionResponseDTO(Vacunacion entity) {
        VacunacionResponseDTO dto = new VacunacionResponseDTO();  // DTO para Vacunación
        mapBaseEntityToResponse(entity, dto);                      // Campos comunes
        dto.setNombreBiologico(entity.getNombreBiologico());      // Mapea el nombre del biológico
        return dto;
    }



    //MÉTODO PRIVADO PARA CAMPOS COMUNES
    private void mapBaseEntityToResponse(Servicio entity, ServicioResponseDTO dto) {
        dto.setId(entity.getId());   // ID del servicio
        dto.setNombre(entity.getNombre());   // Nombre del servicio
        dto.setDescripcion(entity.getDescripcion());  // Descripción
        dto.setDuracionPromedioMinutos(entity.getDuracionPromedioMinutos());  // Duración promedio en minutos
        dto.setPrecio(entity.getPrecio());  // Precio
        dto.setActivo(entity.isActivo());   // Estado de activación del servicio
    }

//MAPEO DE DTO A ENTIDAD
    public Consulta toEntity(ConsultaRequestDTO dto) {
        return new Consulta(
                dto.getNombre(), dto.getDescripcion(),  // Nombre y descripción
                dto.getDuracionPromedioMinutos(), dto.getPrecio()  //Duración y precio
        );
    }

    public Cirugia toEntity(CirugiaRequestDTO dto) {
        return new Cirugia(
                dto.getNombre(), dto.getDescripcion(),
                dto.getDuracionPromedioMinutos(), dto.getPrecio(),
                dto.isRequiereQuirofano(), dto.getNotasPreoperatorias() // Campos específicos de cirugía
        );
    }

    public Estetica toEntity(EsteticaRequestDTO dto) {
        return new Estetica(
                dto.getNombre(), dto.getDescripcion(),
                dto.getDuracionPromedioMinutos(), dto.getPrecio(),
                dto.getTipoArreglo()    // Campo específico de estética
        );
    }

    public Vacunacion toEntity(VacunacionRequestDTO dto) {
        return new Vacunacion(
                dto.getNombre(), dto.getDescripcion(),
                dto.getDuracionPromedioMinutos(), dto.getPrecio(),
                dto.getNombreBiologico()   // Campo específico de vacunación
        );
    }
}