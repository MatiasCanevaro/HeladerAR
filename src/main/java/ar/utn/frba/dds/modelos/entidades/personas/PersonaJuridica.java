package ar.utn.frba.dds.modelos.entidades.personas;


import ar.utn.frba.dds.modelos.entidades.Persistente;
import java.time.LocalDateTime;
import ar.utn.frba.dds.modelos.entidades.cuestionarios.CuestionarioRespondido;

import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.FormaDeColaboracion;
import ar.utn.frba.dds.modelos.entidades.geografia.Direccion;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Oferta;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Rubro;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="persona_juridica")
public class PersonaJuridica extends Persistente {
    @Column(name = "razonSocial")
    private String razonSocial;
    @Enumerated(EnumType.STRING)
    @Column(name="tipo_organizacion")
    private TipoOrganizacion tipoOrganizacion;
    @Column(name="puntos_acumulados", columnDefinition = "FLOAT")
    private Double puntosAcumulados;
    @Column(name = "pesosDonados", columnDefinition = "FLOAT")
    private Double pesosDonados;
    @Enumerated(EnumType.STRING)
    @ElementCollection
    @CollectionTable(name="persona_juridica_forma_de_colaboracion", joinColumns = @JoinColumn(
            name="persona_juridica_id",
            referencedColumnName = "id"))
    @SequenceGenerator(name = "sequence-gen", sequenceName = "sequence_gen", allocationSize = 1)
    @CollectionId(columns = @Column(name="id"), generator = "sequence-gen", type = @Type(type="long"))
    @Column(name="nombre")
    private List<FormaDeColaboracion> formasDeColaboracion;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="persona_juridica_id", referencedColumnName="id")
    private List<Oferta> ofertas;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="persona_juridica_id", referencedColumnName="id")
    private List<Heladera> heladeras;
    @OneToOne
    @JoinColumn(name="cuestionario_respondido_id",referencedColumnName="id")
    private CuestionarioRespondido cuestionarioRespondido;
    @ManyToOne
    @JoinColumn(name="rubro_id",referencedColumnName="id")
    private Rubro rubro;
    @ManyToOne
    @JoinColumn(name="direccion_id",referencedColumnName="id")
    private Direccion direccion;

    public Integer cantidadDeHeladerasActivas(){
        int count = 0;
        for (Heladera heladera : this.heladeras) {
            if (heladera.getEstaActiva()) {
                count++;
            }
        }
        return count;
    }
    public Integer cantidadTotalDeMesesEnQueLasHeladerasActivasLlevanEstandoActivas() {
        if (this.heladeras == null) {
            return 0;
        }
        LocalDate now = LocalDate.now();
        LocalDateTime nowDateTime = now.atStartOfDay();
        return this.heladeras.stream()
                .filter(Heladera::getEstaActiva)
                .mapToInt(heladera -> (int)
                        ChronoUnit.MONTHS.between(heladera.getUltimaFechaYHoraVolvioAEstarActiva(), nowDateTime))
                .sum();
    }
    public void sumarPuntosYResetearAtributosDeColaboraciones(Double puntosASumar){
        this.setPuntosAcumulados(this.getPuntosAcumulados() + puntosASumar);
        this.setPesosDonados(0.0);
    }
    public Boolean contieneLaOferta (Oferta oferta){
        return this.ofertas.contains(oferta);
    }

    public void agregarOferta(Oferta oferta) {
        this.ofertas.add(oferta);
    }
    public void agregarHeladera(Heladera heladera) {
        this.heladeras.add(heladera);
    }
}

