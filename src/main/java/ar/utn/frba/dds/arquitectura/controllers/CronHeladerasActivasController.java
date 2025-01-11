package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.CalculadoraDePuntos;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class CronHeladerasActivasController {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private CalculadoraDePuntos calculadoraDePuntos;
    private Repositorio repositorio;

    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                this.procesarHeladerasActivas();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 30, TimeUnit.DAYS);
    }

    public void stop() {
        scheduler.shutdown();
    }

    public void procesarHeladerasActivas() {
        List<PersonaJuridica> personasJuridicas = repositorio.buscarTodos("PersonaJuridica")
                .stream()
                .map(obj -> (PersonaJuridica) obj)
                .toList();

        for (PersonaJuridica personaJuridica : personasJuridicas) {
            Integer cantidadDeHeladerasActivas = personaJuridica.cantidadDeHeladerasActivas();
            Integer cantidadTotalDeMesesEnQueLasHeladerasActivasLlevanEstandoActivas = personaJuridica.cantidadTotalDeMesesEnQueLasHeladerasActivasLlevanEstandoActivas();

            if (cantidadDeHeladerasActivas > 0) {
                Double puntos = calculadoraDePuntos.calcularPuntosHeladerasActivas(cantidadDeHeladerasActivas, cantidadTotalDeMesesEnQueLasHeladerasActivasLlevanEstandoActivas);
                personaJuridica.sumarPuntosYResetearAtributosDeColaboraciones(puntos);
            }
        }
    }
}