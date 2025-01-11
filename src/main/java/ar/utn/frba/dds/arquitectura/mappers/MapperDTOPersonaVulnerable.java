package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOPersonaVulnerable;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaVulnerable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOPersonaVulnerable {
    public static List<DTOPersonaVulnerable> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOPersonaVulnerable::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOPersonaVulnerable convertirADTO(Object personaVulnerable) {
        PersonaVulnerable personaVulnerableReal = (PersonaVulnerable) personaVulnerable;
        if (personaVulnerableReal == null) {
            return null;
        }
        return new DTOPersonaVulnerable(
                personaVulnerableReal.getId(),
                personaVulnerableReal.getNombre(),
                personaVulnerableReal.getApellido(),
                personaVulnerableReal.getFechaNacimiento(),
                MapperDTODireccion.convertirADTO(personaVulnerableReal.getDireccion()),
                personaVulnerableReal.getNumeroDocumento(),
                personaVulnerableReal.getTipoDocumento(),
                new ArrayList<>((Collection) MapperDTOPersonaVulnerable.convertirListaADTOs(
                        new ArrayList<>(personaVulnerableReal.getMenoresACargo())))
        );
    }
}