package ar.utn.frba.dds.modelos.entidades.geografia;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="direccion")
public class Direccion extends Persistente {
    @Column(name = "calle")
    private String calle;
    @Column(name = "altura")
    private String altura;
    @Column(name = "piso")
    private String piso;
    @Column(name = "codigo_postal")
    private String codigoPostal;
    @ManyToOne
    @JoinColumn(name="ciudad_id", referencedColumnName = "id")
    private Ciudad ciudad;
    @ManyToOne
    @JoinColumn(name="provincia_id", referencedColumnName = "id")
    private Provincia provincia;


}
