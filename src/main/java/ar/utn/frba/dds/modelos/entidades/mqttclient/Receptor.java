package ar.utn.frba.dds.modelos.entidades.mqttclient;

import lombok.Builder;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Builder
public class Receptor implements IMqttMessageListener {
    private ReceptorTemperatura receptorTemperatura;
    private ReceptorMovimiento receptorMovimiento;
    private ReceptorPaseDeTarjeta receptorPaseDeTarjeta;

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String mensaje  = new String(mqttMessage.getPayload());
        String[] mensajeSpliteado = mensaje.split(";");
        switch (topic) {
            case "topic_temperatura" -> {
                try {
                    receptorTemperatura.recibir(mensajeSpliteado);
                } catch (NumberFormatException e) {
                    System.out.println("Error al parsear la temperatura: " + mensaje);
                }
            }
            case "topic_movimiento" -> {
                try {
                    receptorMovimiento.recibir(mensajeSpliteado);
                } catch (NumberFormatException e) {
                    System.out.println("Error en topico movimiento: " + mensaje);
                }
            }
            case "topic_autorizacion" -> {
                try {
                    System.out.println("Mensaje recibido de autorizacion: " + mensaje);
                } catch (NumberFormatException e) {
                    System.out.println("Error en topico autorizacion: " + mensaje);
                }
            }
            case "topic_pase_de_tarjeta" -> {
                try {
                    System.out.println("Mensaje recibido de heladera fisica: " + mensaje);
                    receptorPaseDeTarjeta.recibir(mensajeSpliteado);
                } catch (NumberFormatException e) {
                    System.out.println("Error en topico pase de tarjeta: " + mensaje);
                }
            }
            default -> System.out.println("Mensaje recibido de topico no existente: " + topic + ": " + mensaje);
        }
    }

    public static Receptor crearReceptor(){
        ReceptorTemperatura receptorTemperatura = ReceptorTemperatura.crearReceptorTemperatura();
        ReceptorMovimiento receptorMovimiento = ReceptorMovimiento.crearReceptorMovimiento();
        ReceptorPaseDeTarjeta receptorPaseDeTarjeta = ReceptorPaseDeTarjeta.crearReceptorPaseDeTarjeta();
        return Receptor
                .builder()
                .receptorTemperatura(receptorTemperatura)
                .receptorMovimiento(receptorMovimiento)
                .receptorPaseDeTarjeta(receptorPaseDeTarjeta)
                .build();
    }
}