package ar.utn.frba.dds.modelos.entidades.formasDeColaboracion;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor

@Entity
@Table(name="donacion_de_dinero")
public class DonacionDeDinero extends Persistente {
    @Enumerated(EnumType.STRING)
    @Column(name = "nombre")
    private FormaDeColaboracion nombre;
    @ManyToOne
    @JoinColumn(name="persona_fisica_id", referencedColumnName = "id")
    private PersonaFisica personaFisica;
    @ManyToOne
    @JoinColumn(name="persona_juridica_id", referencedColumnName = "id")
    private PersonaJuridica personaJuridica;
    @Column(name = "fecha_donacion", columnDefinition="DATE")
    private LocalDate fechaDonacion;
    @Column(name = "monto", columnDefinition="FLOAT")
    private Double monto;
    @Column(name = "frecuencia_en_dia", columnDefinition="INTEGER")
    private Integer frecuenciaEnDia;

    public static DonacionDeDinero crearDonacionDeDinero(
            FormaDeColaboracion nombre, PersonaFisica personaFisica,PersonaJuridica personaJuridica,
            Double monto, Integer frecuenciaEnDia){
            return DonacionDeDinero
                    .builder()
                    .nombre(nombre)
                    .personaFisica(personaFisica)
                    .personaJuridica(personaJuridica)
                    .fechaDonacion(LocalDate.now())
                    .monto(monto)
                    .frecuenciaEnDia(frecuenciaEnDia)
                    .build();
    }
    public static DonacionDeDinero crearDonacionDeDineroConFechaDonacion(
            FormaDeColaboracion nombre, PersonaFisica personaFisica,PersonaJuridica personaJuridica,
            Double monto, Integer frecuenciaEnDia, LocalDate fechaDonacion){
        return DonacionDeDinero
                .builder()
                .nombre(nombre)
                .personaFisica(personaFisica)
                .personaJuridica(personaJuridica)
                .fechaDonacion(fechaDonacion)
                .monto(monto)
                .frecuenciaEnDia(frecuenciaEnDia)
                .build();
    }
}
