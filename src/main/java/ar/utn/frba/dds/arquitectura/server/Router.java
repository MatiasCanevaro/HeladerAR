package ar.utn.frba.dds.arquitectura.server;
import ar.utn.frba.dds.arquitectura.config.ServiceLocator;
import ar.utn.frba.dds.arquitectura.controllers.*;
import ar.utn.frba.dds.arquitectura.utils.SessionUtils;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import io.javalin.Javalin;
import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;

import java.util.HashMap;
import java.util.Map;

public class Router implements SimplePersistenceTest {
    public void init(Javalin app){
        app.before(ctx -> {
            entityManager().clear();
        });

        //home
        app.get("/home", ServiceLocator.instanceOf(HomeController.class)::index);
        app.post("/mapa", ctx -> {
            PuntoEstrategico puntoEstrategico = ctx.bodyAsClass(PuntoEstrategico.class);
            // Guardar latitud y longitud en la sesión
            ctx.sessionAttribute("latitud", puntoEstrategico.getLatitud());
            //System.out.println("La latitud es: "+ puntoEstrategico.getLatitud());
            ctx.sessionAttribute("longitud", puntoEstrategico.getLongitud());
            //System.out.println("La longitud es: "+ puntoEstrategico.getLongitud());
            ServiceLocator.instanceOf(HomeController.class).index(ctx);
        });

        app.get("/sobre_nosotros",ctx -> {
            Map<String, Object> model = new HashMap<>();
            model.put("titulo", "Sobre Nosotros");
            ctx.render("generales/about_us.hbs", model);
        });

        app.get("/policy",ctx -> {
            Map<String, Object> model = new HashMap<>();
            model.put("titulo", "Política de privacidad");
            // Verificar si el registro está incompleto y evitar que inicie sesión automáticamente
            //SessionUtils.finalizoRegistro(ctx);
            model.put("usuarioLogueado", SessionUtils.estaLogueado(ctx));
            ctx.render("generales/policy.hbs", model);
        });

        app.get("/contact", ServiceLocator.instanceOf(ContactoController.class)::index);
        app.post("/contact", ServiceLocator.instanceOf(ContactoController.class)::save);

        //catalogo - detalle oferta
        app.post("/dashboard/catalogo/{id}/canje", ServiceLocator.instanceOf(OfertasController.class)::canje,
                TipoRol.PERSONA_JURIDICA,TipoRol.PERSONA_FISICA);
        app.get("/dashboard/catalogo/{id}/canje_exitoso", ServiceLocator.instanceOf(OfertasController.class)::canje_exitoso,
                TipoRol.PERSONA_JURIDICA,TipoRol.PERSONA_FISICA);
        app.get("/dashboard/catalogo/{id}", ServiceLocator.instanceOf(OfertasController.class)::show,
                TipoRol.PERSONA_JURIDICA,TipoRol.PERSONA_FISICA);
        app.get("/dashboard/catalogo", ServiceLocator.instanceOf(OfertasController.class)::index,
                TipoRol.PERSONA_JURIDICA,TipoRol.PERSONA_FISICA);
        app.get("/dashboard/ofertas", ServiceLocator.instanceOf(OfertasController.class)::historialOfertas,
                TipoRol.PERSONA_JURIDICA);


        //canjes
        app.get("/dashboard/mis_canjes", ServiceLocator.instanceOf(CanjesController.class)::historialCanjes,
                TipoRol.PERSONA_JURIDICA,TipoRol.PERSONA_FISICA);

        //heladeras
        app.get("/heladeras", ServiceLocator.instanceOf(HeladerasController.class)::index);
        app.get("/heladeras/carga", ServiceLocator.instanceOf(HeladerasController.class)::getHeladeras);
        app.get("/heladeras/carga_activas", ServiceLocator.instanceOf(HeladerasController.class)::getHeladerasActivas);
        app.get("/heladeras/carga_heladeras_propias", ServiceLocator.instanceOf(HeladerasController.class)::getHeladerasPropias);
        app.get("/heladeras/carga_heladeras_con_espacio", ServiceLocator.instanceOf(HeladerasController.class)::getHeladerasConEspacio);

        //heladeras broker
        // Ruta para recibir temperatura
        app.post("/broker/temperatura", ServiceLocator.instanceOf(HeladerasController.class)::recibirTemperatura);


        //inicio de sesion
        app.get("/login", ServiceLocator.instanceOf(LoginController.class)::index);
        app.post("/login", ServiceLocator.instanceOf(LoginController.class)::validate);
        //inicio de sesion admin
        app.get("/login_admin", ServiceLocator.instanceOf(LoginController.class)::indexAdmin);
        app.post("/login_admin", ServiceLocator.instanceOf(LoginController.class)::validateAdmin);


        //logout
        app.get("/logout", ServiceLocator.instanceOf(LogoutController.class)::logout);

        //perfil
        app.get("/perfil", ServiceLocator.instanceOf(PerfilController.class)::edit,
                TipoRol.PERSONA_FISICA, TipoRol.PERSONA_JURIDICA);
        app.post("/perfil", ServiceLocator.instanceOf(PerfilController.class)::update,
                TipoRol.PERSONA_FISICA, TipoRol.PERSONA_JURIDICA);

        //registros
        // Registro común (abierto)
        app.get("/registro", ServiceLocator.instanceOf(RegistroController.class)::index,
                TipoRol.OPEN);
        app.post("/registro", ServiceLocator.instanceOf(RegistroController.class)::esPersonaFisicaOJuridica,
                TipoRol.OPEN);
        // Registro persona física
        app.post("/registro/persona_fisica/paso1",
                ServiceLocator.instanceOf(RegistroController.class)::identificacionPersonaFisicaPaso1Post,
                TipoRol.OPEN);
        app.get("/registro/persona_fisica/paso1",
                ServiceLocator.instanceOf(RegistroController.class)::identificacionPersonaFisicaPaso1Get,
                TipoRol.OPEN);
        app.post("/registro/persona_fisica/paso2",
                ServiceLocator.instanceOf(RegistroController.class)::identificacionPersonaFisicaPaso2Post,
                TipoRol.OPEN);
        app.get("/registro/persona_fisica/paso2",
                ServiceLocator.instanceOf(RegistroController.class)::identificacionPersonaFisicaPaso2Get,
                TipoRol.OPEN);
        app.get("/registro/persona_fisica/paso3",
                ServiceLocator.instanceOf(RegistroController.class)::identificacionPersonaFisicaPaso3Get,
                TipoRol.OPEN);
        app.post("/registro/persona_fisica/paso3",
                ServiceLocator.instanceOf(RegistroController.class)::identificacionPersonaFisicaPaso3Post,
                TipoRol.OPEN);
        // Registro persona jurídica
        app.get("/registro/persona_juridica/paso1",
                ServiceLocator.instanceOf(RegistroController.class)::identificacionPersonaJuridicaPaso1Get,
                TipoRol.OPEN);
        app.post("/registro/persona_juridica/paso1",
                ServiceLocator.instanceOf(RegistroController.class)::identificacionPersonaJuridicaPaso1Post,
                TipoRol.OPEN);
        app.post("/registro/persona_juridica/paso2",
                ServiceLocator.instanceOf(RegistroController.class)::identificacionPersonaJuridicaPaso2Post,
                TipoRol.OPEN);
        app.get("/registro/persona_juridica/paso2",
                ServiceLocator.instanceOf(RegistroController.class)::identificacionPersonaJuridicaPaso2Get,
                TipoRol.OPEN);
        app.get("/registro/persona_juridica/paso3",
                ServiceLocator.instanceOf(RegistroController.class)::identificacionPersonaJuridicaPaso3Get,
                TipoRol.OPEN);
        app.post("/registro/persona_juridica/paso3",
                ServiceLocator.instanceOf(RegistroController.class)::identificacionPersonaJuridicaPaso3Post,
                TipoRol.OPEN);

        // Éxito
        app.get("/registro/exito", ServiceLocator.instanceOf(RegistroController.class)::exito,
                TipoRol.PERSONA_FISICA, TipoRol.PERSONA_JURIDICA);


        //dashboards
        app.get("/dashboard", ServiceLocator.instanceOf(DashboardController.class)::getDashboard,
                TipoRol.PERSONA_FISICA, TipoRol.PERSONA_JURIDICA,TipoRol.ADMIN, TipoRol.TECNICO);
        app.get("/dashboard/contribuciones", ServiceLocator.instanceOf(DashboardController.class)::show,
                TipoRol.PERSONA_FISICA, TipoRol.PERSONA_JURIDICA);


        app.get("/dashboard/alertas_heladeras", ServiceLocator.instanceOf(HeladerasController.class)::getVerAlertasHeladeras,
                TipoRol.PERSONA_JURIDICA);
        app.get("/dashboard/heladeras_activas", ServiceLocator.instanceOf(HeladerasController.class)::getVerHeladerasActivas,
                TipoRol.PERSONA_FISICA);
        app.get("/dashboard/reportes", ServiceLocator.instanceOf(ReportesController.class)::mostrarReportes,
                TipoRol.PERSONA_FISICA, TipoRol.PERSONA_JURIDICA);
        app.get("/dashboard/reportes/descargar", ServiceLocator.instanceOf(ReportesController.class)::descargarReporte,
                TipoRol.PERSONA_FISICA, TipoRol.PERSONA_JURIDICA);
        app.get("/dashboard/reportes/descargar_todos", ServiceLocator.instanceOf(ReportesController.class)::descargarTodosReportes,
                TipoRol.PERSONA_FISICA, TipoRol.PERSONA_JURIDICA);
        app.get("/dashboard/fallas_resueltas", ServiceLocator.instanceOf(FallaTecnicaController.class)::getIncidentesResueltos,
                TipoRol.PERSONA_JURIDICA);
        app.get("/dashboard/global_fallas_resueltas", ServiceLocator.instanceOf(FallaTecnicaController.class)::getIncidentesResueltosAdmin,
                TipoRol.ADMIN);

        //contribuciones pf y pj
        app.get("/dashboard/contribuciones/donacion_dinero",
                ServiceLocator.instanceOf(ContribucionesController.class)::mostrarDonacionDinero,
                TipoRol.PERSONA_FISICA, TipoRol.PERSONA_JURIDICA);
        app.post("/dashboard/contribuciones/donacion_dinero",
                ServiceLocator.instanceOf(ContribucionesController.class)::donacionDinero,
                TipoRol.PERSONA_FISICA, TipoRol.PERSONA_JURIDICA);
        app.get("/dashboard/contribuciones/forma_contribucion",
                ServiceLocator.instanceOf(ContribucionesController.class)::verFormaDeContribucion,
                TipoRol.PERSONA_FISICA, TipoRol.PERSONA_JURIDICA);
        app.post("/dashboard/contribuciones/forma_contribucion",
                ServiceLocator.instanceOf(ContribucionesController.class)::update,
                TipoRol.PERSONA_FISICA, TipoRol.PERSONA_JURIDICA);


        //contribuciones pf
        app.get("/dashboard/contribuciones/donacion_vianda",
                ServiceLocator.instanceOf(ContribucionesController.class)::getDonacionVianda,
                TipoRol.PERSONA_FISICA);
        app.post("/dashboard/contribuciones/donacion_vianda",
                ServiceLocator.instanceOf(ContribucionesController.class)::postDonacionVianda,
                TipoRol.PERSONA_FISICA);
        app.get("/dashboard/contribuciones/distribucion_vianda/paso1",
        ServiceLocator.instanceOf(ContribucionesController.class)::getDistribucionViandaPaso1,
                TipoRol.PERSONA_FISICA);
        app.post("/dashboard/contribuciones/distribucion_vianda/paso1",
        ServiceLocator.instanceOf(ContribucionesController.class)::postDistribucionViandaPaso1,
                TipoRol.PERSONA_FISICA);
        app.get("/dashboard/contribuciones/distribucion_vianda/paso2",
                ServiceLocator.instanceOf(ContribucionesController.class)::getDistribucionViandaPaso2,
                TipoRol.PERSONA_FISICA);
        app.post("/dashboard/contribuciones/distribucion_vianda/paso2",
                ServiceLocator.instanceOf(ContribucionesController.class)::postDistribucionViandaPaso2,
                TipoRol.PERSONA_FISICA);
        app.get("/dashboard/contribuciones/distribucion_vianda/paso3",
                ServiceLocator.instanceOf(ContribucionesController.class)::getDistribucionViandaPaso3,
                TipoRol.PERSONA_FISICA);
        app.post("/dashboard/contribuciones/distribucion_vianda/paso3",
                ServiceLocator.instanceOf(ContribucionesController.class)::postDistribucionViandaPaso3,
                TipoRol.PERSONA_FISICA);
       app.get("/dashboard/contribuciones/suscripcion_heladera",
                ServiceLocator.instanceOf(ContribucionesController.class)::getSuscribirseHeladera,
                TipoRol.PERSONA_FISICA);
       app.post("/dashboard/contribuciones/suscripcion_heladera",
                ServiceLocator.instanceOf(ContribucionesController.class)::postSuscribirseHeladera,
                TipoRol.PERSONA_FISICA);

        //dashboard admin
        app.get("/dashboard/modelos",
                ServiceLocator.instanceOf(ModeloDeHeladeraController.class)::getABMModeloDeHeladera,
                TipoRol.ADMIN);
        app.get("/dashboard/modelos/alta",
                ServiceLocator.instanceOf(ModeloDeHeladeraController.class)::getAltaModeloDeHeladera,
                TipoRol.ADMIN);
        app.post("/dashboard/modelos/alta",
                ServiceLocator.instanceOf(ModeloDeHeladeraController.class)::postAltaModeloDeHeladera,
                TipoRol.ADMIN);
        app.get("/dashboard/modelos/modificacion/paso1",
                ServiceLocator.instanceOf(ModeloDeHeladeraController.class)::getModificarModeloDeHeladeraPaso1,
                TipoRol.ADMIN);
        app.post("/dashboard/modelos/modificacion/paso1",
                ServiceLocator.instanceOf(ModeloDeHeladeraController.class)::postModificacionModeloDeHeladeraPaso1,
                TipoRol.ADMIN);
        app.post("/dashboard/modelos/modificacion/paso2",
                ServiceLocator.instanceOf(ModeloDeHeladeraController.class)::postModificacionModeloDeHeladeraPaso2,
                TipoRol.ADMIN);
        app.get("/dashboard/modelos/baja",
                ServiceLocator.instanceOf(ModeloDeHeladeraController.class)::getBajaModeloDeHeladera,
                TipoRol.ADMIN);
        app.post("/dashboard/modelos/baja",
                ServiceLocator.instanceOf(ModeloDeHeladeraController.class)::postBajaModeloDeHeladera,
                TipoRol.ADMIN);

        //----rubros
        app.get("/dashboard/rubros",
                ServiceLocator.instanceOf(RubrosController.class)::getABMRubros,
                TipoRol.ADMIN);
        app.get("/dashboard/rubros/alta",
                ServiceLocator.instanceOf(RubrosController.class)::getAltaRubro,
                TipoRol.ADMIN);
        app.post("/dashboard/rubros/alta",
                ServiceLocator.instanceOf(RubrosController.class)::postAltaRubro,
                TipoRol.ADMIN);
        app.get("/dashboard/rubros/modificacion/paso1",
                ServiceLocator.instanceOf(RubrosController.class)::getModificarRubroPaso1,
                TipoRol.ADMIN);
        app.post("/dashboard/rubros/modificacion/paso1",
                ServiceLocator.instanceOf(RubrosController.class)::postModificacionRubroPaso1,
                TipoRol.ADMIN);
        app.post("/dashboard/rubros/modificacion/paso2",
                ServiceLocator.instanceOf(RubrosController.class)::postModificacionRubroPaso2,
                TipoRol.ADMIN);
        app.get("/dashboard/rubros/baja",
                ServiceLocator.instanceOf(RubrosController.class)::getBajaRubro,
                TipoRol.ADMIN);
        app.post("/dashboard/rubros/baja",
                ServiceLocator.instanceOf(RubrosController.class)::postBajaRubro,
                TipoRol.ADMIN);

        app.get("/dashboard/persona_fisica",
                ServiceLocator.instanceOf(PersonaFisicaController.class)::index,
                TipoRol.ADMIN);
        app.get("/dashboard/persona_juridica",
                ServiceLocator.instanceOf(PersonaJuridicaController.class)::index,
                TipoRol.ADMIN);

        app.get("/personaJuridica", ServiceLocator.instanceOf(PersonaJuridicaController.class)::esPersonaJuridica);
        app.get("/personaFisica", ServiceLocator.instanceOf(PersonaFisicaController.class)::esPersonaFisica);
        app.get("/tecnico", ServiceLocator.instanceOf(TecnicoController.class)::esTecnico);

        app.get("/dashboard/heladeras",
                ServiceLocator.instanceOf(HeladerasController.class)::getABMHeladeras,
                TipoRol.ADMIN);
        app.get("/dashboard/heladeras/alta",
                ServiceLocator.instanceOf(HeladerasController.class)::getAltaHeladera,
                TipoRol.ADMIN);
        app.get("/dashboard/heladeras/modificacion",
                ServiceLocator.instanceOf(HeladerasController.class)::getModificarHeladera,
                TipoRol.ADMIN);
        app.get("/dashboard/heladeras/baja",
                ServiceLocator.instanceOf(HeladerasController.class)::getBajaHeladera,
                TipoRol.ADMIN);

        app.get("/dashboard/contribuciones/carga",
                ServiceLocator.instanceOf(ContribucionesController.class)::getCargaCSV,
                TipoRol.ADMIN);
        app.get("/dashboard/contribuciones/archivo_de_ejemplo",
                ServiceLocator.instanceOf(ContribucionesController.class)::getCSVDeEjemplo,
                TipoRol.ADMIN);
        app.get("/dashboard/contribuciones/csv",
                ServiceLocator.instanceOf(ContribucionesController.class)::getHistorialCSVsAceptados,
                TipoRol.ADMIN);
        app.post("/dashboard/contribuciones/csv",
                ServiceLocator.instanceOf(ContribucionesController.class)::recibirCSV,
                TipoRol.ADMIN);
        app.get("/dashboard/contribuciones/csv/colaboraciones",
                ServiceLocator.instanceOf(ContribucionesController.class)::getColaboracionesAceptadas,
                TipoRol.ADMIN);
        app.get("/dashboard/contribuciones/csv/personas_fisicas",
                ServiceLocator.instanceOf(ContribucionesController.class)::getPersonasFisicasAceptadas,
                TipoRol.ADMIN);

        app.get("/dashboard/formularios",
                ServiceLocator.instanceOf(CuestionarioController.class)::getABMFormulario,
                TipoRol.ADMIN);
        app.get("/dashboard/formularios/alta",
                ServiceLocator.instanceOf(CuestionarioController.class)::getAltaFormulario,
                TipoRol.ADMIN);
        app.post("/dashboard/formularios/alta",
                ServiceLocator.instanceOf(CuestionarioController.class)::postAltaFormulario,
                TipoRol.ADMIN);
        app.get("/dashboard/formularios/alta/preguntas",
                ServiceLocator.instanceOf(CuestionarioController.class)::getAltaFormularioPreguntas,
                TipoRol.ADMIN);
        app.post("/dashboard/formularios/alta/preguntas",
                ServiceLocator.instanceOf(CuestionarioController.class)::postAltaFormularioPreguntas,
                TipoRol.ADMIN);
        app.get("/dashboard/formularios/modificacion",
                ServiceLocator.instanceOf(CuestionarioController.class)::getModificarFormulario,
                TipoRol.ADMIN);
        app.get("/dashboard/formularios/baja",
                ServiceLocator.instanceOf(CuestionarioController.class)::getBajaFormulario,
                TipoRol.ADMIN);

        //tecnico
        app.get("/dashboard/registrar_visita/paso1",
                ServiceLocator.instanceOf(TecnicoController.class)::getRegistrarVisitaPaso1,
                TipoRol.TECNICO);
        app.post("/dashboard/registrar_visita/paso1",
                ServiceLocator.instanceOf(TecnicoController.class)::postRegistrarVisitaPaso1,
                TipoRol.TECNICO);
        app.get("/dashboard/registrar_visita/paso2",
                ServiceLocator.instanceOf(TecnicoController.class)::getRegistrarVisitaPaso2,
                TipoRol.TECNICO);
        app.post("/dashboard/registrar_visita/paso2",
                ServiceLocator.instanceOf(TecnicoController.class)::postRegistrarVisitaPaso2,
                TipoRol.TECNICO);
        app.get("/dashboard/{id}/detalle_falla",
                ServiceLocator.instanceOf(TecnicoController.class)::getVerDetalleFalla,
                TipoRol.TECNICO, TipoRol.PERSONA_JURIDICA, TipoRol.ADMIN);
        app.get("/dashboard/alertas_activas",
                ServiceLocator.instanceOf(TecnicoController.class)::getVerAlertasActivas,
                TipoRol.TECNICO);
        app.post("/dashboard/alertas_activas",
                ServiceLocator.instanceOf(TecnicoController.class)::postVerAlertasActivas,
                TipoRol.TECNICO);
        app.get("/dashboard/asignacion_incidente",
                ServiceLocator.instanceOf(TecnicoController.class)::getAsignarIncidente,
                TipoRol.TECNICO);
        app.post("/dashboard/asignacion_incidente",
                ServiceLocator.instanceOf(TecnicoController.class)::postAsignarIncidente,
                TipoRol.TECNICO);

        app.get("/dashboard/tecnicos",
                ServiceLocator.instanceOf(TecnicoController.class)::getABMTecnico,
                TipoRol.ADMIN);
        app.get("/dashboard/tecnicos/alta",
                ServiceLocator.instanceOf(TecnicoController.class)::getAltaTecnico,
                TipoRol.ADMIN);
        app.post("/dashboard/tecnicos/alta",
                ServiceLocator.instanceOf(TecnicoController.class)::postAltaTecnico,
                TipoRol.ADMIN);
        app.get("/dashboard/tecnicos/modificacion",
                ServiceLocator.instanceOf(TecnicoController.class)::getModificarTecnicoPaso1,
                TipoRol.ADMIN);
        app.post("/dashboard/tecnicos/modificacion",
                ServiceLocator.instanceOf(TecnicoController.class)::postModificarTecnicoPaso1,
                TipoRol.ADMIN);
        app.get("/dashboard/tecnicos/modificacion/tecnico",
                ServiceLocator.instanceOf(TecnicoController.class)::getModificarTecnicoPaso2,
                TipoRol.ADMIN);
        app.post("/dashboard/tecnicos/modificacion/tecnico",
                ServiceLocator.instanceOf(TecnicoController.class)::postModificarTecnicoPaso2,
                TipoRol.ADMIN);
        app.get("/dashboard/tecnicos/baja",
                ServiceLocator.instanceOf(TecnicoController.class)::getBajaTecnico,
                TipoRol.ADMIN);
        app.post("/dashboard/tecnicos/baja",
                ServiceLocator.instanceOf(TecnicoController.class)::postBajaTecnico,
                TipoRol.ADMIN);

        //Tarjeta
        app.get("/dashboard/tarjetas",
                ServiceLocator.instanceOf(TarjetaController.class)::getABMTarjeta,
                TipoRol.ADMIN);
        app.get("/dashboard/tarjetas/alta",
                ServiceLocator.instanceOf(TarjetaController.class)::getAltaTarjeta,
                TipoRol.ADMIN);
        app.post("/dashboard/tarjetas/alta",
                ServiceLocator.instanceOf(TarjetaController.class)::postAltaTarjeta,
                TipoRol.ADMIN);
        app.get("/dashboard/tarjetas/alta_persona_fisica",
                ServiceLocator.instanceOf(TarjetaController.class)::getAltaTarjetaPaso2Fisica,
                TipoRol.ADMIN);
        app.post("/dashboard/tarjetas/alta_persona_fisica",
                ServiceLocator.instanceOf(TarjetaController.class)::postAltaTarjetaPaso2Fisica,
                TipoRol.ADMIN);
        app.get("/dashboard/tarjetas/alta_persona_vulnerable",
                ServiceLocator.instanceOf(TarjetaController.class)::getAltaTarjetaPaso2Vulnerable,
                TipoRol.ADMIN);
        app.post("/dashboard/tarjetas/alta_persona_vulnerable",
                ServiceLocator.instanceOf(TarjetaController.class)::postAltaTarjetaPaso2Vulnerable,
                TipoRol.ADMIN);
        app.get("/dashboard/tarjetas/baja",
                ServiceLocator.instanceOf(TarjetaController.class)::getBajaTarjeta,
                TipoRol.ADMIN);
        app.post("/dashboard/tarjetas/baja",
                ServiceLocator.instanceOf(TarjetaController.class)::postBajaTarjeta,
                TipoRol.ADMIN);
        app.get("/dashboard/tarjetas/baja_persona_fisica",
                ServiceLocator.instanceOf(TarjetaController.class)::getBajaTarjetaFisica,
                TipoRol.ADMIN);
        app.get("/dashboard/tarjetas/baja_persona_vulnerable",
                ServiceLocator.instanceOf(TarjetaController.class)::getBajaTarjetaVulnerable,
                TipoRol.ADMIN);
        app.post("/dashboard/tarjetas/baja_persona_fisica",
                ServiceLocator.instanceOf(TarjetaController.class)::postBajaTarjetaFisica,
                TipoRol.ADMIN);
        app.post("/dashboard/tarjetas/baja_persona_vulnerable",
                ServiceLocator.instanceOf(TarjetaController.class)::postBajaTarjetaVulnerable,
                TipoRol.ADMIN);
        app.get("/dashboard/tarjetas/modificacion",
                ServiceLocator.instanceOf(TarjetaController.class)::getModificarTarjeta,
                TipoRol.ADMIN);
        app.post("/dashboard/tarjetas/modificacion",
                ServiceLocator.instanceOf(TarjetaController.class)::postModificarTarjeta,
                TipoRol.ADMIN);
        app.get("/dashboard/tarjetas/modificacion_persona_fisica",
                ServiceLocator.instanceOf(TarjetaController.class)::getModificarTarjetaFisica,
                TipoRol.ADMIN);
        app.get("/dashboard/tarjetas/modificacion_persona_vulnerable",
                ServiceLocator.instanceOf(TarjetaController.class)::getModificarTarjetaVulnerable,
                TipoRol.ADMIN);
        app.post("/dashboard/tarjetas/modificacion_persona_fisica",
                ServiceLocator.instanceOf(TarjetaController.class)::postModificarTarjetaFisica,
                TipoRol.ADMIN);
        app.post("/dashboard/tarjetas/modificacion_persona_vulnerable",
                ServiceLocator.instanceOf(TarjetaController.class)::postModificarTarjetaVulnerable,
                TipoRol.ADMIN);


        //registro persona vulnerable
        app.get("/dashboard/registro_persona_vulnerable",
                ServiceLocator.instanceOf(PersonaVulnerableController.class)::index,
                TipoRol.PERSONA_FISICA);
        app.post("/dashboard/registro_persona_vulnerable",
                ServiceLocator.instanceOf(PersonaVulnerableController.class)::registroPersonaVulnerable,
                TipoRol.PERSONA_FISICA);
        app.get("/dashboard/registrar_menor_vulnerable",
                ServiceLocator.instanceOf(PersonaVulnerableController.class)::indexMenor,
                TipoRol.PERSONA_FISICA);
        app.post("/dashboard/registrar_menor_vulnerable",
                ServiceLocator.instanceOf(PersonaVulnerableController.class)::registroMenorVulnerable,
                TipoRol.PERSONA_FISICA);


        //falla tecnica
        app.get("/dashboard/reporte_falla_tecnica",
                ServiceLocator.instanceOf(FallaTecnicaController.class)::getReportarFallaTecnica,
                TipoRol.PERSONA_FISICA);
        app.post("/dashboard/reporte_falla_tecnica",
                ServiceLocator.instanceOf(FallaTecnicaController.class)::postReportarFallaTecnica,
                TipoRol.PERSONA_FISICA);

        //contribuciones pj
        app.get("/dashboard/contribuciones/publicacion_oferta",
                ServiceLocator.instanceOf(ContribucionesController.class)::publicarOferta,
                TipoRol.PERSONA_JURIDICA);
        app.post("/dashboard/contribuciones/publicacion_oferta",
                ServiceLocator.instanceOf(ContribucionesController.class)::save,
                TipoRol.PERSONA_JURIDICA);

        app.get("/dashboard/contribuciones/hacerse_cargo_de_heladera",
                ServiceLocator.instanceOf(ContribucionesController.class)::getHacerseCargoHeladeraFormulario,
                TipoRol.PERSONA_JURIDICA);
        app.post("/dashboard/contribuciones/hacerse_cargo_de_heladera",
                ServiceLocator.instanceOf(ContribucionesController.class)::postHacerseCargoHeladeraFormulario,
                TipoRol.PERSONA_JURIDICA);
        app.get("/puntosRecomendados/carga",
                ServiceLocator.instanceOf(ContribucionesController.class)::getPuntosRecomendados);
        app.get("/dashboard/contribuciones/hacerse_cargo_de_heladera/recomendaciones/paso1",
                ServiceLocator.instanceOf(ContribucionesController.class)::getHacerseCargoHeladeraRecomendacionesPaso1,
                TipoRol.PERSONA_JURIDICA);
        app.post("/dashboard/contribuciones/hacerse_cargo_de_heladera/recomendaciones/paso1",
                ServiceLocator.instanceOf(ContribucionesController.class)::postHacerseCargoHeladeraRecomendacionesPaso1,
                TipoRol.PERSONA_JURIDICA);
        app.get("/dashboard/contribuciones/hacerse_cargo_de_heladera/recomendaciones/paso2",
                ServiceLocator.instanceOf(ContribucionesController.class)::getHacerseCargoHeladeraRecomendacionesPaso2,
                TipoRol.PERSONA_JURIDICA);
        app.post("/dashboard/contribuciones/hacerse_cargo_de_heladera/recomendaciones/paso2",
                ServiceLocator.instanceOf(ContribucionesController.class)::postHacerseCargoHeladeraRecomendacionesPaso2,
                TipoRol.PERSONA_JURIDICA);
        app.get("/dashboard/contribuciones/hacerse_cargo_de_heladera/recomendaciones/paso3",
                ServiceLocator.instanceOf(ContribucionesController.class)::getHacerseCargoHeladeraRecomendacionesPaso3,
                TipoRol.PERSONA_JURIDICA);
        app.post("/dashboard/contribuciones/hacerse_cargo_de_heladera/recomendaciones/paso3",
                ServiceLocator.instanceOf(ContribucionesController.class)::postHacerseCargoHeladeraRecomendacionesPaso3,
                TipoRol.PERSONA_JURIDICA);

        //generales
        app.get("/exito", ServiceLocator.instanceOf(ExitoController.class)::index);
    }
}
