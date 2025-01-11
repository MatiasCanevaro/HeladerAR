package ar.utn.frba.dds.modulos.recomendadoraDeLugaresDeDonacion;

import ar.utn.frba.dds.modulos.recomendadoraDeLugaresDeDonacion.entities.Key;
import ar.utn.frba.dds.modulos.recomendadoraDeLugaresDeDonacion.entities.ListadoDeLugares;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface IRecomendadoraDeLugaresDeDonacionAPI {
    @GET("api/key")
    Call<Key> key();
    @GET("api/locaciones-donacion")
    Call<ListadoDeLugares> lugares(
            @Header("Authorization") String apiKey,
            @Query("lat") Double lat,
            @Query("lon") Double lon);
}
