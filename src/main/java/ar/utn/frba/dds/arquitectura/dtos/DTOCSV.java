package ar.utn.frba.dds.arquitectura.dtos;

import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DistribucionDeTarjeta;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DistribucionDeVianda;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DonacionDeDinero;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DonacionDeVianda;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytebuddy.asm.Advice;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class DTOCSV {
    private String nombre;
    private Boolean fueProcesado;
    private String fechaYHoraAlta;
    private List<DTODonacionDeVianda> donacionesDeVianda;
    private List<DTODonacionDeDinero> donacionesDeDinero;
    private List<DTODistribucionDeTarjeta> distribucionesDeTarjetas;
    private List<DTODistribucionDeVianda> distribucionesDeViandas;
    private List<DTOPersonaFisica> personasFisicas;

    public DTOCSV (String nombre, Boolean fueProcesado, String fechaYHoraAlta){
        this.nombre = nombre;
        this.fueProcesado = fueProcesado;
        this.fechaYHoraAlta = fechaYHoraAlta;
    }
}


