package ar.utn.frba.dds.modelos.entidades.puntosColocacion;

import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;

import java.util.List;

public interface AdapterAPI {
    public List<PuntoEstrategico> solicitarPuntosDeColocacion(String latitud, String longitud, Double radioEnKM);
}
