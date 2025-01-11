package ar.utn.frba.dds.modelos.entidades.tarjetas;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.heladeras.Vianda;
import lombok.*;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="pase_de_tarjeta")
public class PaseDeTarjeta extends Persistente {
    @ManyToOne
    @JoinColumn(name="tarjeta_id", referencedColumnName = "id")
    private Tarjeta tarjeta; // Heladera x Broker
    @ManyToMany
    @JoinTable(
            name = "pase_de_tarjeta_vianda",
            joinColumns = @JoinColumn(name = "pase_de_tarjeta_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "vianda_id", referencedColumnName = "id")
    )
    @GenericGenerator(name = "sequence-gen", strategy = "sequence")
    @CollectionId(columns = @Column(name="id"), generator = "sequence-gen", type = @Type(type="long"))
    private List<Vianda> viandas; // Solicitud
    @ManyToOne
    @JoinColumn(name="heladera_id", referencedColumnName = "id")
    private Heladera heladera; // Heladera x Broker
    @Enumerated(EnumType.STRING)
    @Column(name="accion_solicitada")
    private AccionSolicitada accionSolicitada; // Solicitud
    @Column(name="fecha_y_hora_pase_de_tarjeta", columnDefinition = "DATETIME")
    private LocalDateTime fechaYHoraPaseDeTarjeta; // Heladera x Broker
    @ManyToOne
    @JoinColumn(name="solicitud_apertura_id", referencedColumnName = "id")
    private SolicitudDeApertura solicitudDeApertura; // Heladera - Broker
    @Enumerated(EnumType.STRING)
    @Column(name="resultado_pase_de_tarjeta")
    private ResultadoPaseDeTarjeta resultadoPaseDeTarjeta; // Heladera x Broker

    public Integer cantidadDeViandas(){
        return this.viandas.size();
    }

    public static PaseDeTarjeta crearPaseDeTarjeta(
            Heladera heladera,Tarjeta tarjeta,List<Vianda> viandas,AccionSolicitada accionSolicitada,
            LocalDateTime fechaYHoraPaseDeTarjeta,SolicitudDeApertura solicitudDeApertura,
            ResultadoPaseDeTarjeta resultadoPaseDeTarjeta){
        return PaseDeTarjeta
                .builder()
                .heladera(heladera)
                .tarjeta(tarjeta)
                .viandas(viandas)
                .accionSolicitada(accionSolicitada)
                .fechaYHoraPaseDeTarjeta(fechaYHoraPaseDeTarjeta)
                .solicitudDeApertura(solicitudDeApertura)
                .resultadoPaseDeTarjeta(resultadoPaseDeTarjeta)
                .build();
    }
}
