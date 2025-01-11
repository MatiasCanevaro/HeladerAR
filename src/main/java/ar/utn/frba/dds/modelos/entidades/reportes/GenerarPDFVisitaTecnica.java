package ar.utn.frba.dds.modelos.entidades.reportes;

import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DonacionDeVianda;
import ar.utn.frba.dds.modelos.entidades.incidentes.VisitaTecnica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import lombok.Builder;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Builder
public class GenerarPDFVisitaTecnica implements GenerarPDF {
    private Repositorio repositorio;
    private PDFBox pdfbox;

    public void reportar() {
        List<Object> visitasTecnica = repositorio.buscarTodos("VisitaTecnica");
        String contenido = this.generarContenidoVisitasTecnicas(visitasTecnica);

        // Crear lista de rutas de imágenes
        List<String> imagePaths = visitasTecnica.stream()
                .map(v -> ((VisitaTecnica) v).getPathImagen())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        LocalDateTime fechaYHoraActual = LocalDateTime.now();
        String fechaYHoraActualConvertida = fechaYHoraActual.format(formatter);

        pdfbox.generarPDF("Reporte Visitas Tecnicas " + fechaYHoraActualConvertida + ".pdf",
                "Reporte Visitas Tecnica", contenido, null);
    }

    public String generarContenidoVisitasTecnicas(List<Object> visitasTecnica) {
        StringBuilder contenido = new StringBuilder();
        contenido.append("Reporte de Visitas Técnicas:\n");

        for (Object o : visitasTecnica) {
            VisitaTecnica visita = (VisitaTecnica) o;
            contenido.append("Técnico: ").append(visita.getTecnico().getNombre()).append("\n")
                    .append("Heladera: ").append(visita.getHeladera().getId()).append("\n")
                    .append("Fecha y Hora de Visita: ").append(visita.getFechaYHoraVisita()).append("\n")
                    .append("Descripción del Trabajo: ").append(visita.getDescripcionDelTrabajo()).append("\n")
                    .append("Falla Técnica: ").append(visita.getFallaTecnica().getDescripcion()).append("\n")
                    .append("Pudo Solucionar el Incidente: ").append(visita.getPudoSolucionarElIncidente()).append("\n\n");
        }

        return contenido.toString();
    }

    public static GenerarPDFVisitaTecnica crearGenerarPDFVisitaTecnica() {
        Repositorio repositorio = new Repositorio();
        PDFBox pdfBox = new PDFBox();
        return GenerarPDFVisitaTecnica
                .builder()
                .repositorio(repositorio)
                .pdfbox(pdfBox)
                .build();
    }

    public static GenerarPDFVisitaTecnica crearGenerarPDFVisitaTecnica(Repositorio repositorio) {
        PDFBox pdfBox = new PDFBox();
        return GenerarPDFVisitaTecnica
                .builder()
                .repositorio(repositorio)
                .pdfbox(pdfBox)
                .build();
    }
}

