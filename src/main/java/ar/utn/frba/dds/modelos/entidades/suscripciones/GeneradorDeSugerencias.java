package ar.utn.frba.dds.modelos.entidades.suscripciones;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modulos.notificador.Mensaje;
import ar.utn.frba.dds.modulos.notificador.Notificador;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneradorDeSugerencias extends Persistente implements OpcionSuscripcion {
    private BuscadorHeladeraMasCercana buscadorHeladeraMasCercana;
    private MensajeGeneradorDeSugerencias mensajeGeneradorDeSugerencias;
    private Notificador notificador;
    private Integer cantidadMinimaDeViandas;
    private Integer cantidadDeHeladerasARetornar;

    public void evaluarEnvioDeMensaje(Suscripcion suscripcion) {
        Heladera heladera = suscripcion.getHeladera();
        PersonaFisica personaFisica = suscripcion.getPersonaFisica();
        if (!heladera.getEstaActiva()){
            List<Heladera> heladeras = buscadorHeladeraMasCercana.buscarHeladerasConEspacioMasCercanas(
                    heladera, this.cantidadMinimaDeViandas, this.cantidadDeHeladerasARetornar);
            Sugerencia sugerencia = Sugerencia.crearSugerencia(heladera, personaFisica, heladeras);
            Mensaje mensaje = mensajeGeneradorDeSugerencias.generarMensaje(sugerencia);
            String email = personaFisica.getEmail().getCorreoElectronico();
            notificador.notificar(mensaje,email);
        }
    }
    
    public static GeneradorDeSugerencias crearGeneradorDeSugerencias(){
        BuscadorHeladeraMasCercana buscadorHeladeraMasCercana = new BuscadorHeladeraMasCercana();
        MensajeGeneradorDeSugerencias mensajeGeneradorDeSugerencias = new MensajeGeneradorDeSugerencias();
        Notificador notificador = Notificador.crearNotificadorApache();
        return GeneradorDeSugerencias
            .builder()
            .buscadorHeladeraMasCercana(buscadorHeladeraMasCercana)
            .mensajeGeneradorDeSugerencias(mensajeGeneradorDeSugerencias)
            .notificador(notificador)
            .build();
    }
    public GeneradorDeSugerencias (BuscadorHeladeraMasCercana buscadorHeladeraMasCercana,
                                   Notificador notificador,
                                   MensajeGeneradorDeSugerencias mensajeGeneradorDeSugerencias){
        this.buscadorHeladeraMasCercana = buscadorHeladeraMasCercana;
        this.mensajeGeneradorDeSugerencias = mensajeGeneradorDeSugerencias;
        this.notificador = notificador;
    }
}

