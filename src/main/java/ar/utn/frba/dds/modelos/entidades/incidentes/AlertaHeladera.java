package ar.utn.frba.dds.modelos.entidades.incidentes;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.Tecnico;
import ar.utn.frba.dds.modelos.repositorios.RepositorioAlertaHeladera;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="alerta_heladera")
public class AlertaHeladera extends Persistente{
    @Enumerated(EnumType.STRING)
    @Column(name="tipo_incidente")
    private TipoIncidente tipoIncidente;
    @Column(name = "fecha_y_hora", columnDefinition="DATETIME")
    private LocalDateTime fechaYHora;
    @ManyToOne
    @JoinColumn(name="punto_estrategico_id", referencedColumnName = "id")
    private PuntoEstrategico puntoEstrategico;
    @OneToOne
    @JoinColumn(name="heladera_id", referencedColumnName = "id")
    private Heladera heladera;
    @Column(name = "esta_solucionado", columnDefinition="TINYINT")
    private Boolean estaSolucionado;
    @Column(name = "fecha_y_hora_fue_solucionado", columnDefinition="DATETIME")
    private LocalDateTime fechaYHoraFueSolucionado;
    @ManyToMany
    @JoinTable(
            name = "alerta_heladera_tecnico",
            joinColumns = @JoinColumn(name = "alerta_heladera_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tecnico_id", referencedColumnName = "id")
    )
    private List<Tecnico> tecnicosSugeridos;
    @ManyToOne
    @JoinColumn(name="tecnico_id", referencedColumnName = "id")
    private Tecnico tecnicoQueAcepto;
    @Transient
    private Sugeridor sugeridor;

    public void fueSolucionado() {
        this.setEstaSolucionado(true);
        this.setFechaYHoraFueSolucionado(LocalDateTime.now());
    }

    public AlertaHeladera(AlertaHeladeraInfo info){
        this.heladera = info.getHeladera();
        this.tipoIncidente = info.getTipoIncidente();
        this.fechaYHora = LocalDateTime.now();
        this.estaSolucionado = false;
        this.puntoEstrategico = info.getHeladera().getPuntoEstrategico();
        this.tecnicosSugeridos = new ArrayList<>();
    }

    public void agregarTecnicos(List<Tecnico> tecnicos){
        this.tecnicosSugeridos.addAll(tecnicos);
    }
}
