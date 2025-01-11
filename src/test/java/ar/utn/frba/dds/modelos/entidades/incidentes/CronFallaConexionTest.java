package ar.utn.frba.dds.modelos.entidades.incidentes;

import ar.utn.frba.dds.arquitectura.controllers.CronFallaConexionController;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.heladeras.MedicionDeTemperatura;
import ar.utn.frba.dds.modelos.repositorios.RepositorioAlertaHeladera;
import ar.utn.frba.dds.modelos.repositorios.RepositorioHeladera;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class CronFallaConexionTest {
    public static void main(String[] args){
        LocalDateTime fechaYHoraReporte1 = LocalDateTime.now();
        MedicionDeTemperatura medicionDeTemperatura1 = mock(MedicionDeTemperatura.class);
        when(medicionDeTemperatura1.getFechaYHoraMedicion()).thenReturn(fechaYHoraReporte1);

        LocalDateTime fechaYHoraReporte2 = LocalDateTime.of(
                1980,1, 1, 1, 1, 1);
        MedicionDeTemperatura medicionDeTemperatura2 = mock(MedicionDeTemperatura.class);
        when(medicionDeTemperatura2.getFechaYHoraMedicion()).thenReturn(fechaYHoraReporte2);

        Heladera heladera1 = mock(Heladera.class);
        when(heladera1.getEstaActiva()).thenReturn(true);
        when(heladera1.obtenerUltimaMedicion()).thenReturn(medicionDeTemperatura1);

        Heladera heladera2 = mock(Heladera.class);
        when(heladera2.getEstaActiva()).thenReturn(true);
        when(heladera2.obtenerUltimaMedicion()).thenReturn(medicionDeTemperatura2);

        RepositorioHeladera repositorioHeladera = mock(RepositorioHeladera.class);
        List<Heladera> heladerasActivas = new ArrayList<>();
        heladerasActivas.add(heladera1);
        heladerasActivas.add(heladera2);
        when(repositorioHeladera.buscarTodasLasHeladerasActivas()).thenReturn(heladerasActivas);

        RepositorioAlertaHeladera repositorioAlertaHeladera = mock(RepositorioAlertaHeladera.class);

        CronFallaConexionController cronFallaConexion =
                CronFallaConexionController.crearCronFallaConexion(
                        1,
                        1,
                        repositorioHeladera,
                        repositorioAlertaHeladera
                );

        cronFallaConexion.comenzarBarrido();
    }
}
