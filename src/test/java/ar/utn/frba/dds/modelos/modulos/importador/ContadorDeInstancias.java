package ar.utn.frba.dds.modelos.modulos.importador;

import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DistribucionDeTarjeta;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DistribucionDeVianda;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DonacionDeDinero;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DonacionDeVianda;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;

import java.util.List;

public class ContadorDeInstancias {
    public static Integer cantidadDePersonasFisicasInstanciadas(List<Object> personasFisicas) {
        Integer contador = 0;
        for(Object o : personasFisicas){
            contador++;
            PersonaFisica personaFisica = (PersonaFisica) o;
            System.out.println(
                    personaFisica.getNombre() + "|" +
                    personaFisica.getApellido() + "|" +
                    personaFisica.getTipoDocumento() + "|" +
                    personaFisica.getNumeroDocumento() + "|" +
                    personaFisica.getEmail().getCorreoElectronico()
            );
        }
        return contador;
    }

    public static Integer cantidadDeDonacionesDeDineroInstanciadas(List<Object> donacionesDeDinero) {
        Integer contador = 0;
        for (Object o : donacionesDeDinero) {
            contador++;
            DonacionDeDinero donacion = (DonacionDeDinero) o;
            System.out.println(
                            donacion.getNombre() + "|" +
                            donacion.getPersonaFisica().getNombre() + "|" +
                            donacion.getFechaDonacion() + "|" +
                            donacion.getMonto() + "|" +
                            donacion.getFrecuenciaEnDia()
            );
        }
        return contador;
    }

    public static Integer cantidadDeDonacionesDeViandaInstanciadas(List<Object> donacionesDeViandas) {
        Integer contador = 0;
        for (Object o : donacionesDeViandas) {
            contador++;
            DonacionDeVianda donacion = (DonacionDeVianda) o;
            System.out.println(
                            donacion.getNombre() + "|" +
                            donacion.getPersonaFisica().getNombre() + "|" +
                            donacion.getFechaDonacion()
            );
        }
        return contador;
    }

    public static Integer cantidadDeDistribucionesDeViandasInstanciadas(List<Object> distribucionDeViandas) {
        Integer contador = 0;
        for (Object o : distribucionDeViandas) {
            contador++;
            DistribucionDeVianda distribucion = (DistribucionDeVianda) o;
            System.out.println(
                            distribucion.getNombre() + "|" +
                            distribucion.getPersonaFisica().getNombre()+ "|" +
                            distribucion.getFechaDistribucion() + "|" +
                            distribucion.getCantViandasAMover()
            );
        }
        return contador;
    }

    public static Integer cantidadDeDistribucionesDeTarjetaInstanciadas(List<Object> distribucionDeTarjetas) {
        Integer contador = 0;
        for (Object o : distribucionDeTarjetas) {
            contador++;
            DistribucionDeTarjeta distribucion = (DistribucionDeTarjeta) o;
            System.out.println(
                            distribucion.getNombre()+ "|" +
                            distribucion.getPersonaFisicaQueLaRegistro().getNombre() + "|" +
                            distribucion.getFechaEntregaTarjeta()
            );
        }
        return contador;
    }
}
