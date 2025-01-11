package ar.utn.frba.dds.arquitectura.dtos;

import ar.utn.frba.dds.modelos.entidades.cuestionarios.TipoPregunta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DTOPregunta {
    private String id;
    private String descripcion;
    private TipoPregunta tipoPregunta;
    private List<DTOOpcion> dtoOpciones;
    private Boolean esAbierta;
    private Boolean esUnica;
    private Boolean esMultiple;
}
