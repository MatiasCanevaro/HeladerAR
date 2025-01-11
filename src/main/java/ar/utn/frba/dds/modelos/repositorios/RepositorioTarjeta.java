package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.suscripciones.Suscripcion;
import ar.utn.frba.dds.modelos.entidades.tarjetas.Tarjeta;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositorioTarjeta extends Repositorio {


    public Tarjeta buscar(PersonaFisica personaFisica) {
        return entityManager()
                .createQuery("from Tarjeta t where t.personaFisica = :personaFisica ", Tarjeta.class)
                .setParameter("personaFisica", personaFisica)
                .getSingleResult();
    }
    public Tarjeta buscar(String codigoAlfanumerico) {
        Tarjeta resultado = null;
        try {
            resultado = entityManager()
                    .createQuery("from Tarjeta t where t.codigoAlfanumerico = :codigoAlfanumerico", Tarjeta.class)
                    .setParameter("codigoAlfanumerico", codigoAlfanumerico)
                    .getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No se encontró ninguna tarjeta para el código: " + codigoAlfanumerico);
        } catch (Exception e) {
            System.out.println("Error al buscar por código alfanumérico: " + e.getMessage());
            e.printStackTrace();
        }

        return resultado;
    }

}
