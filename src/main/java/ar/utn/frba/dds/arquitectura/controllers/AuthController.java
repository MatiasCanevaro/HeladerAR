package ar.utn.frba.dds.arquitectura.controllers;

import io.javalin.http.Context;

public class AuthController {
    public void login(Context context) {
        // Verifica las credenciales del usuario
        String userId = "user123"; // Ejemplo de ID de usuario
        String tipoRol = "ADMIN"; // Ejemplo de rol de usuario

        // Guarda la información en la sesión
        context.sessionAttribute("userId", userId);
        context.sessionAttribute("tipoRol", tipoRol);

        // Redirige al usuario a la página de inicio
        context.redirect("/home");
    }

    public void logout(Context context) {
        // Invalida la sesión
        context.req().getSession().invalidate();

        // Redirige al usuario a la página de inicio de sesión
        context.redirect("/login");
    }
}