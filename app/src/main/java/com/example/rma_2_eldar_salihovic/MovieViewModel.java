package com.example.rma_2_eldar_salihovic;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

public class MovieViewModel extends ViewModel {

    private static final String TAG = "MovieViewModel";
    private MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private MovieRepository movieRepository;

    public MovieViewModel() {
        movieRepository = new MovieRepository();
        loadMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void searchMovies(String query) {
        if (query.isEmpty()) {
            movieRepository.getMovies(new MovieRepository.RepositoryCallback() {
                @Override
                public void onSuccess(List<Movie> movieList) {
                    movies.setValue(movieList);
                    Log.d(TAG, "Uspješno dohvaćeni popularni filmovi, broj: " + movieList.size());
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "Greška pri dohvatanju filmova: " + error);
                }
            });
        } else {
            movieRepository.searchMovies(query, new MovieRepository.RepositoryCallback() {
                @Override
                public void onSuccess(List<Movie> movieList) {
                    movies.setValue(movieList);
                    Log.d(TAG, "Uspješno pretraženi filmovi za upit '" + query + "', broj: " + movieList.size());
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "Greška pri pretrazi filmova: " + error);
                }
            });
        }
    }

    private void loadMovies() {
        searchMovies("");
    }
}