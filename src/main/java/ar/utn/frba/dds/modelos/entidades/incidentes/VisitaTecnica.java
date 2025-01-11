package ar.utn.frba.dds.modelos.entidades.incidentes;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.Tecnico;
import com.twilio.rest.monitor.v1.Alert;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="visita_tecnica")
public class VisitaTecnica extends Persistente {
    @ManyToOne
    @JoinColumn(name="tecnico_id", referencedColumnName = "id")
    private Tecnico tecnico;
    @ManyToOne
    @JoinColumn(name="heladera_id", referencedColumnName = "id")
    private Heladera heladera;
    @Column(name = "fecha_y_hora_visita", columnDefinition="DATETIME")
    private LocalDateTime fechaYHoraVisita;
    @Column(name = "descripcion_del_trabajo", columnDefinition="TEXT")
    private String descripcionDelTrabajo;
    @Column(name = "pathImagen")
    private String pathImagen;
    @ManyToOne
    @JoinColumn(name="falla_tecnica_id", referencedColumnName = "id")
    private FallaTecnica fallaTecnica;
    @ManyToOne
    @JoinColumn(name="alerta_heladera_id", referencedColumnName = "id")
    private AlertaHeladera alertaHeladera;
    @Column(name = "pudo_solucionar_el_incidente", columnDefinition="TINYINT")
    private Boolean pudoSolucionarElIncidente;
    public void incidenteSolucionado(){
        fallaTecnica.fueSolucionado();
        heladera.volverAEstarActiva();
    }
    public static VisitaTecnica crearVisitaTecnica(
            Tecnico tecnico,Heladera heladeraRecibida,
            LocalDateTime fechaYHoraVisita,String descripcionDelTrabajo,
            String pathImagen,FallaTecnica fallaTecnica, AlertaHeladera alertaHeladera){
        return VisitaTecnica
                .builder()
                .tecnico(tecnico)
                .heladera(heladeraRecibida)
                .fechaYHoraVisita(fechaYHoraVisita)
                .descripcionDelTrabajo(descripcionDelTrabajo)
                .pathImagen(pathImagen)
                .fallaTecnica(fallaTecnica)
                .alertaHeladera(alertaHeladera)
                .pudoSolucionarElIncidente(false)
                .build();
    }
}
