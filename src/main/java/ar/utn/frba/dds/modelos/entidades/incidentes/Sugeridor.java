package ar.utn.frba.dds.modelos.entidades.incidentes;

import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.Tecnico;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import ar.utn.frba.dds.modulos.notificador.CronNotificador;
import ar.utn.frba.dds.modulos.notificador.Mensaje;
import ar.utn.frba.dds.modulos.notificador.MensajeTecnicoSugeridoAIncidente;
import lombok.Builder;

import java.util.List;

@Builder
public class Sugeridor {
    private CronNotificador cronNotificador;
    private BuscadoraTecnicoMasCercano buscadoraTecnicoMasCercano;
    private MensajeTecnicoSugeridoAIncidente mensajeTecnicoSugeridoAIncidente;

    public List<Tecnico> sugerirTecnicos(Heladera heladera){
        List<Tecnico> tecnicosSugeridos =
                buscadoraTecnicoMasCercano.buscarTecnicosMasCercanos(heladera.getPuntoEstrategico());
        Mensaje mensaje = mensajeTecnicoSugeridoAIncidente.generarMensaje(heladera);
        for(Tecnico tecnicoSugerido : tecnicosSugeridos){
            String medioDeContacto = tecnicoSugerido.getMedioDeContacto();
            //this.cronNotificador.notificarDiferido(mensaje, medioDeContacto);
            System.out.println("medio de contacto " + medioDeContacto);
            this.cronNotificador.notificarAhora(mensaje, medioDeContacto);
        }
        return tecnicosSugeridos;
    }
    public static Sugeridor crearSugeridor(){
        CronNotificador cronNotificador = CronNotificador.crearCronNotificador();
        Repositorio repositorio = new Repositorio();
        BuscadoraTecnicoMasCercano buscadorTecnicoMasCercano = new BuscadoraTecnicoMasCercano(repositorio,5000);
        MensajeTecnicoSugeridoAIncidente mensajeTecnicoSugeridoAIncidente = new MensajeTecnicoSugeridoAIncidente();
        return Sugeridor
                .builder()
                .cronNotificador(cronNotificador)
                .buscadoraTecnicoMasCercano(buscadorTecnicoMasCercano)
                .mensajeTecnicoSugeridoAIncidente(mensajeTecnicoSugeridoAIncidente)
                .build();
    }
}
