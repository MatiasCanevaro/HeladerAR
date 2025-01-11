package ar.utn.frba.dds.modelos.entidades.heladeras;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Persistence;
import javax.persistence.Table;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name="modelo_de_heladera")
public class ModeloDeHeladera extends Persistente {
    @Column(name="nombre")
    private String nombre;
    @Column(name="capacidad_en_viandas",columnDefinition = "INTEGER")
    private Integer capacidadEnViandas;
    @Column(name = "temperatura_minima_aceptable", columnDefinition="FLOAT")
    private Double temperaturaMinimaAceptable;
    @Column(name = "temperatura_maxima_aceptable", columnDefinition="FLOAT")
    private Double temperaturaMaximaAceptable;

    public ModeloDeHeladera(String nombre, Integer capacidadEnViandas, Double temperaturaMinimaAceptable, Double temperaturaMaximaAceptable) {
        this.nombre = nombre;
        this.capacidadEnViandas = capacidadEnViandas;
        this.temperaturaMinimaAceptable = temperaturaMinimaAceptable;
        this.temperaturaMaximaAceptable = temperaturaMaximaAceptable;
    }
}