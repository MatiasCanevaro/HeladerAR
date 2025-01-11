package ar.utn.frba.dds.modulos.notificador;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notificador {
    private AdapterNotificador adapterNotificador;
    public void notificar(Mensaje mensaje, String receptor){
        adapterNotificador.notificar(mensaje,receptor);
    }

    public static Notificador crearNotificadorApache(){
        AdapterConcretoApacheEmail adapterConcretoApacheEmail = new AdapterConcretoApacheEmail();
        return Notificador
                .builder()
                .adapterNotificador(adapterConcretoApacheEmail)
                .build();
    }
    public static Notificador crearNotificadorTwilio(){
        AdapterConcretoTwilio adapterConcretoTwilio = new AdapterConcretoTwilio();
        return Notificador
                .builder()
                .adapterNotificador(adapterConcretoTwilio)
                .build();
    }
}