package ar.utn.frba.dds.modelos.entidades.rolesYPermisos;

import io.javalin.security.RouteRole;

public enum TipoRol implements RouteRole {
    OPEN,
    PERSONA_FISICA,
    PERSONA_JURIDICA,
    ADMIN,
    TECNICO
}
