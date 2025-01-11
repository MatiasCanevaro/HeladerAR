package ar.utn.frba.dds.modelos.entidades.suscripciones;

import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.repositorios.RepositorioSuscripcion;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor

public class ProcesadorDeSuscripciones {
    private RepositorioSuscripcion repositorioSuscripcion;
    public void procesar(Heladera heladera){
        List<Suscripcion> suscripciones = repositorioSuscripcion.buscarPorHeladera(heladera);
        suscripciones.forEach(Suscripcion::ejecutarOpciones);
    }
}
