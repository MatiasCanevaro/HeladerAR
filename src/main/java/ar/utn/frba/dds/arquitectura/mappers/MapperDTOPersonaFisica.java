package ar.utn.frba.dds.arquitectura.mappers;


import ar.utn.frba.dds.arquitectura.dtos.DTOPersonaFisica;
import ar.utn.frba.dds.arquitectura.dtos.DTOTarjeta;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class MapperDTOPersonaFisica {
    public static List<DTOPersonaFisica> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOPersonaFisica::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOPersonaFisica convertirADTO(Object persona) {
        PersonaFisica personaReal = (PersonaFisica) persona;
        if (personaReal == null) {
            return null;
        }
        return new DTOPersonaFisica(
                personaReal.getId(),
                personaReal.getNombre(),
                personaReal.getApellido(),
                personaReal.getTipoDocumento(),
                personaReal.getNumeroDocumento(),
                personaReal.getFechaNacimiento(),
                MapperDTODireccion.convertirADTO(personaReal.getDireccion()),
                personaReal.getPuntosAcumulados(),
                personaReal.getPesosDonados(),
                personaReal.getViandasDonadas(),
                personaReal.getViandasDistribuidas(),
                personaReal.getTarjetasDistribuidas(),
                personaReal.getFormasDeColaboracion(),
                MapperDTOCuestionarioRespondido.convertirADTO(personaReal.getCuestionarioRespondido()),
                MapperDTOEmail.convertirADTO(personaReal.getEmail()),
                null //TODO: ver si esto esta bien, o mantener lo anterior: MapperDTOTarjeta.convertirListaADTOs(new ArrayList<>(personaReal.getTarjetas()))
        );
    }
}
