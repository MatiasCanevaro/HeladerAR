package ar.utn.frba.dds.modelos.entidades.reportes;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class PDFFileFinder {
    public static List<Path> buscarReportesEnFormatoPDF(String directory) throws IOException {
        List<Path> pdfFiles = new ArrayList<>();
        Path startPath = Paths.get(directory);

        Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".pdf") && file.getFileName().toString().startsWith("Reporte")) {
                    pdfFiles.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return pdfFiles;
    }
    public static Path buscarArchivoPorNombre(String directory, String fileName) throws IOException {
        Path startPath = Paths.get(directory);
        List<Path> foundFiles = new ArrayList<>();

        Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.getFileName().toString().equals(fileName)) {
                    foundFiles.add(file);
                    return FileVisitResult.TERMINATE;
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return foundFiles.isEmpty() ? null : foundFiles.get(0);
    }
}