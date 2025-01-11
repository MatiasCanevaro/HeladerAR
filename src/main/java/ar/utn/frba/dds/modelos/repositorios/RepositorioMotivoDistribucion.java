package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.MotivoDistribucion;
import ar.utn.frba.dds.modelos.entidades.geografia.Provincia;

import java.util.List;

public class RepositorioMotivoDistribucion extends Repositorio{
    public MotivoDistribucion buscarPorNombre(String nombre) {
        List<MotivoDistribucion> resultados = entityManager()
                .createQuery("from MotivoDistribucion t where t.nombre = :nombre", MotivoDistribucion.class)
                .setParameter("nombre", nombre)
                .getResultList();

        return resultados.isEmpty() ? null : resultados.get(0);
    }
}
