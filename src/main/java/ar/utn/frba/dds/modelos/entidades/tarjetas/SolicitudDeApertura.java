package ar.utn.frba.dds.modelos.entidades.tarjetas;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.heladeras.Vianda;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaVulnerable;
import lombok.*;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="solicitud_de_apertura")
public class SolicitudDeApertura extends Persistente {
    @ManyToOne
    @JoinColumn(name="persona_fisica_id", referencedColumnName = "id")
    private PersonaFisica personaFisica;
    @ManyToMany
    @JoinTable(
            name = "solicitud_de_apertura_vianda",
            joinColumns = @JoinColumn(name = "solicitud_de_apertura_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "vianda_id", referencedColumnName = "id")
    )
    private List<Vianda> viandas;
    @ManyToOne
    @JoinColumn(name="heladera_id", referencedColumnName = "id")
    private Heladera heladera;
    @Column(name="fecha_y_hora_solicitud", columnDefinition = "DATETIME")
    private LocalDateTime fechaYHoraSolicitud;
    @Column(name="fecha_y_hora_vencimiento", columnDefinition = "DATETIME")
    private LocalDateTime fechaYHoraVencimiento;
    @Enumerated(EnumType.STRING)
    @Column(name="accion_solicitada")
    private AccionSolicitada accionSolicitada;
    @Column(name="fue_usada", columnDefinition = "TINYINT") //<3
    private Boolean fueUsada;
    @Column(name="horas_vencimiento", columnDefinition = "INTEGER")
    @Builder.Default
    private Long horasVencimiento = 3L;

    public SolicitudDeApertura(PersonaFisica personaFisica, Heladera heladera,
                               LocalDateTime fechaYHoraSolicitud,
                               AccionSolicitada accionSolicitada, Boolean fueUsada) {
        this.personaFisica = personaFisica;
        this.viandas = new ArrayList<>();
        this.heladera = heladera;
        this.fechaYHoraSolicitud = fechaYHoraSolicitud;
        this.fechaYHoraVencimiento = LocalDateTime.now().plusHours(3);
        this.accionSolicitada = accionSolicitada;
        this.fueUsada = fueUsada;
    }

    public void agregarVianda(Vianda vianda){
        this.viandas.add(vianda);
    }
    public void agregarViandas(List<Vianda> viandas){
        this.viandas.addAll(viandas);
    }

}
