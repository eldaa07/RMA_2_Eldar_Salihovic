package com.example.rma_2_eldar_salihovic;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private TextView emailTextView, nameTextView;
    private EditText newPasswordEditText, confirmPasswordEditText;
    private Button updatePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Profil");
        }


        emailTextView = findViewById(R.id.emailTextView);
        nameTextView = findViewById(R.id.nameTextView);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        updatePasswordButton = findViewById(R.id.updatePasswordButton);


        if (currentUser != null) {
            emailTextView.setText(currentUser.getEmail());
            nameTextView.setText(currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Nije postavljeno");
        } else {
            Toast.makeText(this, "Morate biti prijavljeni!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        updatePasswordButton.setOnClickListener(v -> {
            String newPassword = newPasswordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Popunite sva polja!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Lozinke se ne podudaraju!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 6) {
                Toast.makeText(this, "Lozinka mora imati najmanje 6 karaktera!", Toast.LENGTH_SHORT).show();
                return;
            }

            currentUser.updatePassword(newPassword)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Lozinka uspješno ažurirana!", Toast.LENGTH_SHORT).show();
                        newPasswordEditText.setText("");
                        confirmPasswordEditText.setText("");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Greška: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}