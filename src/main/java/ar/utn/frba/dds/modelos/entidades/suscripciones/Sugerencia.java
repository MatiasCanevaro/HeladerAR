package ar.utn.frba.dds.modelos.entidades.suscripciones;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DistribucionDeVianda;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.FormaDeColaboracion;
import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ar.utn.frba.dds.modelos.entidades.suscripciones.EstadoSugerencia.PENDIENTE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="sugerencia")
public class Sugerencia extends Persistente {
    @ManyToOne
    @JoinColumn(name="heladera_id", referencedColumnName = "id")
    private Heladera heladeraOrigen;
    @ManyToOne
    @JoinColumn(name="punto_estrategico_id", referencedColumnName = "id")
    private PuntoEstrategico puntoEstrategico;
    @ManyToOne
    @JoinColumn(name="persona_fisica_id", referencedColumnName = "id")
    private PersonaFisica personaFisica;
    @Column(name="fecha_y_hora_canje_fue_generada", columnDefinition="DATETIME")
    private LocalDateTime fechaYHoraFueGenerada;
    @Column(name="fecha_y_hora_resultado", columnDefinition="DATETIME")
    private LocalDateTime fechaYHorafechaYHoraResultado;
    @OneToMany
    @JoinColumn(name="sugerencia_id", referencedColumnName = "id")
    private List<Distribucion> distribuciones;
    @Enumerated(EnumType.STRING)
    @Column(name="estado_sugerencia")
    private EstadoSugerencia estadoSugerencia;
    @Column(name="cantidad_de_viandas_restantes_por_distribuir", columnDefinition = "INTEGER")
    private Integer cantidadDeViandasRestantesPorDistribuir;

    public static Sugerencia crearSugerencia(Heladera heladera, PersonaFisica personaFisica, List<Heladera> heladeras){
        Sugerencia sugerencia = Sugerencia
                .builder()
                .heladeraOrigen(heladera)
                .puntoEstrategico(heladera.getPuntoEstrategico())
                .personaFisica(personaFisica)
                .fechaYHoraFueGenerada(LocalDateTime.now())
                .estadoSugerencia(PENDIENTE)
                .cantidadDeViandasRestantesPorDistribuir(heladera.obtenerCantidadDeViandasActuales())
                .build();
        sugerencia.setDistribucion(heladeras);
        return sugerencia;
    }

    public void setDistribucion(List<Heladera> heladeras){
        for(Heladera heladera : heladeras){
            Distribucion distribucion1 =
                    Distribucion.crearDistribucion(heladera,cantidadDeViandasRestantesPorDistribuir);
            this.distribuciones.add(distribucion1);
            this.cantidadDeViandasRestantesPorDistribuir -= heladera.cantidadDeEspacioDisponibleEnViandas();
        }
    }
}
