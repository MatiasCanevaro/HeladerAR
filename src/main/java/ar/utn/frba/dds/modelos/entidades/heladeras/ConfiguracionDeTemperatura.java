package ar.utn.frba.dds.modelos.entidades.heladeras;


import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="configuracion_de_temperatura")
public class ConfiguracionDeTemperatura extends Persistente {
    @Column(name = "temperatura_maxima_configurada", columnDefinition="FLOAT")
    private Double temperaturaMaximaConfigurada;
    @Column(name = "temperatura_minima_configurada", columnDefinition="FLOAT")
    private Double temperaturaMinimaConfigurada;
    @ManyToOne
    @JoinColumn(name="persona_fisica_id", referencedColumnName = "id")
    private PersonaFisica personaFisicaQueLaConfiguro;
    @Column(name = "fecha_y_hora_configuracion", columnDefinition="DATETIME")
    private LocalDateTime fechaYHoraConfiguracion;

    public ConfiguracionDeTemperatura(
            Double temperaturaMaximaConfigurada, Double temperaturaMinimaConfigurada,
            PersonaFisica personaFisicaQueLaConfiguro, LocalDateTime fechaYHoraConfiguracion) {
        this.temperaturaMaximaConfigurada = temperaturaMaximaConfigurada;
        this.temperaturaMinimaConfigurada = temperaturaMinimaConfigurada;
        this.personaFisicaQueLaConfiguro = personaFisicaQueLaConfiguro;
        this.fechaYHoraConfiguracion = fechaYHoraConfiguracion;
    }

    public ConfiguracionDeTemperatura() {

    }
}
