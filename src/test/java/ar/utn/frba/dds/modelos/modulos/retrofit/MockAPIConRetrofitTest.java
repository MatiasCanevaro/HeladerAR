package ar.utn.frba.dds.modelos.modulos.retrofit;

import ar.utn.frba.dds.modulos.retrofit.ServicioMockAPI;
import ar.utn.frba.dds.modulos.retrofit.entities.ListadoDePoints;
import ar.utn.frba.dds.modulos.retrofit.entities.Point;

import java.io.IOException;

public class MockAPIConRetrofitTest {
    public static void main(String[] args) throws IOException {
        ServicioMockAPI servicioMockAPI = ServicioMockAPI.instancia();

        ListadoDePoints listadoDePoints = servicioMockAPI.listadoDePoints();
        for(Point point : listadoDePoints.points){
            System.out.println("latitude: " + point.latitude +
                    "\nlongitude: " + point.longitude +
                    "\n");
        }
    }
}
