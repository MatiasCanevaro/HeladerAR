package ar.utn.frba.dds.modulos.notificador;

import lombok.*;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CronNotificador {
    private LocalTime horaMaximaAceptable;
    private LocalTime horaMinimaAceptable;
    private LocalTime horaEnvio;
    private Notificador notificador;
    public void notificarDiferido(Mensaje mensaje, String receptor) {
        LocalTime ahora = LocalTime.now();

       if (ahora.isAfter(horaMaximaAceptable) || ahora.isBefore(horaMinimaAceptable)) {
            long delay = Duration.between(ahora, horaEnvio).toMillis();
            if (delay < 0) {
                // Si el delay es negativo significa que ya son las 9:00 o después
                // y debemos esperar hasta las 9:00 del siguiente día
                delay += Duration.ofHours(24).toMillis();
            }

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                private final Notificador notificador = Notificador.crearNotificadorApache();
                @Override
                public void run() {
                    this.notificador.notificar(mensaje, receptor);
                }
            }, delay);

        } else {
            this.notificador.notificar(mensaje, receptor);
        }
    }

    public void notificarAhora(Mensaje mensaje, String receptor) {
        Notificador notificador = Notificador.crearNotificadorApache();
        notificador.notificar(mensaje, receptor);
    }

    public static CronNotificador crearCronNotificador(){
        Notificador notificador = Notificador.crearNotificadorApache();
        return CronNotificador
                .builder()
                //.horaMaximaAceptable(LocalTime.of(23, 0))
                //.horaMinimaAceptable(LocalTime.of(8, 0))
                .horaMaximaAceptable(LocalTime.of(23, 59))
                .horaMinimaAceptable(LocalTime.of(0, 0))
                .horaEnvio(LocalTime.now())
                .notificador(notificador)
                .build();
    }
}
