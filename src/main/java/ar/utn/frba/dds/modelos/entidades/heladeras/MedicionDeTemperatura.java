package ar.utn.frba.dds.modelos.entidades.heladeras;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor

@Entity
@Table(name="medicion_de_temperatura")
public class MedicionDeTemperatura extends Persistente {
    @Column(name = "valor", columnDefinition="FLOAT")
    private Double valor;
    @Column(name = "fecha_y_hora_medicion", columnDefinition="DATETIME")
    private LocalDateTime fechaYHoraMedicion;

    public static MedicionDeTemperatura crearMedicionDeTemperatura(Double valor){
        return MedicionDeTemperatura
                .builder()
                .valor(valor)
                .fechaYHoraMedicion(LocalDateTime.now())
                .build();
    }
}
