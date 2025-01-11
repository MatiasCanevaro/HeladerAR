package ar.utn.frba.dds.modelos.entidades.incidentes;

import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.personas.Tecnico;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BuscadorTecnicoMasCercanoTest {
    @Test
    public void calcularDistanciaHastaTest(){
        PuntoEstrategico parqueCentenario =
                PuntoEstrategico.crearPuntoEstrategico(
                        "parque centenario","-34.606490302531355","-58.43554948685646");

        PuntoEstrategico parqueRivadavia = PuntoEstrategico.crearPuntoEstrategico(
                "parque rivadavia","-34.61786226824605","-58.43332795086638");

       /* System.out.println("distancia entre Parque Centenario y Parque Rivadavia: " +
                parqueCentenario.calcularDistanciaEnMetrosHasta(parqueRivadavia));*/

        PuntoEstrategico marDelPlata = PuntoEstrategico.crearPuntoEstrategico(
                "marDelPlata","-38.00904714515322","-57.556532883743756");
      /*  System.out.println("distancia entre Mar Del Plata y Buenos Aires: " +
                marDelPlata.calcularDistanciaEnMetrosHasta(parqueRivadavia));*/
    }

    @Test
    public void tecnicoMasCercanoTest(){
        Repositorio repositorio = mock(Repositorio.class);
        Tecnico tecnico1 = mock(Tecnico.class);
        Tecnico tecnico2 = mock(Tecnico.class);
        Tecnico tecnico3 = mock(Tecnico.class);

        PuntoEstrategico buenosAires =
                PuntoEstrategico.crearPuntoEstrategico(
                        "buenosAires","-34.606490302531355","-58.43554948685646");

        PuntoEstrategico marDelPlata = PuntoEstrategico.crearPuntoEstrategico(
                "marDelPlata","-38.00904714515322","-57.556532883743756");


        PuntoEstrategico chubut = PuntoEstrategico.crearPuntoEstrategico(
                "chubut","-44.215444","-69.898525");
/*
        when(tecnico1.getCentroDeSuCiudad()).thenReturn(buenosAires);
        when(tecnico2.getCentroDeSuCiudad()).thenReturn(marDelPlata);
        when(tecnico3.getCentroDeSuCiudad()).thenReturn(chubut);

        List<Object> tecnicos = new ArrayList<>();
        tecnicos.add(tecnico1);
        tecnicos.add(tecnico2);
        tecnicos.add(tecnico3);
        when(repositorio.buscarTodos("Tecnico")).thenReturn(tecnicos);

        BuscadoraTecnicoMasCercano buscadorTecnicoMasCercano =
                new BuscadoraTecnicoMasCercano(repositorio,2000);

        PuntoEstrategico buenosAires2 = PuntoEstrategico.crearPuntoEstrategico(
                "buenosAires2","-34.61786226824605","-58.43332795086638");

        Tecnico tecnicoResultado1 = buscadorTecnicoMasCercano.buscarTecnicoMasCercano(buenosAires2);
        assertEquals(tecnico1,tecnicoResultado1);

        CronNotificador cronNotificador = mock(CronNotificador.class);
        MensajeTecnicoAsignadoAIncidente mensajeTecnicoAsignadoAIncidente = mock(MensajeTecnicoAsignadoAIncidente.class);
        Asignador asignador = Asignador.crearAsignador(
                cronNotificador,
                buscadorTecnicoMasCercano,
                mensajeTecnicoAsignadoAIncidente);

        ReportadorFallaTecnica reportadorFallaTecnica = new ReportadorFallaTecnica(asignador);
        Heladera heladera = mock(Heladera.class);
        when(heladera.getPuntoEstrategico()).thenReturn(buenosAires);
        FallaTecnicaInfo fallaTecnicaInfo = mock(FallaTecnicaInfo.class);
        when(fallaTecnicaInfo.getHeladera()).thenReturn(heladera);
        reportadorFallaTecnica.reportar(fallaTecnicaInfo);
        verify(cronNotificador).notificarDiferido(any(),any());*/
    }
}
