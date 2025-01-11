package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import ar.utn.frba.dds.arquitectura.utils.SessionUtils;
import ar.utn.frba.dds.modelos.entidades.contacto.Contacto;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class ContactoController implements ICrudViewsHandler {
    private Repositorio repositorio;
    @Override
    public void index(Context context) {
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Contacto");
        //SessionUtils.finalizoRegistro(context);
        model.put("usuarioLogueado", SessionUtils.estaLogueado(context));
        context.render("generales/contact.hbs", model);
    }

    @Override
    public void show(Context context) {
    }

    @Override
    public void create(Context context) {

    }

    @Override
    public void save(Context context) {
        String nombre = context.formParam("nombre");
        String correo = context.formParam("correo");
        String mensaje = context.formParam("mensaje");

        Contacto contacto = new Contacto();
        contacto.setNombre(nombre);
        contacto.setCorreoElectronico(correo);
        contacto.setMensaje(mensaje);

        repositorio.guardar(contacto);

        context.redirect("/home");
    }

    @Override
    public void edit(Context context) {
    }

    @Override
    public void update(Context context) {

    }

    @Override
    public void delete(Context context) {

    }
}
