package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.contacto.Administrador;
import ar.utn.frba.dds.modelos.entidades.contacto.Usuario;
import ar.utn.frba.dds.modelos.entidades.csv.CSV;

import java.util.List;

public class RepositorioCSV extends Repositorio{
    public List<CSV> buscarCSVsPorAdministrador(Administrador administrador) {
        return entityManager()
                .createQuery("from CSV c where c.administrador = :administrador", CSV.class)
                .setParameter("administrador", administrador)
                .getResultList();
    }
}
