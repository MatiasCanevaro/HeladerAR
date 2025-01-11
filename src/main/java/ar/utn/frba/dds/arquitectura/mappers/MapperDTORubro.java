package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOHeladera;
import ar.utn.frba.dds.arquitectura.dtos.DTORubro;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Rubro;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTORubro {
    public static List<DTORubro> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTORubro::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTORubro convertirADTO(Object obj) {
        if (obj instanceof Rubro) {
            Rubro rubro = (Rubro) obj;
            return new DTORubro(rubro.getId(), rubro.getNombre());
        } else {
            throw new ClassCastException("Expected Rubro but received " + obj.getClass().getName());
        }
    }
}
