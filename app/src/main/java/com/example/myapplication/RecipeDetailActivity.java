package com.example.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecipeDetailActivity extends Activity {

    private DatabaseHelper databaseHelper;
    private LinearLayout ingredientsContainer;

    private final int darkGreen = Color.rgb(36, 92, 67);
    private final int lightMint = Color.rgb(221, 243, 228);
    private final int darkText = Color.rgb(24, 61, 43);
    private final int cream = Color.rgb(250, 249, 246);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new DatabaseHelper(this);

        int recipeId = getIntent().getIntExtra("recipe_id", -1);

        if (recipeId == -1) {
            Toast.makeText(this, "Recipe not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        createScreen();

        displayRecipe(recipeId);

        Button btnBack = findViewById(1001);
        btnBack.setOnClickListener(v -> finish());
    }

    private void createScreen() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(20, 20, 20, 20);
        root.setBackgroundColor(cream);

        android.widget.ScrollView scrollView =
                new android.widget.ScrollView(this);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);

        TextView heading = new TextView(this);
        heading.setText("RECIPE DETAILS");
        heading.setTextColor(darkGreen);
        heading.setTextSize(25);
        heading.setTypeface(null, Typeface.BOLD);
        heading.setPadding(0, 0, 0, 16);

        content.addView(heading);

        TextView recipeName = new TextView(this);
        recipeName.setId(1002);
        recipeName.setTextColor(darkText);
        recipeName.setTextSize(24);
        recipeName.setTypeface(null, Typeface.BOLD);
        recipeName.setPadding(0, 8, 0, 8);

        content.addView(recipeName);

        TextView description = new TextView(this);
        description.setId(1003);
        description.setTextColor(Color.DKGRAY);
        description.setTextSize(16);
        description.setPadding(0, 0, 0, 12);

        content.addView(description);

        TextView prepTime = new TextView(this);
        prepTime.setId(1004);
        prepTime.setTextColor(darkGreen);
        prepTime.setTextSize(15);
        prepTime.setTypeface(null, Typeface.BOLD);
        prepTime.setPadding(0, 0, 0, 20);

        content.addView(prepTime);

        TextView ingredientsHeading = new TextView(this);
        ingredientsHeading.setText("Ingredients required");
        ingredientsHeading.setTextColor(darkText);
        ingredientsHeading.setTextSize(20);
        ingredientsHeading.setTypeface(null, Typeface.BOLD);
        ingredientsHeading.setPadding(0, 8, 0, 10);

        content.addView(ingredientsHeading);

        ingredientsContainer = new LinearLayout(this);
        ingredientsContainer.setOrientation(LinearLayout.VERTICAL);

        GradientDrawable ingredientsBackground = new GradientDrawable();
        ingredientsBackground.setColor(Color.WHITE);
        ingredientsBackground.setCornerRadius(20);
        ingredientsBackground.setStroke(2, lightMint);
        ingredientsContainer.setBackground(ingredientsBackground);
        ingredientsContainer.setPadding(16, 12, 16, 12);

        content.addView(ingredientsContainer);

        TextView instructionsHeading = new TextView(this);
        instructionsHeading.setText("Cooking instructions");
        instructionsHeading.setTextColor(darkText);
        instructionsHeading.setTextSize(20);
        instructionsHeading.setTypeface(null, Typeface.BOLD);
        instructionsHeading.setPadding(0, 20, 0, 10);

        content.addView(instructionsHeading);

        TextView instructions = new TextView(this);
        instructions.setId(1005);
        instructions.setTextColor(Color.DKGRAY);
        instructions.setTextSize(16);
        instructions.setPadding(16, 16, 16, 16);

        GradientDrawable instructionsBackground = new GradientDrawable();
        instructionsBackground.setColor(Color.WHITE);
        instructionsBackground.setCornerRadius(20);
        instructionsBackground.setStroke(2, lightMint);
        instructions.setBackground(instructionsBackground);

        content.addView(instructions);

        scrollView.addView(content);

        root.addView(scrollView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        Button btnBack = new Button(this);
        btnBack.setId(1001);
        btnBack.setText("Back to Recipes");
        btnBack.setTextColor(Color.WHITE);
        btnBack.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(darkGreen));

        root.addView(btnBack, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        setContentView(root);
    }

    private void displayRecipe(int recipeId) {

        Cursor cursor = databaseHelper.getReadableDatabase().query(
                DatabaseHelper.TABLE_RECIPES,
                null,
                "id = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                null
        );

        try {
            if (!cursor.moveToFirst()) {
                Toast.makeText(this, "Recipe not found.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow("name"));

            String description = cursor.getString(
                    cursor.getColumnIndexOrThrow("description"));

            int prepTime = cursor.getInt(
                    cursor.getColumnIndexOrThrow("prep_time"));

            String instructions = cursor.getString(
                    cursor.getColumnIndexOrThrow("instructions"));

            ((TextView) findViewById(1002)).setText(name);

            ((TextView) findViewById(1003)).setText(
                    description == null ? "" : description);

            ((TextView) findViewById(1004)).setText(
                    "Preparation time: " + prepTime + " minutes");

            ((TextView) findViewById(1005)).setText(instructions);

        } finally {
            cursor.close();
        }

        displayIngredients(recipeId);
    }

    private void displayIngredients(int recipeId) {

        Cursor cursor = databaseHelper.getRecipeIngredients(recipeId);

        try {
            while (cursor.moveToNext()) {

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("ingredient_name"));

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("required_quantity"));

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow("unit"));

                TextView ingredient = new TextView(this);

                ingredient.setText(
                        "\u2022 " + name + " — " + quantity + " " + unit);

                ingredient.setTextColor(darkText);
                ingredient.setTextSize(16);
                ingredient.setPadding(0, 8, 0, 8);

                ingredientsContainer.addView(ingredient);
            }
        } finally {
            cursor.close();
        }
    }
}

