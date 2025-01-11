package ar.utn.frba.dds.modelos.entidades.accesoAlimentario.formasDeColaboracion;

import ar.utn.frba.dds.arquitectura.controllers.CronDonacionDeDineroController;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.CalculadoraDePuntos;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;

public class CronDonacionDeDineroTest {
    public static void main(String[] args) {
        CalculadoraDePuntos calculadoraDePuntos = CalculadoraDePuntos.crearCalculadoraDePuntos();
        Repositorio repositorio = new Repositorio();
        CronDonacionDeDineroController cronTask = new CronDonacionDeDineroController(calculadoraDePuntos,repositorio);

        cronTask.start();

        Runtime.getRuntime().addShutdownHook(new Thread(cronTask::stop));
    }
}