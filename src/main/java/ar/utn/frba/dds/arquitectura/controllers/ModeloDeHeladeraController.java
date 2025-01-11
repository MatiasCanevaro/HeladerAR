package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.dtos.DTOModeloDeHeladera;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOModeloDeHeladera;
import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import ar.utn.frba.dds.modelos.entidades.heladeras.ModeloDeHeladera;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ModeloDeHeladeraController implements ICrudViewsHandler {
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

    public void getABMModeloDeHeladera(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "ABM Modelos De Heladera");
        context.render("admin/abm_modelos/abm_modelos_seleccionar_opcion.hbs", model);
    }
    public void postAltaModeloDeHeladera(Context context){
        String nombre = context.formParam("modelo_nuevo");
        String capacidadEnViandas = context.formParam("modelo_nuevo_capacidad_en_viandas");
        String temperaturaMinimaAceptable = context.formParam("modelo_nuevo_temp_min");
        String temperaturaMaximaAceptable = context.formParam("modelo_nuevo_temp_max");
        ModeloDeHeladera modeloDeHeladera = new ModeloDeHeladera(
                nombre,
                Integer.parseInt(capacidadEnViandas),
                Double.parseDouble(temperaturaMinimaAceptable),
                Double.parseDouble(temperaturaMaximaAceptable)
        );
        repositorio.guardar(modeloDeHeladera);

        context.redirect("/dashboard");
    }
    public void getAltaModeloDeHeladera(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Alta Modelo De Heladera");
        context.render("admin/abm_modelos/abm_modelos_alta.hbs", model);
    }
    public void getModificarModeloDeHeladeraPaso1(Context context){
        Map<String, Object> model = new HashMap<>();
        List<Object> todosLosModelos = repositorio.buscarTodos("ModeloDeHeladera");
        List<DTOModeloDeHeladera> dtosModelos = MapperDTOModeloDeHeladera.convertirListaADTOs(todosLosModelos);

        model.put("modelos",dtosModelos);
        model.put("titulo", "Modificar Modelo De Heladera Paso 1");
        context.render("admin/abm_modelos/abm_modelos_modificacion_paso1.hbs", model);
    }
    public void postModificacionModeloDeHeladeraPaso1(Context context){
        Map<String, Object> model = new HashMap<>();
        String id = context.formParam("modelo_elegido_mod");
        ModeloDeHeladera modeloDeHeladera = (ModeloDeHeladera) repositorio.buscarPorId(id, "ModeloDeHeladera");
        DTOModeloDeHeladera dtoModeloDeHeladera = MapperDTOModeloDeHeladera.convertirADTO(modeloDeHeladera);
        context.sessionAttribute("idModelo",modeloDeHeladera.getId());
        model.put("modelo",dtoModeloDeHeladera);
        model.put("titulo", "Modificar Modelo De Heladera Paso 2");
        context.render("admin/abm_modelos/abm_modelos_modificacion_paso2.hbs", model);
    }
    public void postModificacionModeloDeHeladeraPaso2(Context context){
        String nombre = context.formParam("modelo_mod");
        String capacidadEnViandas = context.formParam("modelo_capacidad_en_viandas_mod");
        String tempMax = context.formParam("modelo_temp_max_mod");
        String tempMin = context.formParam("modelo_temp_min_mod");
        String id = context.sessionAttribute("idModelo");
        ModeloDeHeladera modeloDeHeladera = (ModeloDeHeladera) repositorio.buscarPorId(id, "ModeloDeHeladera");
        modeloDeHeladera.setNombre(nombre);
        modeloDeHeladera.setCapacidadEnViandas(Integer.parseInt(capacidadEnViandas));
        modeloDeHeladera.setTemperaturaMaximaAceptable(Double.parseDouble(tempMax));
        modeloDeHeladera.setTemperaturaMinimaAceptable(Double.parseDouble(tempMin));

        repositorio.actualizar(modeloDeHeladera);
        context.redirect("/dashboard");
    }
    public void getBajaModeloDeHeladera(Context context){
        Map<String, Object> model = new HashMap<>();
        List<Object> todosLosModelos = repositorio.buscarTodos("ModeloDeHeladera");
        List<DTOModeloDeHeladera> dtosModelos = MapperDTOModeloDeHeladera.convertirListaADTOs(todosLosModelos);

        model.put("modelos",dtosModelos);
        model.put("titulo", "Baja Modelo De Heladera");
        context.render("admin/abm_modelos/abm_modelos_baja.hbs", model);
    }
    public void postBajaModeloDeHeladera(Context context){
        String id = context.formParam("modelo_elegido_baja");
        ModeloDeHeladera modeloDeHeladera = (ModeloDeHeladera) repositorio.buscarPorId(id, "ModeloDeHeladera");
        repositorio.bajaLogica(modeloDeHeladera);
        context.redirect("/dashboard");
    }
}
