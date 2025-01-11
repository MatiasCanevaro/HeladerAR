package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.dtos.DTOErroresValidacion;
import ar.utn.frba.dds.arquitectura.dtos.DTOHeladera;
import ar.utn.frba.dds.arquitectura.dtos.DTOModeloDeHeladera;
import ar.utn.frba.dds.arquitectura.dtos.DTOProvincia;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOErroresValidacion;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOHeladera;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOModeloDeHeladera;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOProvincia;
import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import ar.utn.frba.dds.arquitectura.utils.SessionUtils;
import ar.utn.frba.dds.modelos.entidades.contacto.Usuario;
import ar.utn.frba.dds.modelos.entidades.geografia.Ciudad;
import ar.utn.frba.dds.modelos.entidades.geografia.Direccion;
import ar.utn.frba.dds.modelos.entidades.geografia.Provincia;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.personas.Email;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import ar.utn.frba.dds.modelos.repositorios.RepositorioHeladera;
import ar.utn.frba.dds.modelos.repositorios.RepositorioProvincia;
import ar.utn.frba.dds.modelos.repositorios.RepositorioUsuario;
import ar.utn.frba.dds.modulos.notificador.Mensaje;
import ar.utn.frba.dds.modulos.notificador.Notificador;
import ar.utn.frba.dds.modulos.validador.ResultadoValidacion;
import ar.utn.frba.dds.modulos.validador.Validador;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
public class PerfilController implements ICrudViewsHandler {
    private Repositorio repositorio;
    private RepositorioProvincia repositorioProvincia;
    private RepositorioUsuario repositorioUsuario;
    private Notificador notificador;
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
    Map<String, Object> model = new HashMap<>();

    // Obtener ID del usuario de la sesión
    String userId = context.sessionAttribute("userId");
    String rol = context.sessionAttribute("tipoRol");

    if (TipoRol.PERSONA_FISICA.name().equals(rol)) {
        PersonaFisica personaFisica = (PersonaFisica) repositorio.buscarPorId(userId, "PersonaFisica");
        model.put("usuario", personaFisica);
        model.put("direccion", personaFisica.getDireccion());
    } else if (TipoRol.PERSONA_JURIDICA.name().equals(rol)) {
        PersonaJuridica personaJuridica = (PersonaJuridica) repositorio.buscarPorId(userId, "PersonaJuridica");
        model.put("usuario", personaJuridica);
        model.put("direccion", personaJuridica.getDireccion());
    }

        List<Ciudad> ciudades = repositorio.buscarTodos("Ciudad")
                .stream()
                .map(c -> (Ciudad) c)
                .toList();

    model.put("ciudades", ciudades);
    model.put("titulo", "Modificar Perfil");

    // Renderizar la vista con los datos del perfil y la dirección
    context.render("generales/profile.hbs", model);
}


    @Override
    public void update(Context context) {
        List<String> errores = new ArrayList<>();
        String calle = context.formParam("calle");
        String altura = context.formParam("altura");
        String piso = context.formParam("piso");
        String cp = context.formParam("cp");
        String ciudad = context.formParam("ciudad");
        String email = context.formParam("email");
        String contraseniaAnterior = context.formParam("contrasenia_anterior");
        String contraseniaNueva = context.formParam("nueva_contrasenia");
        String contraseniaNuevaConfirmar = context.formParam("confirmar_nueva_contrasenia");
        Map<String, Object> model = new HashMap<>();
        Ciudad ciudadEncontrada = (Ciudad) repositorio.buscarPorId(ciudad, "Ciudad");

        Usuario usuario = repositorioUsuario.buscarPorEmailYContrasenia(email,contraseniaAnterior);
        Validador validador = Validador.crearValidador();
        if(usuario == null) {
            System.out.println("Usuario no encontrado");
            errores.add("Email o contraseña incorrecto");
        }

        if(contraseniaNueva != null && !contraseniaNueva.isEmpty() && contraseniaNueva.equals(contraseniaNuevaConfirmar)){
            ResultadoValidacion resultadoValidacion = validador.esValida(contraseniaNueva);
            if(!resultadoValidacion.getErrores().isEmpty()){
                System.out.println("Contraseña no valida");
                errores.addAll(resultadoValidacion.getErrores());;
            }else {
                assert usuario != null;
                usuario.setContrasenia(contraseniaNueva);
            }}
        else {
            context.status(401).result("Las contraseñas no coinciden");
        }
        if(!Objects.equals(contraseniaAnterior, usuario.getContrasenia()) ||
            !Objects.equals(email, usuario.getCorreoElectronico())){
                context.status(401).result("Email o contraseña incorrecto");
        }

        if (!errores.isEmpty()) {
            System.out.println("Errores en la validación");
            model.put("errores", errores);
            context.render("generales/profile.hbs", model);
        }else{
            String userId = context.sessionAttribute("userId");
            String rol = context.sessionAttribute("tipoRol");

            PersonaFisica personaFisica = null;
            PersonaJuridica personaJuridica = null;

            Direccion direccion = null;
            if(rol.equals(TipoRol.PERSONA_FISICA.name())){
                personaFisica = (PersonaFisica) repositorio.buscarPorId(userId,"PersonaFisica");

                direccion = personaFisica.getDireccion();

                Email emailNuevo = new Email(email);
                personaFisica.setEmail(emailNuevo);
            }else if(rol.equals(TipoRol.PERSONA_JURIDICA.name())){
                personaJuridica = (PersonaJuridica) repositorio.buscarPorId(userId,"PersonaJuridica");
                direccion = personaJuridica.getDireccion();
            }

            direccion.setCalle(calle);
            direccion.setAltura(altura);
            direccion.setPiso(piso);
            direccion.setCodigoPostal(cp);
            direccion.setProvincia(ciudadEncontrada.getProvincia());

            if(personaFisica!=null){
                repositorio.actualizar(personaFisica);
            }
            if(personaJuridica!=null){
                repositorio.actualizar(personaJuridica);
            }
            Mensaje mensaje = Mensaje.crearMensaje("Perfil actualizado", "Se ha actualizado su perfil correctamente");
            notificador.notificar(mensaje, usuario.getCorreoElectronico()); //TODO: lo dejo comentado asi no se envian mails a cada rato!!!

            SessionUtils.mostrarPantallaDeExito(context,
                    "Su perfil fue actualizado correctamente",
                    "mi Perfil",
                    "/perfil");
        }

            }





    @Override
    public void delete(Context context) {

    }
}
