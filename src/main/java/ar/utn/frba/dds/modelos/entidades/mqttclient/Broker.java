package ar.utn.frba.dds.modelos.entidades.mqttclient;

import ar.utn.frba.dds.modelos.entidades.tarjetas.SolicitudDeApertura;
import ar.utn.frba.dds.modelos.repositorios.RepositorioTarjeta;
import lombok.*;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static java.lang.Integer.parseInt;

@Getter
@Setter
@AllArgsConstructor

@Builder
public class Broker {
    private String host;
    private String clientId;

    public Broker() {
        this.leerArchivoProperties();
    }

    public MqttClient connect() throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        MqttClient sampleClient = new MqttClient(host, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);

        System.out.println("Conectando a broker: " + host);
        sampleClient.connect(connOpts);
        System.out.println("Conectado");
        return sampleClient;
    }

    public void subscribe(MqttClient sampleClient, Receptor receptor, String... topics) throws MqttException {
        for (String topic : topics) {
            sampleClient.subscribe(topic, receptor);
            System.out.println("Suscritos a " + topic);
        }
    }

    public void leerArchivoProperties(){
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("./src/main/resources/archivoDeConfiguracion.properties")) {
            properties.load(fis);
            this.host = properties.getProperty("broker_host");
            this.clientId = properties.getProperty("broker_clientId");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}
