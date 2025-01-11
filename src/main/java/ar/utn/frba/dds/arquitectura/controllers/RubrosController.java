package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.dtos.DTOModeloDeHeladera;
import ar.utn.frba.dds.arquitectura.dtos.DTORubro;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOModeloDeHeladera;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTORubro;
import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import ar.utn.frba.dds.arquitectura.utils.SessionUtils;
import ar.utn.frba.dds.modelos.entidades.contacto.Contacto;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Rubro;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class RubrosController implements ICrudViewsHandler {
    private Repositorio repositorio;
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

    public void getABMRubros(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "ABM Rubros");
        context.render("admin/abm_rubros/abm_rubros_seleccionar_opcion.hbs", model);
    }
    public void getAltaRubro(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Alta Rubro");
        context.render("admin/abm_rubros/abm_rubros_alta.hbs", model);
    }
    public void postAltaRubro(Context context){
        String nombreRubro = context.formParam("rubro_nuevo");
        Rubro rubro = new Rubro(nombreRubro);
        repositorio.guardar(rubro);

        context.redirect("/dashboard");
    }
    public void getModificarRubroPaso1(Context context){
        Map<String, Object> model = new HashMap<>();
        List<Object> todosLosRubros = repositorio.buscarTodos("Rubro");
        List<DTORubro> dtosRubros = MapperDTORubro.convertirListaADTOs(todosLosRubros);

        model.put("rubros",dtosRubros);
        model.put("titulo", "Modificar Rubro Paso 1");
        context.render("admin/abm_rubros/abm_rubros_modificacion_paso1.hbs", model);
    }
    public void postModificacionRubroPaso1(Context context){
        Map<String, Object> model = new HashMap<>();
        String idRubro = context.formParam("rubro_elegido_mod");
        Rubro rubro = (Rubro) repositorio.buscarPorId(idRubro, "Rubro");
        DTORubro dtoRubro = MapperDTORubro.convertirADTO(rubro);
        context.sessionAttribute("idRubro",rubro.getId());
        model.put("rubro",dtoRubro);
        model.put("titulo", "Modificar Rubro Paso 2");
        context.render("admin/abm_rubros/abm_rubros_modificacion_paso2.hbs", model);
    }
    public void postModificacionRubroPaso2(Context context){
        String nuevoNombre = context.formParam("rubro_nombre_mod");
        String id = context.sessionAttribute("idRubro");
        Rubro rubro = (Rubro) repositorio.buscarPorId(id, "Rubro");
        rubro.setNombre(nuevoNombre);
        repositorio.actualizar(rubro);
        context.redirect("/dashboard");
    }
    public void getBajaRubro(Context context){
        Map<String, Object> model = new HashMap<>();
        List<Object> todosLosRubros = repositorio.buscarTodos("Rubro");
        List<DTORubro> dtosRubros = MapperDTORubro.convertirListaADTOs(todosLosRubros);

        model.put("rubros",dtosRubros);
        model.put("titulo", "Baja Rubro");
        context.render("admin/abm_rubros/abm_rubros_baja.hbs", model);
    }
    public void postBajaRubro(Context context){
        String idRubro = context.formParam("rubro_baja");
        Rubro rubro = (Rubro) repositorio.buscarPorId(idRubro, "Rubro");
        repositorio.bajaLogica(rubro);
        context.redirect("/dashboard");
    }
}
