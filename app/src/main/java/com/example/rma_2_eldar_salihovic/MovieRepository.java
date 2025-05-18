package com.example.rma_2_eldar_salihovic;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.List;
import android.util.Log;

public class MovieRepository {

    private static final String TAG = "MovieRepository";
    private final TmdbApi api;
    private final String API_KEY = BuildConfig.TMDB_API_KEY; // Koristimo BuildConfig umjesto hardkodiranog ključa

    public MovieRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(TmdbApi.class);
    }

    public interface RepositoryCallback {
        void onSuccess(List<Movie> movies);
        void onError(String error);
    }

    public void getMovies(RepositoryCallback callback) {
        api.getPopularMovies(API_KEY, 1).enqueue(new Callback<TmdbResponse>() {
            @Override
            public void onResponse(Call<TmdbResponse> call, Response<TmdbResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getMovies();
                    Log.d(TAG, "Dohvaćeno " + movies.size() + " popularnih filmova");
                    for (Movie movie : movies) {
                        Log.d(TAG, "Film: " + movie.getTitle() + ", Poster: " + movie.getPosterPath() + ", Release: " + movie.getReleaseDate());
                    }
                    callback.onSuccess(movies);
                } else {
                    Log.e(TAG, "Greška u responsu: " + response.message());
                    callback.onError("Greška pri dohvatanju filmova");
                }
            }

            @Override
            public void onFailure(Call<TmdbResponse> call, Throwable t) {
                Log.e(TAG, "Mrežna greška: " + t.getMessage());
                callback.onError(t.getMessage());
            }
        });
    }

    public void searchMovies(String query, RepositoryCallback callback) {
        api.searchMovies(API_KEY, query, 1).enqueue(new Callback<TmdbResponse>() {
            @Override
            public void onResponse(Call<TmdbResponse> call, Response<TmdbResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getMovies();
                    Log.d(TAG, "Dohvaćeno " + movies.size() + " filmova za pretragu: " + query);
                    for (Movie movie : movies) {
                        Log.d(TAG, "Film: " + movie.getTitle() + ", Poster: " + movie.getPosterPath() + ", Release: " + movie.getReleaseDate());
                    }
                    callback.onSuccess(movies);
                } else {
                    Log.e(TAG, "Greška u responsu: " + response.message());
                    callback.onError("Greška pri pretrazi filmova");
                }
            }

            @Override
            public void onFailure(Call<TmdbResponse> call, Throwable t) {
                Log.e(TAG, "Mrežna greška: " + t.getMessage());
                callback.onError(t.getMessage());
            }
        });
    }
}