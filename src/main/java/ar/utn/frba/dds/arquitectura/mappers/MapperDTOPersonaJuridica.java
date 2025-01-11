package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOCanje;
import ar.utn.frba.dds.arquitectura.dtos.DTOPersonaJuridica;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Canje;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOPersonaJuridica {

    public static List<DTOPersonaJuridica> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOPersonaJuridica::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOPersonaJuridica convertirADTO(Object objeto){
        PersonaJuridica personaJuridica = (PersonaJuridica) objeto;
        if(personaJuridica!=null){
            return new DTOPersonaJuridica(
                    personaJuridica.getId(),
                    personaJuridica.getRazonSocial(),
                    personaJuridica.getTipoOrganizacion(),
                    personaJuridica.getPuntosAcumulados(),
                    personaJuridica.getPesosDonados(),
                    personaJuridica.getFormasDeColaboracion(),
                    MapperDTOOferta.convertirListaADTOs(new ArrayList<>(personaJuridica.getOfertas())),
                    MapperDTOHeladera.convertirListaADTOs(new ArrayList<>(personaJuridica.getHeladeras())),
                    MapperDTOCuestionarioRespondido.convertirADTO(personaJuridica.getCuestionarioRespondido()),
                    MapperDTORubro.convertirADTO(personaJuridica.getRubro()),
                    MapperDTODireccion.convertirADTO(personaJuridica.getDireccion())
            );
        }
        else{
            return null;
        }
    }
}
