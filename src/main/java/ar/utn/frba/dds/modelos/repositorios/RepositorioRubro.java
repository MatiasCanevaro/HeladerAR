package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Rubro;

import java.util.List;

public class RepositorioRubro extends Repositorio {
    public Rubro buscarPorNombre(String nombre) {
        List<Rubro> resultados = entityManager()
                .createQuery("from Rubro t where t.nombre = :nombre", Rubro.class)
                .setParameter("nombre", nombre)
                .getResultList();

        return resultados.isEmpty() ? null : resultados.get(0);
    }
}
