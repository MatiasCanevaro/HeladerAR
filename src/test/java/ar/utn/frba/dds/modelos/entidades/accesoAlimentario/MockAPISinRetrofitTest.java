package ar.utn.frba.dds.modelos.entidades.accesoAlimentario;

import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.puntosColocacion.AdapterAPI;
import ar.utn.frba.dds.modelos.entidades.puntosColocacion.SolicitudDePuntosDeColocacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
public class MockAPISinRetrofitTest {
    @Test
    public void adapterAPISolicitaCorrectamenteLosPuntosDeColocacion(){
        PuntoEstrategico puntoEstrategico1 = PuntoEstrategico.crearPuntoEstrategico(
                "medrano", "-34.598633", "-58.419943");
        PuntoEstrategico puntoEstrategico2 = PuntoEstrategico.crearPuntoEstrategico(
                "campus", "-34.659783", "-58.468073");
        List<PuntoEstrategico> puntosEstrategicos = new ArrayList<>();
        puntosEstrategicos.add(puntoEstrategico1);
        puntosEstrategicos.add(puntoEstrategico2);

        AdapterAPI adapterAPI = mock(AdapterAPI.class);
        SolicitudDePuntosDeColocacion solicitudDePuntosDeColocacion =
                SolicitudDePuntosDeColocacion.crearSolicitudDePuntosDeColocacion(adapterAPI);

        when(adapterAPI.solicitarPuntosDeColocacion(
                "-34.603124","-58.420941", 2.5)).thenReturn(puntosEstrategicos);

        List<PuntoEstrategico> resultadoPuntosEstrategicos = solicitudDePuntosDeColocacion.solicitarPuntosDeColocacion(
                "-34.603124","-58.420941",2.5);

        Assertions.assertEquals(puntosEstrategicos, resultadoPuntosEstrategicos);
        verify(adapterAPI).solicitarPuntosDeColocacion("-34.603124","-58.420941", 2.5);
    }
}
