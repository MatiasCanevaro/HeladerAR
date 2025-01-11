package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.config.ServiceLocator;
import ar.utn.frba.dds.arquitectura.dtos.DTOHeladera;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOHeladera;
import ar.utn.frba.dds.arquitectura.utils.SessionUtils;
import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.suscripciones.BuscadorHeladeraMasCercana;
import ar.utn.frba.dds.modelos.repositorios.RepositorioHeladera;
import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HomeController implements ICrudViewsHandler {
    private RepositorioHeladera repositorioHeladera;
    private List<Heladera> heladerasCasteadas;
    private List<DTOHeladera> dtosTodasLasHeladeras;

    public HomeController(RepositorioHeladera repositorioHeladera) {
        this.repositorioHeladera = repositorioHeladera;
        this.heladerasCasteadas = null;
        this.dtosTodasLasHeladeras = null;
    }

    @Override
    public void index(Context context) {
        String latitud = "-34.620353926412854";
        String longitud = "-58.455100671362146";

        if (!this.esPrimerEjecucion(context)) {
            latitud = context.sessionAttribute("latitud");
            longitud = context.sessionAttribute("longitud");
        }
        PuntoEstrategico puntoEstrategicoUsuario = new PuntoEstrategico(latitud,longitud);

        if(this.repositorioHeladerasNull(context)) {
            List<Object> todasLasHeladeras = this.repositorioHeladera.buscarTodos("Heladera");
            this.setHeladerasCasteadas((List<Heladera>) (List<?>) todasLasHeladeras);
            this.setDtosTodasLasHeladeras(MapperDTOHeladera.convertirListaADTOs(todasLasHeladeras));
        }

        List<Heladera> heladerasMasCercanas = BuscadorHeladeraMasCercana.buscarHeladerasMasCercanasPorPuntoEstrategico(
                this.getHeladerasCasteadas(), puntoEstrategicoUsuario,3);
        @SuppressWarnings("unchecked")
        List<Object> heladerasMasCercanasCasteadas = (List<Object>) (List<?>) heladerasMasCercanas;
        List<DTOHeladera> dtosHeladerasMasCercana = MapperDTOHeladera.convertirListaADTOs(heladerasMasCercanasCasteadas);

        Map<String, Object> model = new HashMap<>();
        model.put("todasLasHeladeras", this.getDtosTodasLasHeladeras());
        model.put("heladerasMasCercanas", dtosHeladerasMasCercana);
        //SessionUtils.finalizoRegistro(context);
        model.put("usuarioLogueado", SessionUtils.estaLogueado(context));
        model.put("estaLoggueado", SessionUtils.estaLogueado(context));
        model.put("tipoRol", context.sessionAttribute("tipoRol"));
        model.put("titulo", "Home - Bienvenido");
        ServiceLocator.instanceOf(StepMeterRegistry.class).counter("Home", "status", "ok").increment();
        context.render("generales/home.hbs", model);
    }

    public Boolean esPrimerEjecucion(Context context) {
        return context.sessionAttribute("latitud") == null && context.sessionAttribute("longitud") == null;
    }

    public Boolean repositorioHeladerasNull(Context context) {
        return this.getDtosTodasLasHeladeras() == null && this.getHeladerasCasteadas() == null;
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
}

