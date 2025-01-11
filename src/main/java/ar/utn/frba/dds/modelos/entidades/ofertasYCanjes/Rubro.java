package ar.utn.frba.dds.modelos.entidades.ofertasYCanjes;

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
@Table(name="rubro")
public class Rubro extends Persistente {
    @Column(name = "nombre")
    private String nombre;
}
