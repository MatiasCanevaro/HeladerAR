package ar.utn.frba.dds.modelos.entidades.suscripciones;

import ar.utn.frba.dds.modelos.entidades.geografia.Ciudad;
import ar.utn.frba.dds.modelos.entidades.geografia.Direccion;
import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.repositorios.RepositorioHeladera;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BuscadorHeladeraMasCercana {
    private RepositorioHeladera repositorioHeladera;
    public List<Heladera> buscarHeladerasConEspacioMasCercanas(
            Heladera heladeraOrigen, Integer cantidadMinimaDeViandas, Integer cantidadDeHeladerasARetornar) {
        Ciudad ciudadOrigen = heladeraOrigen.obtenerCiudad();
        List<Heladera> heladerasEnMismaCiudad = repositorioHeladera.buscarTodos("Heladera")
                .stream()
                .filter(heladera -> heladera instanceof Heladera)
                .map(heladera -> (Heladera) heladera)
                .filter(heladera -> heladera.obtenerCiudad().equals(ciudadOrigen)
                        && heladera.cantidadDeEspacioDisponibleEnViandas() >= cantidadMinimaDeViandas)
                .toList();

        return heladerasEnMismaCiudad.stream()
                .filter(heladera -> !heladera.equals(heladeraOrigen))
                .sorted(Comparator.comparingDouble(
                        h -> heladeraOrigen.getPuntoEstrategico().calcularDistanciaEnMetrosHasta(h.obtenerCiudad().getCentroDeLaCiudad())))
                .limit(cantidadDeHeladerasARetornar)
                .collect(Collectors.toList());
    }
    public static List<Heladera> buscarHeladerasMasCercanas(
            List<Heladera> todasLasHeladeras,PuntoEstrategico puntoOrigen, Integer cantidadDeHeladerasARetornar) {
        Direccion direccion = puntoOrigen.getDireccion();
        Ciudad ciudadOrigen = direccion.getCiudad();
        List<Heladera> heladerasEnMismaCiudad = todasLasHeladeras
                .stream()
                .filter(Objects::nonNull)
                .filter(heladera -> heladera.obtenerCiudad().equals(ciudadOrigen))
                .toList();

        return heladerasEnMismaCiudad.stream()
                .sorted(Comparator.comparingDouble(
                        h -> puntoOrigen.calcularDistanciaEnMetrosHasta(h.obtenerCiudad().getCentroDeLaCiudad())))
                .limit(cantidadDeHeladerasARetornar)
                .collect(Collectors.toList());
    }
    public static List<Heladera> buscarHeladerasMasCercanasPorPuntoEstrategico(
            List<Heladera> todasLasHeladeras,PuntoEstrategico puntoOrigen, Integer cantidadDeHeladerasARetornar) {
            return todasLasHeladeras.stream()
                    .sorted(Comparator.comparingDouble(
                            h -> puntoOrigen.calcularDistanciaEnMetrosHasta(h.getPuntoEstrategico())))
                    .limit(cantidadDeHeladerasARetornar)
                    .collect(Collectors.toList());
    }
}
