package ar.utn.frba.dds.modelos.entidades.tarjetas;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaVulnerable;
import lombok.*;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="tarjeta")
public class Tarjeta extends Persistente {
    @Column(name = "codigo_alfanumerico")
    private String codigoAlfanumerico;
    @Nullable
    @ManyToOne
    @JoinColumn(name="persona_vulnerable_id", referencedColumnName = "id")
    private PersonaVulnerable personaVulnerable;
    @Nullable
    @ManyToOne
    @JoinColumn(name="persona_fisica_id", referencedColumnName = "id")
    private PersonaFisica personaFisica;
    @ElementCollection
    @CollectionTable(name="tarjeta_fecha_retiro_de_vianda", joinColumns = @JoinColumn(
            name="tarjeta_id",
            referencedColumnName = "id"))
    @Column(name = "fecha_retiro_de_vianda", columnDefinition="DATETIME")
    private List<LocalDateTime> fechasRetirosDeViandas;
    @Builder.Default
    @Column(name = "cantidad_de_veces_que_puede_ser_utilizada_por_dia")
    private Integer cantidadDeVecesQuePuedeSerUtilizadaPorDia = 4;

    public Boolean puedeSerUtilizada(){
        Integer cantidadDeVecesEnQueFueUtilizadaHoy = this.contarRetirosDeHoy();
        Integer cantidadMaximaDeVecesEnQuePuedeSerUtilizada =
                this.personaVulnerable.cantidadDeMenoresACargo() * 2 + this.cantidadDeVecesQuePuedeSerUtilizadaPorDia;
        return cantidadDeVecesEnQueFueUtilizadaHoy < cantidadMaximaDeVecesEnQuePuedeSerUtilizada;
    }

    public Integer contarRetirosDeHoy() {
        LocalDate hoy = LocalDate.now();
        return (int) this.fechasRetirosDeViandas.stream()
                .filter(fecha -> fecha.toLocalDate().equals(hoy))
                .count();
    }

    public static Tarjeta crearTarjeta(String codigoAlfanumerico){
        return Tarjeta
                .builder()
                .codigoAlfanumerico(codigoAlfanumerico)
                .build();
    }


}
