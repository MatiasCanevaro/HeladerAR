package ar.utn.frba.dds.modelos.entidades.formasDeColaboracion;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaVulnerable;
import ar.utn.frba.dds.modelos.entidades.tarjetas.Tarjeta;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor

@Entity
@Table(name="distribucion_de_tarjeta")
public class DistribucionDeTarjeta extends Persistente {
    @Enumerated(EnumType.STRING)
    @Column(name = "nombre")
    private FormaDeColaboracion nombre;
    @OneToOne
    @JoinColumn(name="tarjeta_id", referencedColumnName = "id")
    private Tarjeta tarjeta;
    @ManyToOne
    @JoinColumn(name="persona_fisica_id", referencedColumnName = "id")
    private PersonaFisica personaFisicaQueLaRegistro;
    @ManyToOne
    @JoinColumn(name="persona_vulnerable_id", referencedColumnName = "id")
    private PersonaVulnerable personaVulnerable;
    @Column(name = "fecha_entrega_tarjeta", columnDefinition="DATE")
    private LocalDate fechaEntregaTarjeta;

    public static DistribucionDeTarjeta crearDistribucionDeTarjeta(
            FormaDeColaboracion nombre, PersonaFisica personaFisica, LocalDate fechaEntregaTarjeta){
        return DistribucionDeTarjeta
                .builder()
                .nombre(nombre)
                .personaFisicaQueLaRegistro(personaFisica)
                .fechaEntregaTarjeta(fechaEntregaTarjeta)
                .build();
    }
}
