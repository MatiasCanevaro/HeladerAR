package ar.utn.frba.dds.modelos.entidades.formasDeColaboracion;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;

import javax.persistence.*;

@Entity
@Table(name="ofrecer_productos_y_servicios")
public class OfrecerProductosYServicios extends Persistente {
    @Enumerated(EnumType.STRING)
    @Column(name = "nombre")
    private FormaDeColaboracion nombre;
    @ManyToOne
    @JoinColumn(name="persona_juridica_id", referencedColumnName = "id")
    private PersonaJuridica personaJuridica;
}
