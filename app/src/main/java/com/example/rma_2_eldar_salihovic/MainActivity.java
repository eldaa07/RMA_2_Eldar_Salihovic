package com.example.rma_2_eldar_salihovic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private MovieViewModel movieViewModel;
    private MovieAdapter movieAdapter;
    private RecyclerView moviesRecyclerView;
    private SearchView searchView;
    private FirebaseAuth mAuth;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "MovieAppPrefs";
    private static final String PREF_THEME = "theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("MovieApp");
        }


        moviesRecyclerView = findViewById(R.id.moviesRecyclerView);
        searchView = findViewById(R.id.searchView);


        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(new ArrayList<>());
        moviesRecyclerView.setAdapter(movieAdapter);


        movieAdapter.setOnMovieClickListener(movie -> {
            Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            intent.putExtra("MOVIE", movie);
            startActivity(intent);
        });


        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);


        movieViewModel.getMovies().observe(this, movies -> {
            movieAdapter.updateMovies(movies);
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieViewModel.searchMovies(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                movieViewModel.searchMovies(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.action_wishlist) {
            Intent intent = new Intent(MainActivity.this, WishlistActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_toggle_theme) {
            prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            boolean isLightTheme = prefs.getBoolean(PREF_THEME, false);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(PREF_THEME, !isLightTheme);
            editor.apply();
            recreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}