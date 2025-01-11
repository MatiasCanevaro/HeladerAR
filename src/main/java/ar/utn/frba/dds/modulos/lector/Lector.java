package ar.utn.frba.dds.modulos.lector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Lector {
    public String leerArchivoProperties(){
        Properties properties = new Properties();
        try (BufferedReader br = new BufferedReader(new FileReader(
                "./src/main/resources/archivoDeConfiguracion.properties"))) {
            properties.load(br);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return properties.getProperty("lector_caracter_centinela");
    }

    public List<String[]> leerCSV(String rutaArchivo) {
        List<String[]> registros = new ArrayList<>();
        String caracter_centinela = leerArchivoProperties();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(caracter_centinela);
                registros.add(campos);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return registros;
    }
}
