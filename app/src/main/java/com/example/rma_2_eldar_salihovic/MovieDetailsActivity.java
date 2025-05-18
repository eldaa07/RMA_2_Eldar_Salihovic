package com.example.rma_2_eldar_salihovic;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MovieDetailsActivity extends BaseActivity {

    private static final String TAG = "MovieDetailsActivity";
    private TextView titleTextView, releaseDateTextView, overviewTextView;
    private ImageView posterImageView;
    private Button wishlistButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Movie movie;
    private boolean isInWishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        titleTextView = findViewById(R.id.titleTextView);
        releaseDateTextView = findViewById(R.id.releaseDateTextView);
        overviewTextView = findViewById(R.id.overviewTextView);
        posterImageView = findViewById(R.id.posterImageView);
        wishlistButton = findViewById(R.id.wishlistButton);


        movie = getIntent().getParcelableExtra("MOVIE");
        if (movie == null) {
            Toast.makeText(this, "Greška pri učitavanju filma!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        titleTextView.setText(movie.getTitle());
        releaseDateTextView.setText(movie.getReleaseDate() != null ? movie.getReleaseDate() : "N/A");
        overviewTextView.setText(movie.getOverview());
        String posterPath = movie.getPosterPath();
        String posterUrl = posterPath != null && !posterPath.isEmpty()
                ? "https://image.tmdb.org/t/p/w500" + posterPath
                : null;
        Log.d(TAG, "Učitavanje postera za film: " + movie.getTitle() + ", URL: " + posterUrl);

        Glide.with(this)
                .load(posterUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(posterImageView);


        checkWishlistStatus();


        wishlistButton.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() == null) {
                Toast.makeText(this, "Morate biti prijavljeni!", Toast.LENGTH_SHORT).show();
                return;
            }
            toggleWishlist();
        });
    }

    private void checkWishlistStatus() {
        if (mAuth.getCurrentUser() == null) {
            wishlistButton.setText(R.string.add_to_wishlist);
            isInWishlist = false;
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("wishlist")
                .document(String.valueOf(movie.getId()))
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    isInWishlist = documentSnapshot.exists();
                    wishlistButton.setText(isInWishlist ? R.string.remove_from_wishlist : R.string.add_to_wishlist);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Greška: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void toggleWishlist() {
        String userId = mAuth.getCurrentUser().getUid();
        if (isInWishlist) {
            db.collection("users").document(userId).collection("wishlist")
                    .document(String.valueOf(movie.getId()))
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        isInWishlist = false;
                        wishlistButton.setText(R.string.add_to_wishlist);
                        Toast.makeText(this, "Uklonjeno iz wishliste!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Greška: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            db.collection("users").document(userId).collection("wishlist")
                    .document(String.valueOf(movie.getId()))
                    .set(movie)
                    .addOnSuccessListener(aVoid -> {
                        isInWishlist = true;
                        wishlistButton.setText(R.string.remove_from_wishlist);
                        Toast.makeText(this, "Dodano u wishlistu!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Greška: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}