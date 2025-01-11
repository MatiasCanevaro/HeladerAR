package ar.utn.frba.dds.modelos.entidades.suscripciones;

import ar.utn.frba.dds.modulos.converters.OpcionAttributeConverter;
import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name="suscripcion")
public class Suscripcion extends Persistente {
    @ManyToOne
    @JoinColumn(name="persona_fisica_id", referencedColumnName = "id")
    private PersonaFisica personaFisica;
    @ManyToOne
    @JoinColumn(name="heladera_id", referencedColumnName = "id")
    private Heladera heladera;
    @Column(name="fecha_y_hora_inicio", columnDefinition="DATETIME")
    private LocalDateTime fechaYHoraInicio;
    @Column(name="fecha_y_hora_fin", columnDefinition="DATETIME")
    private LocalDateTime fechaYHoraFin;
    @Column(name = "cantidad_viandas_disponibles", columnDefinition = "INTEGER")
    private Integer cantidadViandasDisponibles; // opcion 1 n
    @Column(name = "cantidad_viandas_a_traer", columnDefinition = "INTEGER")
    private Integer cantidadDeViandasATraer; // opcion 1 N
    @Column(name = "cantidad_viandas_faltantes", columnDefinition = "INTEGER")
    private Integer cantidadViandasFaltantes; // opcion 2 n
    @Column(name = "cantidad_viandas_a_llevar", columnDefinition = "INTEGER")
    private Integer cantidadDeViandasALlevar; // opcion 2 N
    @Convert(converter = OpcionAttributeConverter.class)
    @Column(name = "opcion")
    @Builder.Default
    private List<OpcionSuscripcion> opciones = new ArrayList<>();

    public void ejecutarOpciones(){
        this.opciones.forEach(opcion -> opcion.evaluarEnvioDeMensaje(this));
    }

    public void agregarOpciones(List<OpcionSuscripcion> opciones){
        this.opciones.addAll(opciones);
    }
}
