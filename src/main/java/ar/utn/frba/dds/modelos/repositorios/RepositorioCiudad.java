package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.geografia.Ciudad;

import java.util.List;

public class RepositorioCiudad extends Repositorio{
    public Ciudad buscarPorNombre(String nombre) {
        List<Ciudad> resultados = entityManager()
                .createQuery("from Ciudad t where t.nombre = :nombre", Ciudad.class)
                .setParameter("nombre", nombre)
                .getResultList();

        return resultados.isEmpty() ? null : resultados.get(0);
    }

    public Ciudad buscarPrimeraCiudadPorProvinciaId(String provinciaId) {
        List<Ciudad> resultados = entityManager()
                .createQuery("from Ciudad c where c.provincia.id = :provinciaId", Ciudad.class)
                .setParameter("provinciaId", provinciaId)
                .getResultList();

        return resultados.isEmpty() ? null : resultados.get(0);
    }
}
