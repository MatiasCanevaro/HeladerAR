package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.dtos.*;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOAlertasHeladera;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOHeladera;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOPersonaJuridica;
import ar.utn.frba.dds.arquitectura.utils.SessionUtils;
import ar.utn.frba.dds.modelos.entidades.geografia.Direccion;
import ar.utn.frba.dds.modelos.entidades.geografia.Provincia;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.heladeras.ModeloDeHeladera;
import ar.utn.frba.dds.modelos.entidades.incidentes.AlertaHeladera;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import ar.utn.frba.dds.modelos.repositorios.RepositorioHeladera;
import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class HeladerasController implements ICrudViewsHandler {
    private RepositorioHeladera repositorioHeladera;
    private Repositorio repositorio;
    @Override
    public void index(Context context) {
        //PRETENDE DEVOLVER UNA VISTA QUE CONTENGA A TODAS LOS HELADERAS ALMACENADAS EN MI SISTEMA
        List<Object> heladeras = this.repositorioHeladera.buscarTodos(Heladera.class.getName());


        List<DTOHeladera> dtosHeladera = MapperDTOHeladera.convertirListaADTOs(heladeras);


        Map<String, Object> model = new HashMap<>();
        model.put("heladeras", dtosHeladera);
        model.put("titulo", "Donacion de Dinero");
        context.render("contribuciones/donacion_dinero.hbs", model);
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

    public void getVerAlertasHeladeras(Context context){
        Map<String, Object> model = new HashMap<>();

        List<Object> alertas = repositorio.buscarTodos("AlertaHeladera");

        List<DTOAlertasHeladera> dtoTodasAlertasHeladeras = alertas.stream()
                .map(MapperDTOAlertasHeladera::convertirADTO)
                .toList();

        List<DTOAlertasHeladera> dtoAlertasHeladeras = dtoTodasAlertasHeladeras.stream()
                .filter(dtoAlertaHeladera -> !dtoAlertaHeladera.getEstaSolucionado())
                .toList();

        String id = context.sessionAttribute("userId");
        PersonaJuridica personaJuridica = (PersonaJuridica) repositorio.buscarPorId(id, "PersonaJuridica");
        List<Heladera> misHeladeras = personaJuridica.getHeladeras();

        List<DTOAlertasHeladera> dtoMisAlertasHeladeras = dtoAlertasHeladeras.stream()
                .filter(alerta -> misHeladeras.contains(alerta.getHeladera()))
                .toList();

        if (dtoMisAlertasHeladeras.isEmpty()) {
            model.put("mensaje", "No tenés heladeras con alertas activas");
        }else{
            List<DTODisplayAlertasHeladera> listaDTODisplayAlertasHeladera = new ArrayList<>();
            for(DTOAlertasHeladera dtoAlertaHeladera : dtoMisAlertasHeladeras) {
                String direccion = dtoAlertaHeladera.getPuntoEstrategico().getDireccion().getCalle() + " "
                        + dtoAlertaHeladera.getPuntoEstrategico().getDireccion().getAltura();
                if (dtoAlertaHeladera.getPuntoEstrategico().getDireccion().getPiso() != null) {
                    direccion += " " + dtoAlertaHeladera.getPuntoEstrategico().getDireccion().getPiso() + "°";
                }

                LocalDateTime ultima_vez_activa = dtoAlertaHeladera.getHeladera().getFechasYHorasDejoDeEstarActiva().stream().max(LocalDateTime::compareTo).orElse(null);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime ultima_vez_activa_truncada = ultima_vez_activa.truncatedTo(ChronoUnit.SECONDS);
                String fechaFormateada = ultima_vez_activa_truncada.format(formatter);
                
                assert ultima_vez_activa != null;
                DTODisplayAlertasHeladera DTODisplayAlertasHeladera = new DTODisplayAlertasHeladera(
                        dtoAlertaHeladera.getId(),
                        dtoAlertaHeladera.getPuntoEstrategico().getDireccion().getCiudad().getProvincia(),
                        direccion,
                        dtoAlertaHeladera.getHeladera().getModelo(),
                        dtoAlertaHeladera.getTipoIncidente().toString(),
                        fechaFormateada);
                listaDTODisplayAlertasHeladera.add(DTODisplayAlertasHeladera);
            }
            model.put("heladeras", listaDTODisplayAlertasHeladera);
        }

        LocalDateTime ahora = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaFormateada = ahora.format(formatter);

        model.put("fecha_y_hora", fechaFormateada);
        model.put("titulo", "Consulta tus alertas");
        context.render("contribuciones/persona_juridica/ver_alertas_heladeras.hbs", model);
    }

    public void getVerHeladerasActivas(Context context){
        Map<String, Object> model = new HashMap<>();

        List<Object> heladeras = repositorioHeladera.buscarTodos("Heladera");
        List<DTOHeladera> dtoTodasLasHeladeras = heladeras.stream()
                .map(MapperDTOHeladera::convertirADTO)
                .toList();

        List<DTOHeladera> dtoHeladeras = dtoTodasLasHeladeras.stream()
                .filter(dtoHeladera -> dtoHeladera.getEstaActiva())
                .toList();

        List<DTODisplayConsultarHeladerasActivas> listaDTODisplayConsultarHeladerasActivas = new ArrayList<>();

        for(DTOHeladera dtoHeladera : dtoHeladeras) {
            String direccion = dtoHeladera.getPuntoEstrategico().getDireccion().getCalle() + " "
                    + dtoHeladera.getPuntoEstrategico().getDireccion().getAltura();
            if (dtoHeladera.getPuntoEstrategico().getDireccion().getPiso() != null) {
                direccion += " " + dtoHeladera.getPuntoEstrategico().getDireccion().getPiso() + "°";
            }

            LocalDateTime activa_desde = dtoHeladera.getFechasYHorasVolvioAEstarActiva().stream().max(LocalDateTime::compareTo).orElse(null);

            assert activa_desde != null;
            DTODisplayConsultarHeladerasActivas DTODisplayConsultarHeladerasActivas = new DTODisplayConsultarHeladerasActivas(dtoHeladera.getId(),
                    dtoHeladera.getPuntoEstrategico().getDireccion().getCiudad().getProvincia(),
                    direccion,
                    dtoHeladera.getModelo(),
                    activa_desde.truncatedTo(ChronoUnit.SECONDS));
            listaDTODisplayConsultarHeladerasActivas.add(DTODisplayConsultarHeladerasActivas);
        }

        LocalDateTime ahora = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaFormateada = ahora.format(formatter);

        model.put("fecha_y_hora", fechaFormateada);
        model.put("titulo", "Consulta heladeras activas");
        model.put("heladeras", listaDTODisplayConsultarHeladerasActivas);
        context.render("contribuciones/persona_fisica/ver_heladeras_activas.hbs", model);
    }

    public void getHeladeras(Context context) {
        List<Object> heladeras = repositorioHeladera.buscarTodos("Heladera");
        List<DTOHeladera> dtoHeladeras = heladeras.stream()
                .map(MapperDTOHeladera::convertirADTO)
                .collect(Collectors.toList());
        context.json(dtoHeladeras);
        context.status(HttpStatus.OK);
    }

    public void getHeladerasActivas(Context context) {
        List<Heladera> heladeras = repositorioHeladera.buscarTodasLasHeladerasActivas();
        List<DTOHeladera> dtoHeladeras = heladeras.stream()
                .map(MapperDTOHeladera::convertirADTO)
                .collect(Collectors.toList());
        context.json(dtoHeladeras);
        context.status(HttpStatus.OK);
    }

    public void getHeladerasPropias(Context context){
        String userId = context.sessionAttribute("userId");
        PersonaJuridica personaJuridica = (PersonaJuridica) repositorio.buscarPorId(userId, "PersonaJuridica");
        List<Heladera> heladeras = personaJuridica.getHeladeras();
        List<DTOHeladera> dtoHeladeras = heladeras.stream()
                .map(MapperDTOHeladera::convertirADTO)
                .collect(Collectors.toList());
        context.json(dtoHeladeras);
        context.status(HttpStatus.OK);
    }

    public void getHeladerasConEspacio(Context context) {
        List<Heladera> heladeras = repositorioHeladera.buscarTodasLasHeladerasActivas();
        List<DTOHeladera> dtoHeladeras = heladeras.stream()
                .filter(Heladera::tieneEspacioDisponible)
                .map(MapperDTOHeladera::convertirADTO)
                .collect(Collectors.toList());
        context.json(dtoHeladeras);
        context.status(HttpStatus.OK);
    }

    public void getABMHeladeras(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "ABM Heladeras");
        context.render("admin/abm_heladeras/abm_heladeras_seleccionar_opcion.hbs", model);
    }
    public void getAltaHeladera(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Dar de alta a una Heladera");
        List<String> modelos = repositorioHeladera.buscarTodosLosModelos();
        model.put("modelos", modelos);
        context.render("admin/abm_heladeras/abm_heladeras_alta.hbs", model);

    }

    public void postAltaHeladera(Context context){
        String calle = context.formParam("punto_estrategico_calle");
        String altura = context.formParam("punto_estrategico_altura");
        String modelo = context.formParam("modelo");
        String piso = context.formParam("punto_estrategico_piso");
        String codigoPostal = context.formParam("punto_estrategico_codigo_postal");
        String ciudad = context.formParam("punto_estrategico_ciudad");
        String provincia = context.formParam("punto_estrategico_provincia");
        String nombre = calle + " " + altura;
        ModeloDeHeladera modeloDeHeladera = (ModeloDeHeladera) repositorio.buscarPorNombre(modelo, "ModeloDeHeladera", ModeloDeHeladera.class);
        Provincia provinciaEncontrada = (Provincia) repositorio.buscarPorNombre(provincia, "Provincia", Provincia.class);

        //TODO CIUDAD
        Direccion direccion = new Direccion(calle,altura, piso, codigoPostal, null, provinciaEncontrada);
        //PuntoEstrategico puntoEstrategico = new PuntoEstrategico(nombre, Double.parseDouble(latitud), Double.parseDouble(longitud), direccion);
        Heladera heladera = new Heladera(modeloDeHeladera, null);
        repositorioHeladera.guardar(heladera);
        SessionUtils.mostrarPantallaDeExito(context, "Heladera dada de alta exitosamente", "Dashboard", "/dashboard");
    }
    public void getModificarHeladera(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Baja Heladera");
        context.render("admin/abm_heladeras/abm_heladeras_modificacion.hbs", model);
    }
    public void getBajaHeladera(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Baja Heladera");
        context.render("admin/abm_heladeras/abm_heladeras_baja.hbs", model);
    }

    public void recibirTemperatura(Context ctx) {
        String idHeladera = ctx.formParam("id_heladera");
        String temperaturaStr = ctx.formParam("temperatura");

        if (idHeladera != null && temperaturaStr != null) {
            Double temperatura = Double.parseDouble(temperaturaStr);
            Heladera heladera = (Heladera) repositorioHeladera.buscarPorId(idHeladera, "Heladera");

            if (heladera != null) {
                heladera.manejarTemperaturaRecibida(temperatura);
                repositorioHeladera.actualizar(heladera);
                ctx.result("Temperatura actualizada");
            } else {
                ctx.result("Heladera no encontrada");
            }

        } else {
            ctx.result("Parámetros inválidos");
        }
    }
}
