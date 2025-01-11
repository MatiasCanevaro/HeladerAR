package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOHeladera;
import ar.utn.frba.dds.arquitectura.dtos.DTOVianda;
import ar.utn.frba.dds.modelos.entidades.heladeras.Vianda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOVianda {
    public static List<DTOVianda> convertirListaADTOs(List<Vianda> viandas) {
        return viandas.stream()
                .map(MapperDTOVianda::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOVianda convertirADTO(Vianda vianda) {
        if(vianda!=null){
            return new DTOVianda(
                    vianda.getId(),
                    vianda.getNombreComida(),
                    vianda.getFechaCaducidad(),
                    vianda.getCalorias(),
                    vianda.getPeso()
            );
        }
        else{
            return null;
        }
    }
}
