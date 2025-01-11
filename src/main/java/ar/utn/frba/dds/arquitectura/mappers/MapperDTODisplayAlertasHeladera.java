package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTODisplayAlertasHeladera;
import ar.utn.frba.dds.modelos.entidades.heladeras.DisplayAlertasHeladera;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class MapperDTODisplayAlertasHeladera {
    public static List<DTODisplayAlertasHeladera> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTODisplayAlertasHeladera::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTODisplayAlertasHeladera convertirADTO(Object objeto){
        DisplayAlertasHeladera consulta = (DisplayAlertasHeladera) objeto;
        LocalDateTime ultima_vez_activa = consulta.getUltima_vez_activa();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime ultima_vez_activa_truncada = ultima_vez_activa.truncatedTo(ChronoUnit.SECONDS);
        String fechaFormateada = ultima_vez_activa_truncada.format(formatter);
        return new DTODisplayAlertasHeladera(
                consulta.getId(),
                MapperDTOProvincia.convertirADTO(consulta.getProvincia()),
                consulta.getDireccion(),
                consulta.getModelo(),
                consulta.getTipoIncidente().name(),
                fechaFormateada
        );
    }
}
