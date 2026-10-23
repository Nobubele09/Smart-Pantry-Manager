package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnManagePantry = findViewById(R.id.btnManagePantry);
        Button btnFindRecipes = findViewById(R.id.btnFindRecipes);
        Button btnSettings = findViewById(R.id.btnSettings);

        btnManagePantry.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PantryActivity.class);
            startActivity(intent);
        });

        btnFindRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeActivity.this,
                    RecipesActivity.class
            );
            startActivity(intent);
        });


        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        });


    }
}

