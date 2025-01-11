package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Oferta;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;

public class Repositorio implements WithSimplePersistenceUnit {
public Object buscarPorId(String id, String nombreDeClase) {
    List<Object> resultados = null;
    try {
        resultados = entityManager()
                .createQuery("from " + nombreDeClase + " t where t.id = :id", Object.class)
                .setParameter("id", id)
                .getResultList();
    } catch (Exception e) {
        System.out.println("Error al buscar por ID: " + e.getMessage());
        e.printStackTrace();
    }

    if (resultados == null || resultados.isEmpty()) {
        System.out.println("No se encontró ningún resultado para ID: " + id);
        return null;
    }

    return resultados.get(0);
}

    public List<Object> buscarTodos(String nombreDeClase) {
        return entityManager()
                .createQuery("from " + nombreDeClase + " t where t.activo = :activo", Object.class)
                .setParameter("activo", true)
                .getResultList();
    }

    public void guardar(Object o) {
        withTransaction(() -> {
            entityManager().persist(o);
        });
    }

    public void limpiar() {
        withTransaction(() -> {
            entityManager().createNativeQuery("SET FOREIGN_KEY_CHECKS=0").executeUpdate();
            List<Object> entities = entityManager().createQuery("FROM " + Persistente.class.getName()).getResultList();
            for (Object entity : entities) {
                entityManager().remove(entity);
            }
            entityManager().createNativeQuery("SET FOREIGN_KEY_CHECKS=1").executeUpdate();
        });
    }

    public void actualizar(Object o) {
        withTransaction(() -> {
            entityManager().merge(o);
        });
    }

    public void bajaLogica(Object o) {
        withTransaction(() -> {
            ((Persistente) o).setActivo(false);
            entityManager().merge(o);
        });
    }

    public <T> T buscarPorNombre(String nombre, String nombreDeClase, Class<T> tipo) {
        List<T> resultados = entityManager()
                .createQuery("from " + nombreDeClase + " t where t.nombre = :nombre", tipo)
                .setParameter("nombre", nombre)
                .getResultList();

        return resultados.isEmpty() ? null : resultados.get(0);
    }

    public PersonaJuridica buscarPersonaJuridicaQuePublicoLaOferta(Oferta oferta) {
        List<PersonaJuridica> todasLasPersonasJuridicas = (List<PersonaJuridica>) (List<?>) this.buscarTodos(PersonaJuridica.class.getSimpleName());
        for (PersonaJuridica personaJuridica : todasLasPersonasJuridicas) {
            if (personaJuridica.getOfertas().contains(oferta)) {
                return personaJuridica;
            }
        }
        return null; // Retorna null si no se encuentra ninguna persona jurídica que haya publicado la oferta
    }

}
