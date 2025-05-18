package com.example.rma_2_eldar_salihovic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends BaseActivity {

    private static final String TAG = "WishlistActivity";
    private RecyclerView wishlistRecyclerView;
    private MovieAdapter movieAdapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Wishlist");
        }


        wishlistRecyclerView = findViewById(R.id.wishlistRecyclerView);
        wishlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(new ArrayList<>());
        wishlistRecyclerView.setAdapter(movieAdapter);


        movieAdapter.setOnMovieClickListener(movie -> {
            Log.d(TAG, "Klik na film u wishlisti: " + movie.getTitle());
            Intent intent = new Intent(WishlistActivity.this, MovieDetailsActivity.class);
            intent.putExtra("MOVIE", movie);
            startActivity(intent);
        });


        if (mAuth.getCurrentUser() == null) {
            Log.e(TAG, "Korisnik nije prijavljen!");
            Toast.makeText(this, "Morate biti prijavljeni!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        loadWishlist();
    }

    private void loadWishlist() {
        String userId = mAuth.getCurrentUser().getUid();
        Log.d(TAG, "Dohvaćanje wishliste za korisnika: " + userId);
        db.collection("users").document(userId).collection("wishlist")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Movie> wishlistMovies = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            Movie movie = document.toObject(Movie.class);
                            if (movie != null && movie.getTitle() != null) {
                                wishlistMovies.add(movie);
                                Log.d(TAG, "Dodan film u wishlistu: " + movie.getTitle());
                            } else {
                                Log.w(TAG, "Neispravan film u dokumentu: " + document.getId());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Greška pri parsiranju filma: " + document.getId(), e);
                        }
                    }
                    Log.d(TAG, "Ukupno filmova u wishlisti: " + wishlistMovies.size());
                    movieAdapter.updateMovies(wishlistMovies);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Greška pri učitavanju wishliste", e);
                    Toast.makeText(this, "Greška pri učitavanju wishliste: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}