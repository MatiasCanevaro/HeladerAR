package ar.utn.frba.dds.modelos.entidades.reportes;

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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="reporte")
public class Reporte extends Persistente {
    @Column(name="nombre")
    private String nombre;
    @Column(name="tamanio", columnDefinition = "INTEGER")
    private Integer tamanio;
    @Column(name="fecha_creacion", columnDefinition = "DATE")
    private LocalDate fechaCreacion;
    @Column(name="path", columnDefinition = "TEXT")
    private String path;
}
