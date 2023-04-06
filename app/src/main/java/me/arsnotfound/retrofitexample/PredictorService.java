package me.arsnotfound.retrofitexample;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PredictorService {
    @GET("getLangs")
    Call<List<String>> getLangs(@Query("key") String apiKey);

    @GET("complete")
    Call<CompleteResponse> complete(@Query("key") String apiKey, @Query("lang") String lang, @Query("q") String query);

    @GET("complete")
    Call<CompleteResponse> complete(@Query("key") String apiKey, @Query("lang") String lang, @Query("q") String query, @Query("limit") int limit);
}