package ar.utn.frba.dds.modelos.entidades.geografia;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="ciudad")
public class Ciudad extends Persistente {
    @Column(name = "nombre")
    private String nombre;
    @ManyToOne
    @JoinColumn(name="centro_de_la_ciudad_id", referencedColumnName = "id")
    private CentroDeLaCiudad centroDeLaCiudad;
    @ManyToOne
    @JoinColumn(name="provincia_id", referencedColumnName = "id")
    private Provincia provincia;
}
