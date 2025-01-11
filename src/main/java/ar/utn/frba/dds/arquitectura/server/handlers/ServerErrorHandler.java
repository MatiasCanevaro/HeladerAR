package ar.utn.frba.dds.arquitectura.server.handlers;

import ar.utn.frba.dds.arquitectura.exceptions.ImageNotSavedException;
import io.javalin.Javalin;

public class ServerErrorHandler implements IHandler {
    @Override
    public void setHandle(Javalin app) {
        app.exception(ImageNotSavedException.class, (e, context) -> {
            context.status(500);
            context.render("errores_http/500.hbs");
        });
    }
}
