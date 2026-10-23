package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecipesActivity extends Activity {

    private DatabaseHelper databaseHelper;
    private LinearLayout recipesContainer;

    private final int darkGreen = Color.rgb(36, 92, 67);
    private final int lightMint = Color.rgb(221, 243, 228);
    private final int darkText = Color.rgb(24, 61, 43);
    private final int cream = Color.rgb(250, 249, 246);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        databaseHelper = new DatabaseHelper(this);
        recipesContainer = findViewById(R.id.recipesContainer);

        // Back to Home button
        Button btnBackToHome = findViewById(R.id.btnBackToHome);

        btnBackToHome.setOnClickListener(v -> finish());

        // Seed recipes and update fried rice requirements
        RecipeSeeder recipeSeeder = new RecipeSeeder(databaseHelper);
        recipeSeeder.seedRecipes();
        databaseHelper.updateFriedRiceRequirements();

        // Display recipes that match the pantry
        displayMatchingRecipes();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null && recipesContainer != null) {
            displayMatchingRecipes();
        }
    }

    // Find recipes that can be prepared with pantry ingredients
    private void displayMatchingRecipes() {

        recipesContainer.removeAllViews();

        try (Cursor cursor = databaseHelper.getAllRecipes()) {

            if (cursor == null) {
                showMessage(
                        "We couldn't load the recipes. Please try again."
                );
                return;
            }

            int matchingCount = 0;

            int idColumn = cursor.getColumnIndexOrThrow("id");
            int nameColumn = cursor.getColumnIndexOrThrow("name");
            int descriptionColumn =
                    cursor.getColumnIndexOrThrow("description");
            int prepTimeColumn =
                    cursor.getColumnIndexOrThrow("prep_time");

            while (cursor.moveToNext()) {

                int recipeId = cursor.getInt(idColumn);

                String name = cursor.getString(nameColumn);

                String description =
                        cursor.getString(descriptionColumn);

                int prepTime = cursor.getInt(prepTimeColumn);

                if (databaseHelper.canMakeRecipe(recipeId)) {

                    addRecipeCard(
                            recipeId,
                            name,
                            description,
                            prepTime
                    );

                    matchingCount++;
                }
            }

            if (matchingCount == 0) {
                showMessage(
                        "No matching recipes yet.\n\n"
                                + "Add ingredients and enough quantities "
                                + "to your pantry. We'll show recipes "
                                + "when you have everything required."
                );
            }
        }
    }

    // Create a recipe card
    private void addRecipeCard(
            int recipeId,
            String name,
            String description,
            int prepTime
    ) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(20, 18, 20, 18);

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.WHITE);
        background.setCornerRadius(24);
        background.setStroke(2, lightMint);

        card.setBackground(background);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(0, 0, 0, 16);
        card.setLayoutParams(cardParams);

        // Recipe name
        TextView nameView = new TextView(this);
        nameView.setText(name);
        nameView.setTextColor(darkText);
        nameView.setTextSize(19);
        nameView.setTypeface(null, Typeface.BOLD);

        // Recipe description
        TextView descriptionView = new TextView(this);
        descriptionView.setText(
                description == null ? "" : description
        );
        descriptionView.setTextColor(Color.DKGRAY);
        descriptionView.setTextSize(14);

        // Preparation time
        TextView timeView = new TextView(this);
        timeView.setText(
                "Preparation: " + prepTime + " minutes"
        );
        timeView.setTextColor(darkGreen);
        timeView.setTextSize(14);
        timeView.setTypeface(null, Typeface.BOLD);
        timeView.setPadding(0, 10, 0, 8);

        // View Recipe button
        Button detailsButton = new Button(this);
        detailsButton.setText("View Recipe");
        detailsButton.setTextColor(Color.WHITE);

        detailsButton.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(darkGreen)
        );

        // Open recipe details
        detailsButton.setOnClickListener(v -> {

            Intent intent = new Intent(
                    RecipesActivity.this,
                    RecipeDetailActivity.class
            );

            intent.putExtra("recipe_id", recipeId);

            startActivity(intent);
        });

        // Make the entire card content visible
        card.addView(nameView);
        card.addView(descriptionView);
        card.addView(timeView);
        card.addView(detailsButton);

        recipesContainer.addView(card);
    }

    // Display an informational message
    private void showMessage(String message) {

        TextView messageView = new TextView(this);
        messageView.setText(message);
        messageView.setTextColor(darkGreen);
        messageView.setTextSize(16);
        messageView.setPadding(20, 24, 20, 24);

        GradientDrawable background = new GradientDrawable();
        background.setColor(cream);
        background.setCornerRadius(24);
        background.setStroke(2, lightMint);

        messageView.setBackground(background);

        recipesContainer.addView(messageView);
    }
}

