package ar.utn.frba.dds.arquitectura.server.handlers;

import io.javalin.Javalin;
import io.javalin.http.ConflictResponse;

import java.rmi.ServerError;
import java.util.Arrays;

public class AppHandlers {
    private final IHandler[] handlers = new IHandler[]{
            new AccessDeniedHandler(),
            new BadRequestHandler(),
            new ServerErrorHandler(),
            new UsuarioNoAutenticadoHandler(),
            new ConflictHandler()
    };

    public static void applyHandlers(Javalin app) {
        Arrays.stream(new AppHandlers().handlers).toList().forEach(handler -> handler.setHandle(app));
    }
}
