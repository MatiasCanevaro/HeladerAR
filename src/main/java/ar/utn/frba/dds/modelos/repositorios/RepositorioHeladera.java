package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.incidentes.AlertaHeladera;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RepositorioHeladera extends Repositorio {
    public List<String> buscarTodosLosModelos() {
        return entityManager()
                .createQuery("SELECT DISTINCT h.modelo FROM " + Heladera.class.getName() + " h", String.class)
                .getResultList();
    }

    public Heladera buscarHeladeraPorNombrePuntoEstrategico(String nombrePuntoEstrategico) {
        return entityManager().createQuery(
                "SELECT h FROM " + Heladera.class.getName() + " h WHERE h.puntoEstrategico.nombre =:nombrePuntoEstrategico", Heladera.class)
                .setParameter("nombrePuntoEstrategico", nombrePuntoEstrategico)
                .getSingleResult();
    }

    public List<Heladera> buscarTodasLasHeladerasActivas(){
        return entityManager()
                .createQuery("from " + Heladera.class.getName() + " where estaActiva =:estaActiva")
                .setParameter("estaActiva", true)
                .getResultList();
    }
}
