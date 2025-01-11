package ar.utn.frba.dds.modelos.entidades.suscripciones;

import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.repositorios.RepositorioSuscripcion;
import ar.utn.frba.dds.modulos.notificador.Notificador;
import ar.utn.frba.dds.modelos.entidades.personas.Email;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class SuscripcionAEventosTest {
    @Test
    public void tecnicoMasCercanoTest(){
        Heladera heladera = mock(Heladera.class);
        Suscripcion suscripcion = new Suscripcion();
        List<Suscripcion> suscripciones = new ArrayList<>();
        suscripciones.add(suscripcion);
        RepositorioSuscripcion repositorioSuscripcion = mock(RepositorioSuscripcion.class);
        when(repositorioSuscripcion.buscarPorHeladera(heladera)).
                thenReturn(suscripciones);

        when(heladera.cantidadDeEspacioDisponibleEnViandas()).thenReturn(2);
        suscripcion.setCantidadViandasFaltantes(2);

        when(heladera.obtenerCantidadDeViandasActuales()).thenReturn(7);
        suscripcion.setCantidadViandasDisponibles(7);

        when(heladera.getEstaActiva()).thenReturn(false);

        Notificador notificador = mock(Notificador.class);
        MensajeFaltanNViandas mensajeFaltanNViandas = mock(MensajeFaltanNViandas.class);
        MensajeQuedanNViandasDisponibles mensajeQuedanNViandasDisponibles = mock(MensajeQuedanNViandasDisponibles.class);
        MensajeGeneradorDeSugerencias mensajeGeneradorDeSugerencias = mock(MensajeGeneradorDeSugerencias.class);
        BuscadorHeladeraMasCercana buscadorHeladeraMasCercana = mock(BuscadorHeladeraMasCercana.class);

        FaltanNViandas faltanNViandas = new FaltanNViandas(mensajeFaltanNViandas,notificador);
        QuedanNViandasDisponibles quedanNViandasDisponibles =
                new QuedanNViandasDisponibles(mensajeQuedanNViandasDisponibles,notificador);
        GeneradorDeSugerencias generadorDeSugerencias =
                new GeneradorDeSugerencias(buscadorHeladeraMasCercana,notificador,mensajeGeneradorDeSugerencias);

        List<OpcionSuscripcion> opciones = new ArrayList<>();
        opciones.add(faltanNViandas);
        opciones.add(quedanNViandasDisponibles);
        opciones.add(generadorDeSugerencias);

        suscripcion.agregarOpciones(opciones);

        suscripcion.setHeladera(heladera);
        PersonaFisica personaFisica = mock(PersonaFisica.class);
        Email email = mock(Email.class);
        when(email.getCorreoElectronico()).thenReturn("");
        when(personaFisica.getEmail()).thenReturn(email);

        suscripcion.setPersonaFisica(personaFisica);

        ProcesadorDeSuscripciones procesadorDeSuscripciones = new ProcesadorDeSuscripciones(repositorioSuscripcion);
        procesadorDeSuscripciones.procesar(heladera);

        Mockito.verify(notificador, Mockito.times(3)).
                notificar(ArgumentMatchers.any(), ArgumentMatchers.any());
    }
}
