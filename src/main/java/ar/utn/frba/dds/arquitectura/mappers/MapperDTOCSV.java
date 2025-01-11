package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOCSV;
import ar.utn.frba.dds.modelos.entidades.csv.CSV;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOCSV {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static List<DTOCSV> convertirListaADTOs(List<CSV> csvs) {
        return csvs.stream()
                .map(MapperDTOCSV::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOCSV convertirADTO(CSV csv) {
        return new DTOCSV(
                csv.getNombre(),
                csv.getFueProcesado(),
                csv.getFechaYHoraAlta().format(formatter),
                MapperDTODonacionDeVianda.convertirListaADTOs(csv.getDonacionesDeVianda()),
                MapperDTODonacionDeDinero.convertirListaADTOs(csv.getDonacionesDeDinero()),
                MapperDTODistribucionDeTarjeta.convertirListaADTOs(csv.getDistribucionesDeTarjetas()),
                MapperDTODistribucionDeVianda.convertirListaADTOs(csv.getDistribucionesDeViandas()),
                MapperDTOPersonaFisica.convertirListaADTOs(csv.getPersonasFisicas().stream().map(Object.class::cast).collect(Collectors.toList()))
        );
    }
}
