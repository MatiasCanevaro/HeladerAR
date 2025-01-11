package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.cuestionarios.Respuesta;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;

import java.util.List;

public class RepositorioRespuesta extends Repositorio {
    public List<Respuesta> buscarPorPersonaFisica(PersonaFisica personaFisica, String entityName) {
        return entityManager()
                .createQuery("from " + entityName + " r where r.personaFisica = :personaFisica", Respuesta.class)
                .setParameter("personaFisica", personaFisica)
                .getResultList();
    }
    public List<Respuesta> buscarPorPersonaJuridica(PersonaJuridica personaJuridica, String entityName) {
        return entityManager()
                .createQuery("from " + entityName + " r where r.personaJuridica = :personaJuridica", Respuesta.class)
                .setParameter("personaJuridica", personaJuridica)
                .getResultList();
    }
}