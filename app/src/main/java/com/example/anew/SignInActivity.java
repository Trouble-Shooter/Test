package com.example.anew;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText emailEditText, passwordEditText;
    private Button signInSubmitButton;
    private TextView signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up); // Ensure this is your sign-in layout

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        signInSubmitButton = findViewById(R.id.signInSubmit);
        signUpButton = findViewById(R.id.signInBtn);

        // Set up sign-in submit button click listener
        signInSubmitButton.setOnClickListener(v -> signIn());

        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class); // Start Sign Up activity
            startActivity(intent);
        });
    }

    // Sign in method using email and password
    private void signIn() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in successful
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignInActivity.this, "User signed in", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish(); // Close this activity
                        } else {
                            Toast.makeText(SignInActivity.this, "Sign-in failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(SignInActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }
}
