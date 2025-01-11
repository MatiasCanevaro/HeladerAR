package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import ar.utn.frba.dds.modelos.entidades.cuestionarios.Cuestionario;
import ar.utn.frba.dds.modelos.entidades.cuestionarios.Opcion;
import ar.utn.frba.dds.modelos.entidades.cuestionarios.Pregunta;
import ar.utn.frba.dds.modelos.entidades.cuestionarios.TipoPregunta;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import ar.utn.frba.dds.modelos.repositorios.RepositorioCuestionario;
import ar.utn.frba.dds.modulos.importador.MapperImportador;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
public class CuestionarioController implements ICrudViewsHandler {
    private Repositorio repositorio;
    private RepositorioCuestionario repositorioCuestionario;
    private MapperImportador mapperImportador;
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

    public void getABMFormulario(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "ABM Formulario");
        context.render("admin/abm_formularios/abm_formularios_seleccionar_opcion.hbs", model);
    }
    public void getAltaFormulario(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Alta de Formulario");
        context.render("admin/abm_formularios/abm_formularios_alta.hbs", model);
    }
    public void postAltaFormulario(Context context){
        Map<String, Object> model = new HashMap<>();
        String nombre = context.formParam("cuestionario_nuevo");
        String rolPregunta = context.formParam("rol_pregunta");
        Cuestionario cuestionario = new Cuestionario();
        context.sessionAttribute("cuestionarioId",cuestionario.getId());
        cuestionario.setNombre(nombre);

        if(cuestionario!=null){
            List<TipoRol> rol = new ArrayList<>();
            if(rolPregunta=="PERSONA_FISICA"){
                rol.add(TipoRol.PERSONA_FISICA);
                Cuestionario cuestionarioActivo = repositorioCuestionario.buscarActivo(rol);
                cuestionarioActivo.setActivo(false);
            }
            if(rolPregunta=="PERSONA_JURIDICA"){
                rol.add(TipoRol.PERSONA_JURIDICA);
                Cuestionario cuestionarioActivo = repositorioCuestionario.buscarActivo(rol);
                cuestionarioActivo.setActivo(false);
            }
            else{
                rol.add(TipoRol.PERSONA_JURIDICA);
                Cuestionario cuestionarioActivo = repositorioCuestionario.buscarActivo(rol);
                if(cuestionarioActivo!=null){
                    cuestionarioActivo.setActivo(false);
                }
                rol.remove(TipoRol.PERSONA_JURIDICA);
                rol.add(TipoRol.PERSONA_JURIDICA);
                cuestionarioActivo = repositorioCuestionario.buscarActivo(rol);
                if(cuestionarioActivo!=null){
                    cuestionarioActivo.setActivo(false);
                }
            }
        }

        if(Objects.equals(rolPregunta, "Ambos")) {
                List<TipoRol> roles = new ArrayList<>();
                roles.add(TipoRol.PERSONA_FISICA);
                roles.add(TipoRol.PERSONA_JURIDICA);
                cuestionario.setRolesQueAcepta(roles);
        }else{
            TipoRol tipoRol = mapperImportador.tipoRolMapper(rolPregunta);
            cuestionario.agregarRol(tipoRol);
        }
        repositorio.guardar(cuestionario);

        context.redirect("/dashboard/formularios/alta/preguntas");
    }

    public void getAltaFormularioPreguntas(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Alta de Formulario - Preguntas");
        context.render("admin/abm_formularios/abm_formularios_alta2.hbs", model);
    }

    public void postAltaFormularioPreguntas(Context context) {
        String cuestionarioId = context.sessionAttribute("cuestionarioId");
        String descripcion = context.formParam("pregunta_nueva");
        String tipoPregunta = context.formParam("tipo_pregunta");
        List<String> opciones = context.formParams("opciones[]");
        String finalizarOAgregarPregunta = context.formParam("submit_action");

        Cuestionario cuestionario = (Cuestionario) repositorio.buscarPorId(cuestionarioId,"Cuestionario");
        TipoPregunta tipoPreguntaMapeado = mapperImportador.tipoPreguntaMapper(tipoPregunta);
        Pregunta pregunta = new Pregunta(descripcion,tipoPreguntaMapeado,null);
        cuestionario.agregarPregunta(pregunta);
        repositorio.guardar(pregunta);
        repositorio.guardar(cuestionario);
        if ("MULTIPLE".equals(tipoPregunta) || "UNICA".equals(tipoPregunta)) {
            for (String opcion : opciones) {
                Opcion nuevaOpcion = new Opcion(opcion);
                pregunta.agregarOpcion(nuevaOpcion);
                repositorio.guardar(nuevaOpcion);
            }
        }

        if(finalizarOAgregarPregunta.equals("finalizar")){
            context.redirect("/dashboard/formularios");
        }
        else{
            context.sessionAttribute("cuestionarioId",cuestionarioId);
            context.redirect("/dashboard/formularios/alta/preguntas");
        }
    }

    public void getModificarFormulario(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Modificar Formulario");
        context.render("admin/abm_formularios/abm_formularios_modificacion.hbs", model);
    }
    public void getBajaFormulario(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Baja Formulario");
        context.render("admin/abm_formularios/abm_formularios_baja.hbs", model);
    }
}
