package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class DashboardController implements ICrudViewsHandler {
    @Override
    public void index(Context context) {
        Map<String, Object> model = new HashMap<>();
        context.render("contribuciones/persona_fisica/dashboard_pf.hbs");
    }

    @Override
    public void show(Context context) {
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Dashboard Contribuciones");
        String rol = context.sessionAttribute("tipoRol");
        if (rol.equals(TipoRol.PERSONA_FISICA.name())) {
            context.render("contribuciones/persona_fisica/dashboard_pf2.hbs", model);
        } else if (rol.equals(TipoRol.PERSONA_JURIDICA.name())) {
            context.render("contribuciones/persona_juridica/dashboard_pj2.hbs", model);
        } else if (rol.equals(TipoRol.TECNICO.name())) {
            context.render("contribuciones/tecnico/dashboard_t.hbs", model);
        }
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

    public void getDashboard(@NotNull Context context) {
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Dashboard");
        String rol = context.sessionAttribute("tipoRol");
        if (rol.equals(TipoRol.PERSONA_FISICA.name())) {
            context.render("contribuciones/persona_fisica/dashboard_pf.hbs", model);
        } else if (rol.equals(TipoRol.PERSONA_JURIDICA.name())) {
            context.render("contribuciones/persona_juridica/dashboard_pj.hbs", model);
        } else if (rol.equals(TipoRol.ADMIN.name())) {
            context.render("admin/dashboard_admin.hbs", model);
        } else if (rol.equals(TipoRol.TECNICO.name())) {
            context.render("contribuciones/tecnico/dashboard_t.hbs", model);
        }
    }
}