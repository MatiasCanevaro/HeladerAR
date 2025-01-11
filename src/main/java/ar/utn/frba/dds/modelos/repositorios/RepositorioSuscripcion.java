package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.suscripciones.Suscripcion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositorioSuscripcion extends Repositorio {
    public Suscripcion buscar(PersonaFisica personaFisica) {
        return entityManager()
                .createQuery("from Suscripcion s where s.personaFisica = :personaFisica ", Suscripcion.class)
                .setParameter("personaFisica", personaFisica)
                .getSingleResult();
    }
    public List<Suscripcion> buscarPorHeladera(Heladera heladera) {
        return entityManager()
                .createQuery("from Suscripcion s where s.heladera_id = :heladera_id ", Suscripcion.class)
                .setParameter("heladera_id", heladera.getId())
                .getResultList();
    }
}
