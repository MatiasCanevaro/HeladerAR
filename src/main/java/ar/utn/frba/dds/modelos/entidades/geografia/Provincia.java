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
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="provincia")
public class Provincia extends Persistente {
    @Column(name = "nombre")
    private String nombre;
}
