package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTODisplayConsultarHeladerasActivas;
import ar.utn.frba.dds.arquitectura.dtos.DTODisplayDistribucionDeVianda;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DisplayDistribucionDeVianda;
import ar.utn.frba.dds.modelos.entidades.heladeras.DisplayConsultarHeladerasActivas;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTODisplayConsultarHeladerasActivas {
    public static List<DTODisplayConsultarHeladerasActivas> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTODisplayConsultarHeladerasActivas::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTODisplayConsultarHeladerasActivas convertirADTO(Object objeto){
        DisplayConsultarHeladerasActivas consulta = (DisplayConsultarHeladerasActivas) objeto;
        return new DTODisplayConsultarHeladerasActivas(
                consulta.getId(),
                MapperDTOProvincia.convertirADTO(consulta.getProvincia()),
                consulta.getDireccion(),
                consulta.getModelo(),
                consulta.getActivaDesde()
        );
    }
}
