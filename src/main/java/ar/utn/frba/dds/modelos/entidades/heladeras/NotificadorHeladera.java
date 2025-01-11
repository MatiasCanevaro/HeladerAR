package ar.utn.frba.dds.modelos.entidades.heladeras;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.tarjetas.Tarjeta;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.tarjetas.SolicitudDeApertura;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="notificador_heladera")
public class NotificadorHeladera extends Persistente {
    @OneToOne
    @JoinColumn(name="heladera_id",referencedColumnName = "id")
    private Heladera heladera;
    @Column(name="minutos_para_vencer", columnDefinition="FLOAT")
    private Double minutosParaVencer;

    public void notificar(Heladera heladera, SolicitudDeApertura solicitudDeApertura){
        PersonaFisica personaFisica = solicitudDeApertura.getPersonaFisica();
        Tarjeta tarjeta = personaFisica.getTarjetaActiva();
        String codigoAlfanumerico = tarjeta.getCodigoAlfanumerico();
        String fechaYHoraVencimiento = this.obtenerFechaYHoraVencimiento(minutosParaVencer);
        //TODO
        //String jsonAEnviar = this.crearJson(codigoAlfanumerico,fechaYHoraVencimiento);
        //this.enviar(heladera,jsonAEnviar);
    }
    public String obtenerFechaYHoraVencimiento(Double minutosParaVencer) {
        LocalDateTime fechaYHoraActual = LocalDateTime.now();
        LocalDateTime fechaYHoraVencimiento = fechaYHoraActual.plusMinutes(minutosParaVencer.longValue());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return fechaYHoraVencimiento.format(formatter);
    }
}
