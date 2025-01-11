package ar.utn.frba.dds.modulos.retrofit;

import ar.utn.frba.dds.modulos.retrofit.entities.ListadoDePoints;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PointMockAPI {
    @GET("estudiante/{id}")
    Call<ListadoDePoints> points(@Path("id") int pointId);
}
