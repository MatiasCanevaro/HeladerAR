package ar.utn.frba.dds.modelos.entidades.cuestionarios;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name="pregunta")
public class Pregunta extends Persistente {
    @Column(name="descripcion")
    private String descripcion;
    @Enumerated(EnumType.STRING)
    @Column(name="tipo_pregunta")
    private TipoPregunta tipoPregunta;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="pregunta_id", referencedColumnName = "id")
    private List<Opcion> opciones;
    public void agregarOpcion(Opcion opcion){
        this.opciones.add(opcion);
    }

    public Pregunta(String descripcion, TipoPregunta tipoPregunta, List<Opcion> opciones) {
        this.descripcion = descripcion;
        this.tipoPregunta = tipoPregunta;
        this.opciones = new ArrayList<>();
    }
}
