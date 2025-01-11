package ar.utn.frba.dds.modelos.modulos.notificador;

import ar.utn.frba.dds.modulos.notificador.Mensaje;
import ar.utn.frba.dds.modulos.notificador.Notificador;

import java.io.IOException;

public class NotificadorConEnvioDeMailTest {
    public static void main(String[] args) throws IOException {
        Notificador notificador = Notificador.crearNotificadorApache();
        Mensaje mensaje = Mensaje.crearMensaje(
                "Este es un Asunto",
                "Este es un Mensaje"
        );
        String receptor = "";
        notificador.notificar(mensaje,receptor);
    }
}
