package com.example.rma_2_eldar_salihovic;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TmdbApi {
    @GET("movie/popular")
    Call<TmdbResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("search/movie")
    Call<TmdbResponse> searchMovies(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("page") int page
    );
}