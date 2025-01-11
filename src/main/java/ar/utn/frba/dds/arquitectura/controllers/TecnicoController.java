package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.dtos.*;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOAlertasHeladera;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTODisplayAlertasHeladera;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOFallasTecnicas;
import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import ar.utn.frba.dds.arquitectura.utils.SessionUtils;
import ar.utn.frba.dds.modelos.entidades.contacto.Usuario;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.incidentes.*;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Oferta;
import ar.utn.frba.dds.modelos.entidades.personas.*;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import ar.utn.frba.dds.modulos.importador.MapperImportador;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class TecnicoController implements ICrudViewsHandler {
    private Repositorio repositorio;
    private MapperImportador mapperImportador;

    @Override
    public void index(Context context) {
    }
    public void esTecnico(Context context) {
        boolean esTecnico = SessionUtils.esTecnico(context);
        Map<String, Boolean> response = new HashMap<>();
        response.put("esTecnico", esTecnico);
        context.json(response);
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

    public void getABMTecnico(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "ABM Tecnico");
        context.render("admin/abm_personas/tecnico/abm_tecnico_seleccionar_opcion.hbs", model);
    }
    public void getAltaTecnico(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Alta de Formulario");

        context.render("admin/abm_personas/tecnico/abm_tecnico_alta.hbs", model);
    }
    public void postAltaTecnico(Context context) {
        String nombre = context.formParam("nombre");
        String apellido = context.formParam("apellido");
        String cuil = context.formParam("CUIL");
        String tipoDocumento = context.formParam("tipo_documento");
        String numeroDocumento = context.formParam("numero_documento");
        List<String> mediosDeContacto = context.formParams("mediosDeContacto");
        List<String> contactos = context.formParams("contacto");
        //TODO sacar area de cobertura
        String areaDeCobertura = context.formParam("areaDeCobertura");

        TipoDocumento tipoDocumentoMapeado = mapperImportador.tipoDocumentoMapper(tipoDocumento);
        Tecnico tecnico = new Tecnico(nombre, apellido, numeroDocumento, tipoDocumentoMapeado, cuil, null, null);

        if (mediosDeContacto.size() == contactos.size()) {
            for (int i = 0; i < mediosDeContacto.size(); i++) {
                String medioDeContacto = mediosDeContacto.get(i);
                String contacto = contactos.get(i);

                TipoContacto tipoContacto = mapperImportador.medioDeContactoMapper(medioDeContacto);
                if (tipoContacto != null && contacto != null) {
                    MedioDeContacto nuevoMedio = new MedioDeContacto(tipoContacto, contacto);
                    tecnico.getMediosDeContacto().add(nuevoMedio); // Agregar el contacto a la lista del técnico
                }
            }
        }

        repositorio.guardar(tecnico);
        SessionUtils.mostrarPantallaDeExito(context, "Técnico creado con éxito", "ABM tecnico", "/dashboard/tecnicos");
    }

    public void getModificarTecnicoPaso1(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Modificar Tecnico");
        List<Object> todosLosTecnicos = repositorio.buscarTodos("Tecnico");
        model.put("tecnicos", todosLosTecnicos);
        context.render("admin/abm_personas/tecnico/abm_tecnico_modificacion.hbs", model);
    }

    public void postModificarTecnicoPaso1(Context context){
        String idTecnico = context.formParam("idTecnico");
        context.sessionAttribute("idTecnico", idTecnico);
        context.redirect("/dashboard/tecnicos/modificacion/tecnico");
    }

    public void getModificarTecnicoPaso2(Context context) {
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Modificar un Técnico");

        String idTecnico = context.sessionAttribute("idTecnico");
        Tecnico tecnico = (Tecnico) repositorio.buscarPorId(idTecnico, "Tecnico");

        model.put("tecnico", tecnico);

        context.render("admin/abm_personas/tecnico/abm_tecnico_modificacion_paso2.hbs", model);
    }


    public void postModificarTecnicoPaso2(Context context) {
        String idTecnico = context.sessionAttribute("idTecnico");
        Tecnico tecnico = (Tecnico) repositorio.buscarPorId(idTecnico, "Tecnico");

        String nombre = context.formParam("nombre");
        String apellido = context.formParam("apellido");
        String cuil = context.formParam("cuil");
        String tipoDocumento = context.formParam("tipoDocumento");
        String numeroDocumento = context.formParam("numeroDocumento");
        List<String> mediosDeContacto = context.formParams("mediosDeContacto");
        List<String> contactos = context.formParams("contactos");
        String ciudad = context.formParam("areaDeCobertura");

        if (nombre != null) {
            tecnico.setNombre(nombre);
        }
        if (apellido != null) {
            tecnico.setApellido(apellido);
        }
        if (cuil != null) {
            tecnico.setCuil(cuil);
        }
        if (tipoDocumento != null) {
            tecnico.setTipoDocumento(mapperImportador.tipoDocumentoMapper(tipoDocumento));
        }
        if (numeroDocumento != null) {
            tecnico.setNumeroDocumento(numeroDocumento);
        }
        // TODO AREA DE COBERTURA necesita CIUDAD
        if (ciudad != null) {
            //tecnico.setCiudad(null);
        }

        tecnico.getMediosDeContacto().clear();
        if (mediosDeContacto.size() == contactos.size()) {
            for (int i = 0; i < mediosDeContacto.size(); i++) {
                TipoContacto tipoContacto = mapperImportador.medioDeContactoMapper(mediosDeContacto.get(i));
                if (tipoContacto != null && contactos.get(i) != null) {
                    MedioDeContacto nuevoMedio = new MedioDeContacto(tipoContacto, contactos.get(i));
                    tecnico.getMediosDeContacto().add(nuevoMedio);
                }
            }
        }

        repositorio.actualizar(tecnico);
        SessionUtils.mostrarPantallaDeExito(context, "Técnico modificado con éxito", "ABM de tecnico", "/dashboard/tecnicos");
    }


    public void postBajaTecnico(Context context) {
        String idTecnico = context.formParam("idTecnico");
        Tecnico tecnico = (Tecnico) repositorio.buscarPorId(idTecnico, "Tecnico");

        if (tecnico != null) {
            repositorio.bajaLogica(tecnico);
            SessionUtils.mostrarPantallaDeExito(context, "Técnico dado de baja con éxito", "ABM de técnico", "/dashboard/tecnicos");
        } else {
            throw new RuntimeException("No se pudo eliminar al tecnico");

        }
}
    public void getBajaTecnico(Context context){
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Baja de Tecnico");
        List<Object> todosLosTecnicos = repositorio.buscarTodos("Tecnico");
        model.put("tecnicos", todosLosTecnicos);
        context.render("admin/abm_personas/tecnico/abm_tecnico_baja.hbs", model);
    }
    public void getRegistrarVisitaPaso1(Context context) {
        Map<String, Object> model = new HashMap<>();
        String userId = context.sessionAttribute("userId");

        List<FallaTecnica> todasLasFallas = repositorio.buscarTodos("FallaTecnica").stream()
            .map(obj -> (FallaTecnica) obj)
            .toList();

        List<FallaTecnica> casiMisFallas = todasLasFallas.stream()
                .filter(falla ->
                        !falla.getEstaSolucionado() &&
                        falla.getTecnicoQueAcepto() != null)
                .toList();

        List<AlertaHeladera> alertas = repositorio.buscarTodos("AlertaHeladera").stream()
                .map(obj -> (AlertaHeladera) obj)
                .toList();

        List<AlertaHeladera> casiMisAlertas = alertas.stream()
                .filter(alerta ->
                        !alerta.getEstaSolucionado() &&
                                alerta.getTecnicoQueAcepto() != null)
                .toList();


            List<FallaTecnica> misFallas = casiMisFallas.stream()
                    .filter(falla ->
                            !falla.getEstaSolucionado() && Objects.equals(falla.getTecnicoQueAcepto().getId(), userId))
                    .toList();

            //List<DTOFallasTecnicas> dtoMisFallas = MapperDTOFallasTecnicas.convertirListaADTOs(Collections.singletonList(misFallas));
            List<DTOFallasTecnicas> dtoMisFallas = misFallas.stream()
                    .map(MapperDTOFallasTecnicas::convertirADTO)
                    .toList();

            List<AlertaHeladera> misAlertas = casiMisAlertas.stream()
                    .filter(alerta ->
                            !alerta.getEstaSolucionado() && Objects.equals(alerta.getTecnicoQueAcepto().getId(), userId))
                    .toList();

            List<DTOAlertasHeladera> dtoMisAlertasHeladeras = misAlertas.stream()
                    .map(MapperDTOAlertasHeladera::convertirADTO)
                    .toList();

            String id = context.sessionAttribute("userId");
            Tecnico tecnico = (Tecnico) repositorio.buscarPorId(id, "Tecnico");

            List<DTODisplayIncidentes> DTODisplayIncidentes = new ArrayList<>();

                List<DTODisplayIncidentes> listaDTODisplayFallasTecnicas = getDtoDisplayFallasTecnicas(dtoMisFallas);
                List<DTODisplayIncidentes> listaDTODisplayAlertas = getDtoDisplayAlertas(dtoMisAlertasHeladeras);

                DTODisplayIncidentes.addAll(listaDTODisplayFallasTecnicas);
                DTODisplayIncidentes.addAll(listaDTODisplayAlertas);

                model.put("incidentes", DTODisplayIncidentes);



        LocalDateTime ahora = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaFormateada = ahora.format(formatter);

        model.put("titulo", "Registrar visita tecnica");
        model.put("fecha_y_hora", fechaFormateada);

        context.render("contribuciones/tecnico/registrar_visita_tecnica1.hbs", model);
    }

    private List<DTODisplayIncidentes> getDtoDisplayAlertas(List<DTOAlertasHeladera> dtoMisIncidentesAsignados) {
        List<DTODisplayIncidentes> listaDTODIncidentes = new ArrayList<>();
        for(DTOAlertasHeladera dtoAlertasHeladera : dtoMisIncidentesAsignados) {
                String direccion = dtoAlertasHeladera.getPuntoEstrategico().getDireccion().getCalle() + " "
                        + dtoAlertasHeladera.getPuntoEstrategico().getDireccion().getAltura();
                if (dtoAlertasHeladera.getPuntoEstrategico().getDireccion().getPiso() != null) {
                    direccion += " " + dtoAlertasHeladera.getPuntoEstrategico().getDireccion().getPiso() + "°";
                }

                LocalDateTime ultima_vez_activa = dtoAlertasHeladera.getHeladera().getFechasYHorasDejoDeEstarActiva().stream().max(LocalDateTime::compareTo).orElse(null);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime ultima_vez_activa_truncada = ultima_vez_activa.truncatedTo(ChronoUnit.SECONDS);
            String fechaFormateada = ultima_vez_activa_truncada.format(formatter);

            DTODisplayIncidentes dtoDisplayIncidentes = new DTODisplayIncidentes(
                        dtoAlertasHeladera.getId(),
                        dtoAlertasHeladera.getPuntoEstrategico().getDireccion().getCiudad().getProvincia().getNombre(),
                        direccion,
                        Integer.parseInt(dtoAlertasHeladera.getPuntoEstrategico().getDireccion().getCodigoPostal()),
                        dtoAlertasHeladera.getHeladera().getModelo().getNombre(),
                        dtoAlertasHeladera.getTipoIncidente().name(),
                        fechaFormateada,
                    null);
            listaDTODIncidentes.add(dtoDisplayIncidentes);
            }
            return listaDTODIncidentes;
    }


    private List<DTODisplayIncidentes> getDtoDisplayFallasTecnicas(List<DTOFallasTecnicas> dtoFallas) {

        List<DTODisplayIncidentes> listaDTODIncidentes = new ArrayList<>();
        for (DTOFallasTecnicas dtoFalla : dtoFallas) {
            String direccion = dtoFalla.getPuntoEstrategico().getDireccion().getCalle() + " "
                    + dtoFalla.getPuntoEstrategico().getDireccion().getAltura();
            if (dtoFalla.getPuntoEstrategico().getDireccion().getPiso() != null) {
                direccion += " " + dtoFalla.getPuntoEstrategico().getDireccion().getPiso() + "°";
            }
            LocalDateTime ultima_vez_activa = dtoFalla.getHeladera().getFechasYHorasDejoDeEstarActiva().stream().max(LocalDateTime::compareTo).orElse(null);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime ultima_vez_activa_truncada = ultima_vez_activa.truncatedTo(ChronoUnit.SECONDS);
            String fechaFormateada = ultima_vez_activa_truncada.format(formatter);

            DTODisplayIncidentes dtoDisplayIncidentes = new DTODisplayIncidentes(
                    dtoFalla.getId(),
                    dtoFalla.getPuntoEstrategico().getDireccion().getCiudad().getProvincia().getNombre(),
                    direccion,
                    Integer.parseInt(dtoFalla.getPuntoEstrategico().getDireccion().getCodigoPostal()),
                    dtoFalla.getHeladera().getModelo().getNombre(),
                    dtoFalla.getTipoIncidente().name(),
                    fechaFormateada,
                    null);
            listaDTODIncidentes.add(dtoDisplayIncidentes);
        }
        return listaDTODIncidentes;
    }

    public void postRegistrarVisitaPaso1(Context context) {
        String idIncidente = context.formParam("selectedOption");
        String tipoIncidenteInput = context.formParam("tipoIncidente");

        context.sessionAttribute("idIncidente", idIncidente);
        context.sessionAttribute("tipoIncidente", tipoIncidenteInput);

        context.redirect("/dashboard/registrar_visita/paso2");
    }

    public void getRegistrarVisitaPaso2(Context context) {
        Map<String, Object> model = new HashMap<>();
        String tipoIncidente = context.sessionAttribute("tipoIncidente");
        model.put("tipoIncidente", tipoIncidente);

        model.put("titulo", "Registrar visita tecnica");
        context.render("contribuciones/tecnico/registrar_visita_tecnica2.hbs", model);
    }

    public void postRegistrarVisitaPaso2(Context context) {
        String userId = context.sessionAttribute("userId");
        String fecha = context.formParam("fechaVisita");
        String hora = context.formParam("horaVisita");
        String descripcion = context.formParam("descripcion");
        String solucionado = context.formParam("solucionado");

        String tipoIncidente = context.formParam("tipoIncidente");

        //String pathImagen = context.formParam("pathImagen");

        String idIncidente = context.sessionAttribute("idIncidente");

        Tecnico tecnico = (Tecnico) repositorio.buscarPorId(userId, "Tecnico");
        Heladera heladera = null;
        FallaTecnica fallaTecnica = null;
        AlertaHeladera alertaHeladera = null;
        if (idIncidente != null) {
            heladera = switch (Objects.requireNonNull(tipoIncidente)) {
                case "FALLA_TECNICA" -> {
                    fallaTecnica = (FallaTecnica) repositorio.buscarPorId(idIncidente, "FallaTecnica");
                    yield (Heladera) repositorio.buscarPorId(fallaTecnica.getHeladera().getId(), "Heladera");
                }
                case "TEMPERATURA", "FRAUDE", "FALLA_CONEXION" -> {
                    alertaHeladera = (AlertaHeladera) repositorio.buscarPorId(idIncidente, "AlertaHeladera");
                    yield (Heladera) repositorio.buscarPorId(alertaHeladera.getHeladera().getId(), "Heladera");
                }
                default -> throw new RuntimeException("Tipo de incidente no reconocido");
            };

        }

        System.out.println("Fecha: " + fecha + " y Hora: " + hora);

        DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");
        assert fecha != null;
        LocalDate fecha_formateada = LocalDate.parse(fecha, formatterFecha);
        assert hora != null;
        LocalTime hora_formateada = LocalTime.parse(hora, formatterHora);
        LocalDateTime dateTime = fecha_formateada.atTime(hora_formateada);
        System.out.println("Desp del formateo: Fecha: " + fecha_formateada + " y Hora: " + hora_formateada);

        UploadedFile uploadedFile = context.uploadedFile("pathImagen");
        String pathImagen = null;

        if (uploadedFile != null) {
            try {

                Path visitas_tecnicas_dir = Paths.get("./visitas_tecnicas");
                if (!Files.exists(visitas_tecnicas_dir)) {
                    Files.createDirectory(visitas_tecnicas_dir);
                }
                String originalFileName = uploadedFile.filename();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String newFileName = UUID.randomUUID().toString() + fileExtension;
                Path imagePath = visitas_tecnicas_dir.resolve(newFileName);

                try (InputStream inputStream = uploadedFile.content()) {
                    Files.copy(inputStream, imagePath);
                }


                pathImagen = newFileName;
            } catch (IOException e) {
                context.status(500).result("Error al procesar la imagen");
                e.printStackTrace();
                return;
            }
        }

        Boolean solucionadoBool = !Objects.equals(solucionado, "0");

        if (Objects.equals(tipoIncidente, "FALLA_TECNICA")){
            VisitaTecnica visitaTecnica = new VisitaTecnica(
                    tecnico,
                    heladera,
                    dateTime,
                    descripcion,
                    pathImagen,
                    fallaTecnica,
                    null,
                    solucionadoBool

            );
            repositorio.guardar(visitaTecnica);
            if (solucionadoBool) {
                assert heladera != null;
                heladera.volverAEstarActiva();
                assert fallaTecnica != null;
                fallaTecnica.fueSolucionado();
                repositorio.actualizar(heladera);
                repositorio.actualizar(fallaTecnica);
            }

        }else{
            VisitaTecnica visitaTecnica = new VisitaTecnica(
                    tecnico,
                    heladera,
                    dateTime,
                    descripcion,
                    pathImagen,
                    null,
                    alertaHeladera,
                    solucionadoBool
            );

            repositorio.guardar(visitaTecnica);

        if (solucionadoBool){
            assert heladera != null;
            heladera.volverAEstarActiva();
            repositorio.actualizar(heladera);
            if(visitaTecnica.getAlertaHeladera() != null){
                alertaHeladera.fueSolucionado();
                repositorio.actualizar(alertaHeladera);
            }else {
                fallaTecnica.fueSolucionado();
                repositorio.actualizar(fallaTecnica);
            }
        }

        }

        SessionUtils.mostrarPantallaDeExito(context, "Visita técnica registrada con éxito", "Dashboard", "/dashboard");


    }

    public void getVerDetalleFalla(Context context) {
        Map<String, Object> model = new HashMap<>();
        String idFalla = context.pathParam("id");

        FallaTecnica fallaTecnica = (FallaTecnica) repositorio.buscarPorId(idFalla, "FallaTecnica");

        String direccion = fallaTecnica.getPuntoEstrategico().getDireccion().getCalle() + " "
                + fallaTecnica.getPuntoEstrategico().getDireccion().getAltura();
        if (fallaTecnica.getPuntoEstrategico().getDireccion().getPiso() != null) {
            direccion += " " + fallaTecnica.getPuntoEstrategico().getDireccion().getPiso() + "°";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dateTimeFalla = fallaTecnica.getFechaYHoraFalla().truncatedTo(ChronoUnit.SECONDS);
        String fechaFormateadaFalla = dateTimeFalla.format(formatter);

        LocalDateTime dateTimeReporte = fallaTecnica.getFechaYHoraReporte().truncatedTo(ChronoUnit.SECONDS);
        String fechaFormateadaReporte = dateTimeReporte.format(formatter);

        model.put("falla", fallaTecnica);
        model.put("direccion", direccion);
        model.put("fechaYHoraFalla", fechaFormateadaFalla);
        model.put("fechaYHoraReporte", fechaFormateadaReporte);
        model.put("titulo", "Ver detalle de falla");
        context.render("contribuciones/tecnico/ver_detalle_falla.hbs", model);
    }

    public void getVerAlertasActivas(Context context) {
        Map<String, Object> model = new HashMap<>();

        List<Object> alertas = repositorio.buscarTodos("AlertaHeladera");

        List<DTOAlertasHeladera> dtoTodasAlertasHeladeras = MapperDTOAlertasHeladera.convertirListaADTOs(alertas);

        List<DTOAlertasHeladera> dtoAlertasHeladeras = dtoTodasAlertasHeladeras.stream()
                .filter(alerta -> !alerta.getEstaSolucionado())
                .toList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        List<DTODisplayIncidentes> listaDTODisplayIncidentes = new ArrayList<>();
        for (DTOAlertasHeladera alerta : dtoAlertasHeladeras) {
            String direccion = alerta.getPuntoEstrategico().getDireccion().getCalle() + " "
                    + alerta.getPuntoEstrategico().getDireccion().getAltura();
            if (alerta.getPuntoEstrategico().getDireccion().getPiso() != null) {
                direccion += " " + alerta.getPuntoEstrategico().getDireccion().getPiso() + "°";
            }

            LocalDateTime ultima_vez_activa = alerta.getHeladera().getFechasYHorasDejoDeEstarActiva().stream().max(LocalDateTime::compareTo).orElse(null);

            LocalDateTime ultima_vez_activa_truncada = ultima_vez_activa.truncatedTo(ChronoUnit.SECONDS);
            String fechaFormateada = ultima_vez_activa_truncada.format(formatter);
            LocalDateTime fechaFalla = alerta.getFechaYHora().truncatedTo(ChronoUnit.SECONDS);
            String fechaFallaFormateada = fechaFalla.format(formatter);

            DTODisplayIncidentes DTODisplayIncidentes = new DTODisplayIncidentes(
                    alerta.getId(),
                    alerta.getPuntoEstrategico().getDireccion().getCiudad().getProvincia().getNombre(),
                    direccion,
                    Integer.parseInt(alerta.getPuntoEstrategico().getDireccion().getCodigoPostal()),
                    alerta.getHeladera().getModelo().getNombre(),
                    alerta.getTipoIncidente().name(),
                    fechaFormateada,
                    fechaFallaFormateada
            );

            listaDTODisplayIncidentes.add(DTODisplayIncidentes);
        }

        List<Object> fallas = repositorio.buscarTodos("FallaTecnica");
        List<FallaTecnica> todasLasFallasTecnicas = fallas.stream().map(falla -> (FallaTecnica) falla).toList();
        List<FallaTecnica> fallasTecnicas = todasLasFallasTecnicas.stream()
                .filter(falla ->
                        !falla.getEstaSolucionado())
                .toList();

        List<Object> fallasAsignadasObjects = new ArrayList<>(fallasTecnicas);
        List<DTOFallasTecnicas> dtoFallasTecnicas = MapperDTOFallasTecnicas.convertirListaADTOs(fallasAsignadasObjects);

        for (DTOFallasTecnicas dtoFallaTecnica : dtoFallasTecnicas) {
            String direccion = dtoFallaTecnica.getPuntoEstrategico().getDireccion().getCalle() + " "
                    + dtoFallaTecnica.getPuntoEstrategico().getDireccion().getAltura();
            if (dtoFallaTecnica.getPuntoEstrategico().getDireccion().getPiso() != null) {
                direccion += " " + dtoFallaTecnica.getPuntoEstrategico().getDireccion().getPiso() + "°";
            }

            LocalDateTime ultima_vez_activa = dtoFallaTecnica.getHeladera().getFechasYHorasDejoDeEstarActiva().stream().max(LocalDateTime::compareTo).orElse(null);
            assert ultima_vez_activa != null;

            LocalDateTime ult_vez_truncada = ultima_vez_activa.truncatedTo(ChronoUnit.SECONDS);
            LocalDateTime falla_truncada = dtoFallaTecnica.getFechaYHoraFalla().truncatedTo(ChronoUnit.SECONDS);
            String ultima_vez_activa_formateada = ult_vez_truncada.format(formatter);
            String falla_truncada_formateada = falla_truncada.format(formatter);

            DTODisplayIncidentes DTODisplayIncidentes = new DTODisplayIncidentes(
                    dtoFallaTecnica.getId(),
                    dtoFallaTecnica.getPuntoEstrategico().getDireccion().getCiudad().getProvincia().getNombre(),
                    direccion,
                    Integer.parseInt(dtoFallaTecnica.getPuntoEstrategico().getDireccion().getCodigoPostal()),
                    dtoFallaTecnica.getHeladera().getModelo().getNombre(),
                    dtoFallaTecnica.getTipoIncidente().name(),
                    ultima_vez_activa_formateada,
                    falla_truncada_formateada
            );
            listaDTODisplayIncidentes.add(DTODisplayIncidentes);
        }
        LocalDateTime ahora = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        String fechaFormateada = ahora.format(formatter);

        model.put("titulo", "Ver alertas activas");
        model.put("fecha_y_hora", fechaFormateada);
        model.put("incidentes", listaDTODisplayIncidentes);
        context.render("contribuciones/tecnico/ver_alertas_activas.hbs", model);
    }

    public void postVerAlertasActivas(Context context) {
    }

    public void getAsignarIncidente(Context context) {
        Map<String, Object> model = new HashMap<>();
        String userId = context.sessionAttribute("userId");
        Tecnico tecnico = (Tecnico) repositorio.buscarPorId(userId, "Tecnico");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        List<Object> alertas = repositorio.buscarTodos("AlertaHeladera");
        List<AlertaHeladera> alertaHeladeras = alertas.stream().map(alerta -> (AlertaHeladera) alerta).toList();
        BuscadoraTecnicoMasCercano buscadorTecnicoMasCercano = new BuscadoraTecnicoMasCercano(repositorio,10000);

        List<AlertaHeladera> alertasAsignadas = alertaHeladeras.stream()
                .filter(alerta ->
                        !alerta.getEstaSolucionado() &&
                        alerta.getTecnicoQueAcepto() == null
                                && buscadorTecnicoMasCercano.estaDentroDelRango(tecnico, alerta.getHeladera().getPuntoEstrategico())
                )
                .toList();


        List<Object> alertasAsignadasObjects = new ArrayList<>(alertasAsignadas);
        List<DTOAlertasHeladera> dtoalertasAsignadas = MapperDTOAlertasHeladera.convertirListaADTOs(alertasAsignadasObjects);

        List<DTODisplayIncidentes> listaDTODisplayIncidentes = new ArrayList<>();

        for(DTOAlertasHeladera dtoAlertaAsignada : dtoalertasAsignadas) {
            String direccion = dtoAlertaAsignada.getPuntoEstrategico().getDireccion().getCalle() + " "
                    + dtoAlertaAsignada.getPuntoEstrategico().getDireccion().getAltura();
            if (dtoAlertaAsignada.getPuntoEstrategico().getDireccion().getPiso() != null) {
                direccion += " " + dtoAlertaAsignada.getPuntoEstrategico().getDireccion().getPiso() + "°";
            }

            LocalDateTime ultima_vez_activa = dtoAlertaAsignada.getHeladera().getFechasYHorasDejoDeEstarActiva().stream().max(LocalDateTime::compareTo).orElse(null);

            assert ultima_vez_activa != null;

            LocalDateTime ult_vez_truncada = ultima_vez_activa.truncatedTo(ChronoUnit.SECONDS);
            LocalDateTime falla_truncada = dtoAlertaAsignada.getFechaYHora().truncatedTo(ChronoUnit.SECONDS);
            String ultima_vez_activa_formateada = ult_vez_truncada.format(formatter);
            String falla_truncada_formateada = falla_truncada.format(formatter);

            DTODisplayIncidentes DTODisplayIncidentes = new DTODisplayIncidentes(
                    dtoAlertaAsignada.getId(),
                    dtoAlertaAsignada.getPuntoEstrategico().getDireccion().getCiudad().getProvincia().getNombre(),
                    direccion,
                    Integer.parseInt(dtoAlertaAsignada.getPuntoEstrategico().getDireccion().getCodigoPostal()),
                    dtoAlertaAsignada.getHeladera().getModelo().getNombre(),
                    dtoAlertaAsignada.getTipoIncidente().name(),
                    ultima_vez_activa_formateada,
                    falla_truncada_formateada
            );

            listaDTODisplayIncidentes.add(DTODisplayIncidentes);
        }

        List<Object> fallas = repositorio.buscarTodos("FallaTecnica");
        List<FallaTecnica> fallasTecnicas = fallas.stream().map(falla -> (FallaTecnica) falla).toList();
        List<FallaTecnica> fallasAsignadas = fallasTecnicas.stream()
                .filter(falla ->
                        !falla.getEstaSolucionado() &&
                        falla.getTecnicoQueAcepto() == null)
                .toList();


        List<Object> fallasAsignadasObjects = new ArrayList<>(fallasAsignadas);
        List<DTOFallasTecnicas> dtoFallasTecnicas = MapperDTOFallasTecnicas.convertirListaADTOs(fallasAsignadasObjects);

        for(DTOFallasTecnicas dtoFallaTecnica : dtoFallasTecnicas) {
            String direccion = dtoFallaTecnica.getPuntoEstrategico().getDireccion().getCalle() + " "
                    + dtoFallaTecnica.getPuntoEstrategico().getDireccion().getAltura();
            if (dtoFallaTecnica.getPuntoEstrategico().getDireccion().getPiso() != null) {
                direccion += " " + dtoFallaTecnica.getPuntoEstrategico().getDireccion().getPiso() + "°";
            }

            LocalDateTime ultima_vez_activa = dtoFallaTecnica.getHeladera().getFechasYHorasDejoDeEstarActiva().stream().max(LocalDateTime::compareTo).orElse(null);
            assert ultima_vez_activa != null;

            LocalDateTime ult_vez_truncada = ultima_vez_activa.truncatedTo(ChronoUnit.SECONDS);
            LocalDateTime falla_truncada = ultima_vez_activa.truncatedTo(ChronoUnit.SECONDS);
            String ultima_vez_activa_formateada = ult_vez_truncada.format(formatter);
            String falla_truncada_formateada = falla_truncada.format(formatter);

            DTODisplayIncidentes DTODisplayIncidentes = new DTODisplayIncidentes(
                    dtoFallaTecnica.getId(),
                    dtoFallaTecnica.getPuntoEstrategico().getDireccion().getCiudad().getProvincia().getNombre(),
                    direccion,
                    Integer.parseInt(dtoFallaTecnica.getPuntoEstrategico().getDireccion().getCodigoPostal()),
                    dtoFallaTecnica.getHeladera().getModelo().getNombre(),
                    dtoFallaTecnica.getTipoIncidente().name(),
                    ultima_vez_activa_formateada,
                    falla_truncada_formateada
            );

            listaDTODisplayIncidentes.add(DTODisplayIncidentes);
        }
        LocalDateTime ahora = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        String fechaFormateada = ahora.format(formatter);

        model.put("fecha_y_hora", fechaFormateada);
        model.put("titulo", "Asignación de incidentes");
        model.put("incidentes", listaDTODisplayIncidentes);
        context.render("contribuciones/tecnico/asignar_incidente.hbs", model);
    }

    public void postAsignarIncidente(Context context) {
        String userId = context.sessionAttribute("userId");
        String idIncidente = context.formParam("selectedOption");
        String tipoIncidente = context.formParam("tipoIncidente");

        Tecnico tecnico = (Tecnico) repositorio.buscarPorId(userId, "Tecnico");

        if (idIncidente != null) {
            switch (Objects.requireNonNull(tipoIncidente)) {
                case "FALLA_TECNICA":
                    FallaTecnica fallaTecnica = (FallaTecnica) repositorio.buscarPorId(idIncidente, "FallaTecnica");
                    fallaTecnica.setTecnicoQueAcepto(tecnico);
                    repositorio.actualizar(fallaTecnica);
                    break;
                case "TEMPERATURA", "FRAUDE", "FALLA_CONEXION":
                    AlertaHeladera alertaHeladera = (AlertaHeladera) repositorio.buscarPorId(idIncidente, "AlertaHeladera");
                    alertaHeladera.setTecnicoQueAcepto(tecnico);
                    repositorio.actualizar(alertaHeladera);
                    break;
            }

        }
        SessionUtils.mostrarPantallaDeExito(context, "Incidente asignado con éxito", "Dashboard", "/dashboard");
    }
}