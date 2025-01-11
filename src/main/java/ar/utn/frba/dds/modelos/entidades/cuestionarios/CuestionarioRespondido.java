package ar.utn.frba.dds.modelos.entidades.cuestionarios;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="cuestionario_respondido")
public class CuestionarioRespondido extends Persistente {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="cuestionario_id", referencedColumnName = "id")
    private Cuestionario cuestionario;
    @Column(name="fecha_y_hora_respondido", columnDefinition="DATETIME")
    private LocalDateTime fechaYHoraRespondido;
}
