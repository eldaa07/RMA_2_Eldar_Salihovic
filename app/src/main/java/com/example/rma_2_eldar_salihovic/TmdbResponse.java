package com.example.rma_2_eldar_salihovic;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TmdbResponse {
    @SerializedName("results")
    private List<Movie> movies;

    public List<Movie> getMovies() { return movies; }
    public void setMovies(List<Movie> movies) { this.movies = movies; }
}