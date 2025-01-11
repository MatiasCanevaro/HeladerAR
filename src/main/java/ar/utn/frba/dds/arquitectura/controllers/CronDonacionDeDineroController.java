package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DonacionDeDinero;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.CalculadoraDePuntos;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class CronDonacionDeDineroController {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private CalculadoraDePuntos calculadoraDePuntos;
    private Repositorio repositorio;

    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                this.procesarDonacionesDeDinero();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.DAYS);
    }

    public void stop() {
        scheduler.shutdown();
    }

    public void procesarDonacionesDeDinero() {
        List<Object> donaciones = repositorio.buscarTodos("DonacionDeDinero")
                .stream()
                .filter(obj -> {
                    DonacionDeDinero donacion = (DonacionDeDinero) obj;
                    return donacion.getFrecuenciaEnDia() != 0 && donacion.getActivo();
                })
                .toList();

        for (Object objeto : donaciones) {
            DonacionDeDinero donacion = (DonacionDeDinero) objeto;
            LocalDate fechaDonacion = donacion.getFechaDonacion();
            Integer frecuenciaEnDia = donacion.getFrecuenciaEnDia();
            LocalDate today = LocalDate.now();

            Long diasDesdeLaDonacion = ChronoUnit.DAYS.between(fechaDonacion, today);

            if (diasDesdeLaDonacion % frecuenciaEnDia == 0) {
                Double monto = donacion.getMonto();
                Double puntos = calculadoraDePuntos.calcularPuntosPesosDonados(monto);
                PersonaFisica personaFisica = donacion.getPersonaFisica();
                PersonaJuridica personaJuridica = donacion.getPersonaJuridica();

                if (personaFisica != null) {
                    personaFisica.sumarPuntosYResetearAtributosDeColaboraciones(puntos);
                } else if (personaJuridica != null) {
                    personaJuridica.sumarPuntosYResetearAtributosDeColaboraciones(puntos);
                }
            }
        }
    }
}
