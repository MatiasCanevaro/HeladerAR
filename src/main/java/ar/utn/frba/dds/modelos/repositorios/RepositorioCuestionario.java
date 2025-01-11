package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.cuestionarios.Cuestionario;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class RepositorioCuestionario extends Repositorio {

    public Cuestionario buscarActivo(List<TipoRol> roles) {
        List<Cuestionario> resultados = entityManager().createQuery(
                        "SELECT c FROM Cuestionario c JOIN c.rolesQueAcepta r WHERE c.activo = true AND r IN :roles GROUP BY c HAVING COUNT(r) = :rolesSize", Cuestionario.class)
                .setParameter("roles", roles)
                .setParameter("rolesSize", (long) roles.size())
                .getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }
}