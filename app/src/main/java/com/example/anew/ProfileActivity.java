package com.example.anew;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView accountNameText, accountEmailText;
    private Button logoutButton, deleteAccountButton;
    private ImageButton homeButton, addButton, profileButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Initialize UI elements
        accountNameText = findViewById(R.id.accountNameText);
        accountEmailText = findViewById(R.id.accountEmailText);
        logoutButton = findViewById(R.id.logoutButton);
        deleteAccountButton = findViewById(R.id.deleteAccountButton);
        homeButton = findViewById(R.id.homeButton);
        addButton = findViewById(R.id.addButton);
        profileButton = findViewById(R.id.profileButton);

        if (currentUser != null) {
            // Get reference to the user's data in Firebase Realtime Database
            String userId = currentUser.getUid();
            userDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

            // Fetch user data from the database
            userDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String fullName = dataSnapshot.child("fullName").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);

                        // Set the fetched data in TextViews
                        accountEmailText.setText(fullName != null ? fullName : "No Email");
                        accountNameText.setText(email != null ? email : "No Name");
                    } else {
                        Toast.makeText(ProfileActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ProfileActivity.this, "Error fetching user data.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Logout button functionality
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
            startActivity(intent);
            finish(); // Close current activity
        });

        // Delete account button functionality
        deleteAccountButton.setOnClickListener(v -> {
            if (currentUser != null) {
                // Delete user from Firebase Authentication
                currentUser.delete().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Remove user data from Firebase Realtime Database
                        userDatabaseReference.removeValue().addOnCompleteListener(removeTask -> {
                            if (removeTask.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                // Redirect to Sign In activity
                                Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
                                startActivity(intent);
                                finish(); // Close current activity
                            } else {
                                Toast.makeText(ProfileActivity.this, "Failed to delete user data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to delete account: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Bottom navigation button listeners
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AddActivity.class);
            startActivity(intent);
        });

        profileButton.setOnClickListener(v -> {
            // You are already in the profile activity, so this can be left empty or give a toast
        });
    }
}
