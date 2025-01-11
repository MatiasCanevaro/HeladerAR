package ar.utn.frba.dds.modelos.entidades.cuestionarios;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.*;

import javax.persistence.*;
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="opcion")
public class Opcion extends Persistente {
    @Column(name="nombre")
    private String nombre;
}
