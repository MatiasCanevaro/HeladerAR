package ar.utn.frba.dds.modelos.entidades.personas;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Embeddable
public class Email {
    @Column(name="correo_electronico")
    private String correoElectronico;
    public static Email crearEmail(String correoElectronico){
        return Email
                .builder()
                .correoElectronico(correoElectronico)
                .build();
    }
}
