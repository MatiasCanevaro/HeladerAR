package ar.utn.frba.dds.modelos.entidades.reportes;

import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.DonacionDeVianda;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public class GenerarPDFDonacionDeViandas implements GenerarPDF{
    private Repositorio repositorio;
    private PDFBox pdfbox;

    public void reportar(){
        List<Object> donacionesDeViandas = repositorio.buscarTodos("DonacionDeVianda");
        String contenido = this.contarDonacionesPorPersona(donacionesDeViandas);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        LocalDateTime fechaYHoraActual = LocalDateTime.now();
        String fechaYHoraActualConvertida = fechaYHoraActual.format(formatter);
        pdfbox.generarPDF("Reporte Donacion de Viandas " + fechaYHoraActualConvertida + ".pdf",
                "Reporte Semanal de Donacion de Viandas", contenido, null);
    }

    public String contarDonacionesPorPersona(List<Object> donacionesDeViandas) {
        Map<PersonaFisica, Long> conteoPorPersona = donacionesDeViandas.stream()
                .filter(donacion -> donacion instanceof DonacionDeVianda)
                .map(donacion -> (DonacionDeVianda) donacion)
                .collect(Collectors.groupingBy(DonacionDeVianda::getPersonaFisica, Collectors.counting()));

        StringBuilder result = new StringBuilder();
        result.append("Donaciones por Persona Fisica:\n");
        conteoPorPersona.forEach((persona, cantidad) ->
                result.append(persona.getNombre()).append(": ").append(cantidad).append("\n"));

        return result.toString();
    }

    public static GenerarPDFDonacionDeViandas crearGenerarPDFDonacionDeViandas(){
        Repositorio repositorio = new Repositorio();
        PDFBox pdfBox = new PDFBox();
        return GenerarPDFDonacionDeViandas
                .builder()
                .pdfbox(pdfBox)
                .repositorio(repositorio)
                .build();
    }
    public static GenerarPDFDonacionDeViandas crearGenerarPDFDonacionDeViandas(Repositorio repositorio){
        PDFBox pdfBox = new PDFBox();
        return GenerarPDFDonacionDeViandas
                .builder()
                .pdfbox(pdfBox)
                .repositorio(repositorio)
                .build();
    }
}
