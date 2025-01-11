package ar.utn.frba.dds.modelos.entidades.contacto;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="contacto")
public class Contacto extends Persistente {
    @Column(name="nombre")
    private String nombre;
    @Column(name="correo_electronico")
    private String correoElectronico;
    @Column(name="mensaje", columnDefinition = "TEXT")
    private String mensaje;
}
