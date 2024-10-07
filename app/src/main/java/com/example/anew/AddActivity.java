package com.example.anew;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    private Spinner mainCategorySpinner, oilTypeSpinner, motorOilNameSpinner, litersSpinner;
    private Spinner brakeOilNameSpinner, brakeLitersSpinner; // Added Brake Oil spinners
    private Spinner hydraulicOilNameSpinner, hydraulicLitersSpinner;
    private Spinner filterNameSpinner, filterQualitySpinner; // Added Filter spinners
    private Spinner lightCarNameSpinner, lightSideSpinner, lightTypeSpinner; // Added Light Type spinner
    private EditText amountEditText, brakeAmountEditText, hydraulicAmountEditText, filterAmountEditText; // Added EditText for Brake Oil and Filter amount
    private TextView oilTypeLabel, motorOilNameLabel, litersLabel, amountLabel;
    private TextView brakeOilNameLabel, brakeLitersLabel, brakeAmountLabel; // Added Labels for Brake Oil
    private TextView hydraulicOilNameLabel, hydraulicLitersLabel, hydraulicAmountLabel;
    private TextView filterNameLabel, filterQualityLabel, filterAmountLabel; // Added Labels for Filter
    private TextView lightTypeLabel, lightAmountLabel, lightSideLabel, lightCarNameLabel; // Added Labels for Light
    private EditText lightAmountEditText; // Added EditText for Light amount
    private ImageButton homeButton, addButton, profileButton;
    private DatabaseReference myRef;
    private Button addButtontofirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add); // Link to your XML layout file

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("products");

        addButtontofirebase = findViewById(R.id.addButtontofirebase);
        // Initialize the spinners and text views for Oil options
        mainCategorySpinner = findViewById(R.id.mainCategorySpinner);
        oilTypeSpinner = findViewById(R.id.oilTypeSpinner); // New Oil Type Spinner
        motorOilNameSpinner = findViewById(R.id.motorOilNameSpinner);
        litersSpinner = findViewById(R.id.litersSpinner);
        amountEditText = findViewById(R.id.amountEditText);

        // Initialize Brake Oil spinners and labels
        brakeOilNameSpinner = findViewById(R.id.brakeOilNameSpinner);
        brakeLitersSpinner = findViewById(R.id.brakeLitersSpinner);
        brakeAmountEditText = findViewById(R.id.brakeAmountEditText);

        // Initialize hydraulic Oil spinners and labels
        hydraulicOilNameSpinner = findViewById(R.id.hydraulicOilNameSpinner);
        hydraulicLitersSpinner = findViewById(R.id.hydraulicLitersSpinner);
        hydraulicAmountEditText = findViewById(R.id.hydraulicAmountEditText);

        // Initialize Filter spinners and labels
        filterNameSpinner = findViewById(R.id.filterNameSpinner);
        filterQualitySpinner = findViewById(R.id.filterQualitySpinner);
        filterAmountEditText = findViewById(R.id.filterAmountEditText);

        // Initialize Light spinners and labels
        lightCarNameLabel = findViewById(R.id.lightCarNameLabel);
        lightCarNameSpinner = findViewById(R.id.lightCarNameSpinner);
        lightSideLabel = findViewById(R.id.lightSideLabel);
        lightSideSpinner = findViewById(R.id.lightSideSpinner);
        lightTypeSpinner = findViewById(R.id.lightTypeSpinner); // Light Type Spinner
        lightAmountEditText = findViewById(R.id.lightAmountEditText); // Light amount EditText
        lightTypeLabel = findViewById(R.id.lightTypeLabel); // Label for Light Type
        lightAmountLabel = findViewById(R.id.lightAmountLabel); // Label for Light amount

        oilTypeLabel = findViewById(R.id.oilTypeLabel); // Label for Oil Type Spinner
        motorOilNameLabel = findViewById(R.id.motorOilNameLabel);
        litersLabel = findViewById(R.id.litersLabel);
        amountLabel = findViewById(R.id.amountLabel);

        // Labels for Brake Oil
        brakeOilNameLabel = findViewById(R.id.brakeOilNameLabel);
        brakeLitersLabel = findViewById(R.id.brakeLitersLabel);
        brakeAmountLabel = findViewById(R.id.brakeAmountLabel);

        // Labels for hydraulic Oil
        hydraulicOilNameLabel = findViewById(R.id.hydraulicOilNameLabel);
        hydraulicLitersLabel = findViewById(R.id.hydraulicLitersLabel);
        hydraulicAmountLabel = findViewById(R.id.hydraulicAmountLabel);

        // Labels for Filter
        filterNameLabel = findViewById(R.id.filterNameLabel);
        filterQualityLabel = findViewById(R.id.filterQualityLabel);
        filterAmountLabel = findViewById(R.id.filterAmountLabel);

        // Initialize the bottom navigation buttons
        homeButton = findViewById(R.id.homeButton);
        addButton = findViewById(R.id.addButton);
        profileButton = findViewById(R.id.profileButton);

        addButtontofirebase.setOnClickListener(v -> {
            addButtontofirebase();
        });
        // Set up the main category spinner
        String[] mainCategories = {"ዘይት", "ፊልትሮ", "መብራት", "ሌሎች"};
        ArrayAdapter<String> mainCategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mainCategories);
        mainCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainCategorySpinner.setAdapter(mainCategoryAdapter);

        // Set listener for the main category spinner
        mainCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = mainCategories[position];

                if (selectedCategory.equals("ዘይት")) {
                    showOilTypeOptions(); // Show Oil Type options
                    hideFilterOptions(); // Hide Filter options
                    hideLightOptions(); // Hide Light options
                } else if (selectedCategory.equals("ፊልትሮ")) {
                    hideOilTypeOptions(); // Hide Oil Type options
                    showFilterOptions(); // Show Filter options
                    hideLightOptions(); // Hide Light options
                } else if (selectedCategory.equals("መብራት")) {
                    hideOilTypeOptions(); // Hide Oil Type options
                    hideFilterOptions(); // Hide Filter options
                    showLightOptions(); // Show Light options
                } else {
                    hideOilTypeOptions(); // Hide Oil Type options
                    hideFilterOptions(); // Hide Filter options
                    hideLightOptions(); // Hide Light options
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set listener for the Oil Type spinner
        oilTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOilType = parent.getItemAtPosition(position).toString();

                if (selectedOilType.equals("ሞተር ዘይት")) {
                    showMotorOilOptions(); // Show Motor Oil related fields
                    hideBrakeOilOptions(); // Hide Brake Oil fields
                    hideHydraulicOptions(); // Hide Brake Oil fields
                } else if (selectedOilType.equals("ፌሬን ዘይት")) {
                    hideMotorOilOptions(); // Hide Motor Oil fields
                    showBrakeOilOptions(); // Show Brake Oil related fields
                    hideHydraulicOptions(); // Hide Brake Oil fields
                } else if (selectedOilType.equals("ካቢሆን ዘይት")) {
                    hideMotorOilOptions(); // Hide Motor Oil fields
                    hideBrakeOilOptions(); // Hide Brake Oil fields
                    showHydraulicOptions(); // Show Brake Oil related fields
                } else {
                    hideMotorOilOptions(); // Hide
                    hideBrakeOilOptions(); // Hide all options if another type is selected
                    showHydraulicOptions();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set listeners for bottom navigation buttons
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddActivity.this, AddActivity.class);
            startActivity(intent);
        });

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    // Method to show Oil Type options
    private void showOilTypeOptions() {
        oilTypeLabel.setVisibility(View.VISIBLE);
        oilTypeSpinner.setVisibility(View.VISIBLE);

        // Populate Oil Type spinner with options like Motor Oil, Brake Oil
        String[] oilTypes = {"ሞተር ዘይት", "ፌሬን ዘይት", "ካቢሆን ዘይት"};
        ArrayAdapter<String> oilTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, oilTypes);
        oilTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        oilTypeSpinner.setAdapter(oilTypeAdapter);
    }

    // Method to show Motor Oil options and populate the spinners
    private void showMotorOilOptions() {
        motorOilNameLabel.setVisibility(View.VISIBLE);
        motorOilNameSpinner.setVisibility(View.VISIBLE);

        litersLabel.setVisibility(View.VISIBLE);
        litersSpinner.setVisibility(View.VISIBLE);
        amountLabel.setVisibility(View.VISIBLE);
        amountEditText.setVisibility(View.VISIBLE);

        // Populate Motor Oil Name spinner
        String[] motorOilNames = {"ቶታል Quritz", "ቶታል Tir", "ዴሎዋ", "ሀቫሊን"};
        ArrayAdapter<String> motorOilNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, motorOilNames);
        motorOilNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        motorOilNameSpinner.setAdapter(motorOilNameAdapter);

        // Populate Liters spinner
        String[] litersOptions = {"1L", "4L", "5L"};
        ArrayAdapter<String> litersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, litersOptions);
        litersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        litersSpinner.setAdapter(litersAdapter);
    }

    // Method to hide Oil Type options
    private void hideOilTypeOptions() {
        oilTypeLabel.setVisibility(View.GONE);
        oilTypeSpinner.setVisibility(View.GONE);
        hideMotorOilOptions(); // Hide Motor Oil fields as well
        hideBrakeOilOptions(); // Also hide Brake Oil fields
        hideHydraulicOptions(); // Hide Brake Oil fields
    }

    // Method to hide Motor Oil options
    private void hideMotorOilOptions() {
        motorOilNameLabel.setVisibility(View.GONE);
        motorOilNameSpinner.setVisibility(View.GONE);

        litersLabel.setVisibility(View.GONE);
        litersSpinner.setVisibility(View.GONE);
        amountLabel.setVisibility(View.GONE);
        amountEditText.setVisibility(View.GONE);
    }

    // Method to show Brake Oil options
    private void showBrakeOilOptions() {
        brakeOilNameLabel.setVisibility(View.VISIBLE);
        brakeOilNameSpinner.setVisibility(View.VISIBLE);
        brakeLitersLabel.setVisibility(View.VISIBLE);
        brakeLitersSpinner.setVisibility(View.VISIBLE);
        brakeAmountLabel.setVisibility(View.VISIBLE);
        brakeAmountEditText.setVisibility(View.VISIBLE);

        // Populate Brake Oil Name spinner
        String[] brakeOilNames = {"ቶታል", "ኤላ", "ቤስት"};
        ArrayAdapter<String> brakeOilNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, brakeOilNames);
        brakeOilNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brakeOilNameSpinner.setAdapter(brakeOilNameAdapter);

        // Populate Brake Liters spinner
        String[] brakeLitersOptions = {"0.25L", "0.5L", "1L"};
        ArrayAdapter<String> brakeLitersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, brakeLitersOptions);
        brakeLitersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brakeLitersSpinner.setAdapter(brakeLitersAdapter);
    }

    // Method to hide Brake Oil options
    private void hideBrakeOilOptions() {
        brakeOilNameLabel.setVisibility(View.GONE);
        brakeOilNameSpinner.setVisibility(View.GONE);
        brakeLitersLabel.setVisibility(View.GONE);
        brakeLitersSpinner.setVisibility(View.GONE);
        brakeAmountLabel.setVisibility(View.GONE);
        brakeAmountEditText.setVisibility(View.GONE);
    }

    // Method to show Hydraulic Oil options
    private void showHydraulicOptions() {
        hydraulicOilNameLabel.setVisibility(View.VISIBLE);
        hydraulicOilNameSpinner.setVisibility(View.VISIBLE);
        hydraulicLitersLabel.setVisibility(View.VISIBLE);
        hydraulicLitersSpinner.setVisibility(View.VISIBLE);
        hydraulicAmountLabel.setVisibility(View.VISIBLE);
        hydraulicAmountEditText.setVisibility(View.VISIBLE);

        // Populate Hydraulic Oil Name spinner
        String[] hydraulicOilNames = {"ቶታል", "ሀቫሊን"};
        ArrayAdapter<String> hydraulicOilNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hydraulicOilNames);
        hydraulicOilNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hydraulicOilNameSpinner.setAdapter(hydraulicOilNameAdapter);

        // Populate Hydraulic Liters spinner
        String[] hydraulicLitersOptions = {"0.5L", "1L", "3L"};
        ArrayAdapter<String> hydraulicLitersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hydraulicLitersOptions);
        hydraulicLitersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hydraulicLitersSpinner.setAdapter(hydraulicLitersAdapter);
    }

    // Method to hide Hydraulic Oil options
    private void hideHydraulicOptions() {
        hydraulicOilNameLabel.setVisibility(View.GONE);
        hydraulicOilNameSpinner.setVisibility(View.GONE);
        hydraulicLitersLabel.setVisibility(View.GONE);
        hydraulicLitersSpinner.setVisibility(View.GONE);
        hydraulicAmountLabel.setVisibility(View.GONE);
        hydraulicAmountEditText.setVisibility(View.GONE);
    }

    // Method to show Filter options
    private void showFilterOptions() {
        filterNameLabel.setVisibility(View.VISIBLE);
        filterNameSpinner.setVisibility(View.VISIBLE);
        filterQualityLabel.setVisibility(View.VISIBLE);
        filterQualitySpinner.setVisibility(View.VISIBLE);
        filterAmountLabel.setVisibility(View.VISIBLE);
        filterAmountEditText.setVisibility(View.VISIBLE);

        // Populate Filter Name spinner
        String[] filterNames = {"30002", "E1", "D2", "41010"};
        ArrayAdapter<String> filterNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterNames);
        filterNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterNameSpinner.setAdapter(filterNameAdapter);

        // Populate Filter Quality spinner
        String[] filterQualityOptions = {"SDK", "የሀገር ውስጥ", "ዴንሶ"};
        ArrayAdapter<String> filterQualityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterQualityOptions);
        filterQualityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterQualitySpinner.setAdapter(filterQualityAdapter);
    }

    // Method to hide Filter options
    private void hideFilterOptions() {
        filterNameLabel.setVisibility(View.GONE);
        filterNameSpinner.setVisibility(View.GONE);
        filterQualityLabel.setVisibility(View.GONE);
        filterQualitySpinner.setVisibility(View.GONE);
        filterAmountLabel.setVisibility(View.GONE);
        filterAmountEditText.setVisibility(View.GONE);
    }

    // Method to show Light options
    private void showLightOptions() {
        lightCarNameLabel.setVisibility(View.VISIBLE);
        lightCarNameSpinner.setVisibility(View.VISIBLE);
        lightSideLabel.setVisibility(View.VISIBLE);
        lightSideSpinner.setVisibility(View.VISIBLE);
        lightTypeLabel.setVisibility(View.VISIBLE);
        lightTypeSpinner.setVisibility(View.VISIBLE);
        lightAmountLabel.setVisibility(View.VISIBLE);
        lightAmountEditText.setVisibility(View.VISIBLE);


        // Populate Light Car Name spinner
        String[] lightCarNames = {"5l", "3L", "Vitz", "Yaris", "ለዳ", "IC", "Pickup"};
        ArrayAdapter<String> lightCarNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lightCarNames);
        lightCarNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lightCarNameSpinner.setAdapter(lightCarNameAdapter);
        // Populate Light Car Name spinner
        String[] lightSides = {"ግራ", "ቀኝ"};
        ArrayAdapter<String> lightSideAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lightSides);
        lightSideAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lightSideSpinner.setAdapter(lightSideAdapter);
        // Populate Light Type spinner
        String[] lightTypes = {"የፊት", "የኋላ", "የጐን"};
        ArrayAdapter<String> lightTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lightTypes);
        lightTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lightTypeSpinner.setAdapter(lightTypeAdapter);
    }

    // Method to hide Light options
    private void hideLightOptions() {
        lightCarNameLabel.setVisibility(View.GONE);
        lightCarNameSpinner.setVisibility(View.GONE);
        lightSideLabel.setVisibility(View.GONE);
        lightSideSpinner.setVisibility(View.GONE);
        lightTypeLabel.setVisibility(View.GONE);
        lightTypeSpinner.setVisibility(View.GONE);
        lightAmountLabel.setVisibility(View.GONE);
        lightAmountEditText.setVisibility(View.GONE);
    }

    public class Product {
        private String category;
        private String oilType;
        private String oilName;
        private String liters;
        private String amount;
        private String brakeOilName;
        private String brakeLiters;
        private String brakeAmount;
        private String filterName;
        private String filterQuality;
        private String filterAmount;
        private String lightCarName;
        private String lightSide;
        private String lightType;
        private String lightAmount;

        // Empty constructor is needed for Firebase
        public Product() {
        }

        // Constructor with all parameters
        public Product(String category, String oilType, String oilName, String liters, String amount,
                       String brakeOilName, String brakeLiters, String brakeAmount,
                       String filterName, String filterQuality, String filterAmount,
                       String lightCarName, String lightSide, String lightType, String lightAmount) {
            this.category = category;
            this.oilType = oilType;
            this.oilName = oilName;
            this.liters = liters;
            this.amount = amount;
            this.brakeOilName = brakeOilName;
            this.brakeLiters = brakeLiters;
            this.brakeAmount = brakeAmount;
            this.filterName = filterName;
            this.filterQuality = filterQuality;
            this.filterAmount = filterAmount;
            this.lightCarName = lightCarName;
            this.lightSide = lightSide;
            this.lightType = lightType;
            this.lightAmount = lightAmount;
        }

        // New constructor with category, oilType, and amount
        public Product(String category, String oilType, int amount) {
            this.category = category;
            this.oilType = oilType;
            this.amount = String.valueOf(amount);
        }

        // Getters and Setters for all fields
        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getOilType() {
            return oilType;
        }

        public void setOilType(String oilType) {
            this.oilType = oilType;
        }

        public String getOilName() {
            return oilName;
        }

        public void setOilName(String oilName) {
            this.oilName = oilName;
        }

        public String getLiters() {
            return liters;
        }

        public void setLiters(String liters) {
            this.liters = liters;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBrakeOilName() {
            return brakeOilName;
        }

        public void setBrakeOilName(String brakeOilName) {
            this.brakeOilName = brakeOilName;
        }

        public String getBrakeLiters() {
            return brakeLiters;
        }

        public void setBrakeLiters(String brakeLiters) {
            this.brakeLiters = brakeLiters;
        }

        public String getBrakeAmount() {
            return brakeAmount;
        }

        public void setBrakeAmount(String brakeAmount) {
            this.brakeAmount = brakeAmount;
        }

        public String getFilterName() {
            return filterName;
        }

        public void setFilterName(String filterName) {
            this.filterName = filterName;
        }

        public String getFilterQuality() {
            return filterQuality;
        }

        public void setFilterQuality(String filterQuality) {
            this.filterQuality = filterQuality;
        }

        public String getFilterAmount() {
            return filterAmount;
        }

        public void setFilterAmount(String filterAmount) {
            this.filterAmount = filterAmount;
        }

        public String getLightCarName() {
            return lightCarName;
        }

        public void setLightCarName(String lightCarName) {
            this.lightCarName = lightCarName;
        }

        public String getLightSide() {
            return lightSide;
        }

        public void setLightSide(String lightSide) {
            this.lightSide = lightSide;
        }

        public String getLightType() {
            return lightType;
        }

        public void setLightType(String lightType) {
            this.lightType = lightType;
        }

        public String getLightAmount() {
            return lightAmount;
        }

        public void setLightAmount(String lightAmount) {
            this.lightAmount = lightAmount;
        }
    }


    // Method to add product details to Firebase
    private void addButtontofirebase() {
        // Get selected category from mainCategorySpinner
        String selectedCategory = mainCategorySpinner.getSelectedItem() != null ? mainCategorySpinner.getSelectedItem().toString() : "";
        String oilType = oilTypeSpinner.getSelectedItem() != null ? oilTypeSpinner.getSelectedItem().toString() : "";

        // Initialize other necessary variables
        String productName = ""; // Based on your selection
        String liters = ""; // Quantity of liters from spinner
        String amount = ""; // Amount in string format for Firebase
        String brakeOilName = ""; // Name for brake oil if applicable
        String brakeLiters = ""; // Quantity for brake oil
        String brakeAmount = ""; // Amount for brake oil
        String filterName = ""; // Filter name if applicable
        String filterQuality = ""; // Quality of the filter
        String filterAmount = ""; // Amount for filter
        String lightCarName = ""; // Car name for lights
        String lightSide = ""; // Side for lights
        String lightType = ""; // Type of lights
        String lightAmount = ""; // Quantity for lights

        // Handle oil category
        if (selectedCategory.equals("ዘይት")) {
            if (oilType.equals("ሞተር ዘይት") && motorOilNameSpinner.getSelectedItem() != null) {
                productName = motorOilNameSpinner.getSelectedItem().toString();
                liters = litersSpinner.getSelectedItem() != null ? litersSpinner.getSelectedItem().toString() : ""; // Use liters from spinner
                amount = amountEditText.getText().toString(); // Use amount from EditText
            } else if (oilType.equals("ፌሬን ዘይት") && brakeOilNameSpinner.getSelectedItem() != null) {
                brakeOilName = brakeOilNameSpinner.getSelectedItem().toString();
                brakeLiters = litersSpinner.getSelectedItem() != null ? litersSpinner.getSelectedItem().toString() : ""; // Use liters from spinner
                brakeAmount = brakeAmountEditText.getText().toString(); // Use amount from EditText
            } else if (oilType.equals("ካቢሆን ዘይት") && hydraulicOilNameSpinner.getSelectedItem() != null) {
                productName = hydraulicOilNameSpinner.getSelectedItem().toString();
                liters = litersSpinner.getSelectedItem() != null ? litersSpinner.getSelectedItem().toString() : ""; // Use liters from spinner
                amount = hydraulicAmountEditText.getText().toString(); // Use amount from EditText
            }
        }
        // Handle filter category
        else if (selectedCategory.equals("ፊልትሮ") && filterNameSpinner.getSelectedItem() != null) {
            filterName = filterNameSpinner.getSelectedItem().toString();
            filterQuality = filterQualitySpinner.getSelectedItem() != null ? filterQualitySpinner.getSelectedItem().toString() : ""; // Get filter quality
            filterAmount = filterAmountEditText.getText().toString(); // Use amount from EditText
        }
        // Handle light category
        else if (selectedCategory.equals("መብራት")
                && lightCarNameSpinner.getSelectedItem() != null
                && lightSideSpinner.getSelectedItem() != null
                && lightTypeSpinner.getSelectedItem() != null) {
            lightCarName = lightCarNameSpinner.getSelectedItem().toString();
            lightSide = lightSideSpinner.getSelectedItem().toString();
            lightType = lightTypeSpinner.getSelectedItem().toString();
            lightAmount = lightAmountEditText.getText().toString(); // Use amount from EditText
        }

        // Prepare the data to store in Firebase
        Product product = new Product(selectedCategory, oilType, productName, liters, amount,
                brakeOilName, brakeLiters, brakeAmount,
                filterName, filterQuality, filterAmount,
                lightCarName, lightSide, lightType, lightAmount);

        // Push product to Firebase
        myRef.push().setValue(product).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("AddActivity", "Product added successfully.");
                // Show toast message after successful insertion
                Toast.makeText(getApplicationContext(), "Product added successfully.", Toast.LENGTH_SHORT).show();
                clearInputs(); // Call the method to clear inputs
            } else {
                Log.e("AddActivity", "Failed to add product.", task.getException());
                // Show toast message for failure
                Toast.makeText(getApplicationContext(), "Failed to add product.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to clear all input fields and spinners
    private void clearInputs() {
        mainCategorySpinner.setSelection(0); // Reset main category spinner
        oilTypeSpinner.setSelection(0); // Reset oil type spinner
        motorOilNameSpinner.setSelection(0); // Reset motor oil name spinner
        brakeOilNameSpinner.setSelection(0); // Reset brake oil name spinner
        hydraulicOilNameSpinner.setSelection(0); // Reset hydraulic oil name spinner
        filterNameSpinner.setSelection(0); // Reset filter name spinner
        filterQualitySpinner.setSelection(0); // Reset filter quality spinner
        lightCarNameSpinner.setSelection(0); // Reset light car name spinner
        lightSideSpinner.setSelection(0); // Reset light side spinner
        lightTypeSpinner.setSelection(0); // Reset light type spinner

        // Clear EditTexts
        amountEditText.setText(""); // Clear amount EditText for oil
        brakeAmountEditText.setText(""); // Clear brake amount EditText
        hydraulicAmountEditText.setText(""); // Clear hydraulic amount EditText
        filterAmountEditText.setText(""); // Clear filter amount EditText
        lightAmountEditText.setText(""); // Clear light amount EditText
    }
}
