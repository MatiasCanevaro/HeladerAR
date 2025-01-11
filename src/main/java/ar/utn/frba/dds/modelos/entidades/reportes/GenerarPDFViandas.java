package ar.utn.frba.dds.modelos.entidades.reportes;

import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.tarjetas.AccionSolicitada;
import ar.utn.frba.dds.modelos.entidades.tarjetas.PaseDeTarjeta;
import ar.utn.frba.dds.modelos.entidades.tarjetas.ResultadoPaseDeTarjeta;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Builder
public class GenerarPDFViandas implements GenerarPDF{
    private Repositorio repositorio;
    private PDFBox pdfbox;
    public void reportar(){
        List<Object> pasesDeTarjeta = repositorio.buscarTodos("PaseDeTarjeta");
        String contenido = this.obtenerResumenViandas(pasesDeTarjeta);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        LocalDateTime fechaYHoraActual = LocalDateTime.now();
        String fechaYHoraActualConvertida = fechaYHoraActual.format(formatter);
        pdfbox.generarPDF("Reporte Donaciones-Retiros de viandas " + fechaYHoraActualConvertida + ".pdf",
                "Reporte Semanal de Fallas Tecnicas", contenido, null);
    }

    public String obtenerResumenViandas(List<Object> pasesDeTarjeta) {
        Map<Heladera, Integer> viandasRetiradasPorHeladera = new HashMap<>();
        Map<Heladera, Integer> viandasColocadasPorHeladera = new HashMap<>();

        List<PaseDeTarjeta> pasesAceptados = pasesDeTarjeta.stream()
                .filter(pase -> pase instanceof PaseDeTarjeta)
                .map(pase -> (PaseDeTarjeta) pase)
                .filter(pase -> pase.getResultadoPaseDeTarjeta() == ResultadoPaseDeTarjeta.ACEPTADO)
                .toList();

        for (PaseDeTarjeta pase : pasesAceptados) {
            Heladera heladera = pase.getHeladera();
            int cantidadViandas = pase.cantidadDeViandas();

            if (pase.getAccionSolicitada() == AccionSolicitada.RETIRO) {
                viandasRetiradasPorHeladera.put(heladera, viandasRetiradasPorHeladera.
                        getOrDefault(heladera, 0) + cantidadViandas);
            } else if (pase.getAccionSolicitada() == AccionSolicitada.INGRESO) {
                viandasColocadasPorHeladera.put(heladera, viandasColocadasPorHeladera.
                        getOrDefault(heladera, 0) + cantidadViandas);
            }
        }

        StringBuilder resultado = new StringBuilder();
        for (Heladera heladera : viandasRetiradasPorHeladera.keySet()) {
            int retiradas = viandasRetiradasPorHeladera.getOrDefault(heladera, 0);
            resultado.append("Heladera: ").append(heladera.getId())
                    .append(" - Viandas Retiradas: ").append(retiradas).append("\n");
        }
        for (Heladera heladera : viandasColocadasPorHeladera.keySet()) {
            int colocadas = viandasColocadasPorHeladera.getOrDefault(heladera, 0);
            resultado.append("Heladera: ").append(heladera.getId())
                    .append(" - Viandas Ingresadas: ").append(colocadas).append("\n");
        }

        return resultado.toString();
    }

    public static GenerarPDFViandas crearGenerarPDFViandas(){
        Repositorio repositorio = new Repositorio();
        PDFBox pdfBox = new PDFBox();
        return GenerarPDFViandas
                .builder()
                .repositorio(repositorio)
                .pdfbox(pdfBox)
                .build();
    }
    public static GenerarPDFViandas crearGenerarPDFViandas(Repositorio repositorio){
        PDFBox pdfBox = new PDFBox();
        return GenerarPDFViandas
                .builder()
                .repositorio(repositorio)
                .pdfbox(pdfBox)
                .build();
    }
}
