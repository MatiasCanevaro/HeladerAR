package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.config.ServiceLocator;
import ar.utn.frba.dds.arquitectura.dtos.*;
import ar.utn.frba.dds.arquitectura.mappers.*;
import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import ar.utn.frba.dds.arquitectura.utils.SessionUtils;
import ar.utn.frba.dds.modelos.entidades.contacto.Administrador;
import ar.utn.frba.dds.modelos.entidades.csv.CSV;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.*;
import ar.utn.frba.dds.modelos.entidades.geografia.*;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.heladeras.ModeloDeHeladera;
import ar.utn.frba.dds.modelos.entidades.heladeras.Vianda;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.CalculadoraDePuntos;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Oferta;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Rubro;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import ar.utn.frba.dds.modelos.entidades.personas.TipoContacto;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;
import ar.utn.frba.dds.modelos.entidades.suscripciones.*;
import ar.utn.frba.dds.modelos.entidades.tarjetas.AccionSolicitada;
import ar.utn.frba.dds.modelos.entidades.tarjetas.SolicitudDeApertura;
import ar.utn.frba.dds.modelos.repositorios.*;
import ar.utn.frba.dds.modulos.importador.MapperImportador;
import ar.utn.frba.dds.modulos.lector.Lector;
import ar.utn.frba.dds.modulos.recomendadoraDeLugaresDeDonacion.RecomendadoraDeLugaresDeDonacionAPI;
import ar.utn.frba.dds.modulos.recomendadoraDeLugaresDeDonacion.entities.ListadoDeLugares;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.UploadedFile;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import lombok.AllArgsConstructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;



@AllArgsConstructor
public class ContribucionesController implements ICrudViewsHandler {
    private Repositorio repositorio;
    private RepositorioRubro repositorioRubro;
    private RepositorioMotivoDistribucion repositorioMotivoDistribucion;
    private RepositorioProvincia repositorioProvincia;
    private RepositorioCiudad repositorioCiudad;
    private RepositorioModeloHeladera repositorioModeloHeladera;
    private MapperImportador mapperImportador;
    private CalculadoraDePuntos calculadoraDePuntos;
    private RecomendadoraDeLugaresDeDonacionAPI recomendadoraDeLugaresDeDonacionAPI;
    private Lector lector;
    private RepositorioCSV repositorioCSV;
    private String rutaCSVEjemplo;
    private String apiKey;
    private String rutaCarpetaCSVSubidos;

    public ContribucionesController(
            Repositorio repositorio, RepositorioRubro repositorioRubro,
            RepositorioMotivoDistribucion repositorioMotivoDistribucion,
            RepositorioProvincia repositorioProvincia,RepositorioCiudad repositorioCiudad,
            RepositorioModeloHeladera repositorioModeloHeladera,
            MapperImportador mapperImportador, CalculadoraDePuntos calculadoraDePuntos,
            RecomendadoraDeLugaresDeDonacionAPI recomendadoraDeLugaresDeDonacionAPI, Lector lector,
            RepositorioCSV repositorioCSV) {
        this.repositorio = repositorio;
        this.repositorioRubro = repositorioRubro;
        this.repositorioMotivoDistribucion = repositorioMotivoDistribucion;
        this.repositorioProvincia = repositorioProvincia;
        this.repositorioCiudad = repositorioCiudad;
        this.repositorioModeloHeladera = repositorioModeloHeladera;
        this.mapperImportador = mapperImportador;
        this.calculadoraDePuntos = calculadoraDePuntos;
        this.recomendadoraDeLugaresDeDonacionAPI = recomendadoraDeLugaresDeDonacionAPI;
        this.lector = lector;
        this.repositorioCSV = repositorioCSV;
    }

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
        String name = context.formParam("nombre");
        Double valuePoints = Double.parseDouble(context.formParam("valor_puntos"));
        String description = context.formParam("descripcion");
        String rubroNombre = context.formParam("rubroName");
        String userId = context.sessionAttribute("userId");

        // Obtener el archivo de imagen subido
        UploadedFile uploadedFile = context.uploadedFile("pathImagen");
        String pathImagen = null;

        if (uploadedFile != null) {
            try {

                Path ofertasDir = Paths.get("./ofertas");


                String originalFileName = uploadedFile.filename();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String newFileName = UUID.randomUUID().toString() + fileExtension;
                Path imagePath = ofertasDir.resolve(newFileName);

                try (InputStream inputStream = uploadedFile.content()) {
                    Files.copy(inputStream, imagePath);
                }

                pathImagen = "/ofertas/" + newFileName;

                System.out.println("Imagen guardada en: " + pathImagen);
            } catch (IOException e) {
                context.status(500).result("Error al procesar la imagen");
                e.printStackTrace();
                return;
            }
        }

        PersonaJuridica personaJuridica =
                (PersonaJuridica) repositorio.buscarPorId(userId, PersonaJuridica.class.getSimpleName());

