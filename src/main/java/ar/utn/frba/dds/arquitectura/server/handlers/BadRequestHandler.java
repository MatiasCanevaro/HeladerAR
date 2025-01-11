package ar.utn.frba.dds.arquitectura.server.handlers;

import ar.utn.frba.dds.arquitectura.exceptions.AccessDeniedException;
import ar.utn.frba.dds.arquitectura.exceptions.BadRequestException;
import io.javalin.Javalin;

public class BadRequestHandler implements IHandler {
    @Override
    public void setHandle(Javalin app) {
        app.exception(BadRequestException.class, (e, context) -> {
            context.status(400);
            context.render("errores_http/400.hbs");
        });
    }
}
