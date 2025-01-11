package ar.utn.frba.dds.modelos.entidades.geografia;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter

@Entity
@Table(name="area_de_cobertura")
public class AreaDeCobertura extends Persistente {
    @Column(name = "nombre")
    private String nombre;
    @OneToOne
    @JoinColumn(name="ciudad_id", referencedColumnName = "id")
    private Ciudad ciudad;
}
