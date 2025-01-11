package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.heladeras.MedicionDeTemperatura;
import ar.utn.frba.dds.modelos.entidades.incidentes.AlertaHeladera;
import ar.utn.frba.dds.modelos.entidades.incidentes.AlertaHeladeraInfo;
import ar.utn.frba.dds.modelos.entidades.incidentes.TipoIncidente;
import ar.utn.frba.dds.modelos.repositorios.RepositorioAlertaHeladera;
import ar.utn.frba.dds.modelos.repositorios.RepositorioHeladera;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CronFallaConexionController {
    private Integer frecuenciaDeEjecucionEnMinutos;
    private Integer maximaDiferenciaTolerableEnMinutos;
    private ScheduledExecutorService scheduler;
    private RepositorioAlertaHeladera repositorioAlertaHeladera;
    private RepositorioHeladera repositorioHeladera;

    public void comenzarBarrido(){
        Runnable task = this::barrer;
        scheduler.scheduleAtFixedRate(task, 0, frecuenciaDeEjecucionEnMinutos, TimeUnit.MINUTES);
    }

    public void barrer(){
        List<Heladera> heladerasActivas = repositorioHeladera.buscarTodasLasHeladerasActivas();
        List<Heladera> heladerasAReportar =
                this.obtenerHeladerasQueDejaronDeRecibirMedicionesDeTemperatura(heladerasActivas);
        this.reportar(heladerasAReportar);
    }

    public List<Heladera> obtenerHeladerasQueDejaronDeRecibirMedicionesDeTemperatura(List<Heladera> heladeras){
        return heladeras.
                stream().
                filter(h -> this.dejoDeRecibirLaTemperatura(h.obtenerUltimaMedicion())).
                toList();
    }

    public Boolean dejoDeRecibirLaTemperatura(MedicionDeTemperatura medicionDeTemperatura){
        LocalDateTime fechaYHoraUltimaVezQueRecibioTemperatura = medicionDeTemperatura.getFechaYHoraMedicion();
        LocalDateTime fechaYHoraActual = LocalDateTime.now();
        long minutosDeDiferencia = Duration.between(fechaYHoraUltimaVezQueRecibioTemperatura, fechaYHoraActual).toMinutes();
        return minutosDeDiferencia > maximaDiferenciaTolerableEnMinutos;
    }

    public void reportar(List<Heladera> heladeras){
        heladeras.forEach(h -> { LocalDateTime fechaYHoraActual = LocalDateTime.now();
            h.dejarDeEstarActiva();
            AlertaHeladeraInfo info = AlertaHeladeraInfo.crearAlertaHeladeraInfo(h, TipoIncidente.FALLA_CONEXION);
            AlertaHeladera alertaHeladera = new AlertaHeladera(info);
            MedicionDeTemperatura medicionDeTemperatura = h.obtenerUltimaMedicion();
            LocalDateTime fechaYHoraUltimaMedicion = medicionDeTemperatura.getFechaYHoraMedicion();
            System.out.println(
                    "Se reportó una falla en <" + h
                    + ">, a las <" + fechaYHoraActual + ">"
                    + " la última vez que se había recibido fue a las: <" + fechaYHoraUltimaMedicion + ">");
            repositorioAlertaHeladera.guardar(alertaHeladera);
        });
    }

    public static CronFallaConexionController crearCronFallaConexion(
            Integer frecuenciaDeEjecucionEnMinutos,Integer maximaDiferenciaTolerableEnMinutos,
            RepositorioHeladera repositorioHeladera,RepositorioAlertaHeladera repositorioAlertaHeladera){
        return CronFallaConexionController
                .builder()
                .frecuenciaDeEjecucionEnMinutos(frecuenciaDeEjecucionEnMinutos)
                .maximaDiferenciaTolerableEnMinutos(maximaDiferenciaTolerableEnMinutos)
                .repositorioHeladera(repositorioHeladera)
                .repositorioAlertaHeladera(repositorioAlertaHeladera)
                .scheduler(Executors.newScheduledThreadPool(1))
                .build();
    }
}
