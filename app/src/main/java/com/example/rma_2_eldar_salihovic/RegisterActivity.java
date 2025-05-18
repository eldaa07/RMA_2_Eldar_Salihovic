package com.example.rma_2_eldar_salihovic;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends BaseActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText, nameEditText;
    private Button registerButton;
    private TextView loginRedirectText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();


        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        registerButton = findViewById(R.id.registerButton);
        loginRedirectText = findViewById(R.id.loginRedirectText);


        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
            String name = nameEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(name)) {
                Toast.makeText(RegisterActivity.this, "Popunite sva polja!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Lozinke se ne podudaraju!", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(profileTask -> {
                                            if (profileTask.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Registracija uspješna!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Greška pri postavljanju imena: " + profileTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registracija neuspješna: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });


        loginRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}