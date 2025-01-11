package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import ar.utn.frba.dds.arquitectura.utils.SessionUtils;
import ar.utn.frba.dds.modelos.entidades.contacto.Contacto;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class PersonaFisicaController implements ICrudViewsHandler {
    @Override
    public void index(Context context) {
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "ABM Persona Física");
        context.render("admin/abm_personas/persona_fisica/abm_personas_fisicas.hbs", model);
    }
    public void esPersonaFisica(Context context) {
        boolean esPersonaFisica = SessionUtils.esPersonaFisica(context);
        Map<String, Boolean> response = new HashMap<>();
        response.put("esPersonaFisica", esPersonaFisica);
        context.json(response);
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

    public void getModificarFormulario(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Modificar Formulario Persona Física");
        context.render("admin/abm_formularios/abm_form_pf.hbs", model);
    }
}
