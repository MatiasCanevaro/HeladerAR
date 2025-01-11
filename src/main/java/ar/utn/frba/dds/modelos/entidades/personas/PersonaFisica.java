package ar.utn.frba.dds.modelos.entidades.personas;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.cuestionarios.CuestionarioRespondido;
import ar.utn.frba.dds.modelos.entidades.tarjetas.Tarjeta;
import ar.utn.frba.dds.modelos.entidades.geografia.Direccion;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.FormaDeColaboracion;
import lombok.*;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name="persona_fisica")
public class PersonaFisica extends Persistente {
    @Column(name = "nombre")
    private String nombre;
    @Column(name="apellido")
    private String apellido;
    @Enumerated(EnumType.STRING)
    @Column(name="tipo_documento")
    private TipoDocumento tipoDocumento;
    @Column(name="numero_de_documento")
    private String numeroDocumento;
    @Column(name="fecha_nacimiento", columnDefinition = "DATE")
    private LocalDate fechaNacimiento;
    @ManyToOne
    @JoinColumn(name="direccion_id",referencedColumnName="id")
    private Direccion direccion;
    @Column(name="puntos_acumulados", columnDefinition = "FLOAT")
    private Double puntosAcumulados;
    @Column(name="pesos_donados", columnDefinition = "FLOAT")
    private Double pesosDonados;
    @Column(name="viandas_donadas", columnDefinition = "INTEGER")
    private Integer viandasDonadas;
    @Column(name="viandas_distribuidas", columnDefinition = "INTEGER")
    private Integer viandasDistribuidas;
    @Column(name="tarjetas_distribuidas", columnDefinition = "INTEGER")
    private Integer tarjetasDistribuidas;
    @Enumerated(EnumType.STRING)
    @ElementCollection()
    @CollectionTable(name="persona_fisica_forma_de_colaboracion", joinColumns = @JoinColumn(
            name="persona_fisica_id",
            referencedColumnName = "id"))
    @SequenceGenerator(name = "sequence-gen", sequenceName = "sequence_gen", allocationSize = 1)
    @CollectionId(columns = @Column(name="id"), generator = "sequence-gen", type = @Type(type="long"))
    @Column(name="nombre")
    private List<FormaDeColaboracion> formasDeColaboracion;
    @OneToOne
    @JoinColumn(name="cuestionario_respondido_id",referencedColumnName="id")
    private CuestionarioRespondido cuestionarioRespondido;
    @Embedded
    private Email email;
    @OneToMany(mappedBy = "personaFisica", cascade = CascadeType.ALL)
    private List<Tarjeta> tarjetas;

    public void agregarTarjeta(Tarjeta tarjeta){
        this.tarjetas.add(tarjeta);
        tarjeta.setPersonaFisica(this);
    }

    public Tarjeta getTarjetaActiva() {
        return this.tarjetas.stream()
                .filter(Tarjeta::getActivo)
                .findFirst()
                .orElse(null);
    }

    public void sumarPuntosYResetearAtributosDeColaboraciones(Double puntosASumar){
        this.setPuntosAcumulados(this.getPuntosAcumulados() + puntosASumar);
        this.setPesosDonados(0.0);
        this.setViandasDonadas(0);
        this.setViandasDistribuidas(0);
        this.setTarjetasDistribuidas(0);
    }
    public static PersonaFisica crearPersonaFisica(
        TipoDocumento tipoDocumento,String numeroDocumento,String nombre,String apellido,String correoElectronico){
        Email email = Email.crearEmail(correoElectronico);
        return PersonaFisica
                .builder()
                .tipoDocumento(tipoDocumento)
                .numeroDocumento(numeroDocumento)
                .nombre(nombre)
                .apellido(apellido)
                .puntosAcumulados(0.0)
                .pesosDonados(0.0)
                .viandasDonadas(0)
                .viandasDistribuidas(0)
                .tarjetasDistribuidas(0)
                .email(email)
                .build();
    }
    public static PersonaFisica crearPersonaFisica(
            TipoDocumento tipoDocumento,String numeroDocumento,String nombre,
            String apellido,Direccion direccion,LocalDate fechaNacimiento){
        return PersonaFisica
                .builder()
                .tipoDocumento(tipoDocumento)
                .numeroDocumento(numeroDocumento)
                .nombre(nombre)
                .apellido(apellido)
                .direccion(direccion)
                .puntosAcumulados(0.0)
                .pesosDonados(0.0)
                .viandasDonadas(0)
                .viandasDistribuidas(0)
                .tarjetasDistribuidas(0)
                .fechaNacimiento(fechaNacimiento)
                .build();
    }
}
