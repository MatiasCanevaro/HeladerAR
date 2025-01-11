package ar.utn.frba.dds.modelos.entidades.mqttclient;

import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.heladeras.ModeloDeHeladera;
import ar.utn.frba.dds.modelos.entidades.heladeras.SensorDeMovimiento;

import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import lombok.Builder;

@Builder
public class ReceptorMovimiento {
    private Repositorio repositorio;
    public void recibir(String[] mensaje) throws ClassNotFoundException {
        String id = mensaje[0].trim();
        System.out.println("Mensaje recibido de movimiento");
        Object sensorDeMovimiento = repositorio.buscarPorId(id,"SensorDeMovimiento");
        if (sensorDeMovimiento == null) {
            throw new IllegalArgumentException("Sensor de temperatura no encontrado para ID: " + id);
        }

        ((SensorDeMovimiento) sensorDeMovimiento).recibirDeteccionDeMovimiento();

    }
    public static ReceptorMovimiento crearReceptorMovimiento(){
        Repositorio repositorio = new Repositorio();
        return ReceptorMovimiento
                .builder()
                .repositorio(repositorio)
                .build();
    }
}
