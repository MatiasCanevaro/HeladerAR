package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.incidentes.AlertaHeladera;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import java.util.Optional;

public class RepositorioAlertaHeladera extends Repositorio {
    @SuppressWarnings("unchecked")
    public List<AlertaHeladera> buscarTodasPorTipoIncidente(String tipoIncidente) {
        return entityManager()
                .createQuery("from " + AlertaHeladera.class.getName() + " where tipoIncidente =:tipoIncidente")
                .setParameter("tipoIncidente", tipoIncidente)
                .getResultList();
    }
}
