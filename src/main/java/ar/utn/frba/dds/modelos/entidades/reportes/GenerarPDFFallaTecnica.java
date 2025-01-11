package ar.utn.frba.dds.modelos.entidades.reportes;

import ar.utn.frba.dds.modelos.entidades.geografia.Direccion;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.incidentes.FallaTecnica;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
public class GenerarPDFFallaTecnica implements GenerarPDF{
    private Repositorio repositorio;
    private PDFBox pdfbox;
    public void reportar(){
        List<Object> fallasTecnicas = repositorio.buscarTodos("FallaTecnica");
        String contenido = this.calcularCantidadFallasPorHeladera(fallasTecnicas);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        LocalDateTime fechaYHoraActual = LocalDateTime.now();
        String fechaYHoraActualConvertida = fechaYHoraActual.format(formatter);
        pdfbox.generarPDF("Reporte Fallas Tecnicas " + fechaYHoraActualConvertida + ".pdf",
                "Reporte Semanal de Fallas Tecnicas", contenido, null);
    }

    public String calcularCantidadFallasPorHeladera(List<Object> fallasTecnicas) {
        Map<Heladera, Integer> cantidadFallasPorHeladera = new HashMap<>();

        for (Object o : fallasTecnicas) {
            FallaTecnica falla = (FallaTecnica) o;
            Heladera heladera = falla.getHeladera();
            cantidadFallasPorHeladera.put(heladera, cantidadFallasPorHeladera.getOrDefault(heladera, 0) + 1);
        }

        StringBuilder reporte = new StringBuilder();
        reporte.append("Reporte de Fallas por Heladera:\n");

        for (Map.Entry<Heladera, Integer> entry : cantidadFallasPorHeladera.entrySet()) {
            Heladera heladera = entry.getKey();
            Integer cantidadFallas = entry.getValue();
            reporte.append("Heladera ID: ").append(heladera.getId()).append("\n")
                    .append("Direccion: " + heladera.getPuntoEstrategico().getDireccion().getCalle() + " "
                            + heladera.getPuntoEstrategico().getDireccion().getAltura() + " CP"
                    + heladera.getPuntoEstrategico().getDireccion().getCodigoPostal()).append("\n")
                    .append(heladera.getPuntoEstrategico().getDireccion().getCiudad().getNombre() + ", "
                            + heladera.getPuntoEstrategico().getDireccion().getProvincia().getNombre())
                    .append("\n")
                    .append("Cantidad de Fallas: ").append(cantidadFallas)
                    .append("\n")
                    .append("\n");
        }
        return reporte.toString();
    }

    public static GenerarPDFFallaTecnica crearGenerarPDFFallaTecnica(){
        Repositorio repositorio = new Repositorio();
        PDFBox pdfBox = new PDFBox();
        return GenerarPDFFallaTecnica
                .builder()
                .repositorio(repositorio)
                .pdfbox(pdfBox)
                .build();
    }

    public static GenerarPDFFallaTecnica crearGenerarPDFFallaTecnica(Repositorio repositorio){
        PDFBox pdfBox = new PDFBox();
        return GenerarPDFFallaTecnica
                .builder()
                .repositorio(repositorio)
                .pdfbox(pdfBox)
                .build();
    }
}
