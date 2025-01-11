package ar.utn.frba.dds.modulos.importador;

import ar.utn.frba.dds.modelos.entidades.cuestionarios.TipoPregunta;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.FormaDeColaboracion;
import ar.utn.frba.dds.modelos.entidades.personas.TipoContacto;
import ar.utn.frba.dds.modelos.entidades.personas.TipoDocumento;
import ar.utn.frba.dds.modelos.entidades.personas.TipoOrganizacion;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MapperImportador {
    public TipoPregunta tipoPreguntaMapper(String stringTipoOpcion) {
        return switch (stringTipoOpcion) {
            case "UNICA" -> TipoPregunta.UNICA;
            case "MULTIPLE" -> TipoPregunta.MULTIPLE;
            case "ABIERTA" -> TipoPregunta.ABIERTA;
            default -> null;
        };
    }
    public TipoContacto medioDeContactoMapper(String stringMedioDeContacto) {
        return switch (stringMedioDeContacto) {
            case "EMAIL" -> TipoContacto.EMAIL;
            case "TELEFONO" -> TipoContacto.TELEFONO;
            case "WHATSAPP" -> TipoContacto.WHATSAPP;
            default -> null;
        };
    }
    public TipoDocumento tipoDocumentoMapper(String stringTipoDocumentoLeido) {
        return switch (stringTipoDocumentoLeido) {
            case "DNI" -> TipoDocumento.DNI;
            case "LE" -> TipoDocumento.LIBRETA_ENROLAMIENTO;
            case "LC" -> TipoDocumento.LIBRETA_CIVICA;
            default -> null;
        };
    }
    public TipoRol tipoRolMapper(String stringTipoRol) {
        return switch (stringTipoRol) {
            case "PERSONA_FISICA" -> TipoRol.PERSONA_FISICA;
            case "PERSONA_JURIDICA" -> TipoRol.PERSONA_JURIDICA;
            default -> null;
        };
    }

    public TipoOrganizacion tipoOrganizacionMapper(String tipoOrganizacionString) {
        return switch (tipoOrganizacionString) {
            case "GUBERNAMENTAL" -> TipoOrganizacion.GUBERNAMENTAL;
            case "ONG" -> TipoOrganizacion.ONG;
            case "EMPRESA" -> TipoOrganizacion.EMPRESA;
            case "INSTITUCION" -> TipoOrganizacion.INSTITUCION;
            default -> null;
        };
    }


    public List<FormaDeColaboracion> listaDeFormaDeColaboracionMapper(List<String> formasDeColaboracion) {
        return formasDeColaboracion.stream()
                .map(this::formaDeColaboracion)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public FormaDeColaboracion formaDeColaboracion(String stringTipoRol) {
        return switch (stringTipoRol) {
            case "DONACION_DE_DINERO" -> FormaDeColaboracion.DONACION_DE_DINERO;
            case "DONACION_DE_VIANDA" -> FormaDeColaboracion.DONACION_DE_VIANDA;
            case "DISTRIBUCION_DE_VIANDA" -> FormaDeColaboracion.DISTRIBUCION_DE_VIANDA;
            case "ASUMIR_CARGO_DE_HELADERA" -> FormaDeColaboracion.ASUMIR_CARGO_DE_HELADERA;
            case "DISTRIBUCION_DE_TARJETAS" -> FormaDeColaboracion.DISTRIBUCION_DE_TARJETAS;
            case "OFRECER_PRODUCTOS_Y_SERVICIOS" -> FormaDeColaboracion.OFRECER_PRODUCTOS_Y_SERVICIOS;
            default -> null;
        };
    }

    public FormaDeColaboracion formaDeColaboracionMapper(String stringFormaDeColaboracionLeida) {
        FormaDeColaboracion formaDeColaboracion = null;
        switch (stringFormaDeColaboracionLeida){
            case "DINERO":
                formaDeColaboracion = FormaDeColaboracion.DONACION_DE_DINERO;
                break;
            case "DONACION_VIANDAS":
                formaDeColaboracion = FormaDeColaboracion.DONACION_DE_VIANDA;
                break;
            case "REDISTRIBUCION_VIANDAS":
                formaDeColaboracion = FormaDeColaboracion.DISTRIBUCION_DE_VIANDA;
                break;
            case "ENTREGA_TARJETAS":
                formaDeColaboracion = FormaDeColaboracion.DISTRIBUCION_DE_TARJETAS;
                break;
        }
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
}
