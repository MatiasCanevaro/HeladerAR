package ar.utn.frba.dds.modelos.entidades.mqttclient;

import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.tarjetas.SolicitudDeApertura;
import ar.utn.frba.dds.modelos.entidades.tarjetas.Tarjeta;
import ar.utn.frba.dds.modelos.repositorios.RepositorioTarjeta;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Optional;

public class MapperBroker {
    public static MqttMessage mqttMessageMapper(
            SolicitudDeApertura solicitudDeApertura, RepositorioTarjeta repositorioTarjeta){
        PersonaFisica personaFisicaABuscar = solicitudDeApertura.getPersonaFisica();
        Optional<Tarjeta> tarjetaOptional = Optional.ofNullable(repositorioTarjeta.buscar(personaFisicaABuscar));
        Heladera heladeraANotificar = solicitudDeApertura.getHeladera();
        String idAEnviar = heladeraANotificar.getId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime fechaYHoraVencimiento = solicitudDeApertura.getFechaYHoraVencimiento();
        String fechaYHoraVencimientoAEnviar = fechaYHoraVencimiento.format(formatter);
        Tarjeta tarjeta = tarjetaOptional.orElseThrow(() ->
                new NoSuchElementException("Tarjeta no encontrada para la persona física especificada."));
        String mensajeAEnviar = idAEnviar + ", " +
                tarjeta.getCodigoAlfanumerico() + ", " +
                fechaYHoraVencimientoAEnviar + ", " +
                solicitudDeApertura.getId();
        return new MqttMessage(mensajeAEnviar.getBytes());
    }
}
