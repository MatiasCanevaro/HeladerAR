package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.config.ServiceLocator;
import ar.utn.frba.dds.arquitectura.dtos.DTOHeladera;
import ar.utn.frba.dds.arquitectura.exceptions.BadRequestException;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOHeladera;
import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import ar.utn.frba.dds.arquitectura.utils.SessionUtils;
import ar.utn.frba.dds.modelos.entidades.contacto.Administrador;
import ar.utn.frba.dds.modelos.entidades.contacto.Usuario;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import ar.utn.frba.dds.modelos.entidades.personas.Tecnico;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import ar.utn.frba.dds.modelos.repositorios.RepositorioHeladera;
import ar.utn.frba.dds.modelos.repositorios.RepositorioUsuario;
import ar.utn.frba.dds.modulos.validador.ResultadoValidacion;
import ar.utn.frba.dds.modulos.validador.Validador;
import io.javalin.http.Context;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class LoginController implements ICrudViewsHandler {
    private RepositorioUsuario repositorioUsuario;
    @Override
    public void index(Context context) {
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Iniciar sesión");
        context.render("registrosYSesiones/inicio_colaborador.hbs");
    }

    public void indexAdmin(Context context) {
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Iniciar sesión como administrador");
        context.render("admin/inicio_admin.hbs");
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

    public void validate(Context context) {
        String email = context.formParam("email");
        String password = context.formParam("password");

        Usuario usuario = repositorioUsuario.buscarPorEmailYContrasenia(email, password);

        if (usuario != null) {
            String contrasenia = usuario.getContrasenia();
            String correoElectronico = usuario.getCorreoElectronico();
            String tipoRol = String.valueOf(usuario.getTipoRol());
            String userId = "";

            if (correoElectronico.equals(email) && contrasenia.equals(password)) {
                if (tipoRol.equals(TipoRol.PERSONA_FISICA.name())) {
                    PersonaFisica personaFisica = usuario.getPersonaFisica();
                    userId = personaFisica.getId();
                    context.sessionAttribute("userId", userId);
                    context.sessionAttribute("tipoRol", tipoRol);
                    //context.sessionAttribute("registroIncompleto", false); //TODO Marcar completo
                    ServiceLocator.instanceOf(StepMeterRegistry.class).counter("Request", "status", "ok").increment();
                    ServiceLocator.instanceOf(StepMeterRegistry.class).counter("Logins", "status", "ok").increment();
                    context.status(200);
                }else if (tipoRol.equals(TipoRol.PERSONA_JURIDICA.name())) {
                    PersonaJuridica personaJuridica = usuario.getPersonaJuridica();
                    userId = personaJuridica.getId();
                    context.sessionAttribute("userId", userId);
                    context.sessionAttribute("tipoRol", tipoRol);
                    //context.sessionAttribute("registroIncompleto", false); //TODO Marcar completo
                    ServiceLocator.instanceOf(StepMeterRegistry.class).counter("Logins", "status", "ok").increment();
                    context.status(200);
                } else if (tipoRol.equals(TipoRol.TECNICO.name())) {
                    Tecnico tecnico = usuario.getTecnico();
                    userId = tecnico.getId();
                    context.sessionAttribute("userId", userId);
                    context.sessionAttribute("tipoRol", tipoRol);
                    ServiceLocator.instanceOf(StepMeterRegistry.class).counter("Logins", "status", "ok").increment();
                    //context.sessionAttribute("registroIncompleto", false); //TODO Marcar completo
                    context.status(200);
                }else{
                    throw new BadRequestException();
                }
            } else {
                throw new BadRequestException();
            }
        } else {
            ServiceLocator.instanceOf(StepMeterRegistry.class).counter("Logins", "status", "failed").increment();
            throw new BadRequestException();
        }
    }

    public void validateAdmin(Context context) {
        String email = context.formParam("email");
        String password = context.formParam("password");

        Usuario usuario = repositorioUsuario.buscarPorEmailYContrasenia(email, password);

        if (usuario != null) {
            String contrasenia = usuario.getContrasenia();
            String correoElectronico = usuario.getCorreoElectronico();
            String tipoRol = String.valueOf(usuario.getTipoRol());
            String userId = "";

            if (correoElectronico.equals(email) && contrasenia.equals(password)) {
                if (tipoRol.equals(TipoRol.ADMIN.name())) {
                    Administrador administrador = usuario.getAdministrador();
                    userId = administrador.getId();
                    context.sessionAttribute("userId", userId);
                    context.sessionAttribute("tipoRol", tipoRol);
                    //context.sessionAttribute("registroIncompleto", false); //TODO Marcar completo
                    ServiceLocator.instanceOf(StepMeterRegistry.class).counter("Logins", "status", "ok").increment();
                    context.status(200);
                }else {
                    throw new BadRequestException();
                }
            } else {
                throw new BadRequestException();
            }
        } else {
            throw new BadRequestException();
        }
    }
}
