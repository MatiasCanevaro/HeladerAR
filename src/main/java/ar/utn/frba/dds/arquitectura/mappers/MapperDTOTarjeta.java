package ar.utn.frba.dds.arquitectura.mappers;

import ar.utn.frba.dds.arquitectura.dtos.DTOTarjeta;
import ar.utn.frba.dds.modelos.entidades.tarjetas.Tarjeta;
import java.util.List;
import java.util.stream.Collectors;

public class MapperDTOTarjeta {
    public static List<DTOTarjeta> convertirListaADTOs(List<Object> objetos) {
        return objetos.stream()
                .map(MapperDTOTarjeta::convertirADTO)
                .collect(Collectors.toList());
    }

    public static DTOTarjeta convertirADTO(Object tarjeta) {
        Tarjeta tarjetaReal = (Tarjeta) tarjeta;
        if(tarjetaReal!=null){
            return new DTOTarjeta(
                    tarjetaReal.getId(),
                    tarjetaReal.getCodigoAlfanumerico(),
                    MapperDTOPersonaVulnerable.convertirADTO(tarjetaReal.getPersonaVulnerable()),
                    MapperDTOPersonaFisica.convertirADTO(tarjetaReal.getPersonaFisica()),
                    tarjetaReal.getFechasRetirosDeViandas(),
                    tarjetaReal.getCantidadDeVecesQuePuedeSerUtilizadaPorDia()
            );
        }
        else{
            return null;
        }
    }
}
