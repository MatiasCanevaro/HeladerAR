package ar.utn.frba.dds.modulos.validador;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultadoValidacion {
    @Builder.Default
    private Boolean laContraseniaEsValida=true;
    @Builder.Default
    private List<String> errores = new ArrayList<>();
    public void agregarError(String error){
        this.errores.add(error);
    }
}
