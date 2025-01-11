package ar.utn.frba.dds.modelos.entidades.rolesYPermisos;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="permiso")
public class Permiso extends Persistente {
    @Column(name="nombre")
    public String nombre;
}
