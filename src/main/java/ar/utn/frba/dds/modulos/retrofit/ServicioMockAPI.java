package ar.utn.frba.dds.modulos.retrofit;

import ar.utn.frba.dds.modulos.retrofit.entities.ListadoDePoints;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ServicioMockAPI {
    private static ServicioMockAPI instancia = null;
    private static String urlApi;
    private Retrofit retrofit;

    public String leerArchivoProperties(){
        Properties properties = new Properties();
        try (BufferedReader br = new BufferedReader(new FileReader(
                "./src/main/resources/archivoDeConfiguracion.properties"))) {
            properties.load(br);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return properties.getProperty("servicioMockAPI_urlApi");
    }

    private ServicioMockAPI() {
        urlApi = this.leerArchivoProperties();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ServicioMockAPI instancia(){
        if(instancia == null){
            instancia = new ServicioMockAPI();
        }
        return instancia;
    }

    public ListadoDePoints listadoDePoints() throws IOException {
        PointMockAPI pointMockAPI = this.retrofit.create(PointMockAPI.class);
        Call<ListadoDePoints> requestPoints = pointMockAPI.points(1);
        Response<ListadoDePoints> responsePoints = requestPoints.execute();
        return responsePoints.body();
    }
}
