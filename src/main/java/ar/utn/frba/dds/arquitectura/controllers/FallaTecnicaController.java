package ar.utn.frba.dds.arquitectura.controllers;
import java.io.File;
import java.io.IOException;

import ar.utn.frba.dds.arquitectura.dtos.DTODisplayConsultarHeladerasActivas;
import ar.utn.frba.dds.arquitectura.dtos.DTODisplayDistribucionDeVianda;
import ar.utn.frba.dds.arquitectura.dtos.DTODisplayFallasTecnicas;
import ar.utn.frba.dds.arquitectura.dtos.DTOHeladera;
import ar.utn.frba.dds.arquitectura.exceptions.ImageNotSavedException;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOHeladera;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOProvincia;
import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import ar.utn.frba.dds.arquitectura.utils.SessionUtils;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.incidentes.FallaTecnica;
import ar.utn.frba.dds.modelos.entidades.incidentes.FallaTecnicaInfo;
import ar.utn.frba.dds.modelos.entidades.incidentes.ReportadorFallaTecnica;
import ar.utn.frba.dds.modelos.entidades.incidentes.TipoIncidente;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import ar.utn.frba.dds.modelos.entidades.personas.Tecnico;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import lombok.AllArgsConstructor;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FallaTecnicaController implements ICrudViewsHandler {
    private Repositorio repositorio;
    private ReportadorFallaTecnica ReportadorFallaTecnica;
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
    }

    @Override
    public void update(Context context) {

    }

    @Override
    public void delete(Context context) {

    }

    public void getReportarFallaTecnica(Context context){
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
        model.put("titulo", "Reportar Falla Técnica");
        context.render("reportes/reportar_falla_tecnica.hbs", model);
    }

    private void agregarDisplayALista(List<DTODisplayDistribucionDeVianda> listaDTODisplayDistribucionDeVianda, DTOHeladera dtoHeladera, Integer cantidadViandasDisponibles) {
        String direccion = dtoHeladera.getPuntoEstrategico().getDireccion().getCalle() + " "
                + dtoHeladera.getPuntoEstrategico().getDireccion().getAltura();
        if (dtoHeladera.getPuntoEstrategico().getDireccion().getPiso() != null) {
            direccion += " " + dtoHeladera.getPuntoEstrategico().getDireccion().getPiso() + "°";
        }

        String codPostal = dtoHeladera.getPuntoEstrategico().getDireccion().getCodigoPostal();
        DTODisplayDistribucionDeVianda DTODisplayDistribucionDeVianda = new DTODisplayDistribucionDeVianda(
                dtoHeladera.getId(),
                dtoHeladera.getPuntoEstrategico().getDireccion().getCiudad().getProvincia(),
                direccion,
                codPostal!=null ? Integer.parseInt(codPostal) : null,
                cantidadViandasDisponibles,
                dtoHeladera.getEstaActiva());
        listaDTODisplayDistribucionDeVianda.add(DTODisplayDistribucionDeVianda);
    }

    public void postReportarFallaTecnica(Context context) {
        String horaNoParseada = context.formParam("horaFalla");
        String fechaFalla = context.formParam("fechaFalla");

        DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");
        assert fechaFalla != null;
        LocalDate fecha_formateada = LocalDate.parse(fechaFalla, formatterFecha);
        assert horaNoParseada != null;
        LocalTime hora_formateada = LocalTime.parse(horaNoParseada, formatterHora);
        LocalDateTime dateTime = fecha_formateada.atTime(hora_formateada);

        String descripcion = context.formParam("descripcion");
        String pathImagen = null;


        UploadedFile uploadedFile = context.uploadedFile("pathImagen");

        if (uploadedFile != null) {
            try {
                Path fallaTecnicaDir = Paths.get("./fallaTecnica");

                String originalFileName = uploadedFile.filename();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String newFileName = UUID.randomUUID().toString() + fileExtension;
                Path imagePath = fallaTecnicaDir.resolve(newFileName);

                try (InputStream inputStream = uploadedFile.content()) {
                    Files.copy(inputStream, imagePath);
                }

                // Guardar solo el nombre del archivo
                pathImagen = newFileName;
            } catch (IOException e) {
                throw new ImageNotSavedException("Error al guardar la imagen", e);
            }
        }

        String idHeladera = context.formParam("selectedOption");

        System.out.println("idHeladera: " + idHeladera);

        String userId = context.sessionAttribute("userId");
        PersonaFisica personaFisica = (PersonaFisica) repositorio.buscarPorId(userId, PersonaFisica.class.getSimpleName());

        Heladera heladera = (Heladera) repositorio.buscarPorId(idHeladera, "Heladera");
        heladera.dejarDeEstarActiva();
        repositorio.actualizar(heladera);

        FallaTecnicaInfo fallaTecnicaInfo = FallaTecnicaInfo.crearFallaTecnicaInfo(
                dateTime,
                heladera,
                personaFisica,
                descripcion,
                pathImagen,
                TipoIncidente.FALLA_TECNICA
        );
        ReportadorFallaTecnica.reportar(fallaTecnicaInfo);
        FallaTecnica fallaTecnica = FallaTecnica.crearFallaTecnica(fallaTecnicaInfo);
        repositorio.guardar(fallaTecnica);

        SessionUtils.mostrarPantallaDeExito(context,
                "Falla técnica reportada con éxito",
                "Dashboard",
                "/dashboard");
    }

    public void getIncidentesResueltos(Context context){
        Map<String, Object> model = new HashMap<>();
        String userId = context.sessionAttribute("userId");
        PersonaJuridica personaJuridica = (PersonaJuridica) repositorio.buscarPorId(userId, "PersonaJuridica");


        List<FallaTecnica> fallasTecnicasSolucionadas = repositorio.buscarTodos("FallaTecnica").stream()
                .map(falla -> (FallaTecnica) falla)
                .filter(FallaTecnica::getEstaSolucionado)
                .toList();

        List<FallaTecnica> misFallasTecnicas = fallasTecnicasSolucionadas.stream()
                .filter(fallaTecnica -> personaJuridica.getHeladeras().stream()
                        .anyMatch(heladera -> heladera.getId().equals(fallaTecnica.getHeladera().getId())))
                .toList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        List<DTODisplayFallasTecnicas> listaDTODisplayFallasTecnicas = new ArrayList<>();

        if (!misFallasTecnicas.isEmpty()){
            for(FallaTecnica falla : misFallasTecnicas) {
                String direccion = falla.getPuntoEstrategico().getDireccion().getCalle() + " "
                        + falla.getPuntoEstrategico().getDireccion().getAltura();
                if (falla.getPuntoEstrategico().getDireccion().getPiso() != null) {
                    direccion += " " + falla.getPuntoEstrategico().getDireccion().getPiso() + "°";
                }

                LocalDateTime fechaFalla = falla.getFechaYHoraFalla().truncatedTo(ChronoUnit.SECONDS);
                String fechaFallaFormateada = fechaFalla.format(formatter);
                LocalDateTime fechaSolucion = falla.getFechaYHoraFueSolucionado().truncatedTo(ChronoUnit.SECONDS).truncatedTo(ChronoUnit.SECONDS);
                String fechaSolucionFormateada = fechaSolucion.format(formatter);

                DTODisplayFallasTecnicas dtoDisplayFallasTecnicas = new DTODisplayFallasTecnicas(
                        falla.getId(),
                        MapperDTOHeladera.convertirADTO(falla.getHeladera()),
                        MapperDTOProvincia.convertirADTO(falla.getPuntoEstrategico().getDireccion().getCiudad().getProvincia()),
                        direccion,
                        Integer.parseInt(falla.getPuntoEstrategico().getDireccion().getCodigoPostal()),
                        falla.getHeladera().getModelo().getNombre(),
                        null,
                        fechaFallaFormateada,
                        fechaSolucionFormateada
                );
                listaDTODisplayFallasTecnicas.add(dtoDisplayFallasTecnicas);
            }

            model.put("fallas", listaDTODisplayFallasTecnicas);;
        }else{
            model.put("mensaje", "No tenés heladeras con fallas resueltas");
        }

        LocalDateTime ahora = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        String fechaFormateada = ahora.format(formatter);

        model.put("fecha_y_hora", fechaFormateada);
        model.put("titulo", "Fallas resueltas");
        context.render("contribuciones/persona_juridica/ver_incidentes_resueltos.hbs", model);
    }


    public void getIncidentesResueltosAdmin(Context context){
        Map<String, Object> model = new HashMap<>();
        String userId = context.sessionAttribute("userId");
        PersonaJuridica personaJuridica = (PersonaJuridica) repositorio.buscarPorId(userId, "PersonaJuridica");


        List<FallaTecnica> fallasTecnicasSolucionadas = repositorio.buscarTodos("FallaTecnica").stream()
                .map(falla -> (FallaTecnica) falla)
                .filter(FallaTecnica::getEstaSolucionado)
                .toList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        List<DTODisplayFallasTecnicas> listaDTODisplayFallasTecnicas = new ArrayList<>();

        if (!fallasTecnicasSolucionadas.isEmpty()){
            for(FallaTecnica falla : fallasTecnicasSolucionadas) {
                String direccion = falla.getPuntoEstrategico().getDireccion().getCalle() + " "
                        + falla.getPuntoEstrategico().getDireccion().getAltura();
                if (falla.getPuntoEstrategico().getDireccion().getPiso() != null) {
                    direccion += " " + falla.getPuntoEstrategico().getDireccion().getPiso() + "°";
                }

                LocalDateTime fechaFalla = falla.getFechaYHoraFalla().truncatedTo(ChronoUnit.SECONDS);
                String fechaFallaFormateada = fechaFalla.format(formatter);
                LocalDateTime fechaSolucion = falla.getFechaYHoraFueSolucionado().truncatedTo(ChronoUnit.SECONDS).truncatedTo(ChronoUnit.SECONDS);
                String fechaSolucionFormateada = fechaSolucion.format(formatter);

                DTODisplayFallasTecnicas dtoDisplayFallasTecnicas = new DTODisplayFallasTecnicas(
                        falla.getId(),
                        MapperDTOHeladera.convertirADTO(falla.getHeladera()),
                        MapperDTOProvincia.convertirADTO(falla.getPuntoEstrategico().getDireccion().getCiudad().getProvincia()),
                        direccion,
                        Integer.parseInt(falla.getPuntoEstrategico().getDireccion().getCodigoPostal()),
                        falla.getHeladera().getModelo().getNombre(),
                        null,
                        fechaFallaFormateada,
                        fechaSolucionFormateada
                );
                listaDTODisplayFallasTecnicas.add(dtoDisplayFallasTecnicas);
            }

            model.put("fallas", listaDTODisplayFallasTecnicas);

        }else{
            model.put("mensaje", "No hay heladeras con fallas resueltas");
        }

        LocalDateTime ahora = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        String fechaFormateada = ahora.format(formatter);

        model.put("fecha_y_hora", fechaFormateada);
        model.put("titulo", "Fallas resueltas");
        context.render("contribuciones/persona_juridica/ver_incidentes_resueltos.hbs", model);
    }


    public LocalDateTime combinarFechaYHora(LocalDate fechaFalla, Time horaFalla) {
        LocalTime localHoraFalla = horaFalla.toLocalTime();
        return LocalDateTime.of(fechaFalla, localHoraFalla);
    }
}
