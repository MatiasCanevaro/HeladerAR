package ar.utn.frba.dds.modelos.entidades.reportes;

import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.tarjetas.AccionSolicitada;
import ar.utn.frba.dds.modelos.entidades.tarjetas.PaseDeTarjeta;
import ar.utn.frba.dds.modelos.entidades.tarjetas.ResultadoPaseDeTarjeta;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenerarPDFViandasTest {
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

        PaseDeTarjeta paseDeTarjeta1 = mock(PaseDeTarjeta.class);
        PaseDeTarjeta paseDeTarjeta2 = mock(PaseDeTarjeta.class);
        PaseDeTarjeta paseDeTarjeta3 = mock(PaseDeTarjeta.class);
        PaseDeTarjeta paseDeTarjeta4 = mock(PaseDeTarjeta.class);
        PaseDeTarjeta paseDeTarjeta5 = mock(PaseDeTarjeta.class);

        when(paseDeTarjeta1.getResultadoPaseDeTarjeta()).thenReturn(ResultadoPaseDeTarjeta.ACEPTADO);
        when(paseDeTarjeta2.getResultadoPaseDeTarjeta()).thenReturn(ResultadoPaseDeTarjeta.RECHAZADO);
        when(paseDeTarjeta3.getResultadoPaseDeTarjeta()).thenReturn(ResultadoPaseDeTarjeta.ACEPTADO);
        when(paseDeTarjeta4.getResultadoPaseDeTarjeta()).thenReturn(ResultadoPaseDeTarjeta.ACEPTADO);
        when(paseDeTarjeta5.getResultadoPaseDeTarjeta()).thenReturn(ResultadoPaseDeTarjeta.ACEPTADO);

        when(paseDeTarjeta1.getAccionSolicitada()).thenReturn(AccionSolicitada.INGRESO);
        when(paseDeTarjeta2.getAccionSolicitada()).thenReturn(AccionSolicitada.INGRESO);
        when(paseDeTarjeta3.getAccionSolicitada()).thenReturn(AccionSolicitada.INGRESO);
        when(paseDeTarjeta4.getAccionSolicitada()).thenReturn(AccionSolicitada.RETIRO);
        when(paseDeTarjeta5.getAccionSolicitada()).thenReturn(AccionSolicitada.RETIRO);

        when(paseDeTarjeta1.getHeladera()).thenReturn(heladera1);
        when(paseDeTarjeta2.getHeladera()).thenReturn(heladera2);
        when(paseDeTarjeta3.getHeladera()).thenReturn(heladera3);
        when(paseDeTarjeta4.getHeladera()).thenReturn(heladera4);
        when(paseDeTarjeta5.getHeladera()).thenReturn(heladera5);

        when(paseDeTarjeta1.cantidadDeViandas()).thenReturn(1);
        when(paseDeTarjeta2.cantidadDeViandas()).thenReturn(2);
        when(paseDeTarjeta3.cantidadDeViandas()).thenReturn(3);
        when(paseDeTarjeta4.cantidadDeViandas()).thenReturn(4);
        when(paseDeTarjeta5.cantidadDeViandas()).thenReturn(5);

        List<PaseDeTarjeta> pasesDeTarjeta = new ArrayList<>();
        pasesDeTarjeta.add(paseDeTarjeta1);
        pasesDeTarjeta.add(paseDeTarjeta2);
        pasesDeTarjeta.add(paseDeTarjeta3);
        pasesDeTarjeta.add(paseDeTarjeta4);
        pasesDeTarjeta.add(paseDeTarjeta5);

        Repositorio repositorio = mock(Repositorio.class);
        when(repositorio.buscarTodos("PaseDeTarjeta")).thenReturn((List) pasesDeTarjeta);

        GenerarPDFViandas generarPDFViandas =
                GenerarPDFViandas.crearGenerarPDFViandas(repositorio);
        generarPDFViandas.reportar();
    }
}