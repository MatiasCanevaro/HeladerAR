package ar.utn.frba.dds.modelos.modulos.recomendadoraDeLugaresDeDonacion;

import ar.utn.frba.dds.modulos.recomendadoraDeLugaresDeDonacion.RecomendadoraDeLugaresDeDonacionAPI;
import ar.utn.frba.dds.modulos.recomendadoraDeLugaresDeDonacion.entities.Key;
import ar.utn.frba.dds.modulos.recomendadoraDeLugaresDeDonacion.entities.ListadoDeLugares;
import ar.utn.frba.dds.modulos.recomendadoraDeLugaresDeDonacion.entities.Lugar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class RecomendadoraDeLugaresDeDonacionAPITest {
    public static void main(String[] args) throws IOException {
        RecomendadoraDeLugaresDeDonacionAPI recomendadoraDeLugaresDeDonacionAPI =
                RecomendadoraDeLugaresDeDonacionAPI.instancia();

        Key key = recomendadoraDeLugaresDeDonacionAPI.obtenerKey();
        System.out.println("Valor: " + key.key + "\n");

        Double latitud = -34.63566222604892;
        Double longitud = -58.36478532941284;

        String apiKey = leerArchivoProperties();

        ListadoDeLugares listadoDeLugares =
                recomendadoraDeLugaresDeDonacionAPI.listadoDeLugares(apiKey,latitud,longitud);

        for(Lugar lugar : listadoDeLugares.lugares){
            System.out.println("id: " + lugar.id +
                    "\nNombre: " + lugar.nombre +
                    "\nLatitud: " + lugar.lat +
                    "\nLongitud: " + lugar.lon +
                    "\nDistancia en KM: " + lugar.distanciaEnKM +
                    "\n");
        }
    }
    public static String leerArchivoProperties(){
        Properties properties = new Properties();
        try (BufferedReader br = new BufferedReader(new FileReader(
                "./src/test/resources/archivoDeConfiguracion.properties"))) {
            properties.load(br);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return properties.getProperty("recomendadora_de_lugares_de_donacion_API_test_apiKey");
    }
}
