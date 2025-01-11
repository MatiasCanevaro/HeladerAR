package ar.utn.frba.dds.modelos.entidades.mqttclient;

import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.heladeras.SensorDeMovimiento;
import ar.utn.frba.dds.modelos.entidades.heladeras.SensorDeTemperatura;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import ar.utn.frba.dds.modelos.entidades.tarjetas.PaseDeTarjeta;
import ar.utn.frba.dds.modelos.entidades.tarjetas.SolicitudDeApertura;
import ar.utn.frba.dds.modelos.entidades.tarjetas.Tarjeta;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import ar.utn.frba.dds.modelos.repositorios.RepositorioTarjeta;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter

public class BrokerMain {
    private static RepositorioTarjeta repositorioTarjeta = new RepositorioTarjeta();
    private static Repositorio repositorio = new Repositorio();
    private static Broker broker = new Broker();

    public static void main(String[] args) {
        try {
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setAutomaticReconnect(true);
            connOpts.setCleanSession(true); // Asegura una sesión limpia
            connOpts.setConnectionTimeout(10); // Tiempo de espera para reconexión en segundos

            MqttClient sampleClient = broker.connect();

            System.out.println("Construir receptor");
            Receptor receptor = Receptor.crearReceptor();
            broker.subscribe(sampleClient, receptor,
                    "topic_temperatura",
                    "topic_movimiento",
                    "topic_autorizacion",
                    "topic_pase_de_tarjeta");

            // Crear un ScheduledExecutorService para ejecutar tareas periódicas
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            publicarTemperatura(sampleClient);
            // Programar la ejecución de publicarPaseDeTarjeta cada 10 segundos
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    publicarPaseDeTarjeta(sampleClient);
                } catch (MqttException | InterruptedException e) {
                    e.printStackTrace();
                }
            }, 0, 10, TimeUnit.SECONDS);

            // Mantener el programa corriendo
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Apagando el programador...");
                scheduler.shutdown();
                try {
                    if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                        scheduler.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    scheduler.shutdownNow();
                }
            }));

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void publicarPaseDeTarjeta(MqttClient sampleClient) throws MqttException, InterruptedException {
        List<SolicitudDeApertura> solicitudes = repositorio.buscarTodos("SolicitudDeApertura")
                .stream()
                .map(obj -> (SolicitudDeApertura) obj)
                .filter(solicitudDeApertura -> !solicitudDeApertura.getFueUsada())
                .toList();

        System.out.println("Buscando una solicitud de apertura");

        solicitudes.forEach(solicitudDeApertura -> {
            String heladeraId = obtenerIdHeladera(solicitudDeApertura);
            String codigoAlfanumerico = obtenerCodigoAlfanumerico(solicitudDeApertura);
            LocalDateTime fechaYHoraActual = LocalDateTime.now();
            System.out.println("Se encontro una solicitud de apertura");
            String mensaje = "topic_pase_de_tarjeta" + ";" + heladeraId + ";" + codigoAlfanumerico + ";" + fechaYHoraActual + ";" + solicitudDeApertura.getId();

            try {
                publicarMensaje(sampleClient, mensaje);
                solicitudDeApertura.setFueUsada(true);
                repositorio.actualizar(solicitudDeApertura);
            } catch (MqttException e) {
                reconnectClient(sampleClient); // Intentar reconectar
            }
        });
    }

    private static String obtenerIdHeladera(SolicitudDeApertura solicitudDeApertura) {
        return repositorio.buscarTodos("Heladera")
                .stream()
                .map(obj -> (Heladera) obj)
                .filter(heladera -> solicitudDeApertura.getHeladera().getId().equals(heladera.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró una heladera con el ID especificado"))
                .getId();
    }
    private static String obtenerCodigoAlfanumerico(SolicitudDeApertura solicitud) {
        String codigo = solicitud.getPersonaFisica().getTarjetaActiva().getCodigoAlfanumerico();
        boolean codigoValido = repositorio.buscarTodos("Tarjeta").stream()
                .map(obj -> (Tarjeta) obj)
                .anyMatch(tarjeta -> tarjeta.getCodigoAlfanumerico().equals(codigo));
        if (!codigoValido) {
            throw new RuntimeException("No se encontró una tarjeta activa con el código alfanumerico en solicitud");
        }
        return codigo;
    }

    public static void publicarMensaje(@NotNull MqttClient sampleClient,String mensajeCompleto) throws MqttException {
        if (!sampleClient.isConnected()) {
            reconnectClient(sampleClient); // Verificar y reconectar si es necesario
        }
        String[] partes = mensajeCompleto.split(";", 2);
        String topic = partes[0];
        String mensaje = partes.length > 1 ? partes[1] : "";
        MqttMessage mensajeAEnviar = new MqttMessage();
        mensajeAEnviar.setPayload((mensaje).getBytes());
        sampleClient.publish(topic, mensajeAEnviar);
        System.out.println("Mensaje publicado en " + topic + ": " + mensaje);
    }


    public void notificar(MqttClient sampleClient, SolicitudDeApertura solicitudDeApertura) {
        MqttMessage mensajeAEnviar = MapperBroker.mqttMessageMapper(solicitudDeApertura, repositorioTarjeta);
        MqttMessage mqttMessage = new MqttMessage(mensajeAEnviar.getPayload());
        try {
            sampleClient.publish("topic_autorizacion", mqttMessage);
            System.out.println("Publicado en topic_autorizacion");
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public static void publicarMovimientos(MqttClient sampleClient) throws MqttException, InterruptedException {
        //sensores de movimiento
        List<SensorDeMovimiento> sensoresDeMovimiento = repositorio.buscarTodos("SensorDeMovimiento")
                .stream()
                .map(obj -> (SensorDeMovimiento) obj)
                .toList();

        for (int i = 0; i < sensoresDeMovimiento.size(); i++) {
            SensorDeMovimiento sensorDeMovimiento = sensoresDeMovimiento.get(i);
            String mensaje = "topic_movimiento" + ";" + sensorDeMovimiento.getId();
            publicarMensaje(sampleClient, mensaje);
            Thread.sleep(1000);
        }
    }

    public static void publicarTemperatura(MqttClient sampleClient) throws MqttException, InterruptedException {
        System.out.println("ola me voy a romper");
        List<SensorDeTemperatura> sensoresDeTemperatura = repositorio.buscarTodos("SensorDeTemperatura")
                .stream()
                .map(obj -> (SensorDeTemperatura) obj)
                .toList();
        double temperatura =  -45.0;
        SensorDeTemperatura sensorDeTemperatura = sensoresDeTemperatura.get(0);
        System.out.println("Sensor:" + sensorDeTemperatura.getId());
        String mensaje = "topic_temperatura" + ";" + sensorDeTemperatura.getId() + ";" + temperatura;
        publicarMensaje(sampleClient, mensaje);
    }
    public static void publicarTemperaturas(MqttClient sampleClient) throws MqttException, InterruptedException {
        //sensores de temperatura
        List<SensorDeTemperatura> sensoresDeTemperatura = repositorio.buscarTodos("SensorDeTemperatura")
                .stream()
                .map(obj -> (SensorDeTemperatura) obj)
                .toList();
        List<Double> temperaturas = List.of(-1.1, -2.0, -1.2, 1.0, -45.0);
        for (int i = 0; i < sensoresDeTemperatura.size(); i++) {
            SensorDeTemperatura sensorDeTemperatura = sensoresDeTemperatura.get(i);
            Double temperatura = temperaturas.get(i % temperaturas.size());
            String mensaje = "topic_temperatura" + ";" + sensorDeTemperatura.getId() + ";" + temperatura;
            publicarMensaje(sampleClient, mensaje);
            Thread.sleep(1000);
        }
    }
    private static void reconnectClient( MqttClient sampleClient) {
        try {
            if (!sampleClient.isConnected()) {
                sampleClient.reconnect();
                System.out.println("Reconectado al broker MQTT.");
            }
        } catch (MqttException e) {
            System.err.println("Fallo al intentar reconectar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
