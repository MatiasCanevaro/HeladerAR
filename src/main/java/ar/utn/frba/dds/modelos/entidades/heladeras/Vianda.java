package ar.utn.frba.dds.modelos.entidades.heladeras;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="vianda")
public class Vianda extends Persistente {
    @Column(name = "nombre_comida")
    private String nombreComida;
    @Column(name = "fecha_caducidad", columnDefinition="DATE")
    private LocalDate fechaCaducidad;
    @Column(name = "calorias", columnDefinition="FLOAT")
    private Double calorias;
    @Column(name = "peso", columnDefinition="FLOAT")
    private Double peso;
}
