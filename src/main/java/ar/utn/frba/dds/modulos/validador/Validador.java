package ar.utn.frba.dds.modulos.validador;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Validador {
    private List<FormasDeValidacion> formasDeValidacion;

    public ResultadoValidacion esValida(String contrasenia){
        ResultadoValidacion resultadoValidacion = new ResultadoValidacion();
        for (FormasDeValidacion formaDeValidacion : formasDeValidacion) {
                formaDeValidacion.validar(contrasenia, resultadoValidacion);
        }
        return resultadoValidacion;
    }

    public static Validador crearValidador(){
        ASCIIImprimible asciiImprimible = new ASCIIImprimible();
        Longitud longitud = new Longitud();
        Top10000 top10000 = new Top10000();
        return Validador
                .builder()
                .formasDeValidacion(Arrays.asList(asciiImprimible, longitud, top10000))
                .build();
    }
}