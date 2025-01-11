package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.dtos.DTOPersonaFisica;
import ar.utn.frba.dds.arquitectura.dtos.DTOPersonaVulnerable;
import ar.utn.frba.dds.arquitectura.dtos.DTOTarjeta;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOPersonaFisica;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOPersonaVulnerable;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOTarjeta;
import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import ar.utn.frba.dds.arquitectura.utils.SessionUtils;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.GeneradorDeCodigoAlfanumerico;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaVulnerable;
import ar.utn.frba.dds.modelos.entidades.tarjetas.Tarjeta;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import ar.utn.frba.dds.modelos.repositorios.RepositorioPersonaFisica;
import ar.utn.frba.dds.modelos.repositorios.RepositorioTarjeta;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class TarjetaController implements ICrudViewsHandler{
    Repositorio repositorio;
    RepositorioTarjeta repositorioTarjeta;

    @Override
    public void index(io.javalin.http.Context context) {
    }

    @Override
    public void show(io.javalin.http.Context context) {
    }

    @Override
    public void create(io.javalin.http.Context context) {

    }

    @Override
    public void save(io.javalin.http.Context context) {
    }

    @Override
    public void edit(io.javalin.http.Context context) {
    }

    @Override
    public void update(io.javalin.http.Context context) {

    }

    @Override
    public void delete(Context context) {

    }

    public void getABMTarjeta(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "ABM Tarjeta");
        context.render("admin/abm_tarjetas/abm_tarjetas_seleccionar_opcion.hbs", model);
    }

    //Alta tarjeta
    public void getAltaTarjeta(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Alta Tarjeta");
        context.render("admin/abm_tarjetas/abm_tarjetas_alta.hbs", model);
    }

    public void postAltaTarjeta(Context context) {
        String tipoPersona = context.formParam("tipo_persona");

        Map<String, Object> model = new HashMap<>();
        model.put("tipoPersona", tipoPersona);

        if ("PERSONA_FISICA".equals(tipoPersona)) {
            context.redirect("/dashboard/tarjetas/alta_persona_fisica");
        } else if ("PERSONA_VULNERABLE".equals(tipoPersona)) {
            context.redirect("/dashboard/tarjetas/alta_persona_vulnerable");
        } else {
            context.status(400).result("Tipo de persona no válido");
        }
    }

    public void getAltaTarjetaPaso2Fisica(Context context) {
        List<Object> personasFisicas = repositorio.buscarTodos("PersonaFisica");
        List<DTOPersonaFisica> dtoPersonaFisicas = MapperDTOPersonaFisica.convertirListaADTOs(personasFisicas);



        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Alta Tarjeta - Persona Física");
        model.put("personas", dtoPersonaFisicas);

        context.render("admin/abm_tarjetas/abm_tarjetas_alta_paso2_fisica.hbs", model);
    }

    public void getAltaTarjetaPaso2Vulnerable(Context context) {
        List<Object> personasVulnerables = repositorio.buscarTodos("PersonaVulnerable");
        List<DTOPersonaVulnerable> dtoPersonaVulnerable = MapperDTOPersonaVulnerable.convertirListaADTOs(personasVulnerables);

        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Alta Tarjeta - Persona Vulnerable");
        model.put("personas", dtoPersonaVulnerable);

        context.render("admin/abm_tarjetas/abm_tarjetas_alta_paso2_vulnerable.hbs", model);
    }

    public void postAltaTarjetaPaso2Fisica(Context context) {
        String personaId = context.formParam("personaId");
        String usoDiario = context.formParam("uso_diario");

        PersonaFisica personaFisica = (PersonaFisica) repositorio.buscarPorId(personaId, "PersonaFisica");
        Integer usoDiarioInt = Integer.parseInt(usoDiario);
        String codigo = GeneradorDeCodigoAlfanumerico.generarCodigo(repositorioTarjeta);

        Tarjeta tarjeta = new Tarjeta(codigo,null,personaFisica,null, usoDiarioInt);
        repositorio.guardar(tarjeta);
        SessionUtils.mostrarPantallaDeExito(context,
                "Tarjeta creada con éxito",
                "ABM de tarjeta",
                "/dashboard/tarjetas");
    }

    public void postAltaTarjetaPaso2Vulnerable(Context context) {
        String personaId = context.formParam("personaId");
        String usoDiario = context.formParam("uso_diario");

        PersonaVulnerable personaVulnerable = (PersonaVulnerable) repositorio.buscarPorId(personaId, "PersonaVulnerable");
        Integer usoDiarioInt = Integer.parseInt(usoDiario);
        String codigo = GeneradorDeCodigoAlfanumerico.generarCodigo(repositorioTarjeta);

        Tarjeta tarjeta = new Tarjeta(codigo,personaVulnerable,null,null, usoDiarioInt);
        repositorio.guardar(tarjeta);
        SessionUtils.mostrarPantallaDeExito(context,
                "Tarjeta creada con éxito",
                "ABM de tarjeta",
                "/dashboard/tarjetas");
    }

    //Baja tarjeta

    public void getBajaTarjeta(Context context) {
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Baja Tarjeta");
        context.render("admin/abm_tarjetas/abm_tarjetas_baja_seleccionar.hbs", model);
    }

    public void postBajaTarjeta(Context context) {
        String tipoPersona = context.formParam("tipo_persona");

        Map<String, Object> model = new HashMap<>();
        model.put("tipoPersona", tipoPersona);

        if ("PERSONA_FISICA".equals(tipoPersona)) {
            context.redirect("/dashboard/tarjetas/baja_persona_fisica");
        } else if ("PERSONA_VULNERABLE".equals(tipoPersona)) {
            context.redirect("/dashboard/tarjetas/baja_persona_vulnerable");
        } else {
            context.status(400).result("Tipo de persona no válido");
        }
    }

    public void getBajaTarjetaFisica(Context context){
        List<Object> tarjetas = repositorio.buscarTodos("Tarjeta");
        List<DTOTarjeta> dtoTarjetas = MapperDTOTarjeta.convertirListaADTOs(tarjetas);
        List<DTOTarjeta> dtoTarjetasConPersonaFisica = dtoTarjetas.stream()
                .filter(dtoTarjeta -> dtoTarjeta.getPersonaFisica() != null)
                .collect(Collectors.toList());
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Baja Tarjeta");
        model.put("tarjetas", dtoTarjetasConPersonaFisica);
        context.render("admin/abm_tarjetas/abm_tarjetas_baja_pf.hbs", model);
    }

    public void getBajaTarjetaVulnerable(Context context) {
        List<Object> tarjetas = repositorio.buscarTodos("Tarjeta");
        List<DTOTarjeta> dtoTarjetas = MapperDTOTarjeta.convertirListaADTOs(tarjetas);
        List<DTOTarjeta> dtoTarjetasConPersonaVulnerable = dtoTarjetas.stream()
                .filter(dtoTarjeta -> dtoTarjeta.getPersonaVulnerable() != null)
                .collect(Collectors.toList());
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Baja Tarjeta");
        model.put("tarjetas", dtoTarjetasConPersonaVulnerable);
        context.render("admin/abm_tarjetas/abm_tarjetas_baja_pv.hbs", model);
    }

    public void postBajaTarjetaFisica(Context context){
        String tarjetaId = context.formParam("tarjetaId");
        Tarjeta tarjeta = (Tarjeta) repositorio.buscarPorId(tarjetaId, "Tarjeta");
        repositorio.bajaLogica(tarjeta);
        SessionUtils.mostrarPantallaDeExito(context,
                "Tarjeta eliminada con éxito",
                "ABM de tarjeta",
                "/dashboard/tarjetas");
    }

    public void postBajaTarjetaVulnerable(Context context){
        String tarjetaId = context.formParam("tarjetaId");
        Tarjeta tarjeta = (Tarjeta) repositorio.buscarPorId(tarjetaId, "Tarjeta");
        repositorio.bajaLogica(tarjeta);
        SessionUtils.mostrarPantallaDeExito(context,
                "Tarjeta eliminada con éxito",
                "ABM de tarjeta",
                "/dashboard/tarjetas");
    }

    public void getModificarTarjeta(Context context) {
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Modificar Tarjeta");
        context.render("admin/abm_tarjetas/abm_tarjetas_modificacion_seleccionar.hbs", model);
    }

    public void postModificarTarjeta(Context context) {
        String tipoPersona = context.formParam("tipo_persona");

        Map<String, Object> model = new HashMap<>();
        model.put("tipoPersona", tipoPersona);

        if ("PERSONA_FISICA".equals(tipoPersona)) {
            context.redirect("/dashboard/tarjetas/modificacion_persona_fisica");
        } else if ("PERSONA_VULNERABLE".equals(tipoPersona)) {
            context.redirect("/dashboard/tarjetas/modificacion_persona_vulnerable");
        } else {
            context.status(400).result("Tipo de persona no válido");
        }
    }

    public void getModificarTarjetaFisica(Context context){
        List<Object> tarjetas = repositorio.buscarTodos("Tarjeta");
        List<DTOTarjeta> dtoTarjetas = MapperDTOTarjeta.convertirListaADTOs(tarjetas);
        List<DTOTarjeta> dtoTarjetasConPersonaFisica = dtoTarjetas.stream()
                .filter(dtoTarjeta -> dtoTarjeta.getPersonaFisica() != null)
                .collect(Collectors.toList());
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Modificar Tarjeta");
        model.put("tarjetas", dtoTarjetasConPersonaFisica);
        context.render("admin/abm_tarjetas/abm_tarjetas_modificacion_pf.hbs", model);
    }

    public void getModificarTarjetaVulnerable(Context context) {
        List<Object> tarjetas = repositorio.buscarTodos("Tarjeta");
        List<DTOTarjeta> dtoTarjetas = MapperDTOTarjeta.convertirListaADTOs(tarjetas);
        List<DTOTarjeta> dtoTarjetasConPersonaVulnerable = dtoTarjetas.stream()
                .filter(dtoTarjeta -> dtoTarjeta.getPersonaVulnerable() != null)
                .collect(Collectors.toList());
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Modificar Tarjeta");
        model.put("tarjetas", dtoTarjetasConPersonaVulnerable);
        context.render("admin/abm_tarjetas/abm_tarjetas_modificacion_pv.hbs", model);
    }

    public void postModificarTarjetaFisica(Context context) {
        String tarjetaId = context.formParam("tarjetaId");
        String usoDiario = context.formParam("uso_diario");

        Tarjeta tarjeta = (Tarjeta) repositorio.buscarPorId(tarjetaId, "Tarjeta");
        Integer usoDiarioInt = Integer.parseInt(usoDiario);
        tarjeta.setCantidadDeVecesQuePuedeSerUtilizadaPorDia(usoDiarioInt);
        repositorio.actualizar(tarjeta);

        SessionUtils.mostrarPantallaDeExito(context,
                "Tarjeta modificada con éxito",
                "ABM de tarjeta",
                "/dashboard/tarjetas");
    }

    public void postModificarTarjetaVulnerable(Context context) {
        String tarjetaId = context.formParam("tarjetaId");
        String usoDiario = context.formParam("uso_diario");

        Tarjeta tarjeta = (Tarjeta) repositorio.buscarPorId(tarjetaId, "Tarjeta");
        Integer usoDiarioInt = Integer.parseInt(usoDiario);
        tarjeta.setCantidadDeVecesQuePuedeSerUtilizadaPorDia(usoDiarioInt);
        repositorio.actualizar(tarjeta);

        SessionUtils.mostrarPantallaDeExito(context,
                "Tarjeta modificada con éxito",
                "ABM de tarjeta",
                "/dashboard/tarjetas");
    }
}
