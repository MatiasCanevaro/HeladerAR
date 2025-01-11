package ar.utn.frba.dds.modelos.entidades.reportes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class PDFFileFinderTest {
    public static void main(String[] args) {
        try {
            List<Path> pdfFiles = PDFFileFinder.buscarReportesEnFormatoPDF("./");
            pdfFiles.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
