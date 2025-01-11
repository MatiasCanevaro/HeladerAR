package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.geografia.Ciudad;
import ar.utn.frba.dds.modelos.entidades.heladeras.ModeloDeHeladera;

import java.util.List;

public class RepositorioModeloHeladera extends Repositorio{
    public ModeloDeHeladera buscarPorNombre(String nombre) {
        List<ModeloDeHeladera> resultados = entityManager()
                .createQuery("from ModeloDeHeladera t where t.nombre = :nombre", ModeloDeHeladera.class)
                .setParameter("nombre", nombre)
                .getResultList();

        return resultados.isEmpty() ? null : resultados.get(0);
    }
}
