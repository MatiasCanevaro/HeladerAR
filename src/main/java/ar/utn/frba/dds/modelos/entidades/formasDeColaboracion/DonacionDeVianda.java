package ar.utn.frba.dds.modelos.entidades.formasDeColaboracion;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.heladeras.Vianda;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor

@Entity
@Table(name="donacion_de_vianda")
public class DonacionDeVianda extends Persistente {
    @Enumerated(EnumType.STRING)
    @Column(name = "nombre")
    private FormaDeColaboracion nombre;
    @ManyToOne
    @JoinColumn(name="persona_fisica_id", referencedColumnName = "id")
    private PersonaFisica personaFisica;
    @OneToOne
    @JoinColumn(name="vianda_id", referencedColumnName = "id")
    private Vianda vianda;
    @Column(name = "fecha_lista_para_entregar", columnDefinition="DATE")
    private LocalDate fechaListaParaEntregar;
    @Column(name = "fecha_donacion", columnDefinition="DATE")
    private LocalDate fechaDonacion;

    public static DonacionDeVianda crearDonacionDeVianda(
            FormaDeColaboracion nombre, PersonaFisica personaFisica, LocalDate fechaDonacion){
        return DonacionDeVianda
                .builder()
                .nombre(nombre)
                .personaFisica(personaFisica)
                .fechaDonacion(fechaDonacion)
                .build();
    }
}
