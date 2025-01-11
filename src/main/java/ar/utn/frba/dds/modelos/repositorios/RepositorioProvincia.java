package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.geografia.Provincia;

import java.util.List;

public class RepositorioProvincia extends Repositorio{
    public Provincia buscarPorNombre(String nombre) {
        List<Provincia> resultados = entityManager()
                .createQuery("from Provincia t where t.nombre = :nombre", Provincia.class)
                .setParameter("nombre", nombre)
                .getResultList();

        return resultados.isEmpty() ? null : resultados.get(0);
    }
}
