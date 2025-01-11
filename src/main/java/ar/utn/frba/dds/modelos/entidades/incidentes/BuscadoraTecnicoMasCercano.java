package ar.utn.frba.dds.modelos.entidades.incidentes;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.personas.Tecnico;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BuscadoraTecnicoMasCercano extends Persistente {
    private Repositorio repositorio;
    private Integer rangoEnMetros;

    public List<Tecnico> buscarTecnicosMasCercanos(PuntoEstrategico puntoHeladera) {
        List<Object> tecnicos = this.repositorio.buscarTodos("Tecnico");
        return tecnicos.stream()
                .map(t -> (Tecnico) t)
                .filter(t -> this.estaDentroDelRango(t, puntoHeladera))
                .collect(Collectors.toList());
    }

    public Boolean estaDentroDelRango(Tecnico tecnico, PuntoEstrategico puntoHeladera){
        PuntoEstrategico puntoTecnico = tecnico.getPuntoEstrategico();
        Double distancia = puntoHeladera.calcularDistanciaEnMetrosHasta(puntoTecnico);
        return distancia <= this.getRangoEnMetros();
    }
}
