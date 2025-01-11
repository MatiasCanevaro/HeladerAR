package ar.utn.frba.dds.arquitectura.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DTOCuestionario {
    private String id;
    private String nombre;
    private List<DTOPregunta> preguntas;
}
