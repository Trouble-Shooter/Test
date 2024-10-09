package com.example.anew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    // Declare the ImageButtons
    private ImageButton homeButton, addButton, profileButton, light;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // This links to your XML file

        // Initialize the buttons
        homeButton = findViewById(R.id.homeButton);
        addButton = findViewById(R.id.addButton);
        profileButton = findViewById(R.id.profileButton);
        light = findViewById(R.id.light);

        // Set click listeners for the buttons
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle home button click (Optional: if you want it to do something)
            }
        });
        //add button
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddActivity.class);
            startActivity(intent);
        });
        //profile button
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        //light imagebutton
        light.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LightActivity.class);
            startActivity(intent);
        });
    }
}
