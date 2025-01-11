package ar.utn.frba.dds.modelos.entidades.ofertasYCanjes;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DistribucionDeTarjeta;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.FormaDeColaboracion;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CalculadoraDePuntos {
    private Double coeficientePesosDonados;
    private Double coeficienteViandasDistribuidas;
    private Double coeficienteViandasDonadas;
    private Double coeficienteTarjetasDistribuidas;
    private Double coeficienteHeladerasActivas;

    public Double calcularPuntosPesosDonados(Double pesosDonados){
        return pesosDonados * coeficientePesosDonados;
    }

    public Double calcularPuntosViandasDonadas(Integer viandasDonadas){
        return viandasDonadas * coeficienteViandasDonadas;
    }

    public Double calcularPuntosViandasDistribuidas(Integer viandasDistribuidas){
        return viandasDistribuidas * coeficienteViandasDistribuidas;
    }

    public Double calcularPuntosTarjetasDistribuidas(Integer tarjetasDistribuidas){
        return tarjetasDistribuidas * coeficienteTarjetasDistribuidas;
    }

    public Double calcularPuntosHeladerasActivas(
            Integer cantidadDeHeladerasActivas,
            Integer cantidadTotalDeMesesEnQueLasHeladerasActivasLlevanEstandoActivas){
        return cantidadDeHeladerasActivas *
                cantidadTotalDeMesesEnQueLasHeladerasActivasLlevanEstandoActivas *
                coeficienteHeladerasActivas;
    }

    public static CalculadoraDePuntos crearCalculadoraDePuntos(){
        return CalculadoraDePuntos
                .builder()
                .coeficientePesosDonados(1.0)
                .coeficienteViandasDistribuidas(1.0)
                .coeficienteViandasDonadas(1.0)
                .coeficienteTarjetasDistribuidas(1.0)
                .coeficienteHeladerasActivas(1.0)
                .build();
    }
}
