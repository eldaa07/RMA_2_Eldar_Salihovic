package com.example.rma_2_eldar_salihovic;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = "MovieAdapter";
    private List<Movie> movies;
    private OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MovieAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    public void setOnMovieClickListener(OnMovieClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.titleTextView.setText(movie.getTitle());
        holder.releaseDateTextView.setText(movie.getReleaseDate() != null ? movie.getReleaseDate() : "N/A");

        String posterPath = movie.getPosterPath();
        String posterUrl = posterPath != null && !posterPath.isEmpty()
                ? "https://image.tmdb.org/t/p/w500" + posterPath
                : null;
        Log.d(TAG, "Učitavanje postera za film: " + movie.getTitle() + ", URL: " + posterUrl);

        Glide.with(holder.itemView.getContext())
                .load(posterUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.posterImageView);

        holder.itemView.setOnClickListener(v -> {
            Log.d(TAG, "Klik na film: " + movie.getTitle());
            if (listener != null) {
                listener.onMovieClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void updateMovies(List<Movie> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImageView;
        TextView titleTextView, releaseDateTextView;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            releaseDateTextView = itemView.findViewById(R.id.releaseDateTextView);
        }
    }
}