package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;

import io.javalin.http.Context;


public class LogoutController implements ICrudViewsHandler {
    @Override
    public void index(Context context) {

    }

    @Override
    public void show(Context context) {
    }

    @Override
    public void create(Context context) {

    }

    @Override
    public void save(Context context) {

    }

    @Override
    public void edit(Context context) {
    }

    @Override
    public void update(Context context) {

    }

    @Override
    public void delete(Context context) {

    }

    public void logout(Context context) {
        context.sessionAttribute("userId", null);
        context.sessionAttribute("tipoRol", null);
        context.redirect("/login");
    }
}
