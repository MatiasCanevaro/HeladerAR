package ar.utn.frba.dds.modelos.entidades.reportes;

import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DonacionDeVianda;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenerarPDFDonacionDeViandasTest {
    public static void main(String[] args){
        PersonaFisica personaFisica1 = mock(PersonaFisica.class);
        PersonaFisica personaFisica2 = mock(PersonaFisica.class);
        PersonaFisica personaFisica3 = mock(PersonaFisica.class);
        PersonaFisica personaFisica4 = mock(PersonaFisica.class);
        PersonaFisica personaFisica5 = mock(PersonaFisica.class);

        when(personaFisica1.getNombre()).thenReturn("Mati");
        when(personaFisica2.getNombre()).thenReturn("Facu");
        when(personaFisica3.getNombre()).thenReturn("Flor");
        when(personaFisica4.getNombre()).thenReturn("Gari");
        when(personaFisica5.getNombre()).thenReturn("Juanma");

        DonacionDeVianda donacionDeVianda1 = mock(DonacionDeVianda.class);
        DonacionDeVianda donacionDeVianda2 = mock(DonacionDeVianda.class);
        DonacionDeVianda donacionDeVianda3 = mock(DonacionDeVianda.class);
        DonacionDeVianda donacionDeVianda4 = mock(DonacionDeVianda.class);
        DonacionDeVianda donacionDeVianda5 = mock(DonacionDeVianda.class);

        when(donacionDeVianda1.getPersonaFisica()).thenReturn(personaFisica1);
        when(donacionDeVianda2.getPersonaFisica()).thenReturn(personaFisica2);
        when(donacionDeVianda3.getPersonaFisica()).thenReturn(personaFisica3);
        when(donacionDeVianda4.getPersonaFisica()).thenReturn(personaFisica4);
        when(donacionDeVianda5.getPersonaFisica()).thenReturn(personaFisica5);

        List<DonacionDeVianda> donacionesDeViandas = new ArrayList<>();
        donacionesDeViandas.add(donacionDeVianda1);
        donacionesDeViandas.add(donacionDeVianda2);
        donacionesDeViandas.add(donacionDeVianda3);
        donacionesDeViandas.add(donacionDeVianda4);
        donacionesDeViandas.add(donacionDeVianda5);

        Repositorio repositorio = mock(Repositorio.class);
        when(repositorio.buscarTodos("DonacionDeVianda")).thenReturn(new ArrayList<>(donacionesDeViandas));

        GenerarPDFDonacionDeViandas generarPDFDonacionDeViandas =
                GenerarPDFDonacionDeViandas.crearGenerarPDFDonacionDeViandas(repositorio);
        generarPDFDonacionDeViandas.reportar();
    }
}