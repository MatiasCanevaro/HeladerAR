package ar.utn.frba.dds.modelos.entidades.geografia;

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
@Table(name="centro_de_la_ciudad")
public class CentroDeLaCiudad extends Persistente {
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "latitud")
    private String latitud;
    @Column(name = "longitud")
    private String longitud;
}
