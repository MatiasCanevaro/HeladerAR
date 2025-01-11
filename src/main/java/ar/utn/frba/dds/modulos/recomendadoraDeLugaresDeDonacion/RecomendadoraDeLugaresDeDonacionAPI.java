package ar.utn.frba.dds.modulos.recomendadoraDeLugaresDeDonacion;

import ar.utn.frba.dds.modulos.recomendadoraDeLugaresDeDonacion.entities.Key;
import ar.utn.frba.dds.modulos.recomendadoraDeLugaresDeDonacion.entities.ListadoDeLugares;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class RecomendadoraDeLugaresDeDonacionAPI {
    private static RecomendadoraDeLugaresDeDonacionAPI instancia = null;
    private static String host;
    private Retrofit retrofit;

    public String leerArchivoProperties(){
        Properties properties = new Properties();
        try (BufferedReader br = new BufferedReader(new FileReader(
                "./src/main/resources/archivoDeConfiguracion.properties"))) {
            properties.load(br);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return properties.getProperty("recomendadora_de_lugares_de_donacion_API_host");
    }

    private RecomendadoraDeLugaresDeDonacionAPI() {
        host = this.leerArchivoProperties();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RecomendadoraDeLugaresDeDonacionAPI instancia(){
        if(instancia == null){
            instancia = new RecomendadoraDeLugaresDeDonacionAPI();
        }
        return instancia;
    }

    public Key obtenerKey() throws IOException {
        IRecomendadoraDeLugaresDeDonacionAPI iRecomendadoraDeLugaresDeDonacionAPI =
                this.retrofit.create(IRecomendadoraDeLugaresDeDonacionAPI.class);
        Call<Key> requestKey = iRecomendadoraDeLugaresDeDonacionAPI.key();
        Response<Key> responsePoints = requestKey.execute();
        return responsePoints.body();
    }
    public ListadoDeLugares listadoDeLugares(String apiKey,Double latitud,Double longitud) throws IOException {
        IRecomendadoraDeLugaresDeDonacionAPI iRecomendadoraDeLugaresDeDonacionAPI =
                this.retrofit.create(IRecomendadoraDeLugaresDeDonacionAPI.class);
        Call<ListadoDeLugares> requestLugares = iRecomendadoraDeLugaresDeDonacionAPI.lugares(apiKey,latitud,longitud);
        Response<ListadoDeLugares> responseLugares = requestLugares.execute();
        return responseLugares.body();
    }
}
