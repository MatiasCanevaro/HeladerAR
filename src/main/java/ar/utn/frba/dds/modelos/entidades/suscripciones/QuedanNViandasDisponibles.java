package ar.utn.frba.dds.modelos.entidades.suscripciones;

import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modulos.notificador.Mensaje;
import ar.utn.frba.dds.modulos.notificador.Notificador;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuedanNViandasDisponibles implements OpcionSuscripcion {
    private MensajeQuedanNViandasDisponibles mensajeQuedanNViandasDisponibles;
    private Notificador notificador;
     public void evaluarEnvioDeMensaje(Suscripcion suscripcion) {
         Heladera heladera = suscripcion.getHeladera();
         if (Objects.equals(heladera.obtenerCantidadDeViandasActuales(), suscripcion.getCantidadViandasDisponibles())) {
             PersonaFisica personaFisica = suscripcion.getPersonaFisica();
             String email = personaFisica.getEmail().getCorreoElectronico();
             Mensaje mensaje = mensajeQuedanNViandasDisponibles.generarMensaje(suscripcion);
             notificador.notificar(mensaje, email);
         }
     }
    public static QuedanNViandasDisponibles crearQuedanNViandasDisponibles(){
        MensajeQuedanNViandasDisponibles mensajeQuedanNViandasDisponibles = new MensajeQuedanNViandasDisponibles();
        Notificador notificador = Notificador.crearNotificadorApache();
        return QuedanNViandasDisponibles
                .builder()
                .mensajeQuedanNViandasDisponibles(mensajeQuedanNViandasDisponibles)
                .notificador(notificador)
                .build();
    }
}
