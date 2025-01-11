package ar.utn.frba.dds.modelos.entidades.heladeras;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="sensor_de_movimiento")
public class SensorDeMovimiento extends Persistente {
    @OneToOne
    @JoinColumn(name="heladera_id", referencedColumnName = "id")
    private Heladera heladera;

    public SensorDeMovimiento(Heladera heladera) {
        this.heladera = heladera;
    }

    public void recibirDeteccionDeMovimiento(){
        heladera.manejarDeteccionDeMovimiento();
    }
}
