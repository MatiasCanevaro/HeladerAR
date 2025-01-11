package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOHeladera;
import ar.utn.frba.dds.arquitectura.dtos.DTOMedicionDeTemperatura;
import ar.utn.frba.dds.modelos.entidades.heladeras.MedicionDeTemperatura;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOMedicionDeTemperatura {
    public static List<DTOMedicionDeTemperatura> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOMedicionDeTemperatura::convertirADTO)
                .collect(Collectors.toList());
    }

    private static DTOMedicionDeTemperatura convertirADTO(Object objeto) {
        MedicionDeTemperatura medicionReal = (MedicionDeTemperatura) objeto;
        return new DTOMedicionDeTemperatura(
                medicionReal.getId(),
                medicionReal.getValor(),
                medicionReal.getFechaYHoraMedicion()
        );
    }
}
