package ar.utn.frba.dds.modulos.notificador;

import ar.utn.frba.dds.modelos.entidades.contacto.Usuario;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MensajeCredencialesDeUsuario {
    public static Mensaje generarMensaje(Usuario usuario){
        Mensaje mensaje = new Mensaje("Tus Credenciales en el Sistema de Acceso Alimentario",
                "Tus credenciales son:\n\n" +
                        "Usuario: " + usuario.getCorreoElectronico() + "\n" +
                        "Contraseña: " + usuario.getContrasenia() + "\n\n" +
                        "Gracias,\n" +
                        "Equipo de asistencia del Sistema de Acceso Alimentario.", LocalDateTime.now());
        return mensaje;
    }
}