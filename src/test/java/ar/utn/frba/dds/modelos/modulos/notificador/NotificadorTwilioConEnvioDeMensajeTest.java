package ar.utn.frba.dds.modelos.modulos.notificador;

import ar.utn.frba.dds.modulos.notificador.Mensaje;
import ar.utn.frba.dds.modulos.notificador.Notificador;

import java.io.IOException;

public class NotificadorTwilioConEnvioDeMensajeTest {
    public static void main(String[] args) throws IOException {
        Notificador notificador = Notificador.crearNotificadorTwilio();
        Mensaje mensaje = Mensaje.crearMensaje(
                null,
                "Funciono el enviar whatsapp"
        );
        String receptor = "whatsapp:+5492323340620";
        notificador.notificar(mensaje,receptor);
    }
}
