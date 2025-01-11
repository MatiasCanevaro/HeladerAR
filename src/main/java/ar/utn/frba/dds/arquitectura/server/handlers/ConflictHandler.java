package ar.utn.frba.dds.arquitectura.server.handlers;

import ar.utn.frba.dds.arquitectura.exceptions.ConflictException;
import io.javalin.Javalin;

public class ConflictHandler implements IHandler {
    @Override
    public void setHandle(Javalin app) {
        app.exception(ConflictException.class,(e, context) -> {
            context.status(409);
            context.render("errores_http/409.hbs");
        });
    }
}
