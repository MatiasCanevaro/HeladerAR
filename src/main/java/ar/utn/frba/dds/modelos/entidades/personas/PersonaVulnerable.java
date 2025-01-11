package ar.utn.frba.dds.modelos.entidades.personas;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.geografia.Direccion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="persona_vulnerable")
public class PersonaVulnerable extends Persistente {
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellido")
    private String apellido;
    @Column(name="fecha_nacimiento", columnDefinition="DATE")
    private LocalDate fechaNacimiento;
    @OneToOne
    @JoinColumn(name="direccion_id", referencedColumnName="id")
    private Direccion direccion;
    @Column(name = "numero_de_documento")
    private String numeroDocumento;
    @Enumerated(EnumType.STRING)
    @Column(name="tipo_documento")
    private TipoDocumento tipoDocumento;
    @OneToMany
    @JoinColumn(name="persona_vulnerable_id")
    private List<PersonaVulnerable> menoresACargo;

    public Integer cantidadDeMenoresACargo(){
        if(this.menoresACargo.isEmpty()){
            return 0;
        }
        else{
            return this.getMenoresACargo().size();
        }
    }

    public void agregarMenorACargo(PersonaVulnerable personaVulnerable){
        this.menoresACargo.add(personaVulnerable);
    }
}
