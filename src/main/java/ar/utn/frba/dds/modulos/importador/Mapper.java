package ar.utn.frba.dds.modulos.importador;

import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.FormaDeColaboracion;
import ar.utn.frba.dds.modelos.entidades.tarjetas.Tarjeta;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.tarjetas.SolicitudDeApertura;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.TipoDocumento;
import ar.utn.frba.dds.modelos.repositorios.RepositorioTarjeta;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.NoSuchElementException;

public class Mapper {
    public TipoDocumento tipoDocumentoMapper(String stringTipoDocumentoLeido) {
        TipoDocumento tipoDocumento = switch (stringTipoDocumentoLeido) {
            case "DNI" -> TipoDocumento.DNI;
            case "LE" -> TipoDocumento.LIBRETA_ENROLAMIENTO;
            case "LC" -> TipoDocumento.LIBRETA_CIVICA;
            default -> null;
        };
        return tipoDocumento;
    }
    public FormaDeColaboracion formaDeColaboracionMapper(String stringFormaDeColaboracionLeida) {
        FormaDeColaboracion formaDeColaboracion = switch (stringFormaDeColaboracionLeida) {
            case "DINERO" -> FormaDeColaboracion.DONACION_DE_DINERO;
            case "DONACION_VIANDAS" -> FormaDeColaboracion.DONACION_DE_VIANDA;
            case "REDISTRIBUCION_VIANDAS" -> FormaDeColaboracion.DISTRIBUCION_DE_VIANDA;
            case "ENTREGA_TARJETAS" -> FormaDeColaboracion.DISTRIBUCION_DE_TARJETAS;
            default -> null;
        };
        return formaDeColaboracion;
    }
    public LocalDate localDateMapper(String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            return LocalDate.parse(fecha, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Formato de fecha inválido: " + fecha);
            return null;
        }
    }
    public static MqttMessage mqttMessageMapper(
            SolicitudDeApertura solicitudDeApertura,RepositorioTarjeta repositorioTarjeta){
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
                        solicitudDeApertura.getId()
                        ;
                return new MqttMessage(mensajeAEnviar.getBytes());
    }
}
