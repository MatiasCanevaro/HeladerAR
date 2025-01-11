package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.TipoDocumento;

import java.util.ArrayList;
import java.util.List;

public class RepositorioPersonaFisica extends Repositorio {

    public PersonaFisica buscar(TipoDocumento tipoDocumento, String numeroDocumento) {
        return entityManager()
                .createQuery("from PersonaFisica p where p.tipoDocumento = :tipoDocumento " +
                        "and p.numeroDocumento = :numeroDocumento",
                        PersonaFisica.class)
                .setParameter("tipoDocumento", tipoDocumento)
                .setParameter("numeroDocumento", numeroDocumento)
                .getSingleResult();
    }
    //para solucionar el error de query did not return a unique result cuando usamos procesarCSV
    /*repositorioPersonaFisica.buscar(tipoDocumentoMapeado, numeroDocumento);
    está devolviendo más de un resultado, mientras que se espera un único resultado.*/
    public List<PersonaFisica> buscarPersonas(TipoDocumento tipoDocumento, String numeroDocumento) {
        return entityManager()
                .createQuery("from PersonaFisica p where p.tipoDocumento = :tipoDocumento " +
                                "and p.numeroDocumento = :numeroDocumento",
                        PersonaFisica.class)
                .setParameter("tipoDocumento", tipoDocumento)
                .setParameter("numeroDocumento", numeroDocumento)
                .getResultList();
    }
}
