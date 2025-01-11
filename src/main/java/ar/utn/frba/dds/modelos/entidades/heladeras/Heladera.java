package ar.utn.frba.dds.modelos.entidades.heladeras;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.geografia.Ciudad;
import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.incidentes.AlertaHeladera;
import ar.utn.frba.dds.modelos.entidades.incidentes.AlertaHeladeraInfo;
import ar.utn.frba.dds.modelos.entidades.incidentes.ReportadorFallaTecnica;
import ar.utn.frba.dds.modelos.entidades.incidentes.TipoIncidente;
import ar.utn.frba.dds.modelos.entidades.personas.Tecnico;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import ar.utn.frba.dds.modelos.repositorios.RepositorioAlertaHeladera;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="heladera")
public class Heladera extends Persistente {
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="heladera_id", referencedColumnName = "id")
    private List<Vianda> viandas;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="punto_estrategico_id", referencedColumnName = "id")
    private PuntoEstrategico puntoEstrategico;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="modelo_id", referencedColumnName = "id")
    private ModeloDeHeladera modelo;
    @OneToMany
    @JoinColumn(name="heladera_id", referencedColumnName = "id")
    private List<ConfiguracionDeTemperatura> configuracionesDeTemperaturas;
    @OneToMany
    @JoinColumn(name="heladera_id", referencedColumnName = "id")
    private List<MedicionDeTemperatura> medicionesDeTemperatura;
    @ElementCollection
    @CollectionTable(name = "heladera_fecha_y_hora_dejo_de_estar_activa", joinColumns = @JoinColumn(name = "heladera_id"))
    @Column(name = "fecha_y_hora_dejo_de_estar_activa", columnDefinition = "DATETIME")
    private List<LocalDateTime> fechasYHorasDejoDeEstarActiva;
    @ElementCollection
    @CollectionTable(name = "heladera_fecha_y_hora_volvio_a_estar_activa", joinColumns = @JoinColumn(name = "heladera_id"))
    @Column(name = "fecha_y_hora_volvio_a_estar_activa", columnDefinition = "DATETIME")
    private List<LocalDateTime> fechasYHorasVolvioAEstarActiva;
    @ElementCollection
    @CollectionTable(name = "heladera_fecha_y_hora_reubicada", joinColumns = @JoinColumn(name = "heladera_id"))
    @Column(name = "fecha_y_hora_reubicada", columnDefinition = "DATETIME")
    private List<LocalDateTime> fechasYHorasReubicada;
    @Column(name = "esta_activa", columnDefinition="TINYINT")
    private Boolean estaActiva;
    @Transient
    private RepositorioAlertaHeladera repositorioAlertaHeladera = new RepositorioAlertaHeladera();
    @Transient
    private Repositorio repositorio = new Repositorio();
    @Transient
    private ReportadorFallaTecnica reportadorFallaTecnica = new ReportadorFallaTecnica();

    public Heladera(ModeloDeHeladera modeloDeHeladera, PuntoEstrategico puntoEstrategico) {
        this.modelo = modeloDeHeladera;
        this.viandas = new ArrayList<>();
        this.estaActiva = true;
        this.configuracionesDeTemperaturas = new ArrayList<>();
        this.medicionesDeTemperatura = new ArrayList<>();
        this.fechasYHorasDejoDeEstarActiva = new ArrayList<>();
        this.fechasYHorasVolvioAEstarActiva = new ArrayList<>();
        this.fechasYHorasReubicada = new ArrayList<>();
        this.puntoEstrategico = puntoEstrategico;
    }

    public Boolean desperfectoPorTemperatura() {
        MedicionDeTemperatura ultimaMedicion = this.obtenerUltimaMedicion();

        if(this.configuracionesDeTemperaturas.isEmpty()){
            return ultimaMedicion.getValor() > this.modelo.getTemperaturaMinimaAceptable()
                    && ultimaMedicion.getValor() < this.modelo.getTemperaturaMaximaAceptable();
        }
        else{
            ConfiguracionDeTemperatura ultimaConfiguracionDeTemperatura =
                    this.configuracionesDeTemperaturas.get(this.configuracionesDeTemperaturas.size() - 1);
            return ultimaMedicion.getValor() > ultimaConfiguracionDeTemperatura.getTemperaturaMaximaConfigurada()
                    && ultimaMedicion.getValor() < ultimaConfiguracionDeTemperatura.getTemperaturaMinimaConfigurada();
        }
    }

    public MedicionDeTemperatura obtenerUltimaMedicion(){
        System.out.println(this.medicionesDeTemperatura.get(this.medicionesDeTemperatura.size() - 1).getValor());
        return this.medicionesDeTemperatura.get(this.medicionesDeTemperatura.size() - 1);
    }

    public LocalDateTime getUltimaFechaYHoraVolvioAEstarActiva() {
        return this.fechasYHorasVolvioAEstarActiva.get(this.fechasYHorasVolvioAEstarActiva.size() - 1);
    }

    public void manejarDeteccionDeMovimiento(){
        if(this.estaActiva) {
            this.dejarDeEstarActiva();
        }
        AlertaHeladeraInfo alertaHeladeraInfo = AlertaHeladeraInfo.crearAlertaHeladeraInfo( this,TipoIncidente.TEMPERATURA);
        reportadorFallaTecnica.reportar(alertaHeladeraInfo);
        //repositorioAlertaHeladera.guardar(alertaHeladera);
    }
    public void manejarTemperaturaRecibida(Double temperaturaRecibida){
        MedicionDeTemperatura medicionDeTemperatura =
                MedicionDeTemperatura.crearMedicionDeTemperatura(temperaturaRecibida);

        this.medicionesDeTemperatura.add(medicionDeTemperatura);
        repositorio.guardar(medicionDeTemperatura);

        Boolean estaActivaAhora = this.desperfectoPorTemperatura();

        if(!this.estaActiva && estaActivaAhora){
            System.out.println("Estoy activada");
            this.volverAEstarActiva();
        }
        if(this.estaActiva && !estaActivaAhora){
            System.out.println("Estoy desactivada");
            AlertaHeladeraInfo alertaHeladeraInfo = AlertaHeladeraInfo.crearAlertaHeladeraInfo( this,TipoIncidente.TEMPERATURA);
            System.out.println("Voy a reportar");
            reportadorFallaTecnica.reportar(alertaHeladeraInfo);
            //repositorioAlertaHeladera.guardar(alertaHeladera);
        }
    }

    public void volverAEstarActiva(){
        this.fechasYHorasVolvioAEstarActiva.add(LocalDateTime.now());
        this.setEstaActiva(true);
    }

    public void dejarDeEstarActiva(){
        this.setEstaActiva(false);
        this.fechasYHorasDejoDeEstarActiva.add(LocalDateTime.now());
    }

    public Double getTemperaturaMinimaConfiguradaActual(){
        if(this.configuracionesDeTemperaturas.isEmpty()){
            return this.modelo.getTemperaturaMinimaAceptable();
        }
        else{
            ConfiguracionDeTemperatura ultConfiguracionDeTemperatura =
                    this.configuracionesDeTemperaturas.get(this.configuracionesDeTemperaturas.size() - 1);
            return ultConfiguracionDeTemperatura.getTemperaturaMaximaConfigurada();
        }
    }
    public Double getTemperaturaMaximaConfiguradaActual(){
        if(this.configuracionesDeTemperaturas.isEmpty()){
            return this.modelo.getTemperaturaMaximaAceptable();
        }
        else{
            ConfiguracionDeTemperatura ultConfiguracionDeTemperatura =
                    this.configuracionesDeTemperaturas.get(this.configuracionesDeTemperaturas.size() - 1);
            return ultConfiguracionDeTemperatura.getTemperaturaMaximaConfigurada();
        }
    }
    public void descontarViandas(List<Vianda> viandasADescontar){
        this.viandas.removeAll(viandasADescontar);
    }
    public void agregarViandas(Vianda ... viandas) {
        Collections.addAll(this.viandas,viandas);
    }
    public void agregarViandas(List<Vianda> viandas) {
        this.viandas.addAll(viandas);
    }
    public Ciudad obtenerCiudad() {
        return this.getPuntoEstrategico().getDireccion().getCiudad();
    }
    public Boolean tieneEspacioDisponible() {
        return !Objects.equals(this.obtenerCantidadDeViandasActuales(), this.modelo.getCapacidadEnViandas());
    }
    public Integer cantidadDeEspacioDisponibleEnViandas(){
        return this.modelo.getCapacidadEnViandas() - this.obtenerCantidadDeViandasActuales();
    }
    public Integer obtenerCantidadDeViandasActuales(){
        return this.getViandas().size();
    }
}
