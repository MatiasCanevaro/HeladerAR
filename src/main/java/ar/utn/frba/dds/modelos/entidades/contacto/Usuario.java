package ar.utn.frba.dds.modelos.entidades.contacto;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import ar.utn.frba.dds.modelos.entidades.personas.Tecnico;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="usuario")
public class Usuario extends Persistente {
    @Column(name="correo_electronico")
    private String correoElectronico;
    @Column(name="contrasenia")
    private String contrasenia;
    @Enumerated(EnumType.STRING)
    @Column(name="tipo_rol")
    private TipoRol tipoRol;
    @OneToOne
    @JoinColumn(name="persona_fisica_id", referencedColumnName = "id")
    private PersonaFisica personaFisica;
    @OneToOne
    @JoinColumn(name="persona_juridica_id", referencedColumnName = "id")
    private PersonaJuridica personaJuridica;
    @OneToOne
    @JoinColumn(name="administrador_id", referencedColumnName = "id")
    private Administrador administrador;
    @OneToOne
    @JoinColumn(name="tecnico_id", referencedColumnName = "id")
    private Tecnico tecnico;
}
