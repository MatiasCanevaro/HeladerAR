package ar.utn.frba.dds.modelos.entidades.cuestionarios;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name="respuesta")
public class Respuesta extends Persistente {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="cuestionario_respondido_id", referencedColumnName = "id")
    private CuestionarioRespondido cuestionarioRespondido;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="pregunta_id", referencedColumnName = "id")
    private Pregunta pregunta;
    @Column(name="contenido", columnDefinition="TEXT")
    private String contenido;
    @ManyToMany
    @JoinTable(
            name = "respuesta_opcion",
            joinColumns = @JoinColumn(name = "respuesta_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "opcion_id", referencedColumnName = "id")
    )
    private List<Opcion> opcionesSeleccionadas;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="persona_fisica_id", referencedColumnName = "id")
    private PersonaFisica personaFisica;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="persona_juridica_id", referencedColumnName = "id")
    private PersonaJuridica personaJuridica;

    public void agregarOpcion(Opcion opcion){
        this.opcionesSeleccionadas.add(opcion);
    }

    public Respuesta() {
        this.opcionesSeleccionadas = new ArrayList<>();
    }
}