        Oferta oferta = new Oferta();
        oferta.setNombre(name);
        oferta.setCantidadDePuntosNecesariosParaAccederAlBeneficio(valuePoints);
        oferta.setDescripcion(description);
        oferta.setPathImagen(pathImagen);

        Rubro rubro = repositorioRubro.buscarPorNombre(rubroNombre);
        oferta.setRubro(rubro);
        personaJuridica.agregarOferta(oferta);

        repositorio.guardar(oferta);

        ServiceLocator.instanceOf(StepMeterRegistry.class).counter("OfertasPublicadas", "status", "ok").increment();

        SessionUtils.mostrarPantallaDeExito(context,
                "Oferta creada con éxito",
                "Dashboard - Contribuciones",
                "/dashboard/contribuciones");
    }

    @Override
    public void edit(Context context) {
    }

    @Override
    public void update(Context context) {
        String userId = context.sessionAttribute("userId");
        String tipoRol = context.sessionAttribute("tipoRol");

        String money = context.formParam("money");

        if (tipoRol.equals(TipoRol.PERSONA_FISICA.name())) {
            PersonaFisica personaFisica =
                    (PersonaFisica) repositorio.buscarPorId(userId, PersonaFisica.class.getSimpleName());
            String foodDistribution = context.formParam("food-distridistribution");
            String cardDistribution = context.formParam("cards-distribution");
            String foodDonation = context.formParam("food-donation");
            List<String> listaFormasDeColaboracion = Stream.of(money, foodDistribution, cardDistribution, foodDonation)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            List<FormaDeColaboracion> formasDeColaboracion = mapperImportador.
                    listaDeFormaDeColaboracionMapper(listaFormasDeColaboracion);
            personaFisica.setFormasDeColaboracion(formasDeColaboracion);
            repositorio.actualizar(personaFisica);
        } else if (tipoRol.equals(TipoRol.PERSONA_JURIDICA.name())) {
            PersonaJuridica personaJuridica =
                    (PersonaJuridica) repositorio.buscarPorId(userId, PersonaJuridica.class.getSimpleName());
            String fridge = context.formParam("fridge");
            String ps = context.formParam("ps");
            List<String> listaFormasDeColaboracion = Stream.of(money, fridge, ps)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            List<FormaDeColaboracion> formasDeColaboracion =
                    mapperImportador.listaDeFormaDeColaboracionMapper(listaFormasDeColaboracion);
            personaJuridica.setFormasDeColaboracion(formasDeColaboracion);
            repositorio.actualizar(personaJuridica);
        }

        context.redirect("/dashboard/contribuciones");
    }

    @Override
    public void delete(Context context) {

    }

    public void donacionDinero(Context context){
        String frecuenciaPersonalizada = context.formParam("custom-amount");
        String monto = context.formParam("monto");
        String frecuencia = context.formParam("frequency");

        String userId = context.sessionAttribute("userId");
        String tipoRol = context.sessionAttribute("tipoRol");

        PersonaFisica personaFisica = null;
        PersonaJuridica personaJuridica = null;

        Double puntosASumar = 0.0;
        puntosASumar = calculadoraDePuntos.calcularPuntosPesosDonados(Double.parseDouble(monto));

        if (tipoRol.equals(TipoRol.PERSONA_FISICA.name())) {
            personaFisica = (PersonaFisica) repositorio.buscarPorId(userId, PersonaFisica.class.getSimpleName());
            personaFisica.sumarPuntosYResetearAtributosDeColaboraciones(puntosASumar);
        } else if (tipoRol.equals(TipoRol.PERSONA_JURIDICA.name())) {
            personaJuridica = (PersonaJuridica) repositorio.buscarPorId(userId, PersonaJuridica.class.getSimpleName());
            personaJuridica.sumarPuntosYResetearAtributosDeColaboraciones(puntosASumar);
        }

        DonacionDeDinero donacionDeDinero = DonacionDeDinero.crearDonacionDeDinero(
                FormaDeColaboracion.DONACION_DE_DINERO,
                personaFisica,
                personaJuridica,
                Double.parseDouble(monto),
                null
        );

        if(frecuenciaPersonalizada!=null){
            donacionDeDinero.setFrecuenciaEnDia(Integer.parseInt(frecuenciaPersonalizada));
        }
        else{
            donacionDeDinero.setFrecuenciaEnDia(Integer.parseInt(frecuencia));
        }

        repositorio.guardar(donacionDeDinero);

        SessionUtils.mostrarPantallaDeExito(context,
                "Donación de dinero realizada con éxito",
                "Dashboard - Contribuciones",
                "/dashboard/contribuciones");
    }

    public void mostrarDonacionDinero(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Dashboard Donacion de Dinero");
        context.render("contribuciones/donacion_dinero.hbs", model);
    }

    public void verFormaDeContribucion(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Dashboard Forma de Contribucion");

        String tipoRol = context.sessionAttribute("tipoRol");

        if (tipoRol.equals(TipoRol.PERSONA_FISICA.name())) {
            context.render("contribuciones/persona_fisica/cambiar_fisica_contribuciones.hbs", model);
        } else if (tipoRol.equals(TipoRol.PERSONA_JURIDICA.name())) {
            context.render("contribuciones/persona_juridica/cambiar_juridica_contribuciones.hbs", model);
        }
    }

    public void publicarOferta(Context context){
        Map<String, Object> model = new HashMap<>();
        List<Object> todosLosRubros = repositorio.buscarTodos("Rubro");
        List<DTORubro> dtosRubro = MapperDTORubro.convertirListaADTOs(todosLosRubros);

        model.put("titulo", "Publicar Oferta");
        model.put("rubros", dtosRubro);
        context.render("contribuciones/persona_juridica/publicar_oferta.hbs", model);
    }

    public void getDistribucionViandaPaso1(Context context){
        Map<String, Object> model = new HashMap<>();

        List<Object> todasLasHeladeras = repositorio.buscarTodos("Heladera");
        List<DTOHeladera> dtoTodasLasHeladeras = MapperDTOHeladera.convertirListaADTOs(todasLasHeladeras);

        List<DTOHeladera> dtoHeladeras = dtoTodasLasHeladeras.stream()
                .filter(dtoHeladera -> !dtoHeladera.getViandas().isEmpty())
                .toList();

        List<DTODisplayDistribucionDeVianda> listaDTODisplayDistribucionDeVianda = new ArrayList<>();

        for (DTOHeladera dtoHeladera : dtoHeladeras) {
            Integer cantidadViandasDisponibles = dtoHeladera.getViandas().size();

            agregarDisplayALista(listaDTODisplayDistribucionDeVianda, dtoHeladera, cantidadViandasDisponibles);
        }
        LocalDateTime ahora = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaFormateada = ahora.format(formatter);

        model.put("heladeras", listaDTODisplayDistribucionDeVianda);
        model.put("fecha_y_hora", fechaFormateada);
        model.put("titulo", "Distribucion de Vianda - Origen");
        context.render("contribuciones/persona_fisica/distribucion_vianda1.hbs",model);
    }

    public void postDistribucionViandaPaso1(Context context){
        String idHeladeraOrigen = context.formParam("selectedOptionOrigen");
        if (idHeladeraOrigen == null) {
            Map<String, Object> model = new HashMap<>();
            model.put("errores", "Por favor seleccione una heladera de origen");
            getDistribucionViandaPaso1(context);
        }else{
            context.sessionAttribute("idHeladeraOrigen",idHeladeraOrigen);
            context.redirect("/dashboard/contribuciones/distribucion_vianda/paso2");
        }
    }

    public void getDistribucionViandaPaso2(Context context){
        Map<String, Object> model = new HashMap<>();


        List<Object> todasLasHeladeras = repositorio.buscarTodos("Heladera");
        List<DTOHeladera> dtoTodasLasHeladeras = MapperDTOHeladera.convertirListaADTOs(todasLasHeladeras);

        List<DTOHeladera> dtosHeladera = dtoTodasLasHeladeras.stream()
                .filter(dtoHeladera -> !dtoHeladera.getViandas().isEmpty())
                .toList();

        String idHeladeraOrigen = context.sessionAttribute("idHeladeraOrigen");
        Heladera heladeraOrigen = (Heladera) repositorio.buscarPorId(idHeladeraOrigen,"Heladera");
        DTOHeladera dtoHeladeraOrigen = MapperDTOHeladera.convertirADTO(heladeraOrigen);

        // Excluyo a la heladera origen de las posibilidades de destino
        List<DTOHeladera> dtosHeladeraSinOrigen = dtosHeladera.stream()
                .filter(dtoHeladera -> !dtoHeladera.getId().equals(dtoHeladeraOrigen.getId()))
                .toList();

        List<DTODisplayDistribucionDeVianda> listaDTODisplayDistribucionDeVianda = new ArrayList<>();
        for (DTOHeladera dtoHeladera : dtosHeladeraSinOrigen) {
            Integer cantidadViandasDisponibles = dtoHeladera.getViandas().size();
            if(cantidadViandasDisponibles < dtoHeladera.getModelo().getCapacidadEnViandas() && dtoHeladera.getEstaActiva()){
                agregarDisplayALista(listaDTODisplayDistribucionDeVianda, dtoHeladera, cantidadViandasDisponibles);
            }
        }
        if(!listaDTODisplayDistribucionDeVianda.isEmpty()) {
            model.put("heladeras", listaDTODisplayDistribucionDeVianda);
        }else{
            model.put("mensaje", "No hay heladeras disponibles para recibir viandas");
        }

        LocalDateTime ahora = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaFormateada = ahora.format(formatter);

        model.put("fecha_y_hora", fechaFormateada);
        model.put("titulo", "Distribucion de Vianda - Destino");
        context.render("contribuciones/persona_fisica/distribucion_vianda2.hbs",model);
    }

    private void agregarDisplayALista(List<DTODisplayDistribucionDeVianda> listaDTODisplayDistribucionDeVianda, DTOHeladera dtoHeladera, Integer cantidadViandasDisponibles) {
        String direccion = dtoHeladera.getPuntoEstrategico().getDireccion().getCalle() + " "
                + dtoHeladera.getPuntoEstrategico().getDireccion().getAltura();
        if (dtoHeladera.getPuntoEstrategico().getDireccion().getPiso() != null) {
            direccion += " " + dtoHeladera.getPuntoEstrategico().getDireccion().getPiso() + "°";
        }

        DTODisplayDistribucionDeVianda DTODisplayDistribucionDeVianda = new DTODisplayDistribucionDeVianda(
                dtoHeladera.getId(),
                dtoHeladera.getPuntoEstrategico().getDireccion().getCiudad().getProvincia(),
                direccion,
                Integer.parseInt(dtoHeladera.getPuntoEstrategico().getDireccion().getCodigoPostal()),
                cantidadViandasDisponibles,
                dtoHeladera.getEstaActiva());
        listaDTODisplayDistribucionDeVianda.add(DTODisplayDistribucionDeVianda);
    }

    public void postDistribucionViandaPaso2(Context context){
        String idHeladeraDestino = context.formParam("selectedOptionDestino");
        if (idHeladeraDestino == null) {
            Map<String, Object> model = new HashMap<>();
            model.put("errores", "Por favor seleccione una heladera de destino");
            getDistribucionViandaPaso2(context);
        }else{
            context.sessionAttribute("idHeladeraDestino",idHeladeraDestino);
            context.redirect("/dashboard/contribuciones/distribucion_vianda/paso3");
        }
    }

    public void getDistribucionViandaPaso3(Context context){
        Map<String, Object> model = new HashMap<>();

        // Heladera origen
        String idHeladeraOrigen = context.sessionAttribute("idHeladeraOrigen");
        Heladera heladeraOrigen = (Heladera) repositorio.buscarPorId(idHeladeraOrigen,"Heladera");

        // Viandas heladera origen
        List<Vianda> viandas = heladeraOrigen.getViandas();
        List<DTOVianda> viandasMapeadas = MapperDTOVianda.convertirListaADTOs(viandas);

        // Motivos
        List<Object> motivos = this.repositorio.buscarTodos(MotivoDistribucion.class.getName());
        List<DTOMotivoDistribucion> dtosMotivos = MapperDTOMotivoDistribucion.convertirListaADTOs(motivos);

        LocalDateTime ahora = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaFormateada = ahora.format(formatter);

        model.put("titulo", "Distribucion de Vianda");
        model.put("fecha_y_hora", fechaFormateada);
        model.put("motivos", dtosMotivos);
        model.put("viandas", viandasMapeadas);
        context.render("contribuciones/persona_fisica/distribucion_vianda3.hbs",model);
    }

    public void postDistribucionViandaPaso3(Context context){
        String idHeladeraOrigen = context.sessionAttribute("idHeladeraOrigen");
        String idHeladeraDestino = context.sessionAttribute("idHeladeraDestino");
        List<String> viandasOrigen = context.formParams("viandas");
        Integer cantidadViandas = viandasOrigen.size();


        String userId = context.sessionAttribute("userId");
        PersonaFisica personaFisica = (PersonaFisica) repositorio.buscarPorId(userId, PersonaFisica.class.getSimpleName());
        Double puntosASumar = calculadoraDePuntos.calcularPuntosViandasDistribuidas(cantidadViandas);
        personaFisica.sumarPuntosYResetearAtributosDeColaboraciones(puntosASumar);

        // Busco las heladeras elegidas
        Heladera heladeraOrigen = (Heladera) repositorio.buscarPorId(idHeladeraOrigen,"Heladera");
        Heladera heladeraDestino = (Heladera) repositorio.buscarPorId(idHeladeraDestino,"Heladera");

        // Busco motivo distribucion
        String nombreMotivo = context.formParam("nombreMotivo");
        MotivoDistribucion motivoDistribucion = repositorioMotivoDistribucion.buscarPorNombre(nombreMotivo);
        //TODO setear atributos de suscripcion. preguntarle a Gari si no se acuerdan

        // Instancio y guardo la distribucion
        DistribucionDeVianda distribucionDeVianda = new DistribucionDeVianda(FormaDeColaboracion.DISTRIBUCION_DE_VIANDA,
                heladeraOrigen,
                heladeraDestino,
                cantidadViandas,
                motivoDistribucion,
                LocalDate.now(),
                personaFisica
        );
        repositorio.guardar(distribucionDeVianda);

        // Muevo las viandas de una heladera a otra
        // por ahora esta en broker esta logica (en receptor pase de tarjeta)
        List<Vianda> viandas = new ArrayList<>();
        for (String idVianda : viandasOrigen) {
            Vianda vianda = (Vianda) repositorio.buscarPorId(idVianda,"Vianda");
            viandas.add(vianda);
        }

        SolicitudDeApertura solicitudDeAperturaOrigen = new SolicitudDeApertura(
                personaFisica,
                heladeraOrigen,
                LocalDateTime.now(),
                AccionSolicitada.RETIRO,
                false
        );
        solicitudDeAperturaOrigen.agregarViandas(viandas);
        SolicitudDeApertura solicitudDeAperturaDestino = new SolicitudDeApertura(
                personaFisica,
                heladeraDestino,
                LocalDateTime.now(),
                AccionSolicitada.INGRESO,
                false
        );
        solicitudDeAperturaDestino.agregarViandas(viandas);
        repositorio.guardar(solicitudDeAperturaDestino);
        repositorio.guardar(solicitudDeAperturaOrigen);

        heladeraOrigen.descontarViandas(viandas);
        heladeraDestino.agregarViandas(viandas);

        repositorio.actualizar(heladeraOrigen);
        repositorio.actualizar(heladeraDestino);

        for(Vianda vianda : viandas){
            repositorio.actualizar(vianda);
        }


        SessionUtils.mostrarPantallaDeExito(context,
                "Distribución de vianda registrada con éxito",
                "Dashboard",
                "/dashboard");
    }

    public void getHacerseCargoHeladeraFormulario(Context context){
        Map<String, Object> model = new HashMap<>();

        List<Object> todosLosModelos = repositorio.buscarTodos("ModeloDeHeladera").stream()
                .distinct()
                .collect(Collectors.toList());
        List<DTOModeloDeHeladera> modelosMapeadas = MapperDTOModeloDeHeladera.convertirListaADTOs(todosLosModelos);

        List<Object> todasLasCiudades = repositorio.buscarTodos("Ciudad");
        List<DTOCiudad> ciudadesMapeadas = MapperDTOCiudad.convertirListaADTOs(todasLasCiudades);

        model.put("titulo", "Hacerse Cargo de Heladera");
        model.put("modelos", modelosMapeadas);
        model.put("ciudades", ciudadesMapeadas);
        context.render("contribuciones/persona_juridica/hacerse_cargo_heladera1.hbs",model);
    }

    public void postHacerseCargoHeladeraFormulario(Context context){
        String calle = context.formParam("calleProfile");
        String altura = context.formParam("altura");
        String piso = context.formParam("piso");
        String cp = context.formParam("cp");
        String nombreModeloHeladera = context.formParam("modeloHeladera");
        String ciudadId = context.formParam("ciudad");

        Ciudad ciudad = (Ciudad) repositorio.buscarPorId(ciudadId,"Ciudad");
        ModeloDeHeladera modeloDeHeladera = repositorioModeloHeladera.buscarPorNombre(nombreModeloHeladera);

        Direccion direccion = new Direccion(
                calle,
                altura,
                piso,
                cp,
                ciudad,
                ciudad.getProvincia()
        );
        repositorio.guardar(direccion);
        Map <String, String> coordenadas = Nominative.obtenerCoordenadas(calle, altura, ciudad.getNombre());
        PuntoEstrategico puntoEstrategico = new PuntoEstrategico(
                calle,
                coordenadas.get("lat"),
                coordenadas.get("lon"),
                direccion
        );
        repositorio.guardar(puntoEstrategico);

        Heladera heladera = new Heladera(modeloDeHeladera,puntoEstrategico);

        String userId = context.sessionAttribute("userId");
        PersonaJuridica personaJuridica = (PersonaJuridica) repositorio.buscarPorId(userId,"PersonaJuridica");
        personaJuridica.agregarHeladera(heladera);

        repositorio.guardar(personaJuridica);
        repositorio.guardar(heladera);

        SessionUtils.mostrarPantallaDeExito(context,
                "Usted se hizo cargo de una heladera correctamente",
                "Dashboard - Contribuciones",
                "/dashboard/contribuciones");
    }

    public void getHacerseCargoHeladeraRecomendacionesPaso1(Context context) {
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Recomendaciones Paso 1");
        context.render("contribuciones/persona_juridica/hacerse_cargo_heladera_paso1.hbs",model);
    }

    public void postHacerseCargoHeladeraRecomendacionesPaso1(Context context) {
        Double latitud = Double.parseDouble(context.formParam("latitud"));
        Double longitud = Double.parseDouble(context.formParam("longitud"));

        context.sessionAttribute("latitudRecomendada", latitud);
        context.sessionAttribute("longitudRecomendada", longitud);

        Map<String, Object> response = new HashMap<>();

        Map<String, String> direccion = Nominative.obtenerDireccion(String.valueOf(latitud), String.valueOf(longitud));
        String provincia = direccion.get("provincia");
        String provinciaMapeada = this.provinciaMapper(provincia);

        if (provinciaMapeada != null) {
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("error", "Provincia no reconocida");
        }
        context.json(response);
    }

    public void getHacerseCargoHeladeraRecomendacionesPaso2(Context context) {
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Recomendaciones Paso 2");
        context.render("contribuciones/persona_juridica/hacerse_cargo_heladera_paso2.hbs",model);
    }

    public void postHacerseCargoHeladeraRecomendacionesPaso2(Context context){
        String latitud = context.formParam("latitud");
        String longitud = context.formParam("longitud");

        Map<String, String> direccion = Nominative.obtenerDireccion(latitud,longitud);
        String calle = direccion.get("calle");
        String altura = direccion.get("altura");
        String provincia = direccion.get("provincia");
        String provinciaMapeada = this.provinciaMapper(provincia);

        Provincia provinciaRecibida = repositorio.buscarPorNombre(provinciaMapeada,"Provincia",Provincia.class);
        Ciudad ciudad = repositorioCiudad.buscarPrimeraCiudadPorProvinciaId(provinciaRecibida.getId());

        Direccion direccionRecibida = new Direccion(
                calle,
                altura,
                null,
                null,
                ciudad,
                provinciaRecibida
        );
        repositorio.guardar(direccionRecibida);

        PuntoEstrategico puntoEstrategico = new PuntoEstrategico(
                "heladera",
                latitud,
                longitud,
                direccionRecibida
        );
        repositorio.guardar(puntoEstrategico);
        context.sessionAttribute("puntoEstrategicoId",puntoEstrategico.getId());
    }

    public void getHacerseCargoHeladeraRecomendacionesPaso3(Context context){
        Map<String, Object> model = new HashMap<>();
        List<Object> todosLosModelos = repositorio.buscarTodos("ModeloDeHeladera");
        List<DTOModeloDeHeladera> dtosModelos = MapperDTOModeloDeHeladera.convertirListaADTOs(todosLosModelos);
        model.put("titulo", "Recomendaciones Paso 3");
        model.put("modelos", dtosModelos);
        context.render("contribuciones/persona_juridica/hacerse_cargo_heladera_paso3.hbs",model);
    }

    public void postHacerseCargoHeladeraRecomendacionesPaso3(Context context){
        String puntoEstrategicoId = context.sessionAttribute("puntoEstrategicoId");
        String nombreModeloHeladera = context.formParam("modeloHeladera");

        PuntoEstrategico puntoEstrategico =
                (PuntoEstrategico) repositorio.buscarPorId(puntoEstrategicoId,"PuntoEstrategico");

        ModeloDeHeladera modeloDeHeladera = repositorioModeloHeladera.buscarPorNombre(nombreModeloHeladera);
        Heladera heladera = new Heladera(modeloDeHeladera,puntoEstrategico);
        repositorio.guardar(heladera);
        String userId = context.sessionAttribute("userId");
        PersonaJuridica personaJuridica =
                (PersonaJuridica) repositorio.buscarPorId(userId, PersonaJuridica.class.getSimpleName());
        personaJuridica.agregarHeladera(heladera);
        repositorio.actualizar(heladera);
    }

    public void getSuscribirseHeladera(Context context){
        Map<String, Object> model = new HashMap<>();

        model.put("titulo", "Suscribirse a Heladera");
        context.render("contribuciones/persona_fisica/suscripcion_heladeras.hbs",model);
    }

    public void postSuscribirseHeladera(Context context){
        String idHeladera = context.formParam("idHeladera");
        Heladera heladera = (Heladera) repositorio.buscarPorId(idHeladera,"Heladera");
        String userId = context.sessionAttribute("userId");
        PersonaFisica personaFisica = (PersonaFisica) repositorio.buscarPorId(userId,"PersonaFisica");

        String checkViandasDisponibles = context.formParam("food_available");
        String viandasDisponibles = context.formParam("number_available");
        String checkViandasFaltantes = context.formParam("food_lack");
        String viandasFaltantes = context.formParam("number_lack");
        String checkDesperfectoYTraslado = context.formParam("food_fridges");

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setHeladera(heladera);
        suscripcion.setPersonaFisica(personaFisica);
        suscripcion.setFechaYHoraInicio(LocalDateTime.now());
        List<OpcionSuscripcion> opciones = new ArrayList<>();

        if(checkViandasDisponibles!=null){
            suscripcion.setCantidadViandasDisponibles(Integer.parseInt(viandasDisponibles));
            QuedanNViandasDisponibles quedanNViandasDisponibles =
                    QuedanNViandasDisponibles.crearQuedanNViandasDisponibles();
            opciones.add(quedanNViandasDisponibles);
        }

        if(checkViandasFaltantes!=null){
            suscripcion.setCantidadViandasDisponibles(Integer.parseInt(viandasFaltantes));
            FaltanNViandas faltanNViandas = FaltanNViandas.crearFaltanNViandas();
            opciones.add(faltanNViandas);
        }

        if(checkDesperfectoYTraslado!=null){
            GeneradorDeSugerencias generadorDeSugerencias = GeneradorDeSugerencias.crearGeneradorDeSugerencias();
            opciones.add(generadorDeSugerencias);
        }

        suscripcion.setOpciones(opciones);
        repositorio.guardar(suscripcion);

        SessionUtils.mostrarPantallaDeExito(context,
                "Suscripción realizada con éxito",
                "Dashboard",
                "/dashboard");
    }

    public void getDonacionVianda(Context context){
        Map<String, Object> model = new HashMap<>();

        List<Object> todasLasHeladeras = repositorio.buscarTodos("Heladera");
        List<DTOHeladera> dtoTodasLasHeladeras = MapperDTOHeladera.convertirListaADTOs(todasLasHeladeras);

        List<DTOHeladera> dtoHeladeras = dtoTodasLasHeladeras.stream()
                .filter(dtoHeladera -> dtoHeladera.getEstaActiva())
                .toList();

        List<DTODisplayDistribucionDeVianda> listaDTODisplayDistribucionDeVianda = new ArrayList<>();

        for (DTOHeladera dtoHeladera : dtoHeladeras) {
            Integer cantidadViandasDisponibles = dtoHeladera.getViandas().size();

            agregarDisplayALista(listaDTODisplayDistribucionDeVianda, dtoHeladera, cantidadViandasDisponibles);
        }
        LocalDateTime ahora = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaFormateada = ahora.format(formatter);

        model.put("heladeras", listaDTODisplayDistribucionDeVianda);
        model.put("fecha_y_hora", fechaFormateada);
        model.put("titulo", "Donar Vianda");
        context.render("contribuciones/persona_fisica/donacion_vianda.hbs", model);
    }

    public void postDonacionVianda(Context context) {

        String nombreComida = context.formParam("foodType");
        LocalDate fechaCaducidad = LocalDate.parse(context.formParam("startDate"));
        String caloriasParam = context.formParam("calorias");
        String pesoParam = context.formParam("peso");
        LocalDate fechaListaParaEntregar = LocalDate.parse(context.formParam("fechaListaParaEntregar"));

        String heladeraId = context.formParam("selectedOption");
        String userId = context.sessionAttribute("userId");
        PersonaFisica personaFisica = (PersonaFisica) repositorio.buscarPorId(userId, PersonaFisica.class.getSimpleName());
        Double puntosASumar = calculadoraDePuntos.calcularPuntosViandasDonadas(1);
        personaFisica.sumarPuntosYResetearAtributosDeColaboraciones(puntosASumar);

        Double calorias = (!Objects.equals(caloriasParam, "")) ? Double.parseDouble(caloriasParam) : null;
        Double peso = (!Objects.equals(pesoParam, "")) ? Double.parseDouble(pesoParam) : null;

        Vianda vianda = new Vianda(
                nombreComida,
                fechaCaducidad,
                calorias,
                peso
        );
        repositorio.guardar(vianda);

        Heladera heladera = (Heladera) repositorio.buscarPorId(heladeraId, Heladera.class.getSimpleName());

        DonacionDeVianda donacionDeVianda = new DonacionDeVianda(
                FormaDeColaboracion.DONACION_DE_VIANDA,
                personaFisica,
                vianda,
                fechaListaParaEntregar,
                LocalDate.now()
        );
        repositorio.guardar(donacionDeVianda);

        heladera.agregarViandas(vianda);

        repositorio.actualizar(vianda);
        repositorio.actualizar(heladera);

        SolicitudDeApertura solicitudDeApertura = new SolicitudDeApertura(
                personaFisica,
                heladera,
                LocalDateTime.now(),
                AccionSolicitada.INGRESO,
                false
        );
        solicitudDeApertura.agregarVianda(vianda);
        repositorio.guardar(solicitudDeApertura);

        SessionUtils.mostrarPantallaDeExito(context,
                "Donación de vianda realizada con éxito",
                "Dashboard - Contribuciones",
                "/dashboard/contribuciones");
    }

    public void getHistorialCSVsAceptados(Context context){
        Map<String, Object> model = new HashMap<>();
        String userId = context.sessionAttribute("userId");
        Administrador administrador = (Administrador) repositorio.buscarPorId(userId,"Administrador");
        List<CSV> CSVs = repositorioCSV.buscarCSVsPorAdministrador(administrador);
        List<DTOCSV> dtoCSVs = MapperDTOCSV.convertirListaADTOs(CSVs);

        model.put("dtoCSVs", dtoCSVs);
        model.put("titulo", "Historial CSVs Aceptados");
        context.render("admin/historial_csvs_aceptados.hbs", model);
    }

    public void getCargaCSV(Context context) throws IOException {
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Carga Masiva de Colaboraciones");
        context.render("admin/carga_csv.hbs", model);
    }

    public void getCSVDeEjemplo(Context context) throws IOException {
        Path path = Paths.get(rutaCSVEjemplo);
        if (Files.exists(path)) {
            context.result(Files.newInputStream(path))
                    .contentType("text/csv")
                    .header("Content-Disposition", "attachment; filename=\"" + path.getFileName().toString() + "\"");
        } else {
            context.status(404).result("File not found");
        }
    }

    public void recibirCSV(Context context) {
        try {
            UploadedFile uploadedFile = context.uploadedFile("file");
            if (uploadedFile == null) {
                context.status(400).result("No file uploaded");
            }

            Files.createDirectories(Paths.get(rutaCarpetaCSVSubidos));

            String originalFileName = uploadedFile.filename();
            String fileNameWithoutExtension = originalFileName.replaceFirst("[.][^.]+$", "");
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String rutaArchivoRecibido = rutaCarpetaCSVSubidos + originalFileName;
            Path path = Paths.get(rutaArchivoRecibido);

            Integer count = 1;
            while (Files.exists(path)) {
                rutaArchivoRecibido = String.format("%s%s (%d)%s", rutaCarpetaCSVSubidos, fileNameWithoutExtension, count, fileExtension);
                path = Paths.get(rutaArchivoRecibido);
                count++;
            }

            try (InputStream inputStream = uploadedFile.content()) {
                Files.copy(inputStream, path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String userId = context.sessionAttribute("userId");
            Administrador admin = (Administrador) repositorio.buscarPorId(userId,"Administrador");
            this.guardarCSVEnBase(originalFileName,rutaArchivoRecibido,admin);

            SessionUtils.mostrarPantallaDeExito(context,
                    "Archivo CSV recibido con éxito",
                    "Dashboard - Contribuciones",
                    "/dashboard");


        } catch (IOException e) {
            System.err.println("Error al procesar el archivo CSV: " + e.getMessage());
            context.status(500).result("Error interno al procesar el archivo.");
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            context.status(500).result("Ocurrió un error inesperado.");
        }
    }



    public void guardarCSVEnBase(String nombreArchivo, String rutaArchivoRecibido, Administrador admin){
        CSV csv = new CSV(
                nombreArchivo,
                rutaArchivoRecibido,
                false,
                admin
        );
        repositorio.guardar(csv);
    }

    public void leerArchivoProperties(){
        Properties properties = new Properties();
        try (BufferedReader br = new BufferedReader(new FileReader(
                "./src/main/resources/archivoDeConfiguracion.properties"))) {
            properties.load(br);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        this.apiKey = properties.getProperty("recomendadora_de_lugares_de_donacion_API_test_apiKey");
        this.rutaCSVEjemplo = properties.getProperty("csv_ejemplo");
        this.rutaCarpetaCSVSubidos = properties.getProperty("carpeta_csv_subidos");
    }

    public void getPuntosRecomendados(Context context) throws IOException {
        Double latitud = context.sessionAttribute("latitudRecomendada");
        Double longitud = context.sessionAttribute("longitudRecomendada");

        ListadoDeLugares listadoDeLugares =
                recomendadoraDeLugaresDeDonacionAPI.listadoDeLugares(apiKey,latitud,longitud);
        List<DTOLugar> dtosLugar = MapperDTOLugar.convertirListaADTOs(listadoDeLugares.lugares);
        context.json(dtosLugar);
        context.status(HttpStatus.OK);
    }

    public void getColaboracionesAceptadas(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Colaboraciones Aceptadas");
        String userId = context.sessionAttribute("userId");
        Administrador administrador = (Administrador) repositorio.buscarPorId(userId,"Administrador");
        List<CSV> CSVs = repositorioCSV.buscarCSVsPorAdministrador(administrador);
        List<DTOCSV> dtoCSVs = MapperDTOCSV.convertirListaADTOs(CSVs);
        model.put("dtoCSVs", dtoCSVs);
        context.render("admin/csv_colaboraciones_registradas.hbs", model);
    }

    public void getPersonasFisicasAceptadas(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Personas Fisicas Aceptadas");
        String userId = context.sessionAttribute("userId");
        Administrador administrador = (Administrador) repositorio.buscarPorId(userId,"Administrador");
        List<CSV> CSVs = repositorioCSV.buscarCSVsPorAdministrador(administrador);
        List<DTOCSV> dtoCSVs = MapperDTOCSV.convertirListaADTOs(CSVs);
        model.put("dtoCSVs", dtoCSVs);
        context.render("admin/csv_personas_fisicas_registradas.hbs", model);
    }

    public String provinciaMapper(String provinciaNominative) {
        return switch (provinciaNominative.toLowerCase()) {
            case "ciudad autónoma de buenos aires" -> "Buenos Aires";
            case "buenos aires" -> "Buenos Aires";
            case "catamarca" -> "Catamarca";
            case "chaco" -> "Chaco";
            case "chubut" -> "Chubut";
            case "córdoba" -> "Córdoba";
            case "corrientes" -> "Corrientes";
            case "entre ríos" -> "Entre Ríos";
            case "formosa" -> "Formosa";
            case "jujuy" -> "Jujuy";
            case "la pampa" -> "La Pampa";
            case "la rioja" -> "La Rioja";
            case "mendoza" -> "Mendoza";
            case "misiones" -> "Misiones";
            case "neuquén" -> "Neuquén";
            case "río negro" -> "Río Negro";
            case "salta" -> "Salta";
            case "san juan" -> "San Juan";
            case "san luis" -> "San Luis";
            case "santa cruz" -> "Santa Cruz";
            case "santa fe" -> "Santa Fe";
            case "santiago del estero" -> "Santiago del Estero";
            case "tierra del fuego" -> "Tierra del Fuego";
            case "tucumán" -> "Tucumán";
            default -> null;
        };
    }

}

