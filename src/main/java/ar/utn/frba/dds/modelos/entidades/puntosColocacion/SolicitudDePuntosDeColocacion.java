package ar.utn.frba.dds.modelos.entidades.puntosColocacion;

import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolicitudDePuntosDeColocacion {
    private AdapterAPI adapterAPI;

    public List<PuntoEstrategico> solicitarPuntosDeColocacion(String latitud, String longitud, Double radioEnKM){
        return adapterAPI.solicitarPuntosDeColocacion(latitud, longitud, radioEnKM);
    }
    public static SolicitudDePuntosDeColocacion crearSolicitudDePuntosDeColocacion(AdapterAPI adapterAPI){
        return SolicitudDePuntosDeColocacion
                .builder()
                .adapterAPI(adapterAPI)
                .build();
    }
}
