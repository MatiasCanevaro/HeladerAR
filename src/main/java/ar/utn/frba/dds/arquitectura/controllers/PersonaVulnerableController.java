package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.dtos.DTOCiudad;
import ar.utn.frba.dds.arquitectura.dtos.DTOProvincia;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOCiudad;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOProvincia;
import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import ar.utn.frba.dds.arquitectura.utils.SessionUtils;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.GeneradorDeCodigoAlfanumerico;
import ar.utn.frba.dds.modelos.entidades.geografia.Ciudad;
import ar.utn.frba.dds.modelos.entidades.geografia.Direccion;
import ar.utn.frba.dds.modelos.entidades.geografia.Provincia;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.CalculadoraDePuntos;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaVulnerable;
import ar.utn.frba.dds.modelos.entidades.personas.TipoDocumento;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;
import ar.utn.frba.dds.modelos.entidades.tarjetas.Tarjeta;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import ar.utn.frba.dds.modelos.repositorios.RepositorioProvincia;
import ar.utn.frba.dds.modelos.repositorios.RepositorioTarjeta;
import ar.utn.frba.dds.modulos.importador.MapperImportador;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@AllArgsConstructor
public class PersonaVulnerableController implements ICrudViewsHandler {
    private Repositorio repositorio;
    private RepositorioProvincia repositorioProvincia;
    private RepositorioTarjeta repositorioTarjeta;
    private MapperImportador mapperImportador;
    private CalculadoraDePuntos calculadoraDePuntos;
    @Override
    public void index(Context context) {
        Map<String, Object> model = new HashMap<>();
        List<Object> todasLasCiudades = repositorio.buscarTodos("Ciudad");
        List<DTOCiudad> ciudadesMapeadas = MapperDTOCiudad.convertirListaADTOs(todasLasCiudades);
        model.put("ciudades", ciudadesMapeadas);
        model.put("titulo", "Registro de Persona Vulnerable");
        context.render("contribuciones/persona_fisica/registrar_persona_vulnerable.hbs",model);
    }

