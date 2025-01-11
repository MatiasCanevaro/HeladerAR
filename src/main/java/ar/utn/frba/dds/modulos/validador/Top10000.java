package ar.utn.frba.dds.modulos.validador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Top10000 implements FormasDeValidacion {

    private List<String> contraseniasDebiles = this.cargarListaContraseniasDebiles();

    public String leerArchivoProperties(){
        Properties properties = new Properties();
        try (BufferedReader br = new BufferedReader(new FileReader(
                "./src/main/resources/archivoDeConfiguracion.properties"))) {
            properties.load(br);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return properties.getProperty("validador_ruta_archivo_top_10000");
    }

    public List<String> cargarListaContraseniasDebiles() {
        String rutaArchivo = this.leerArchivoProperties();
        List<String> listaConstrasenias = new ArrayList<>();

        try (FileReader fr = new FileReader(rutaArchivo)) {
            BufferedReader br = new BufferedReader(fr);
            String linea;

            while ((linea = br.readLine()) != null) {
                listaConstrasenias.add(linea);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return listaConstrasenias;
    }

    public void validar(String contrasenia, ResultadoValidacion resultadoValidacion) {
        Boolean estado = !this.contraseniasDebiles.contains(contrasenia);
        if(!estado){
            resultadoValidacion.agregarError("La contraseña se encuentra en el listado de 10000 peores contraseñas");
            resultadoValidacion.setLaContraseniaEsValida(false);
        }
    }
}