package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PantryActivity extends Activity {

    private static final int ADD_INGREDIENT_REQUEST = 1;

    private DatabaseHelper databaseHelper;
    private RecyclerView pantryRecyclerView;
    private PantryAdapter pantryAdapter;

    private final List<PantryIngredient> ingredientList =
            new ArrayList<>();

    private TextView txtTotalIngredients;
    private TextView txtExpiringSoon;
    private TextView txtExpired;
    private TextView txtPantryEmpty;

    private static final int DARK_GREEN = Color.rgb(36, 92, 67);
    private static final int LIGHT_MINT = Color.rgb(221, 243, 228);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        databaseHelper = new DatabaseHelper(this);

        // Connect the layout views
        pantryRecyclerView = findViewById(R.id.pantryRecyclerView);
        txtTotalIngredients = findViewById(R.id.txtTotalIngredients);
        txtExpiringSoon = findViewById(R.id.txtExpiringSoon);
        txtExpired = findViewById(R.id.txtExpired);

        Button btnAddIngredient = findViewById(R.id.btnAddIngredient);
        Button btnBackToHome = findViewById(R.id.btnBackToHome);

        // Configure RecyclerView
        pantryRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        pantryAdapter = new PantryAdapter(
                this,
                ingredientList,
                databaseHelper,
                this::displayIngredients
        );

        pantryRecyclerView.setAdapter(pantryAdapter);

        // Show a helpful message when the pantry is empty
        createEmptyMessage();

        // Open the Add Ingredient screen
        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(
                    PantryActivity.this,
                    AddIngredientActivity.class
            );

            startActivityForResult(
                    intent,
                    ADD_INGREDIENT_REQUEST
            );
        });

        // Return to the home screen
        btnBackToHome.setOnClickListener(v -> finish());

        // Load saved ingredients
        displayIngredients();
    }

    private void createEmptyMessage() {
        txtPantryEmpty = new TextView(this);

        txtPantryEmpty.setText(
                "Your pantry is empty.\nTap + Add Ingredient to begin."
        );

        txtPantryEmpty.setTextColor(DARK_GREEN);
        txtPantryEmpty.setTextSize(15);
        txtPantryEmpty.setGravity(Gravity.CENTER);
        txtPantryEmpty.setPadding(20, 24, 20, 24);

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.rgb(250, 249, 246));
        background.setCornerRadius(20);
        background.setStroke(2, LIGHT_MINT);

        txtPantryEmpty.setBackground(background);
        txtPantryEmpty.setVisibility(View.GONE);

        ViewGroup parent =
                (ViewGroup) pantryRecyclerView.getParent();

        if (parent instanceof LinearLayout) {

            int index = ((LinearLayout) parent)
                    .indexOfChild(pantryRecyclerView);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

            params.setMargins(0, 12, 0, 12);

            ((LinearLayout) parent).addView(
                    txtPantryEmpty,
                    index + 1,
                    params
            );
        }
    }

    // Receive the result from AddIngredientActivity
    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_INGREDIENT_REQUEST
                && resultCode == RESULT_OK) {
            displayIngredients();
        }
    }

    // Refresh the pantry whenever this screen becomes active
    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null && pantryAdapter != null) {
            displayIngredients();
        }
    }

    // Called by PantryAdapter when the Edit button is clicked
    public void editIngredient(Intent intent) {
        startActivityForResult(
                intent,
                ADD_INGREDIENT_REQUEST
        );
    }

    // Load ingredients from SQLite into the RecyclerView
    private void displayIngredients() {

        ingredientList.clear();

        try (Cursor cursor = databaseHelper.getAllIngredients()) {

            if (cursor != null) {

                int idColumn = cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COL_ID
                );

                int nameColumn = cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COL_NAME
                );

                int quantityColumn = cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COL_QUANTITY
                );

                int unitColumn = cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COL_UNIT
                );

                int expiryColumn = cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COL_EXPIRY
                );

                while (cursor.moveToNext()) {

                    String expiry = cursor.isNull(expiryColumn)
                            ? ""
                            : cursor.getString(expiryColumn);

                    PantryIngredient ingredient =
                            new PantryIngredient(
                                    cursor.getInt(idColumn),
                                    cursor.getString(nameColumn),
                                    cursor.getDouble(quantityColumn),
                                    cursor.getString(unitColumn),
                                    expiry
                            );

                    ingredientList.add(ingredient);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        pantryAdapter.notifyDataSetChanged();

        // Display the empty message when no ingredients exist
        txtPantryEmpty.setVisibility(
                ingredientList.isEmpty()
                        ? View.VISIBLE
                        : View.GONE
        );

        updatePantrySummary();
    }

    // Update the ingredient, expiring soon and expired counters
    private void updatePantrySummary() {

        int total = 0;
        int expiringSoon = 0;
        int expired = 0;

        try (Cursor cursor = databaseHelper.getAllIngredients()) {

            if (cursor != null) {

                int expiryColumn = cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COL_EXPIRY
                );

                while (cursor.moveToNext()) {

                    total++;

                    String expiry = cursor.isNull(expiryColumn)
                            ? ""
                            : cursor.getString(expiryColumn);

                    String status = getExpiryStatus(expiry);

                    if (status.startsWith("Expired")) {
                        expired++;
                    } else if (status.startsWith("Expiring Soon")) {
                        expiringSoon++;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        txtTotalIngredients.setText(String.valueOf(total));
        txtExpiringSoon.setText(String.valueOf(expiringSoon));
        txtExpired.setText(String.valueOf(expired));
    }

    // Determine the expiry status of an ingredient
    private String getExpiryStatus(String expiry) {

        if (expiry == null || expiry.trim().isEmpty()) {
            return "No Expiry Date";
        }

        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
        );

        format.setLenient(false);

        try {

            Date expiryDate = format.parse(expiry.trim());

            if (expiryDate == null) {
                return "Invalid expiry date";
            }

            Calendar today = Calendar.getInstance();
            clearTime(today);

            Calendar expiryCalendar = Calendar.getInstance();
            expiryCalendar.setTime(expiryDate);
            clearTime(expiryCalendar);

            long difference =
                    expiryCalendar.getTimeInMillis()
                            - today.getTimeInMillis();

            long daysRemaining =
                    difference / (24 * 60 * 60 * 1000L);

            if (daysRemaining < 0) {
                return "Expired";
            } else if (daysRemaining <= 7) {
                return "Expiring Soon";
            } else {
                return "Fresh";
            }

        } catch (ParseException e) {
            return "Invalid expiry date";
        }
    }

    // Remove the time portion when comparing dates
    private void clearTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}

