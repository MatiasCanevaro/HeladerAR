package ar.utn.frba.dds.modulos.notificador;

import ar.utn.frba.dds.modelos.entidades.geografia.Direccion;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MensajeTecnicoSugeridoAIncidente {
    public Mensaje generarMensaje(Heladera heladera){
        Direccion direccion = heladera.getPuntoEstrategico().getDireccion();
        return new Mensaje("Nuevo trabajo sugerido.",
                "Algunos de los detalles: \n\n" +
                        "La direccion de la heladera es " + direccion.getCalle() + " " + direccion.getAltura() + "\n\n"+
                        "Gracias,\n" +
                        "Equipo de asistencia del Sistema de Acceso Alimentario.", LocalDateTime.now());
    }
}