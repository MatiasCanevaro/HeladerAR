package ar.utn.frba.dds.arquitectura.utils;

import ar.utn.frba.dds.arquitectura.dtos.DTOErroresValidacion;
import ar.utn.frba.dds.modelos.entidades.contacto.Usuario;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;
import io.javalin.http.Context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SessionUtils {
    public static Boolean estaLogueado(Context context) {
        String userId = context.sessionAttribute("userId");
        //Boolean registroIncompleto = context.sessionAttribute("registroIncompleto");
        return userId != null; //|| (registroIncompleto == null || !registroIncompleto);
    }

    public static void finalizoRegistro(Context ctx) {
        Boolean registroIncompleto = ctx.sessionAttribute("registroIncompleto");

        Map<String, Object> sessionAttributes = ctx.attribute("sessionAttributes");
        if (sessionAttributes == null) {
            sessionAttributes = new HashMap<>();
        }

        if (registroIncompleto != null && registroIncompleto) {
            ctx.sessionAttribute("userId", null);
            sessionAttributes.put("usuarioLogueado", false);
            ctx.attribute("sessionAttributes", sessionAttributes);
        }
    }

    public static void mostrarPantallaDeExito(Context context, String mensaje, String nombreRuta, String ruta) {
        context.sessionAttribute("mensaje", mensaje);
        context.sessionAttribute("ruta", ruta);
        context.sessionAttribute("nombreRuta", nombreRuta);
        context.redirect("/exito");
    }

    public static Boolean esPersonaJuridica(Context ctx) {
        String rol = ctx.sessionAttribute("tipoRol");
        System.out.println("Mi rol es: " + rol);
        return Objects.equals(rol, "PERSONA_JURIDICA");
    }

    public static Boolean esTecnico(Context ctx) {
        String rol = ctx.sessionAttribute("tipoRol");
        System.out.println("Mi rol es: " + rol);
        return Objects.equals(rol, "TECNICO");
    }

    public static Boolean esPersonaFisica(Context ctx) {
        String rol = ctx.sessionAttribute("tipoRol");
        return rol != null && rol == "PERSONA_FISICA";
    }

}