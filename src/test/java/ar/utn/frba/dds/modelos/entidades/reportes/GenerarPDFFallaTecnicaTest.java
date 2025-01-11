package ar.utn.frba.dds.modelos.entidades.reportes;

import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.incidentes.FallaTecnica;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenerarPDFFallaTecnicaTest {
    public static void main(String[] args){
        Heladera heladera1 = mock(Heladera.class);
        Heladera heladera2 = mock(Heladera.class);
        Heladera heladera3 = mock(Heladera.class);
        Heladera heladera4 = mock(Heladera.class);
        Heladera heladera5 = mock(Heladera.class);

        String id1 = "00000000-0000-0000-0000-000000000001";
        String id2 = "00000000-0000-0000-0000-000000000002";
        String id3 = "00000000-0000-0000-0000-000000000003";
        String id4 = "00000000-0000-0000-0000-000000000004";
        String id5 = "00000000-0000-0000-0000-000000000005";

        when(heladera1.getId()).thenReturn(id1);
        when(heladera2.getId()).thenReturn(id2);
        when(heladera3.getId()).thenReturn(id3);
        when(heladera4.getId()).thenReturn(id4);
        when(heladera5.getId()).thenReturn(id5);

        FallaTecnica fallaTecnica1 = mock(FallaTecnica.class);
        FallaTecnica fallaTecnica2 = mock(FallaTecnica.class);
        FallaTecnica fallaTecnica3 = mock(FallaTecnica.class);
        FallaTecnica fallaTecnica4 = mock(FallaTecnica.class);
        FallaTecnica fallaTecnica5 = mock(FallaTecnica.class);

        when(fallaTecnica1.getHeladera()).thenReturn(heladera1);
        when(fallaTecnica2.getHeladera()).thenReturn(heladera2);
        when(fallaTecnica3.getHeladera()).thenReturn(heladera3);
        when(fallaTecnica4.getHeladera()).thenReturn(heladera4);
        when(fallaTecnica5.getHeladera()).thenReturn(heladera5);

        List<Object> fallasTecnicas = new ArrayList<>();
        fallasTecnicas.add(fallaTecnica1);
        fallasTecnicas.add(fallaTecnica2);
        fallasTecnicas.add(fallaTecnica3);
        fallasTecnicas.add(fallaTecnica4);
        fallasTecnicas.add(fallaTecnica5);

        Repositorio repositorioFallaTecnica = mock(Repositorio.class);
        when(repositorioFallaTecnica.buscarTodos("FallaTecnica")).
                thenReturn(fallasTecnicas);
        GenerarPDFFallaTecnica generarPDFFallaTecnica =
                GenerarPDFFallaTecnica.crearGenerarPDFFallaTecnica(repositorioFallaTecnica);
        generarPDFFallaTecnica.reportar();
    }
}
