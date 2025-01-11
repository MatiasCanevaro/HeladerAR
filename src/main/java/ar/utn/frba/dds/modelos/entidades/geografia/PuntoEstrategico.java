package ar.utn.frba.dds.modelos.entidades.geografia;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.*;

import javax.persistence.*;

import static java.lang.Double.parseDouble;
import static java.lang.Math.abs;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name="punto_estrategico")
public class PuntoEstrategico extends Persistente {
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "latitud")
    private String latitud;
    @Column(name = "longitud")
    private String longitud;
    @OneToOne
    @JoinColumn(name="direccion_id", referencedColumnName = "id")
    private Direccion direccion;

    public Double calcularDistanciaEnMetrosHasta(PuntoEstrategico puntoEstrategico2) {
        String lat1Str = this.getLatitud();
        String lon1Str = this.getLongitud();
        String lat2Str = puntoEstrategico2.getLatitud();
        String lon2Str = puntoEstrategico2.getLongitud();

        try {
            Double lat1 = parseDouble(lat1Str);
            Double lon1 = parseDouble(lon1Str);
            Double lat2 = parseDouble(lat2Str);
            Double lon2 = parseDouble(lon2Str);

            final int R = 6371; // radio de la tierra

            double latDistance = Math.toRadians(lat2 - lat1);
            double lonDistance = Math.toRadians(lon2 - lon1);
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = R * c * 1000; // convertir a metros

            return abs(distance);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid coordinate format", e);
        }
    }

    public Double calcularDistanciaEnMetrosHasta(CentroDeLaCiudad puntoEstrategico2) {
        Double lat1 = parseDouble(this.getLatitud());
        Double lon1 = parseDouble(this.getLongitud());
        Double lat2 = parseDouble(puntoEstrategico2.getLatitud());
        Double lon2 = parseDouble(puntoEstrategico2.getLongitud());

        final int R = 6371; // radio de la tierra

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convertir a metros

        return abs(distance);
    }

    public static PuntoEstrategico crearPuntoEstrategico(String nombre, String latitud, String longitud){
        return PuntoEstrategico
                .builder()
                .nombre(nombre)
                .latitud(latitud)
                .longitud(longitud)
                .build();
    }
    public PuntoEstrategico(String latitud, String longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
}
