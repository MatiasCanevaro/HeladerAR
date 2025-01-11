package ar.utn.frba.dds.arquitectura.server.handlers;

import ar.utn.frba.dds.arquitectura.exceptions.AccessDeniedException;
import io.javalin.Javalin;

public class AccessDeniedHandler implements IHandler {
    @Override
    public void setHandle(Javalin app) {
        app.exception(AccessDeniedException.class, (e, context) -> {
            context.status(401);
            context.render("errores_http/401.hbs");
        });
    }
}
