package ar.utn.frba.dds.modelos.entidades.reportes;

import ar.utn.frba.dds.arquitectura.dtos.DTOReporte;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PDFConverter {
    public static List<DTOReporte> convertirADTOReporte(List<Object> objetos) throws IOException {
        List<DTOReporte> reportes = new ArrayList<>();
        for (Object objeto : objetos) {
            Reporte reporte = (Reporte) objeto;
            reportes.add(convertirADTOReporte(reporte));
        }
        return reportes;
    }

    public static DTOReporte convertirADTOReporte(Reporte reporte) throws IOException {
        String nombre = reporte.getNombre();
        Integer tamanio = reporte.getTamanio();
        LocalDate fecha = reporte.getFechaCreacion();
        String path = reporte.getPath();

        return new DTOReporte(nombre, tamanio, fecha, path);
    }

    public static List<Reporte> convertirAReportes(List<Path> pdfFilePaths) throws IOException {
        List<Reporte> reportes = new ArrayList<>();
        for (Path pdfFilePath : pdfFilePaths) {
            reportes.add(convertirAReporte(pdfFilePath.toString()));
        }
        return reportes;
    }

    public static Reporte convertirAReporte(String pdfFilePath) throws IOException {
        Path path = Paths.get(pdfFilePath);
        BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);

        String nombre = obtenerNombreDelArchivo(path);
        long tamanioEnBytes = obtenerTamanioEnBytesDelArchivo(attributes);
        Integer tamanio = (int) tamanioEnBytes;
        LocalDate fecha = obtenerFechaDelArchivo(path,attributes);

        return new Reporte(nombre, tamanio, fecha, pdfFilePath);
    }

    public static String obtenerNombreDelArchivo(Path path){
        return path.getFileName().toString();
    }

    public static long obtenerTamanioEnBytesDelArchivo(BasicFileAttributes attributes){
        return attributes.size();
    }

    public static LocalDate obtenerFechaDelArchivo(Path path, BasicFileAttributes attributes){
        return LocalDate.parse(attributes.lastModifiedTime()
                        .toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static Integer bytesToMegabytes(Long bytes) {
        return (int) (bytes / (1024.0 * 1024.0));
    }
}
