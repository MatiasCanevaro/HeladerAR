package ar.utn.frba.dds.modelos.entidades.formasDeColaboracion;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="motivo_de_distribucion")
public class MotivoDistribucion extends Persistente {
    @Column(name = "nombre")
    private String nombre;
}
