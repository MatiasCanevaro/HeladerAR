package ar.utn.frba.dds.modelos.entidades.personas;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name="medio_de_contacto")
public class MedioDeContacto extends Persistente {
    @Enumerated(EnumType.STRING)
    @Column(name="tipo_contacto")
    private TipoContacto tipoContacto;
    @Column(name = "dato")
    private String dato;

    public MedioDeContacto(TipoContacto tipoContacto, String contacto) {
        this.tipoContacto = tipoContacto;
        this.dato = contacto;
    }
}
