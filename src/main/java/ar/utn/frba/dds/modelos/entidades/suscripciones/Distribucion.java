package ar.utn.frba.dds.modelos.entidades.suscripciones;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static ar.utn.frba.dds.modelos.entidades.suscripciones.EstadoSugerencia.PENDIENTE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="distribucion")
public class Distribucion extends Persistente {
    @ManyToOne
    @JoinColumn(name="heladera_id", referencedColumnName = "id")
    private Heladera heladeraDestino;
    @ManyToOne
    @JoinColumn(name="punto_estrategico_id", referencedColumnName = "id")
    private PuntoEstrategico puntoEstrategico;
    @Column(name = "cantidad_de_viandas", columnDefinition = "INTEGER")
    private Integer cantidadDeViandas;
    public static Distribucion crearDistribucion(Heladera heladera, Integer cantidadDeViandasADistribuir){
        return Distribucion
                .builder()
                .heladeraDestino(heladera)
                .puntoEstrategico(heladera.getPuntoEstrategico())
                .cantidadDeViandas(cantidadDeViandasADistribuir)
                .build();
    }
}
