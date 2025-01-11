package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOOpcion;
import ar.utn.frba.dds.arquitectura.dtos.DTOPregunta;
import ar.utn.frba.dds.modelos.entidades.cuestionarios.Opcion;
import ar.utn.frba.dds.modelos.entidades.cuestionarios.Pregunta;
import ar.utn.frba.dds.modelos.entidades.cuestionarios.TipoPregunta;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOPregunta {
    public static DTOPregunta convertirADTO(Object objeto){
        Pregunta pregunta = (Pregunta) objeto;
        TipoPregunta tipoPregunta = pregunta.getTipoPregunta();
        DTOPregunta dtoPregunta = new DTOPregunta(
                pregunta.getId(),
                pregunta.getDescripcion(),
                tipoPregunta,
                MapperDTOOpcion.convertirListaADTOs(pregunta.getOpciones()),
                false,
                false,
                false
        );

        if(TipoPregunta.ABIERTA==tipoPregunta){
            dtoPregunta.setEsAbierta(true);
        }
        if(TipoPregunta.UNICA==tipoPregunta){
            dtoPregunta.setEsUnica(true);
        }
        if(TipoPregunta.MULTIPLE==tipoPregunta){
            dtoPregunta.setEsMultiple(true);
        }
        return dtoPregunta;
    }
    public static List<DTOPregunta> convertirListaADTOs(List<Pregunta> preguntas) {
        return preguntas.stream()
                .map(MapperDTOPregunta::convertirADTO)
                .collect(Collectors.toList());
    }
}
