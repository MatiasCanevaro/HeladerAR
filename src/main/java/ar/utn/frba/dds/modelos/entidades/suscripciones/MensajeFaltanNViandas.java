package ar.utn.frba.dds.modelos.entidades.suscripciones;

import ar.utn.frba.dds.modelos.entidades.geografia.Direccion;
import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modulos.notificador.Mensaje;

import java.time.LocalDateTime;

public class MensajeFaltanNViandas {
    public Mensaje generarMensaje(Suscripcion suscripcion){
        Heladera heladera = suscripcion.getHeladera();
        PuntoEstrategico puntoEstrategico = heladera.getPuntoEstrategico();
        Direccion direccion = puntoEstrategico.getDireccion();
        String altura = direccion.getAltura();
        String calle = direccion.getCalle();

        return new Mensaje(
                "Queda poco lugar",
                "Te informamos que en la heladera de la dirección "
                        + calle + " " + altura +
                        " quedan " + suscripcion.getCantidadViandasDisponibles() + " lugares libres.",
                LocalDateTime.now());
    }
}
