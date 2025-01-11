package ar.utn.frba.dds.arquitectura.server.handlers;

import ar.utn.frba.dds.arquitectura.exceptions.UsuarioNoAutenticadoException;
import io.javalin.Javalin;

public class UsuarioNoAutenticadoHandler implements IHandler{

    @Override
    public void setHandle(Javalin app) {
        app.exception(UsuarioNoAutenticadoException.class,(e, context) -> {
            context.redirect("/login");
        });
    }
}
