package com.example.anew;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
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
    private ImageView selectImageIcon;
    private TextView noImageText;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add); // Link to your XML layout file

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("products");
        databaseReference = FirebaseDatabase.getInstance().getReference("images");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        selectImageIcon = findViewById(R.id.selectImageIcon);
        noImageText = findViewById(R.id.noImageText);
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


        // Initialize hydraulic Oil spinners and labels
        hydraulicOilNameSpinner = findViewById(R.id.hydraulicOilNameSpinner);
        hydraulicLitersSpinner = findViewById(R.id.hydraulicLitersSpinner);


        // Initialize Filter spinners and labels
        filterNameSpinner = findViewById(R.id.filterNameSpinner);
        filterQualitySpinner = findViewById(R.id.filterQualitySpinner);

        // Initialize Light spinners and labels
        lightCarNameLabel = findViewById(R.id.lightCarNameLabel);
        lightCarNameSpinner = findViewById(R.id.lightCarNameSpinner);
        lightSideLabel = findViewById(R.id.lightSideLabel);
        lightSideSpinner = findViewById(R.id.lightSideSpinner);
        lightTypeSpinner = findViewById(R.id.lightTypeSpinner); // Light Type Spinner

        lightTypeLabel = findViewById(R.id.lightTypeLabel); // Label for Light Type


        oilTypeLabel = findViewById(R.id.oilTypeLabel); // Label for Oil Type Spinner
        motorOilNameLabel = findViewById(R.id.motorOilNameLabel);
        litersLabel = findViewById(R.id.litersLabel);
        amountLabel = findViewById(R.id.amountLabel);

        // Labels for Brake Oil
        brakeOilNameLabel = findViewById(R.id.brakeOilNameLabel);
        brakeLitersLabel = findViewById(R.id.brakeLitersLabel);


        // Labels for hydraulic Oil
        hydraulicOilNameLabel = findViewById(R.id.hydraulicOilNameLabel);
        hydraulicLitersLabel = findViewById(R.id.hydraulicLitersLabel);


        // Labels for Filter
        filterNameLabel = findViewById(R.id.filterNameLabel);
        filterQualityLabel = findViewById(R.id.filterQualityLabel);


        // Initialize the bottom navigation buttons
        homeButton = findViewById(R.id.homeButton);
        addButton = findViewById(R.id.addButton);
        profileButton = findViewById(R.id.profileButton);

        // Initialize the launcher with a callback to handle the result
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        selectImageIcon.setImageURI(imageUri);
                        uploadImageToFirebase(imageUri);
                    }
                }
        );

        // Set the onClickListener for the selectImageIcon
        selectImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                galleryLauncher.launch(galleryIntent);  // Use the launcher to start the intent
            }
        });

        addButtontofirebase.setOnClickListener(v -> {
            if (imageUri != null) {
                addButtontofirebase(imageUri); // Pass the selected image URI
            } else {
                Toast.makeText(this, "Please select an image first.", Toast.LENGTH_SHORT).show();
            }
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

        // Handle image selection
        selectImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();  // Store the selected image URI
            selectImageIcon.setImageURI(imageUri);// Display the selected image
            Toast.makeText(this, "Image selected successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // Compress with quality 100
        return baos.toByteArray();
    }
    private Bitmap resizeImage(Uri imageUri, int width, int height) throws IOException {
        Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        return Bitmap.createScaledBitmap(originalBitmap, width, height, false);
    }

    // Method to upload the image to Firebase Storage
    private void uploadImageToFirebase(Uri imageUri) {
        if (imageUri != null) {
            try {
                // Resize the image to 300x300 pixels (adjust as needed)
                Bitmap resizedBitmap = resizeImage(imageUri, 200, 200);
                byte[] imageData = bitmapToByteArray(resizedBitmap);

                // Create a reference to Firebase Storage with a unique filename
                StorageReference fileRef = storageReference.child(System.currentTimeMillis() + ".jpg");

                // Upload the resized image as byte array
                fileRef.putBytes(imageData).addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL once the upload is complete
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        Log.d("AddActivity", "Image uploaded. URL: " + downloadUrl);
                        saveImageUrlToDatabase(downloadUrl);  // Save URL in Realtime Database
                        //Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    });
                }).addOnFailureListener(e -> {
                    Log.e("AddActivity", "Failed to upload image.", e);
                    Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error resizing image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }


    // Method to store the image URL in Realtime Database
    private void saveImageUrlToDatabase(String imageUrl) {
        // Create a unique ID in the 'products' node
        String productId = databaseReference.push().getKey();

        // Create a product object with the image URL and other details
        Product product = new Product();
        product.setImageUrl(imageUrl);

        // Save the product object in Realtime Database
        databaseReference.child(productId).setValue(product)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("AddActivity", "Image URL stored in Realtime Database.");
                        Toast.makeText(this, "Image URL saved to database.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("AddActivity", "Failed to store image URL in database.", task.getException());
                        Toast.makeText(this, "Failed to store image URL.", Toast.LENGTH_SHORT).show();
                    }
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
    }

    // Method to show Brake Oil options
    private void showBrakeOilOptions() {
        brakeOilNameLabel.setVisibility(View.VISIBLE);
        brakeOilNameSpinner.setVisibility(View.VISIBLE);
        brakeLitersLabel.setVisibility(View.VISIBLE);
        brakeLitersSpinner.setVisibility(View.VISIBLE);


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
    }

    // Method to show Hydraulic Oil options
    private void showHydraulicOptions() {
        hydraulicOilNameLabel.setVisibility(View.VISIBLE);
        hydraulicOilNameSpinner.setVisibility(View.VISIBLE);
        hydraulicLitersLabel.setVisibility(View.VISIBLE);
        hydraulicLitersSpinner.setVisibility(View.VISIBLE);


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
    }

    // Method to show Filter options
    private void showFilterOptions() {
        filterNameLabel.setVisibility(View.VISIBLE);
        filterNameSpinner.setVisibility(View.VISIBLE);
        filterQualityLabel.setVisibility(View.VISIBLE);
        filterQualitySpinner.setVisibility(View.VISIBLE);


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
    }

    // Method to show Light options
    private void showLightOptions() {
        lightCarNameLabel.setVisibility(View.VISIBLE);
        lightCarNameSpinner.setVisibility(View.VISIBLE);
        lightSideLabel.setVisibility(View.VISIBLE);
        lightSideSpinner.setVisibility(View.VISIBLE);
        lightTypeLabel.setVisibility(View.VISIBLE);
        lightTypeSpinner.setVisibility(View.VISIBLE);



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
    }

    public class Product {
        private String category;
        private String oilType;
        private String oilName;
        private String liters;
        private String amount;
        private String brakeOilName;
        private String brakeLiters;

        private String filterName;
        private String filterQuality;

        private String lightCarName;
        private String lightSide;
        private String lightType;
        private String imageUrl;


        // Empty constructor is needed for Firebase
        public Product() {
        }

        // Constructor with all parameters
        public Product(String category, String oilType, String oilName, String liters, String amount,
                       String brakeOilName, String brakeLiters,
                       String filterName, String filterQuality,
                       String lightCarName, String lightSide, String lightType, String imageUrl) {
            this.category = category;
            this.oilType = oilType;
            this.oilName = oilName;
            this.liters = liters;
            this.amount = amount;
            this.brakeOilName = brakeOilName;
            this.brakeLiters = brakeLiters;

            this.filterName = filterName;
            this.filterQuality = filterQuality;

            this.lightCarName = lightCarName;
            this.lightSide = lightSide;
            this.lightType = lightType;

            this.imageUrl = imageUrl;

        }

        // New constructor with category, oilType, and amount
        public Product(String category, String oilType, int amount) {
            this.category = category;
            this.oilType = oilType;
            this.amount = String.valueOf(amount);
        }

        // Getters and Setters for all fields
        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
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


    }


    // Method to add product details to Firebase
    private void addButtontofirebase(Uri imageUri) {
        if (imageUri != null) {
            // Upload image to Firebase Storage
            uploadImage(imageUri);
        } else {
            // If no image selected, save product details without image URL
            saveProductToDatabase(null);
        }
    }

    // Method to upload image to Firebase Storage
    private void uploadImage(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("product_images")
                .child(System.currentTimeMillis() + ".jpg");

        storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveProductToDatabase(imageUrl); // Save product details with the image URL
                }).addOnFailureListener(e -> {
                    Log.e("AddActivity", "Failed to get image URL.", e);
                    Toast.makeText(this, "Failed to get image URL.", Toast.LENGTH_SHORT).show();
                })
        ).addOnFailureListener(e -> {
            Log.e("AddActivity", "Image upload failed.", e);
            Toast.makeText(this, "Image upload failed.", Toast.LENGTH_SHORT).show();
        });
    }

    // Method to save product details in Firebase Realtime Database
    private void saveProductToDatabase(String imageUrl) {
        // Get selected category from mainCategorySpinner
        String selectedCategory = mainCategorySpinner.getSelectedItem() != null ?
                mainCategorySpinner.getSelectedItem().toString() : "";
        String oilType = oilTypeSpinner.getSelectedItem() != null ?
                oilTypeSpinner.getSelectedItem().toString() : "";

        // Initialize other necessary variables
        String productName = "", liters = "", amount = "", brakeOilName = "", brakeLiters = "";
        String filterName = "", filterQuality = "", lightCarName = "", lightSide = "", lightType = "";

        // Handle oil category
        if (selectedCategory.equals("ዘይት")) {
            if (oilType.equals("ሞተር ዘይት") && motorOilNameSpinner.getSelectedItem() != null) {
                productName = motorOilNameSpinner.getSelectedItem().toString();
                liters = litersSpinner.getSelectedItem() != null ? litersSpinner.getSelectedItem().toString() : "";
                amount = amountEditText.getText().toString();
            } else if (oilType.equals("ፌሬን ዘይት") && brakeOilNameSpinner.getSelectedItem() != null) {
                brakeOilName = brakeOilNameSpinner.getSelectedItem().toString();
                brakeLiters = litersSpinner.getSelectedItem() != null ? litersSpinner.getSelectedItem().toString() : "";
                amount = amountEditText.getText().toString();
            } else if (oilType.equals("ካቢሆን ዘይት") && hydraulicOilNameSpinner.getSelectedItem() != null) {
                productName = hydraulicOilNameSpinner.getSelectedItem().toString();
                liters = litersSpinner.getSelectedItem() != null ? litersSpinner.getSelectedItem().toString() : "";
                amount = amountEditText.getText().toString();
            }
        } else if (selectedCategory.equals("ፊልትሮ") && filterNameSpinner.getSelectedItem() != null) {
            filterName = filterNameSpinner.getSelectedItem().toString();
            filterQuality = filterQualitySpinner.getSelectedItem() != null ? filterQualitySpinner.getSelectedItem().toString() : "";
            amount = amountEditText.getText().toString();
        } else if (selectedCategory.equals("መብራት") && lightCarNameSpinner.getSelectedItem() != null
                && lightSideSpinner.getSelectedItem() != null && lightTypeSpinner.getSelectedItem() != null) {
            lightCarName = lightCarNameSpinner.getSelectedItem().toString();
            lightSide = lightSideSpinner.getSelectedItem().toString();
            lightType = lightTypeSpinner.getSelectedItem().toString();
            amount = amountEditText.getText().toString();
        }

        // Create a product object with the collected data
        Product product = new Product(selectedCategory, oilType, productName, liters, amount,
                brakeOilName, brakeLiters, filterName, filterQuality, lightCarName, lightSide, lightType, imageUrl);

        // Save the product data to Firebase Realtime Database
        myRef.push().setValue(product).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("AddActivity", "Product added successfully.");
                Toast.makeText(this, "Product added successfully.", Toast.LENGTH_SHORT).show();
                clearInputs(); // Clear inputs after saving
            } else {
                Log.e("AddActivity", "Failed to add product.", task.getException());
                Toast.makeText(this, "Failed to add product.", Toast.LENGTH_SHORT).show();
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
        selectImageIcon.setImageDrawable(null);

    }
}
