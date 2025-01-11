package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.modelos.entidades.reportes.GenerarPDF;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@AllArgsConstructor
public class CronReportesController {
    private ScheduledExecutorService scheduler;
    private List<GenerarPDF> generadoresPDF;
    private Integer frecuenciaDeEjecucionEnDias;

    public void generarReportes() {
        Runnable task = this::realizarReporte;
        //agrego 1 día de delay para que no se generen reportes constantemente
        scheduler.scheduleAtFixedRate(task, 1, frecuenciaDeEjecucionEnDias, TimeUnit.HOURS);
    }

    public void realizarReporte(){
        this.generadoresPDF.forEach(GenerarPDF::reportar);}

}
