package ar.utn.frba.dds.modelos.entidades.personas;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.geografia.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name="tecnico")
public class Tecnico extends Persistente {
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellido")
    private String apellido;
    @Column(name = "numero_documento")
    private String numeroDocumento;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDocumento tipoDocumento;
    @Column(name = "cuil")
    private String cuil;
    @Getter
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="tecnico_id", referencedColumnName = "id")
    private List<MedioDeContacto> mediosDeContacto;
    @ManyToOne
    @JoinColumn(name="punto_estrategico_id", referencedColumnName = "id")
    private PuntoEstrategico puntoEstrategico;
    public String getMedioDeContacto(){
        return this.mediosDeContacto.get(0).getDato();
    }

    public Tecnico(String nombre, String apellido, String numeroDocumento, TipoDocumento tipoDocumento, String cuil, List<MedioDeContacto> mediosDeContacto, PuntoEstrategico puntoEstrategico) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.numeroDocumento = numeroDocumento;
    this.tipoDocumento = tipoDocumento;
    this.cuil = cuil;
    this.mediosDeContacto = mediosDeContacto;
    this.puntoEstrategico = puntoEstrategico;
}
}