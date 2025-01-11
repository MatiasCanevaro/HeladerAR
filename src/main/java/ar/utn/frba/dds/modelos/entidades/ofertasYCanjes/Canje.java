package ar.utn.frba.dds.modelos.entidades.ofertasYCanjes;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="canje")
public class Canje extends Persistente {
    @ManyToOne
    @JoinColumn(name="persona_juridica_que_ofrecio_id", referencedColumnName = "id")
    private PersonaJuridica personaJuridicaQueOfrecio;
    @ManyToOne
    @JoinColumn(name="persona_juridica_que_canjeo_id", referencedColumnName = "id")
    private PersonaJuridica personaJuridicaQueCanjeo;
    @ManyToOne
    @JoinColumn(name="persona_fisica_que_canjeo_id", referencedColumnName = "id")
    private PersonaFisica personaFisicaQueCanjeo;
    @Column(name="fecha_y_hora_canje", columnDefinition="DATETIME")
    private LocalDateTime fechaYHoraCanje;
    @ManyToOne
    @JoinColumn(name = "oferta_canjeada_id", referencedColumnName = "id")
    private Oferta ofertaCanjeada;
}
