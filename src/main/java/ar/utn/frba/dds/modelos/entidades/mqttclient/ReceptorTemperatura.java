package ar.utn.frba.dds.modelos.entidades.mqttclient;

import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.heladeras.ModeloDeHeladera;
import ar.utn.frba.dds.modelos.entidades.heladeras.SensorDeTemperatura;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import lombok.Builder;

@Builder
public class ReceptorTemperatura {
    private Repositorio repositorio;
    public void recibir(String[] mensaje) throws ClassNotFoundException {
        String id = (mensaje[0].trim());
        Double valorTemperatura = Double.parseDouble(mensaje[1].trim());

        Object sensorDeTemperatura = repositorio.buscarPorId(id,"SensorDeTemperatura");
        if (sensorDeTemperatura == null) {
            throw new IllegalArgumentException("Sensor de temperatura no encontrado para ID: " + id);
        }
        ((SensorDeTemperatura) sensorDeTemperatura).recibirTemperatura(valorTemperatura);
    }
    public static ReceptorTemperatura crearReceptorTemperatura(){
        Repositorio repositorio = new Repositorio();
        return ReceptorTemperatura
                .builder()
                .repositorio(repositorio)
                .build();
    }
}
