package com.example.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.view.View;

public class SettingsActivity extends Activity {

    private final int darkGreen = Color.rgb(36, 92, 67);
    private final int lightMint = Color.rgb(221, 243, 228);
    private final int darkText = Color.rgb(24, 61, 43);
    private final int cream = Color.rgb(250, 249, 246);

    private SharedPreferences preferences;
    private EditText nameInput;
    private CheckBox showExpiredCheckBox;
    private CheckBox matchingRecipesCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(
                "SmartPantrySettings", MODE_PRIVATE
        );

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(cream);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(24, 24, 24, 24);
        mainLayout.setBackgroundColor(cream);

        scrollView.addView(mainLayout);

        // Heading
        TextView heading = new TextView(this);
        heading.setText("SETTINGS");
        heading.setTextColor(darkGreen);
        heading.setTextSize(14);
        heading.setTypeface(null, Typeface.BOLD);
        mainLayout.addView(heading);

        TextView title = new TextView(this);
        title.setText("App preferences");
        title.setTextColor(darkText);
        title.setTextSize(28);
        title.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        titleParams.setMargins(0, 8, 0, 8);
        mainLayout.addView(title, titleParams);

        TextView subtitle = new TextView(this);
        subtitle.setText(
                "Personalise your Smart Pantry Manager experience."
        );
        subtitle.setTextColor(Color.DKGRAY);
        subtitle.setTextSize(15);
        mainLayout.addView(subtitle);

        // Profile section
        addSectionTitle(mainLayout, "Your Profile");

        nameInput = new EditText(this);
        nameInput.setSingleLine(true);
        nameInput.setHint("Enter your name");
        nameInput.setText(
                preferences.getString("user_name", "")
        );
        nameInput.setTextColor(darkText);
        nameInput.setBackgroundTintList(
                ColorStateList.valueOf(darkGreen)
        );
        mainLayout.addView(nameInput);

        Button saveNameButton = createButton("Save Profile");
        mainLayout.addView(saveNameButton);

        saveNameButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();

            if (name.isEmpty()) {
                nameInput.setError("Please enter your name");
                return;
            }

            preferences.edit()
                    .putString("user_name", name)
                    .apply();

            android.widget.Toast.makeText(
                    this, "Profile saved successfully!",
                    android.widget.Toast.LENGTH_SHORT
            ).show();
        });

        // Pantry section
        addSectionTitle(mainLayout, "Pantry Preferences");

        showExpiredCheckBox = new CheckBox(this);
        showExpiredCheckBox.setText(
                "Show expired ingredients in my pantry"
        );
        showExpiredCheckBox.setTextColor(darkText);
        showExpiredCheckBox.setButtonTintList(
                ColorStateList.valueOf(darkGreen)
        );
        showExpiredCheckBox.setChecked(
                preferences.getBoolean("show_expired", true)
        );
        mainLayout.addView(showExpiredCheckBox);

        // Recipe section
        addSectionTitle(mainLayout, "Recipe Preferences");

        matchingRecipesCheckBox = new CheckBox(this);
        matchingRecipesCheckBox.setText(
                "Show recipes I can make with my ingredients"
        );
        matchingRecipesCheckBox.setTextColor(darkText);
        matchingRecipesCheckBox.setButtonTintList(
                ColorStateList.valueOf(darkGreen)
        );
        matchingRecipesCheckBox.setChecked(
                preferences.getBoolean("matching_recipes", true)
        );
        mainLayout.addView(matchingRecipesCheckBox);

        // Save preferences
        Button saveSettingsButton = createButton("Save Preferences");
        mainLayout.addView(saveSettingsButton);

        saveSettingsButton.setOnClickListener(v -> {
            preferences.edit()
                    .putBoolean(
                            "show_expired",
                            showExpiredCheckBox.isChecked()
                    )
                    .putBoolean(
                            "matching_recipes",
                            matchingRecipesCheckBox.isChecked()
                    )
                    .apply();

            android.widget.Toast.makeText(
                    this, "Preferences saved successfully!",
                    android.widget.Toast.LENGTH_SHORT
            ).show();
        });

        // About section
        addSectionTitle(mainLayout, "About the App");

        TextView aboutText = new TextView(this);
        aboutText.setText(
                "Smart Pantry Manager helps you organise pantry " +
                        "ingredients, track expiry dates, and discover recipes " +
                        "based on the ingredients you have available."
        );
        aboutText.setTextColor(Color.DKGRAY);
        aboutText.setTextSize(15);
        aboutText.setPadding(16, 16, 16, 16);

        GradientDrawable cardBackground = new GradientDrawable();
        cardBackground.setColor(Color.WHITE);
        cardBackground.setCornerRadius(20);
        cardBackground.setStroke(2, lightMint);
        aboutText.setBackground(cardBackground);

        mainLayout.addView(aboutText);

        TextView version = new TextView(this);
        version.setText("Smart Pantry Manager • Version 1.0");
        version.setTextColor(darkGreen);
        version.setTextSize(14);
        version.setPadding(0, 16, 0, 16);
        mainLayout.addView(version);

        // Back to Home
        Button backButton = createButton("← Back to Home");
        mainLayout.addView(backButton);

        backButton.setOnClickListener(v -> finish());

        setContentView(scrollView);
    }

    private void addSectionTitle(
            LinearLayout layout, String text
    ) {
        TextView sectionTitle = new TextView(this);
        sectionTitle.setText(text);
        sectionTitle.setTextColor(darkGreen);
        sectionTitle.setTextSize(20);
        sectionTitle.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(0, 28, 0, 8);
        layout.addView(sectionTitle, params);
    }

    private Button createButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTransformationMethod(null);
        button.setTextColor(Color.WHITE);
        button.setBackgroundTintList(
                ColorStateList.valueOf(darkGreen)
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(0, 8, 0, 8);
        button.setLayoutParams(params);

        return button;
    }
}
