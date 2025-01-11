package ar.utn.frba.dds.modelos.entidades.cuestionarios;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor

@Entity
@Table(name="cuestionario")
public class Cuestionario extends Persistente {
    @Column(name="nombre")
    private String nombre;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="cuestionario_id", referencedColumnName = "id")
    private List<Pregunta> preguntas;
    @Enumerated(EnumType.STRING)
    @ElementCollection()
    @CollectionTable(name="cuestionario_rol", joinColumns = @JoinColumn(
            name="cuestionario_id",
            referencedColumnName = "id"))
    @SequenceGenerator(name = "sequence-gen", sequenceName = "sequence_gen", allocationSize = 1)
    @CollectionId(columns = @Column(name="id"), generator = "sequence-gen", type = @Type(type="long"))
    @Column(name="roles_que_acepta")
    private List<TipoRol> rolesQueAcepta;
    public void agregarPreguntas(List<Pregunta> preguntas){
        this.preguntas.addAll(preguntas);
    }
    public void agregarPregunta(Pregunta pregunta){
        this.preguntas.add(pregunta);
    }
    public void agregarRoles(List<TipoRol> roles) {
        this.rolesQueAcepta.addAll(roles);
    }
    public void agregarRol(TipoRol rol) {
        this.rolesQueAcepta.add(rol);
    }

    public Cuestionario() {
        this.preguntas = new ArrayList<>();
        this.rolesQueAcepta = new ArrayList<>();
    }
}
