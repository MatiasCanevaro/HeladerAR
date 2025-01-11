package ar.utn.frba.dds.arquitectura.middlewares;

import ar.utn.frba.dds.arquitectura.exceptions.AccessDeniedException;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class AuthMiddleware {
    public static void apply(Javalin app) {
        app.beforeMatched(ctx -> {
            var rol = getUserRoleType(ctx);

            if (!ctx.routeRoles().isEmpty() && !ctx.routeRoles().contains(rol)) {
                throw new AccessDeniedException();
            }
        });
    }

    private static TipoRol getUserRoleType(Context context) {
        String tipoRolStr = context.sessionAttribute("tipoRol");

        if (tipoRolStr == null) {
            context.sessionAttribute("tipoRol","OPEN");
            return TipoRol.OPEN; // Retorna el rol OPEN si no hay rol en la sesión
        }

        return switch (tipoRolStr) {
            case "PERSONA_FISICA" -> TipoRol.PERSONA_FISICA;
            case "PERSONA_JURIDICA" -> TipoRol.PERSONA_JURIDICA;
            case "ADMIN" -> TipoRol.ADMIN;
            case "TECNICO" -> TipoRol.TECNICO;
            default -> TipoRol.OPEN;
        };
    }
}
