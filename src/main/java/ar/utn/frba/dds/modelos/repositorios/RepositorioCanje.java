package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Canje;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;

import java.util.Collections;
import java.util.List;

public class RepositorioCanje extends Repositorio{

    public List<Canje> buscarCanjePorPersonaFisica(PersonaFisica personaFisica) {
        return entityManager()
                .createQuery("from Canje c where c.personaFisicaQueCanjeo = :personaFisica", Canje.class)
                .setParameter("personaFisica", personaFisica)
                .getResultList();
    }

    public List<Canje> buscarCanjesPorPersonaJuridica(PersonaJuridica personaJuridica) {
        return entityManager()
                .createQuery("from Canje c where c.personaJuridicaQueCanjeo = :personaJuridica", Canje.class)
                .setParameter("personaJuridica", personaJuridica)
                .getResultList();
    }

}