    public void indexMenor(Context context){
        Map<String, Object> model = new HashMap<>();
        List<Object> todasLasCiudades = repositorio.buscarTodos("Ciudad");
        List<DTOCiudad> ciudadesMapeadas = MapperDTOCiudad.convertirListaADTOs(todasLasCiudades);
        model.put("ciudades", ciudadesMapeadas);
        model.put("titulo", "Registro de un Menor Vulnerable");
        context.render("contribuciones/persona_fisica/registrar_menor_vulnerable.hbs", model);
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

    public void registroPersonaVulnerable(Context context) {
        List<String> errores = new ArrayList<>();

        String nombre = context.formParam("nombre");
        String apellido = context.formParam("apellido");
        String fechaNacimiento = context.formParam("fechaNacimiento");
        String calle = context.formParam("calle");
        String altura = context.formParam("altura");
        String piso = context.formParam("piso");
        String codigoPostal = context.formParam("codigoPostal");
        String cantidadDeMenoresACargo = context.formParam("menores");
        String ciudadId = context.formParam("ciudad");
        String tipoDocumento = context.formParam("id");
        String numeroDocumento = context.formParam("numeroDocumento");

        // Parse y validación de datos
        assert cantidadDeMenoresACargo != null;
        int cantMenores = Integer.parseInt(cantidadDeMenoresACargo);

        if (cantMenores < 0) {
            errores.add("La cantidad de menores a cargo no puede ser negativa");
        }

        if (LocalDate.parse(fechaNacimiento).isAfter(LocalDate.now())) {
            errores.add("La fecha de nacimiento tiene que ser una fecha coherente");
        }

        // Mapea campos al modelo para que queden visibles en la recarga
        Map<String, Object> model = new HashMap<>();
        model.put("nombre", nombre);
        model.put("apellido", apellido);
        model.put("fechaNacimiento", fechaNacimiento);
        model.put("calle", calle);
        model.put("altura", altura);
        model.put("piso", piso);
        model.put("codigoPostal", codigoPostal);
        model.put("ciudad", ciudadId);
        model.put("tipoDocumento", tipoDocumento);
        model.put("numeroDocumento", numeroDocumento);
        model.put("cantidadDeMenoresACargo", cantidadDeMenoresACargo);
        model.put("errores", errores);

        if (!errores.isEmpty()) {
            List<Ciudad> ciudades = repositorio.buscarTodos("Ciudad")
                    .stream()
                    .map(c -> (Ciudad) c)
                    .toList();
            model.put("ciudades", ciudades);
            model.put("errores", errores);
            // Renderiza la vista con los datos ingresados y los errores
            context.render("contribuciones/persona_fisica/registrar_persona_vulnerable.hbs", model);
        } else {
            String userId = context.sessionAttribute("userId");
            // Procesa y guarda la persona vulnerable si no hay errores
            PersonaFisica personaFisica = (PersonaFisica) repositorio.buscarPorId(userId, PersonaFisica.class.getSimpleName());
            Double puntosASumar = calculadoraDePuntos.calcularPuntosTarjetasDistribuidas(1);
            personaFisica.sumarPuntosYResetearAtributosDeColaboraciones(puntosASumar);


            Ciudad ciudadEncontrada = (Ciudad) repositorio.buscarPorId(ciudadId, "Ciudad");
            Direccion direccion = new Direccion(calle, altura, piso, codigoPostal, ciudadEncontrada, ciudadEncontrada.getProvincia());
            repositorio.guardar(direccion);
            TipoDocumento tipoDocumentoMapeado = mapperImportador.tipoDocumentoMapper(tipoDocumento);

            PersonaVulnerable personaVulnerable = new PersonaVulnerable(
                    nombre, apellido, LocalDate.parse(fechaNacimiento), direccion, numeroDocumento, tipoDocumentoMapeado, null
            );

            repositorio.guardar(personaVulnerable);

            String codigo = GeneradorDeCodigoAlfanumerico.generarCodigo(repositorioTarjeta);
            Tarjeta tarjeta = new Tarjeta(
                    codigo,
                    personaVulnerable,
                    personaFisica,
                    null,
                    4);
            repositorio.guardar(tarjeta);

            if (cantMenores == 0) {
                SessionUtils.mostrarPantallaDeExito(context,
                        "Persona Vulnerable registrada con éxito",
                        "Dashboard",
                        "/dashboard"
                );
            } else {
                context.sessionAttribute("personaVulnerableId",personaVulnerable.getId());

                context.sessionAttribute("cantidadDePantallasMostradas", 1);
                model.put("cantidadDePantallasMostradas", 1);

                model.put("cantidadDePantallasAMostrar", cantMenores);

                context.sessionAttribute("cantidadDePantallasRestantesAMostrar", cantMenores);

                List<Ciudad> ciudades = repositorio.buscarTodos("Ciudad")
                        .stream()
                        .map(c -> (Ciudad) c)
                        .toList();
                model.put("ciudades", ciudades);

                context.render("contribuciones/persona_fisica/registrar_menor_vulnerable.hbs", model);
            }
        }
    }

    public void registroMenorVulnerable(Context context) {
        List<String> errores = new ArrayList<>();

        String nombre = context.formParam("nombre");
        String apellido = context.formParam("apellido");
        String fechaNacimiento = context.formParam("fechaNacimiento");
        String calle = context.formParam("calle");
        String altura = context.formParam("altura");
        String piso = context.formParam("piso");
        String codigoPostal = context.formParam("codigoPostal");
        String ciudadId = context.formParam("ciudad");
        String tipoDocumento = context.formParam("id");
        String numeroDocumento = context.formParam("numeroDocumento");

        // Validación de datos
        if (fechaNacimiento != null && LocalDate.parse(fechaNacimiento).isAfter(LocalDate.now())) {
            errores.add("La fecha de nacimiento tiene que ser una fecha coherente");
        }

        // Mapea campos al modelo para que queden visibles en la recarga
        Map<String, Object> model = new HashMap<>();
        model.put("nombre", nombre);
        model.put("apellido", apellido);
        model.put("fechaNacimiento", fechaNacimiento);
        model.put("calle", calle);
        model.put("altura", altura);
        model.put("piso", piso);
        model.put("codigoPostal", codigoPostal);
        model.put("ciudad", ciudadId);
        model.put("tipoDocumento", tipoDocumento);
        model.put("numeroDocumento", numeroDocumento);
        model.put("errores", errores);

        if (!errores.isEmpty()) {
            // Si hay errores, renderiza la vista con los datos ingresados y los errores
            Integer cantidadDePantallasMostradas = context.sessionAttribute("cantidadDePantallasMostradas");
            Integer cantidadDePantallasRestantesAMostrar = context.sessionAttribute("cantidadDePantallasRestantesAMostrar");
            model.put("cantidadDePantallasMostradas", cantidadDePantallasMostradas);
            model.put("cantidadDePantallasAMostrar", cantidadDePantallasRestantesAMostrar);

            List<Ciudad> ciudades = repositorio.buscarTodos("Ciudad")
                    .stream()
                    .map(c -> (Ciudad) c)
                    .toList();
            model.put("ciudades", ciudades);

            context.render("contribuciones/persona_fisica/registrar_menor_vulnerable.hbs", model);
        } else {
            String personaVulnerableId = context.sessionAttribute("personaVulnerableId");
            PersonaVulnerable personaMayorVulnerable =
                    (PersonaVulnerable) repositorio.buscarPorId(personaVulnerableId, PersonaVulnerable.class.getSimpleName());

            Ciudad ciudadEncontrada = (Ciudad) repositorio.buscarPorId(ciudadId, "Ciudad");
            Direccion direccion = new Direccion(calle, altura, piso, codigoPostal, ciudadEncontrada, ciudadEncontrada.getProvincia());
            repositorio.guardar(direccion);
            TipoDocumento tipoDocumentoMapeado = mapperImportador.tipoDocumentoMapper(tipoDocumento);

            PersonaVulnerable personaMenorVulnerable = new PersonaVulnerable(
                    nombre,
                    apellido,
                    LocalDate.parse(fechaNacimiento),
                    direccion,
                    numeroDocumento,
                    tipoDocumentoMapeado,
                    null
            );
            repositorio.guardar(personaMenorVulnerable);
            personaMayorVulnerable.agregarMenorACargo(personaMenorVulnerable);
            repositorio.guardar(personaMayorVulnerable);

            String userId = context.sessionAttribute("userId");
            PersonaFisica personaFisica = (PersonaFisica) repositorio.buscarPorId(userId, PersonaFisica.class.getSimpleName());
            String codigo = GeneradorDeCodigoAlfanumerico.generarCodigo(repositorioTarjeta);
            Tarjeta tarjeta = new Tarjeta(
                    codigo,
                    personaMenorVulnerable,
                    personaFisica,
                    null,
                    4);
            repositorio.guardar(tarjeta);

            Integer cantidadDePantallasRestantesAMostrar = context.sessionAttribute("cantidadDePantallasRestantesAMostrar");
            cantidadDePantallasRestantesAMostrar -= 1;

            if (cantidadDePantallasRestantesAMostrar == 0) {
                SessionUtils.mostrarPantallaDeExito(context,
                        "Personas Vulnerables registradas con éxito",
                        "Dashboard",
                        "/dashboard"
                );
            } else {
                context.sessionAttribute("cantidadDePantallasRestantesAMostrar", cantidadDePantallasRestantesAMostrar);

                Integer cantidadDePantallasMostradas = (Integer) context.sessionAttribute("cantidadDePantallasMostradas");
                cantidadDePantallasMostradas += 1;
                context.sessionAttribute("cantidadDePantallasMostradas", cantidadDePantallasMostradas);
                model.put("cantidadDePantallasMostradas", cantidadDePantallasMostradas);
                model.put("cantidadDePantallasAMostrar", cantidadDePantallasRestantesAMostrar);

                List<Object> todasLasCiudades = repositorio.buscarTodos("Ciudad");
                List<DTOCiudad> todasLasCiudadesMapeadas = MapperDTOCiudad.convertirListaADTOs(todasLasCiudades);
                model.put("ciudades", todasLasCiudadesMapeadas);

                context.render("contribuciones/persona_fisica/registrar_menor_vulnerable.hbs", model);
            }
        }
    }

}
