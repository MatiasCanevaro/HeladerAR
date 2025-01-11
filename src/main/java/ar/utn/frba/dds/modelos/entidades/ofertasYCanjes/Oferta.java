package ar.utn.frba.dds.modelos.entidades.ofertasYCanjes;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="oferta")
public class Oferta extends Persistente {
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    @Column(name = "pathImagen")
    private String pathImagen;
    @Column(name = "cantidad_de_puntos_necesarios_para_acceder_al_beneficio", columnDefinition = "FLOAT")
    private Double cantidadDePuntosNecesariosParaAccederAlBeneficio;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="rubro_id", referencedColumnName = "id")
    private Rubro rubro;

}
