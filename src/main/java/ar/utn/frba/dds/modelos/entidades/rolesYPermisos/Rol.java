package ar.utn.frba.dds.modelos.entidades.rolesYPermisos;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.heladeras.Vianda;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="rol")
public class Rol extends Persistente {
    @Enumerated(EnumType.STRING)
    @Column(name="tipo_rol")
    private TipoRol tipoRol;
    @ManyToMany
    @JoinTable(
            name = "rol_permiso",
            joinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permiso_id", referencedColumnName = "id")
    )
    @GenericGenerator(name = "sequence-gen", strategy = "sequence")
    @CollectionId(columns = @Column(name="id"), generator = "sequence-gen", type = @Type(type="long"))
    private List<Permiso> permisos;
    public Boolean tienePermiso(Permiso permiso){
        return this.permisos.contains(permiso);
    }
    public Boolean tienePermisos(List<Permiso> permisos){
        return new HashSet<>(this.permisos).containsAll(permisos);
    }
}
