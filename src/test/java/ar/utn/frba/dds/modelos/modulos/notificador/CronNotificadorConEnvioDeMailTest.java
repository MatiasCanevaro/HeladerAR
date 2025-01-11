package ar.utn.frba.dds.modelos.modulos.notificador;

import ar.utn.frba.dds.modulos.notificador.CronNotificador;
import ar.utn.frba.dds.modulos.notificador.Mensaje;

import java.io.IOException;

public class CronNotificadorConEnvioDeMailTest {
    public static void main(String[] args) throws IOException {
        CronNotificador cronNotificador = CronNotificador.crearCronNotificador();
        Mensaje mensaje = Mensaje.crearMensaje(
                "Este es un Asunto",
                "Este es un Mensaje"
        );
        String receptor = "";
        cronNotificador.notificarDiferido(mensaje,receptor);
    }
}
