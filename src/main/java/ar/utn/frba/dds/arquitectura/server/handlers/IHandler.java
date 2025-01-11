package ar.utn.frba.dds.arquitectura.server.handlers;

import io.javalin.Javalin;

public interface IHandler {
    void setHandle(Javalin app);
}
