package ar.utn.frba.dds.modelos.entidades.formasDeColaboracion;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

import static java.lang.Integer.parseInt;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor

@Entity
@Table(name="distribucion_de_vianda")
public class DistribucionDeVianda extends Persistente {
    @Enumerated(EnumType.STRING)
    @Column(name = "nombre")
    private FormaDeColaboracion nombre;
    @ManyToOne
    @JoinColumn(name = "heladera_origen_id", referencedColumnName = "id")
    private Heladera heladeraOrigen;
    @ManyToOne
    @JoinColumn(name = "heladera_destino_id", referencedColumnName = "id")
    private Heladera heladeraDestino;
    @Column(name = "cant_viandas_a_mover")
    private Integer cantViandasAMover;
    @ManyToOne
    @JoinColumn(name = "motivo_distribucion_id", referencedColumnName = "id")
    private MotivoDistribucion motivoDistribucion;
    @Column(name = "fecha_distribucion")
    private LocalDate fechaDistribucion;
    @ManyToOne
    @JoinColumn(name = "persona_fisica_id", referencedColumnName = "id")
    private PersonaFisica personaFisica;

    public static DistribucionDeVianda crearDistribucionDeVianda(
            FormaDeColaboracion nombre, PersonaFisica personaFisica,
            LocalDate fechaDistribucion, Integer cantViandasAMover){
        return DistribucionDeVianda
                .builder()
                .nombre(nombre)
                .personaFisica(personaFisica)
                .fechaDistribucion(fechaDistribucion)
                .cantViandasAMover(cantViandasAMover)
                .build();
    }
    public static DistribucionDeVianda crearDistribucionDeVianda2(
            MotivoDistribucion motivoDistribucion, PersonaFisica personaFisica){
        return DistribucionDeVianda
                .builder()
                .nombre(FormaDeColaboracion.DISTRIBUCION_DE_VIANDA)
                .personaFisica(personaFisica)
                .fechaDistribucion(LocalDate.now())
                .build();
    }
}
