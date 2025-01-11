package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.dtos.DTOReporte;
import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import ar.utn.frba.dds.modelos.entidades.incidentes.VisitaTecnica;
import ar.utn.frba.dds.modelos.entidades.reportes.PDFFileFinder;
import ar.utn.frba.dds.modelos.entidades.reportes.PDFConverter;
import ar.utn.frba.dds.modelos.entidades.reportes.Reporte;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@AllArgsConstructor
public class ReportesController implements ICrudViewsHandler {
    private Repositorio repositorio;
    private String rutaCarpetaReportes;
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

    public void mostrarReportes(Context context) throws IOException {
        Map<String, Object> model = new HashMap<>();
        List<Object> objetosReporte = repositorio.buscarTodos("Reporte");
        List<DTOReporte> dtosReporte = PDFConverter.convertirADTOReporte(objetosReporte);

        model.put("reportes", dtosReporte);
        model.put("titulo", "Reportes");
        context.render("reportes/reportes.hbs", model);
    }

    public void descargarReporte(Context context) throws IOException {
        String nombre = context.queryParam("nombre");
        Reporte reporte = repositorio.buscarPorNombre(nombre,"Reporte",Reporte.class);
        if (nombre != null) {
            String pathReporte = reporte.getPath();
            Path path = Paths.get(pathReporte);
            if (Files.exists(path)) {
                context.result(Files.newInputStream(path))
                        .contentType("application/pdf")
                        .header("Content-Disposition", "attachment; filename=\"" + path.getFileName().toString() + "\"");
            } else {
                context.status(404).result("File not found");
            }
        } else {
            context.status(400).result("Missing file path");
        }
    }

    public void descargarTodosReportes(Context context) throws IOException {
        List<String> nombres = context.queryParams("nombres");
        if (!nombres.isEmpty()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                for (String nombre : nombres) {
                    Reporte reporte = repositorio.buscarPorNombre(nombre, "Reporte", Reporte.class);
                    Path path = Paths.get(reporte.getPath());
                    if (Files.exists(path)) {
                        zos.putNextEntry(new ZipEntry(path.getFileName().toString()));
                        Files.copy(path, zos);
                        zos.closeEntry();
                    }
                }
            }
            context.result(baos.toByteArray())
                    .contentType("application/zip")
                    .header("Content-Disposition", "attachment; filename=\"reportes.zip\"");
        } else {
            context.status(400).result("No files to download");
        }
    }
    public static String leerArchivoProperties(){
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("./src/main/resources/archivoDeConfiguracion.properties")) {
            properties.load(fis);
            return properties.getProperty("carpeta_reportes");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return null;
    }
}
