package ar.utn.frba.dds.modelos.entidades.incidentes;

import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.Tecnico;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ReportadorFallaTecnica {
    private Sugeridor sugeridor;
    private Repositorio repositorio;

    public ReportadorFallaTecnica() {
        this.repositorio = new Repositorio();
        this.sugeridor = Sugeridor.crearSugeridor();
    }

    public void reportar(FallaTecnicaInfo fallaTecnicaInfo) {
        Heladera heladera = fallaTecnicaInfo.getHeladera();
        heladera.dejarDeEstarActiva();
        sugeridor.sugerirTecnicos(heladera);
    }

    public void reportar(AlertaHeladeraInfo alertaHeladeraInfo) {
        AlertaHeladera alertaHeladera = new AlertaHeladera(alertaHeladeraInfo);
        List<Tecnico> tecnicosSugeridos = sugeridor.sugerirTecnicos(alertaHeladeraInfo.getHeladera());
        alertaHeladera.agregarTecnicos(tecnicosSugeridos);
        repositorio.guardar(alertaHeladera);
        Heladera heladera = alertaHeladera.getHeladera();
        heladera.dejarDeEstarActiva();
        System.out.println(alertaHeladeraInfo.getHeladera().getEstaActiva());
        repositorio.guardar(heladera);
        sugeridor.sugerirTecnicos(heladera);
    }
}
