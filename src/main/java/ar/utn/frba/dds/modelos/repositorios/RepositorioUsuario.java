package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.contacto.Usuario;

import java.util.List;

public class RepositorioUsuario extends Repositorio{
    public Usuario buscarPorEmailYContrasenia(String correoElectronico, String contrasenia) {
        List<Usuario> usuarios = entityManager()
                .createQuery("from Usuario u where u.correoElectronico = :correo_electronico and u.contrasenia = :contrasenia", Usuario.class)
                .setParameter("correo_electronico", correoElectronico)
                .setParameter("contrasenia", contrasenia)
                .getResultList();

        return usuarios.isEmpty() ? null : usuarios.get(0);
    }
    public Usuario buscarPorEmail(String correoElectronico) {
        List<Usuario> usuarios = entityManager()
                .createQuery("from Usuario u where u.correoElectronico = :correo_electronico", Usuario.class)
                .setParameter("correo_electronico", correoElectronico)
                .getResultList();

        return usuarios.isEmpty() ? null : usuarios.get(0);
    }
}
