package ar.utn.frba.dds.modelos.entidades.incidentes;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.Tecnico;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="falla_tecnica")
public class FallaTecnica extends Persistente{
    @Column(name = "fecha_y_hora_falla", columnDefinition="DATETIME")
    private LocalDateTime fechaYHoraFalla;
    @Column(name = "fecha_y_hora_reporte", columnDefinition="DATETIME")
    private LocalDateTime fechaYHoraReporte;
    @ManyToOne
    @JoinColumn(name="punto_estrategico_id", referencedColumnName = "id")
    private PuntoEstrategico puntoEstrategico;
    @ManyToOne
    @JoinColumn(name="heladera_id", referencedColumnName = "id")
    private Heladera heladera;
    @Column(name = "esta_solucionado", columnDefinition="TINYINT")
    private Boolean estaSolucionado;
    @Column(name = "fecha_y_hora_fue_solucionado", columnDefinition="DATETIME")
    private LocalDateTime fechaYHoraFueSolucionado;
    @ManyToOne
    @JoinColumn(name="persona_fisica_id", referencedColumnName = "id")
    private PersonaFisica personaFisicaQueLoReporto;
    @Column(name = "descripcion", columnDefinition="TEXT")
    private String descripcion;
    @Column(name = "pathImagen")
    private String pathImagen;
    @Column(name="tipo_incidente")
    private TipoIncidente tipoIncidente;
    @ManyToMany
    @JoinTable(
            name = "falla_tecnica_tecnico",
            joinColumns = @JoinColumn(name = "falla_tecnica_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tecnico_id", referencedColumnName = "id")
    )
    private List<Tecnico> tecnicosSugeridos;
    @ManyToOne
    @JoinColumn(name="tecnico_id", referencedColumnName = "id")
    private Tecnico tecnicoQueAcepto;

    public static FallaTecnica crearFallaTecnica(FallaTecnicaInfo fallaTecnicaInfo){
        LocalDateTime fechaYHoraFalla = fallaTecnicaInfo.getFechaYHoraFalla();
        Heladera heladera = fallaTecnicaInfo.getHeladera();
        PuntoEstrategico puntoEstrategico = heladera.getPuntoEstrategico();
        PersonaFisica personaFisica = fallaTecnicaInfo.getPersonaFisicaQueLoReporto();
        String descripcion = fallaTecnicaInfo.getDescripcion();
        String pathImagen = fallaTecnicaInfo.getPathImagen();
        return FallaTecnica
                .builder()
                .fechaYHoraFalla(fechaYHoraFalla)
                .fechaYHoraReporte(LocalDateTime.now())
                .puntoEstrategico(puntoEstrategico)
                .heladera(heladera)
                .estaSolucionado(false)
                .personaFisicaQueLoReporto(personaFisica)
                .descripcion(descripcion)
                .pathImagen(pathImagen)
                .tipoIncidente(fallaTecnicaInfo.getTipoIncidente())
                .build();
    }

    public void fueSolucionado(){
        this.setEstaSolucionado(true);
        this.setFechaYHoraFueSolucionado(LocalDateTime.now());
    }
}
