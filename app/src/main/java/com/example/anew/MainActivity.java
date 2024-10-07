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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText fullNameEditText, emailEditText, passwordEditText;
    private Button signUpSubmitButton;
    private TextView signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize UI elements
        fullNameEditText = findViewById(R.id.full_name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        signUpSubmitButton = findViewById(R.id.signUpSubmit);
        signInButton = findViewById(R.id.signUpBtn);

        // Set up sign-up submit button click listener
        signUpSubmitButton.setOnClickListener(v -> createAccount());

        signInButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class); // Start Sign In activity
            startActivity(intent);
        });
    }

    // Create a new account with email and password
    private void createAccount() {
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        saveUserInfo(user, fullName);
                        Toast.makeText(MainActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                        // Open main activity or home page
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish(); // Close this activity
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Save user information to Firebase Database
    private void saveUserInfo(FirebaseUser user, String fullName) {
        String email = user.getEmail();
        // Create a User object to store in the database
        User newUser = new User(fullName, email);

        // Save the user to Firebase Realtime Database
        mDatabase.child(user.getUid()).setValue(newUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to create user", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Create a User class to represent a user object
    public static class User {
        public String fullName;
        public String email;

        // Empty constructor needed for Firebase
        public User() {}

        public User(String fullName, String email) {
            this.fullName = fullName;
            this.email = email;
        }
    }
}
