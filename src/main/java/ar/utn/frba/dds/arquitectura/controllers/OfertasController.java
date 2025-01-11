package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.config.ServiceLocator;
import ar.utn.frba.dds.arquitectura.dtos.DTOCanje;
import ar.utn.frba.dds.arquitectura.dtos.DTOOferta;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOCanje;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOOferta;
import ar.utn.frba.dds.arquitectura.utils.SessionUtils;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Canje;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Oferta;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OfertasController implements ICrudViewsHandler {
    private Repositorio repositorio;
    @Override
    public void index(Context context) {
        List<Object> ofertas = this.repositorio.buscarTodos(Oferta.class.getSimpleName());
        List<DTOOferta> dtosOferta = MapperDTOOferta.convertirListaADTOs(ofertas);
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Catálogo de ofertas");
        model.put("esPersonaJuridica", SessionUtils.esPersonaJuridica(context));
        model.put("ofertas", dtosOferta);

        String userId = context.sessionAttribute("userId");
        PersonaJuridica personaJuridica = (PersonaJuridica) repositorio.buscarPorId(
                userId, PersonaJuridica.class.getSimpleName());

        String tipoRol = context.sessionAttribute("tipoRol");

        if(tipoRol.equals(TipoRol.PERSONA_FISICA.name())){
            PersonaFisica personaFisica = (PersonaFisica) repositorio.buscarPorId(
                    userId, PersonaFisica.class.getSimpleName());
            model.put("puntos", personaFisica.getPuntosAcumulados());
        }
        else{
            model.put("puntos", personaJuridica.getPuntosAcumulados());
        }
        // Puntos disponibles usuario
        context.render("ofertasYCanjes/catalogo.hbs", model);
    }

    @Override
    public void show(Context context) {
        Object posibleOfertaBuscada = this.repositorio.buscarPorId(
                context.pathParam("id"),Oferta.class.getSimpleName());

        //TODO
//        if(posibleProductoBuscado.isEmpty()) {
//        if(posibleProductoBuscado.isEmpty()) {
//            context.status(HttpStatus.NOT_FOUND);
//            return;
//        }

        DTOOferta dtoOferta = MapperDTOOferta.convertirADTO(posibleOfertaBuscada);

        Map<String, Object> model = new HashMap<>();
        model.put("oferta", dtoOferta);
        model.put("titulo", "Canje");

        context.render("ofertasYCanjes/oferta.hbs", model);
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

    public void historialOfertas(Context context) {
        String userId = context.sessionAttribute("userId");

        PersonaJuridica personaJuridica = (PersonaJuridica) repositorio.buscarPorId(userId, PersonaJuridica.class.getSimpleName());

        List<Oferta> historialOfertas = personaJuridica.getOfertas();

        List<DTOOferta> dtoHistorialOfertas = historialOfertas.stream()
                .map(MapperDTOOferta::convertirADTO)
                .collect(Collectors.toList());

        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Historial de Ofertas");
        model.put("historialOfertas", dtoHistorialOfertas);
        context.render("ofertasYCanjes/historial_ofertas.hbs", model);
    }


    public void canje_exitoso(Context context) {
        String ofertaId = context.pathParam("id");
        Oferta oferta = (Oferta) this.repositorio.buscarPorId(ofertaId, Oferta.class.getSimpleName());
        DTOOferta dtoOferta = MapperDTOOferta.convertirADTO(oferta);

        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Canje Exitoso");
        model.put("producto", dtoOferta); // Agregar la oferta al modelo

        context.render("ofertasYCanjes/canje_ok.hbs", model);
    }

    public PersonaJuridica buscarPersonaJuridicaQuePoseeLaOferta (
            List<Object> todasLasPersonasJuridicas, Oferta oferta){
        for (Object obj : todasLasPersonasJuridicas) {
            PersonaJuridica personaJuridica = (PersonaJuridica) obj;
            if (personaJuridica.contieneLaOferta(oferta)) {
                return personaJuridica;
            }
        }
        return null;
    }

    public void canje(Context context) {
        String ofertaId = context.pathParam("id");
        Oferta oferta = (Oferta) this.repositorio.buscarPorId(ofertaId, Oferta.class.getSimpleName());

        String userId = context.sessionAttribute("userId");
        String tipoRol = context.sessionAttribute("tipoRol");

        PersonaJuridica personaJuridicaQueOfrecio = this.repositorio.buscarPersonaJuridicaQuePublicoLaOferta(oferta);

        if (tipoRol.equals(TipoRol.PERSONA_FISICA.name())) {
            PersonaFisica personaFisica = (PersonaFisica) repositorio.buscarPorId(userId,
                    PersonaFisica.class.getSimpleName());
            if (personaFisica.getPuntosAcumulados() < oferta.getCantidadDePuntosNecesariosParaAccederAlBeneficio()) {
                ServiceLocator.instanceOf(StepMeterRegistry.class).counter("CanjeExitoso", "status", "failed").increment();
                context.status(400);
            } else {
                Canje canje = this.instanciarYSetearCanje(oferta);
                this.setearPersonaFisicaParaCanje(canje, personaFisica, oferta);
                canje.setPersonaJuridicaQueOfrecio(personaJuridicaQueOfrecio);
                this.repositorio.guardar(canje);
                String rutaARedireccionar = "/dashboard/catalogo/" + oferta.getId() + "/canje_exitoso";
                oferta.setActivo(false);
                repositorio.actualizar(oferta);
                ServiceLocator.instanceOf(StepMeterRegistry.class).counter("CanjeExitoso", "status", "ok").increment();
                context.redirect(rutaARedireccionar);
            }
        } else if (tipoRol.equals(TipoRol.PERSONA_JURIDICA.name())) {
            PersonaJuridica personaJuridica = (PersonaJuridica) repositorio.buscarPorId(userId,
                    PersonaJuridica.class.getSimpleName());
            if (personaJuridica.getPuntosAcumulados() < oferta.getCantidadDePuntosNecesariosParaAccederAlBeneficio()) {
                ServiceLocator.instanceOf(StepMeterRegistry.class).counter("CanjeExitoso", "status", "failed").increment();
                context.status(400);
            } else {
                Canje canje = this.instanciarYSetearCanje(oferta);
                this.setearPersonaJuridicaParaCanje(canje, personaJuridica, oferta);
                canje.setPersonaJuridicaQueOfrecio(personaJuridicaQueOfrecio);
                this.repositorio.guardar(canje);
                String rutaARedireccionar = "/dashboard/catalogo/" + oferta.getId() + "/canje_exitoso";
                oferta.setActivo(false);
                repositorio.actualizar(oferta);
                ServiceLocator.instanceOf(StepMeterRegistry.class).counter("CanjeExitoso", "status", "ok").increment();
                context.redirect(rutaARedireccionar);
            }
        }
    }

    private Canje instanciarYSetearCanje(Oferta oferta){
        Canje nuevoCanje = new Canje();
        nuevoCanje.setFechaYHoraCanje(LocalDateTime.now());
        nuevoCanje.setOfertaCanjeada(oferta);
        List<Object> todasLasPersonasJuridicas = repositorio.buscarTodos(PersonaJuridica.class.getSimpleName());
        PersonaJuridica personaQuePoseeLaOferta = buscarPersonaJuridicaQuePoseeLaOferta(todasLasPersonasJuridicas, oferta);
        nuevoCanje.setPersonaJuridicaQueOfrecio(personaQuePoseeLaOferta);
        return nuevoCanje;
    }

    private void setearPersonaFisicaParaCanje(Canje canje, PersonaFisica personaFisica, Oferta oferta) {
        canje.setPersonaFisicaQueCanjeo(personaFisica);
        personaFisica.setPuntosAcumulados(personaFisica.getPuntosAcumulados() - oferta.getCantidadDePuntosNecesariosParaAccederAlBeneficio());
        repositorio.guardar(personaFisica);
    }

    private void setearPersonaJuridicaParaCanje(Canje canje, PersonaJuridica personaJuridica, Oferta oferta) {
        canje.setPersonaJuridicaQueCanjeo(personaJuridica);
        personaJuridica.setPuntosAcumulados(personaJuridica.getPuntosAcumulados() - oferta.getCantidadDePuntosNecesariosParaAccederAlBeneficio());
        repositorio.guardar(personaJuridica);
    }



}