package ar.utn.frba.dds.modulos.converters;

import ar.utn.frba.dds.modelos.entidades.suscripciones.FaltanNViandas;
import ar.utn.frba.dds.modelos.entidades.suscripciones.GeneradorDeSugerencias;
import ar.utn.frba.dds.modelos.entidades.suscripciones.OpcionSuscripcion;
import ar.utn.frba.dds.modelos.entidades.suscripciones.QuedanNViandasDisponibles;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter(autoApply = true)
public class OpcionAttributeConverter implements AttributeConverter<List<OpcionSuscripcion>, String> {
//    @Override
//    public String convertToDatabaseColumn(OpcionSuscripcion iopcion){
//        if(iopcion==null){
//            return null;
//        }
//        String opcion = "";
//        if(iopcion instanceof FaltanNViandas){
//            opcion = "FALTAN_N_VIANDAS";
//        }
//        else if(iopcion instanceof QuedanNViandasDisponibles){
//            opcion = "QUEDAN_N_VIANDAS_DISPONIBLES";
//        }
//        else if(iopcion instanceof GeneradorDeSugerencias){
//            opcion = "GENERADOR_DE_SUGERENCIAS";
//        }
//        return opcion;
//    }
//
//    @Override
//    public OpcionSuscripcion convertToEntityAttribute(String s) {
//        if (s == null) {
//            return null;
//        }
//
//        OpcionSuscripcion opcion = switch (s) {
//            case "FALTAN_N_VIANDAS" -> new FaltanNViandas();
//            case "QUEDAN_N_VIANDAS_DISPONIBLES" -> new QuedanNViandasDisponibles();
//            case "GENERADOR_DE_SUGERENCIAS" -> GeneradorDeSugerencias.crearGeneradorDeSugerencias();
//            default -> null;
//        };
//
//        return opcion;
//    }

    @Override
    public String convertToDatabaseColumn(List<OpcionSuscripcion> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
                .map(opcion -> {
                    if (opcion instanceof FaltanNViandas) {
                        return "FALTAN_N_VIANDAS";
                    } else if (opcion instanceof QuedanNViandasDisponibles) {
                        return "QUEDAN_N_VIANDAS_DISPONIBLES";
                    } else if (opcion instanceof GeneradorDeSugerencias) {
                        return "GENERADOR_DE_SUGERENCIAS";
                    } else {
                        throw new IllegalArgumentException("Unknown OpcionSuscripcion type");
                    }
                })
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }

    @Override
    public List<OpcionSuscripcion> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>();
        }
        String[] data = dbData.split(",");
        List<OpcionSuscripcion> list = new ArrayList<>();
        for (String s : data) {
            switch (s) {
                case "FALTAN_N_VIANDAS":
                    list.add(new FaltanNViandas());
                    break;
                case "QUEDAN_N_VIANDAS_DISPONIBLES":
                    list.add(new QuedanNViandasDisponibles());
                    break;
                case "GENERADOR_DE_SUGERENCIAS":
                    list.add(new GeneradorDeSugerencias());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown OpcionSuscripcion type: " + s);
            }
        }
        return list;
    }

}
