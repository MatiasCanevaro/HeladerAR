package ar.utn.frba.dds.modelos.entidades.suscripciones;

import ar.utn.frba.dds.modelos.entidades.geografia.Direccion;
import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modulos.notificador.Mensaje;

import java.time.LocalDateTime;
import java.util.List;

public class MensajeGeneradorDeSugerencias {
    public Mensaje generarMensaje(Sugerencia sugerencia){
        Heladera heladera = sugerencia.getHeladeraOrigen();
        PuntoEstrategico puntoEstrategico = heladera.getPuntoEstrategico();
        Direccion direccion = puntoEstrategico.getDireccion();
        String altura = direccion.getAltura();
        String calle = direccion.getCalle();

        Mensaje mensaje = new Mensaje(
                "Nueva sugerencia",
                "Te informamos que en la heladera de la dirección "
                        + calle + " " + altura +
                        " hubo un desperfecto. Te sugerimos trasladar las viandas a:" +
                        this.listarHeladeras(sugerencia.getDistribuciones()), LocalDateTime.now()
        );
        return mensaje;
    }

    public String listarHeladeras(List<Distribucion> distribuciones){
        String stringARetornar = "";
        for(Distribucion distribucion : distribuciones){
            Heladera heladera = distribucion.getHeladeraDestino();
            PuntoEstrategico puntoEstrategico = heladera.getPuntoEstrategico();
            Direccion direccion = puntoEstrategico.getDireccion();
            String altura = direccion.getAltura();
            String calle = direccion.getCalle();
            Integer cantidadDeEspacioDisponibleEnViandas = heladera.cantidadDeEspacioDisponibleEnViandas();
            stringARetornar += "\n\n" + calle + " " + altura
                    + " tiene " + cantidadDeEspacioDisponibleEnViandas + " lugares libres ";
        }
        return stringARetornar;
    }
}
