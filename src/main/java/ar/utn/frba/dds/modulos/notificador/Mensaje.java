package ar.utn.frba.dds.modulos.notificador;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Mensaje {
    private String asunto;
    private String cuerpo;
    private LocalDateTime fechaYHoraDeEnvio;
    public static Mensaje crearMensaje(String asunto, String cuerpo){
        return Mensaje
                .builder()
                .asunto(asunto)
                .cuerpo(cuerpo)
                .fechaYHoraDeEnvio(LocalDateTime.now())
                .build();
    }
}
