package ar.utn.frba.dds.modelos.entidades.reportes;

import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import lombok.Builder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
public class PDFBox {
    private String rutaBase;
    private Repositorio repositorio;

    public PDFBox() {
        this.rutaBase = this.leerArchivoProperties();
        this.repositorio = new Repositorio();
    }

    public void generarPDF(String nombreArchivo, String titulo, String cuerpo, List<String> imagePaths) {
        String rutaCompleta = rutaBase + nombreArchivo;
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Escribir título
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 18);
                contentStream.newLineAtOffset(160, 750);
                contentStream.showText(titulo);
                contentStream.endText();

                // Escribir cuerpo
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(100, 700);

                String[] lineas = cuerpo.split("\n");
                for (String linea : lineas) {
                    contentStream.showText(linea);
                    contentStream.newLineAtOffset(0, -15);
                }
                contentStream.endText();

                // Agregar imágenes debajo del texto
                float imageYOffset = 600; // Ajuste la posición vertical de la imagen
                if(imagePaths != null) {
                    for (String imagePath : imagePaths) {
                        if (imagePath != null && !imagePath.isEmpty()) {
                            //addImage(document, page, contentStream, imagePath, imageYOffset);
                            //imageYOffset -= 200; // Espacio vertical entre imágenes
                            return;
                        }
                    }
                }
            }

            document.save(rutaCompleta);
            Reporte reporte = PDFConverter.convertirAReporte(rutaCompleta);
            repositorio.guardar(reporte);
            System.out.println("PDF creado en " + rutaCompleta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addImage(PDDocument document, PDPage page, PDPageContentStream contentStream, String imagePath, float yOffset) {
        try {
            PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, document);
            PDRectangle mediaBox = page.getMediaBox();

            float maxWidth = mediaBox.getWidth() - 100; // Margen de 50 a cada lado
            float maxHeight = yOffset - 50; // Espacio desde el texto superior

            float imageWidth = pdImage.getWidth();
            float imageHeight = pdImage.getHeight();

            // Escala proporcional si excede el tamaño disponible
            if (imageWidth > maxWidth || imageHeight > maxHeight) {
                float widthRatio = maxWidth / imageWidth;
                float heightRatio = maxHeight / imageHeight;
                float scale = Math.min(widthRatio, heightRatio);

                imageWidth *= scale;
                imageHeight *= scale;
            }

            float startX = (mediaBox.getWidth() - imageWidth) / 2; // Centrar horizontalmente
            contentStream.drawImage(pdImage, startX, yOffset - imageHeight, imageWidth, imageHeight);
        } catch (IOException e) {
            System.err.println("Error al añadir la imagen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String leerArchivoProperties() {
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
