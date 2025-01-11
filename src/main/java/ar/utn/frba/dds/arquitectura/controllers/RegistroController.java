package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.dtos.DTOOpcion;
import ar.utn.frba.dds.arquitectura.dtos.DTOPregunta;
import ar.utn.frba.dds.arquitectura.dtos.DTOProvincia;
import ar.utn.frba.dds.arquitectura.dtos.DTORubro;
import ar.utn.frba.dds.arquitectura.mappers.*;
import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import ar.utn.frba.dds.arquitectura.utils.SessionUtils;
import ar.utn.frba.dds.modelos.entidades.Persistente;
import ar.utn.frba.dds.modelos.entidades.contacto.Usuario;
import ar.utn.frba.dds.modelos.entidades.cuestionarios.*;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.FormaDeColaboracion;
import ar.utn.frba.dds.modelos.entidades.geografia.Ciudad;
import ar.utn.frba.dds.modelos.entidades.geografia.Direccion;
import ar.utn.frba.dds.modelos.entidades.geografia.Provincia;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Rubro;
import ar.utn.frba.dds.modelos.entidades.personas.*;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;
import ar.utn.frba.dds.modelos.repositorios.*;
import ar.utn.frba.dds.modulos.importador.MapperImportador;
import ar.utn.frba.dds.modulos.notificador.Mensaje;
import ar.utn.frba.dds.modulos.notificador.MensajeCredencialesDeUsuario;
import ar.utn.frba.dds.modulos.notificador.Notificador;
import ar.utn.frba.dds.modulos.validador.ResultadoValidacion;
import ar.utn.frba.dds.modulos.validador.Validador;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class RegistroController implements ICrudViewsHandler {
    private Repositorio repositorio;
    private RepositorioPersonaFisica repositorioPersonaFisica;
    private RepositorioProvincia repositorioProvincia;
    private RepositorioRubro repositorioRubro;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioCuestionario repositorioCuestionario;
    private RepositorioRespuesta repositorioRespuesta;
    private MapperImportador mapperImportador;
    private Notificador notificador;
    @Override
    public void index(Context context) {
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Registro");
        context.render("registrosYSesiones/registro_colaborador.hbs",model);
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

    public void exito(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Registro Exitoso");
        //context.sessionAttribute("registroIncompleto", false); // Marcar como completado
        context.render("registrosYSesiones/registro_ok.hbs",model);
    }

    public void esPersonaFisicaOJuridica(Context context){
        //context.sessionAttribute("registroIncompleto", true);
        Map<String, Object> model = new HashMap<>();
        String rolARegistrar = context.formParam("tipoRol");
        if(Objects.equals(rolARegistrar, "Persona Física")){
            context.redirect("/registro/persona_fisica/paso1");
        }else{
            context.redirect("/registro/persona_juridica/paso1");
        }
    }

    public void identificacionPersonaFisicaPaso1Get(Context context) {
        //context.sessionAttribute("registroIncompleto", true);
        Map<String, Object> model = new HashMap<>();
        List<Ciudad> ciudades = repositorio.buscarTodos("Ciudad")
                .stream()
                .map(ciudad -> (Ciudad) ciudad)
                .toList();

        model.put("titulo", "Registro Persona Fisica");


        List<Object> todasLasProvincias = repositorio.buscarTodos("Provincia");
        List<DTOProvincia> provinciasMapeadas = MapperDTOProvincia.convertirListaADTOs(todasLasProvincias);
        model.put("provincias", provinciasMapeadas);


        model.put("ciudades", ciudades);
        context.render("registrosYSesiones/registro_fisica.hbs", model);
    }


    public void identificacionPersonaFisicaPaso1Post(Context context){
        //context.sessionAttribute("registroIncompleto", true); // Marcar incompleto
        String nombre = context.formParam("name");
        String apellido = context.formParam("last-name");
        String tipoDocumento = context.formParam("id");
        String numeroDocumento = context.formParam("id-number");
        String calle = context.formParam("street");
        String altura = context.formParam("height");
        String piso = context.formParam("floor");
        String codigoPostal = context.formParam("pc");
        String provincia = context.formParam("province");
        LocalDate fechaNacimiento = LocalDate.parse(context.formParam("birthday"));

        Provincia provinciaEncontrada = repositorioProvincia.buscarPorNombre(provincia);

        TipoDocumento tipoDocumentoMapeado = mapperImportador.tipoDocumentoMapper(tipoDocumento);
        Direccion direccion = new Direccion(calle, altura, piso, codigoPostal, null, provinciaEncontrada);
        PersonaFisica personaFisica = PersonaFisica.crearPersonaFisica(
                tipoDocumentoMapeado,
                numeroDocumento,
                nombre,
                apellido,
                direccion,
                fechaNacimiento
        );

        repositorio.guardar(direccion);

        String id = personaFisica.getId();
        context.sessionAttribute("userId",id);

        repositorioPersonaFisica.guardar(personaFisica);

        context.redirect("/registro/persona_fisica/paso2");
    }

    public void identificacionPersonaFisicaPaso2Get(Context context){
        //context.sessionAttribute("registroIncompleto", true);
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Registro Persona Fisica");
        context.render("registrosYSesiones/registro_fisica2.hbs",model);
    }
    public void identificacionPersonaFisicaPaso2Post(Context context){
        //context.sessionAttribute("registroIncompleto", true); // Marcar incompleto
        String email = context.formParam("email");
        String contrasenia = context.formParam("password");
        String contraseniaConfirmar = context.formParam("confirm-password");
        List<String> contributions = context.formParams("contribution[]");

        Validador validador = Validador.crearValidador();
        ResultadoValidacion resultadoValidacion = validador.esValida(contrasenia);

        Map<String, Object> model = new HashMap<>();
        String id = context.sessionAttribute("userId");
        PersonaFisica personaFisica =
                (PersonaFisica) repositorioPersonaFisica.buscarPorId(id,"PersonaFisica");
        Usuario usuarioAux = repositorioUsuario.buscarPorEmail(email);
        List<String> errores = new ArrayList<>(); // Lista centralizada de errores

        // Validación de contraseña
        if (!resultadoValidacion.getErrores().isEmpty()) {
            errores.addAll(resultadoValidacion.getErrores());
        }

        // Validación de email duplicado
        if (usuarioAux != null) {
            errores.add("El mail introducido ya fue utilizado");
        }

        // Validación de contraseñas coincidentes
        if (!Objects.equals(contrasenia, contraseniaConfirmar)) {
            errores.add("Las contraseñas introducidas no coinciden");
        }

        // Si hay errores, los mostramos
        if (!errores.isEmpty()) {
            model.put("errores", errores); // Pasar errores como lista simple
            context.sessionAttribute("session_email", email);
            context.sessionAttribute("session_contributions", contributions);
            context.render("registrosYSesiones/registro_fisica2.hbs", model);
        }
        else {
            // Registro exitoso
            Email emailCreado = Email.crearEmail(email);
            personaFisica.setEmail(emailCreado);
            Usuario usuario = new Usuario();
            usuario.setContrasenia(contrasenia);
            usuario.setCorreoElectronico(email);
            usuario.setTipoRol(TipoRol.PERSONA_FISICA);
            usuario.setPersonaFisica(personaFisica);
            usuario.setPersonaJuridica(null);
            repositorioUsuario.guardar(usuario);

            List<FormaDeColaboracion> formasDeColaboracion =
                    mapperImportador.listaDeFormaDeColaboracionMapper(contributions);
            personaFisica.setFormasDeColaboracion(formasDeColaboracion);

            repositorioPersonaFisica.guardar(personaFisica);
            context.sessionAttribute("userId",personaFisica.getId());

            Mensaje mensaje = MensajeCredencialesDeUsuario.generarMensaje(usuario);
            notificador.notificar(mensaje, String.valueOf(usuario.getCorreoElectronico()));

            List<TipoRol> rol = new ArrayList<>();
            rol.add(TipoRol.PERSONA_FISICA);
            Cuestionario cuestionario = repositorioCuestionario.buscarActivo(rol);
            if(cuestionario!=null){
                context.sessionAttribute("cuestionarioAMostrar",cuestionario.getId());
                context.redirect("/registro/persona_fisica/paso3");
            }
            else{
                context.sessionAttribute("tipoRol", "PERSONA_FISICA");
                context.redirect("/registro/exito");
            }
        }
    }

    public void identificacionPersonaFisicaPaso3Get(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Registro Persona Fisica");

        String cuestionarioId = context.sessionAttribute("cuestionarioAMostrar");
        Cuestionario cuestionario = (Cuestionario) repositorioCuestionario.buscarPorId(cuestionarioId,"Cuestionario");

        String personaFisicaId = context.sessionAttribute("userId");
        PersonaFisica personaFisica = (PersonaFisica) repositorio.buscarPorId(personaFisicaId,"PersonaFisica");
        List<Respuesta> respuestas = repositorioRespuesta.buscarPorPersonaFisica(personaFisica, "Respuesta")
                .stream()
                .map(respuesta -> (Respuesta) respuesta)
                .toList();
        List<Pregunta> preguntas = cuestionario.getPreguntas();

        Set<String> preguntasRespondidasIds = respuestas.stream()
                .map(respuesta -> respuesta.getPregunta().getId())
                .collect(Collectors.toSet());

        List<Pregunta> preguntasNoRespondidas = preguntas.stream()
                .filter(pregunta -> !preguntasRespondidasIds.contains(pregunta.getId()))
                .collect(Collectors.toList());

        if(preguntasNoRespondidas.isEmpty()){
            context.sessionAttribute("tipoRol", "PERSONA_FISICA");
            SessionUtils.mostrarPantallaDeExito(context, "Registro Exitoso", "Dashboard", "/dashboard");
        }
        else{
            List<DTOPregunta> dtoPreguntas = MapperDTOPregunta.convertirListaADTOs(preguntasNoRespondidas);

            List<DTOPregunta> primeraTanda = dtoPreguntas.subList(0, Math.min(2, dtoPreguntas.size()));
            List<DTOPregunta> segundaTanda = dtoPreguntas.subList(Math.min(2, dtoPreguntas.size()), Math.min(4, dtoPreguntas.size()));

            model.put("primeraTanda", primeraTanda);
            model.put("segundaTanda", segundaTanda);
            context.sessionAttribute("cuestionarioAMostrar",cuestionario.getId());
            context.sessionAttribute("userId",personaFisica.getId());
            context.render("admin/abm_personas/persona_fisica/abm_pf_alta.hbs",model);
        }
    }

    public void identificacionPersonaFisicaPaso3Post(Context context){
        String cuestionarioId = context.sessionAttribute("cuestionarioAMostrar");
        Cuestionario cuestionario = (Cuestionario) repositorioCuestionario.buscarPorId(cuestionarioId,"Cuestionario");
        CuestionarioRespondido cuestionarioRespondido = new CuestionarioRespondido(
                cuestionario,
                LocalDateTime.now()
        );
        String personaFisicaId = context.sessionAttribute("userId");
        PersonaFisica personaFisica = (PersonaFisica) repositorio.buscarPorId(personaFisicaId,"PersonaFisica");

        Map<String, List<String>> formParams = context.formParamMap();
        Map<String, Respuesta> respuestasMultiples = new HashMap<>();

        formParams.forEach((preguntaId, opciones) -> {
            for (String opcion : opciones) {
                Pregunta pregunta = (Pregunta) repositorio.buscarPorId(preguntaId, "Pregunta");
                if (pregunta.getTipoPregunta() == TipoPregunta.ABIERTA) {
                    Respuesta respuesta = new Respuesta();
                    respuesta.setPregunta(pregunta);
                    respuesta.setCuestionarioRespondido(cuestionarioRespondido);
                    respuesta.setPersonaFisica(personaFisica);
                    respuesta.setContenido(opcion);
                    repositorio.guardar(respuesta);
                } else if (pregunta.getTipoPregunta() == TipoPregunta.UNICA) {
                    Respuesta respuesta = new Respuesta();
                    respuesta.setPregunta(pregunta);
                    respuesta.setCuestionarioRespondido(cuestionarioRespondido);
                    respuesta.setPersonaFisica(personaFisica);
                    Opcion opcionSeleccionada = (Opcion) repositorio.buscarPorId(opcion, "Opcion");
                    respuesta.agregarOpcion(opcionSeleccionada);
                    repositorio.guardar(respuesta);
                } else if (pregunta.getTipoPregunta() == TipoPregunta.MULTIPLE) {
                    Respuesta respuesta = respuestasMultiples.get(preguntaId);
                    if (respuesta == null) {
                        respuesta = new Respuesta();
                        respuesta.setPregunta(pregunta);
                        respuesta.setCuestionarioRespondido(cuestionarioRespondido);
                        respuesta.setPersonaFisica(personaFisica);
                        respuestasMultiples.put(preguntaId, respuesta);
                    }
                    Opcion opcionSeleccionada = (Opcion) repositorio.buscarPorId(opcion, "Opcion");
                    respuesta.agregarOpcion(opcionSeleccionada);
                    repositorio.guardar(respuesta);
                }
            }
        });
        context.sessionAttribute("cuestionarioAMostrar",cuestionario.getId());
        context.sessionAttribute("userId",personaFisica.getId());
        context.redirect("/registro/persona_fisica/paso3");
    }

    public void identificacionPersonaJuridicaPaso1Get(Context context){
        //context.sessionAttribute("registroIncompleto", true);
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Registro Persona Juridica");

        List<Object> todasLasProvincias = repositorio.buscarTodos("Provincia");
        List<DTOProvincia> provinciasMapeadas = MapperDTOProvincia.convertirListaADTOs(todasLasProvincias);
        model.put("provincias", provinciasMapeadas);

        List<Object> todosLosRubros = repositorio.buscarTodos("Rubro");
        List<DTORubro> dtosRubro = MapperDTORubro.convertirListaADTOs(todosLosRubros);
        model.put("rubros", dtosRubro);

        context.render("registrosYSesiones/registro_juridica.hbs",model);
    }
    public void identificacionPersonaJuridicaPaso1Post(Context context) {
        //context.sessionAttribute("registroIncompleto", true); // Marcar incompleto
        String razonSocial = context.formParam("razon-social");
        String rubro = context.formParam("rubro");
        String tipoOrganizacion = context.formParam("category");

        PersonaJuridica personaJuridica = new PersonaJuridica();
        personaJuridica.setRazonSocial(razonSocial);

        Rubro rubroEncontrado = repositorioRubro.buscarPorNombre(rubro);
        personaJuridica.setRubro(rubroEncontrado);

        TipoOrganizacion tipoOrganizacionMappeado = mapperImportador.tipoOrganizacionMapper(tipoOrganizacion);
        personaJuridica.setTipoOrganizacion(tipoOrganizacionMappeado);

        String id = personaJuridica.getId();
        context.sessionAttribute("userId", id);

        repositorio.guardar(personaJuridica);
        context.redirect("/registro/persona_juridica/paso2");
    }

    public void identificacionPersonaJuridicaPaso2Get(Context context){
        //context.sessionAttribute("registroIncompleto", true);
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Registro Persona Juridica");
        context.render("registrosYSesiones/registro_juridica2.hbs",model);
    }
    public void identificacionPersonaJuridicaPaso2Post(Context context){
        //context.sessionAttribute("registroIncompleto", true); // Marcar incompleto
        String contrasenia = context.formParam("password");
        String contraseniaConfirmar = context.formParam("confirm-password");
        String email = context.formParam("email");
        List<String> contributions = context.formParams("contribution[]");

        Validador validador = Validador.crearValidador();
        ResultadoValidacion resultadoValidacion = validador.esValida(contrasenia);

        List<String> errores = new ArrayList<>(); // Lista centralizada de errores
        Map<String, Object> model = new HashMap<>();

        String id = context.sessionAttribute("userId");
        PersonaJuridica personaJuridica = (PersonaJuridica) repositorio.buscarPorId(id,"PersonaJuridica");
        Usuario usuarioAux = repositorioUsuario.buscarPorEmail(email);

        if(!resultadoValidacion.getErrores().isEmpty()){
            errores.addAll(resultadoValidacion.getErrores());
        }
        if(usuarioAux!=null){
            errores.add("El mail introducido ya fue utilizado");
        }
        if(!Objects.equals(contrasenia, contraseniaConfirmar)){
            errores.add("Las contraseñas introducidas no coinciden");
        }
        if (!errores.isEmpty()) {
            model.put("errores", errores); // Pasar errores como lista simple
            context.sessionAttribute("session_email", email);
            context.sessionAttribute("session_contributions", contributions);
            context.render("registrosYSesiones/registro_juridica2.hbs", model);
        }
        else{
            List<FormaDeColaboracion> formasDeColaboracion = mapperImportador.listaDeFormaDeColaboracionMapper(contributions);
            personaJuridica.setFormasDeColaboracion(formasDeColaboracion);

            repositorio.guardar(personaJuridica);

            Usuario usuario = new Usuario();
            usuario.setCorreoElectronico(email);
            usuario.setPersonaFisica(null);
            usuario.setPersonaJuridica(personaJuridica);
            usuario.setContrasenia(contrasenia);
            usuario.setTipoRol(TipoRol.PERSONA_JURIDICA);
            repositorioUsuario.guardar(usuario);

            //Notificacion Lo comento para no enviar mails constantemente
            Mensaje mensaje = MensajeCredencialesDeUsuario.generarMensaje(usuario);
            notificador.notificar(mensaje, String.valueOf(usuario.getCorreoElectronico()));

            List<TipoRol> rol = new ArrayList<>();
            rol.add(TipoRol.PERSONA_JURIDICA);
            Cuestionario cuestionario = repositorioCuestionario.buscarActivo(rol);
            if(cuestionario!=null){
                context.sessionAttribute("cuestionarioAMostrar",cuestionario.getId());
                context.redirect("/registro/persona_juridica/paso3");
            }
            else{
                context.sessionAttribute("tipoRol", "PERSONA_JURIDICA");
                context.redirect("/registro/exito");
            }
        }
    }

    public void identificacionPersonaJuridicaPaso3Get(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Registro Persona Juridica");

        String cuestionarioId = context.sessionAttribute("cuestionarioAMostrar");
        Cuestionario cuestionario = (Cuestionario) repositorioCuestionario.buscarPorId(cuestionarioId,"Cuestionario");

        String personaJuridicaId = context.sessionAttribute("userId");
        PersonaJuridica personaJuridica = (PersonaJuridica) repositorio.buscarPorId(personaJuridicaId,"PersonaJuridica");
        List<Respuesta> respuestas = repositorioRespuesta.buscarPorPersonaJuridica(personaJuridica, "Respuesta")
                .stream()
                .map(respuesta -> (Respuesta) respuesta)
                .toList();
        List<Pregunta> preguntas = cuestionario.getPreguntas();

        Set<String> preguntasRespondidasIds = respuestas.stream()
                .map(respuesta -> respuesta.getPregunta().getId())
                .collect(Collectors.toSet());

        List<Pregunta> preguntasNoRespondidas = preguntas.stream()
                .filter(pregunta -> !preguntasRespondidasIds.contains(pregunta.getId()))
                .collect(Collectors.toList());

        if(preguntasNoRespondidas.isEmpty()){
            context.sessionAttribute("tipoRol", "PERSONA_JURIDICA");
            SessionUtils.mostrarPantallaDeExito(context, "Registro Exitoso", "Dashboard", "/dashboard");
        }
        else{
            List<DTOPregunta> dtoPreguntas = MapperDTOPregunta.convertirListaADTOs(preguntasNoRespondidas);

            List<DTOPregunta> primeraTanda = dtoPreguntas.subList(0, Math.min(2, dtoPreguntas.size()));
            List<DTOPregunta> segundaTanda = dtoPreguntas.subList(Math.min(2, dtoPreguntas.size()), Math.min(4, dtoPreguntas.size()));

            model.put("primeraTanda", primeraTanda);
            model.put("segundaTanda", segundaTanda);
            context.sessionAttribute("cuestionarioAMostrar",cuestionario.getId());
            context.sessionAttribute("userId",personaJuridica.getId());
            context.render("admin/abm_personas/persona_juridica/abm_pj_alta.hbs",model);
        }
    }

    public void identificacionPersonaJuridicaPaso3Post(Context context){
        String cuestionarioId = context.sessionAttribute("cuestionarioAMostrar");
        Cuestionario cuestionario = (Cuestionario) repositorioCuestionario.buscarPorId(cuestionarioId,"Cuestionario");
        CuestionarioRespondido cuestionarioRespondido = new CuestionarioRespondido(
                cuestionario,
                LocalDateTime.now()
        );
        String personaJuridicaId = context.sessionAttribute("userId");
        PersonaJuridica personaJuridica = (PersonaJuridica) repositorio.buscarPorId(personaJuridicaId,"PersonaJuridica");

        Map<String, List<String>> formParams = context.formParamMap();
        Map<String, Respuesta> respuestasMultiples = new HashMap<>();

        formParams.forEach((preguntaId, opciones) -> {
            for (String opcion : opciones) {
                Pregunta pregunta = (Pregunta) repositorio.buscarPorId(preguntaId, "Pregunta");
                if (pregunta.getTipoPregunta() == TipoPregunta.ABIERTA) {
                    Respuesta respuesta = new Respuesta();
                    respuesta.setPregunta(pregunta);
                    respuesta.setCuestionarioRespondido(cuestionarioRespondido);
                    respuesta.setPersonaJuridica(personaJuridica);
                    respuesta.setContenido(opcion);
                    repositorio.guardar(respuesta);
                } else if (pregunta.getTipoPregunta() == TipoPregunta.UNICA) {
                    Respuesta respuesta = new Respuesta();
                    respuesta.setPregunta(pregunta);
                    respuesta.setCuestionarioRespondido(cuestionarioRespondido);
                    respuesta.setPersonaJuridica(personaJuridica);
                    Opcion opcionSeleccionada = (Opcion) repositorio.buscarPorId(opcion, "Opcion");
                    respuesta.agregarOpcion(opcionSeleccionada);
                    repositorio.guardar(respuesta);
                } else if (pregunta.getTipoPregunta() == TipoPregunta.MULTIPLE) {
                    Respuesta respuesta = respuestasMultiples.get(preguntaId);
                    if (respuesta == null) {
                        respuesta = new Respuesta();
                        respuesta.setPregunta(pregunta);
                        respuesta.setCuestionarioRespondido(cuestionarioRespondido);
                        respuesta.setPersonaJuridica(personaJuridica);
                        respuestasMultiples.put(preguntaId, respuesta);
                    }
                    Opcion opcionSeleccionada = (Opcion) repositorio.buscarPorId(opcion, "Opcion");
                    respuesta.agregarOpcion(opcionSeleccionada);
                    repositorio.guardar(respuesta);
                }
            }
        });
        context.sessionAttribute("cuestionarioAMostrar",cuestionario.getId());
        context.sessionAttribute("userId",personaJuridica.getId());
        context.redirect("/registro/persona_juridica/paso3");
    }
}
