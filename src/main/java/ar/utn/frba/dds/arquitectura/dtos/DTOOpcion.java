package ar.utn.frba.dds.arquitectura.dtos;
import ar.utn.frba.dds.modelos.entidades.cuestionarios.TipoPregunta;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DTOOpcion {
    private String id;
    private String nombre;
}
