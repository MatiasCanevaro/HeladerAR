package ar.utn.frba.dds.modelos.entidades.puntosColocacion;

import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdapterConcretoAPI implements AdapterAPI {
    private APIAdaptada apiAdaptada;
    public List<PuntoEstrategico> solicitarPuntosDeColocacion(String latitud, String longitud, Double radioEnKM) {
        return apiAdaptada.solicitarPuntosDeColocacion(latitud, longitud, radioEnKM);
    }
}
