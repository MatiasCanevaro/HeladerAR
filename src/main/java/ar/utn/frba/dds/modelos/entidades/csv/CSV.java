package ar.utn.frba.dds.modelos.entidades.csv;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.contacto.Administrador;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DistribucionDeTarjeta;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DistribucionDeVianda;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DonacionDeDinero;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DonacionDeVianda;
import ar.utn.frba.dds.modelos.entidades.heladeras.Vianda;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="csv")
public class CSV extends Persistente {
    @Column(name="nombre")
    private String nombre;
    @Column(name="path", columnDefinition = "TEXT")
    private String path;
    @Column(name="fue_procesado", columnDefinition = "TINYINT")
    private Boolean fueProcesado;
    @ManyToOne
    @JoinColumn(name="administrador_id", referencedColumnName = "id")
    private Administrador administrador;
    @OneToMany
    @JoinColumn(name="csv_id", referencedColumnName = "id")
    private List<DonacionDeVianda> donacionesDeVianda;
    @OneToMany
    @JoinColumn(name="csv_id", referencedColumnName = "id")
    private List<DonacionDeDinero> donacionesDeDinero;
    @OneToMany
    @JoinColumn(name="csv_id", referencedColumnName = "id")
    private List<DistribucionDeTarjeta> distribucionesDeTarjetas;
    @OneToMany
    @JoinColumn(name="csv_id", referencedColumnName = "id")
    private List<DistribucionDeVianda> distribucionesDeViandas;
    @OneToMany
    @JoinColumn(name="csv_id", referencedColumnName = "id")
    private List<PersonaFisica> personasFisicas;

    public CSV(String nombre, String path, Boolean fueProcesado, Administrador administrador) {
        this.nombre = nombre;
        this.path = path;
        this.fueProcesado = fueProcesado;
        this.administrador = administrador;
        this.donacionesDeVianda = new ArrayList<>();
        this.donacionesDeDinero = new ArrayList<>();
        this.distribucionesDeTarjetas = new ArrayList<>();
        this.distribucionesDeViandas = new ArrayList<>();
        this.personasFisicas = new ArrayList<>();
    }

    public void agregarPersonaFisica(PersonaFisica personaFisica) {
        this.personasFisicas.add(personaFisica);
    }

    public void agregarDonacionDeVianda(DonacionDeVianda donacionDeVianda) {
        this.donacionesDeVianda.add(donacionDeVianda);
    }

    public void agregarDonacionDeDinero(DonacionDeDinero donacionDeDinero) {
        this.donacionesDeDinero.add(donacionDeDinero);
    }

    public void agregarDistribucionDeTarjeta(DistribucionDeTarjeta distribucionDeTarjeta) {
        this.distribucionesDeTarjetas.add(distribucionDeTarjeta);
    }

    public void agregarDistribucionDeVianda(DistribucionDeVianda distribucionDeVianda) {
        this.distribucionesDeViandas.add(distribucionDeVianda);
    }
}

