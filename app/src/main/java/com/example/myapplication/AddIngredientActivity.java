package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddIngredientActivity extends Activity {

    private EditText editName;
    private EditText editQuantity;
    private EditText editExpiry;
    private Spinner spinnerUnit;

    private DatabaseHelper databaseHelper;

    private boolean isEditing = false;
    private int ingredientId = -1;

    private Button btnSave;

    private final String[] units = {
            "Select unit", "pieces", "g", "kg", "ml", "litres",
            "cups", "tablespoons", "teaspoons", "packets"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        databaseHelper = new DatabaseHelper(this);

        editName = findViewById(R.id.editIngredientName);
        editQuantity = findViewById(R.id.editIngredientQuantity);
        editExpiry = findViewById(R.id.editIngredientExpiry);
        spinnerUnit = findViewById(R.id.spinnerIngredientUnit);

        btnSave = findViewById(R.id.btnSaveIngredient);
        Button btnCancel = findViewById(R.id.btnCancelIngredient);

        // Set up unit choices
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerUnit.setAdapter(adapter);

        // Check whether an existing ingredient is being edited
        if (getIntent().hasExtra("ingredient_id")) {
            isEditing = true;
            ingredientId = getIntent().getIntExtra(
                    "ingredient_id", -1
            );

            loadIngredientDetails();

            btnSave.setText("Save Changes");
        } else {
            btnSave.setText("Save Ingredient");
        }

        btnSave.setOnClickListener(v -> saveIngredient());

        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadIngredientDetails() {

        String name = getIntent().getStringExtra("ingredient_name");
        double quantity = getIntent().getDoubleExtra(
                "ingredient_quantity", 0
        );
        String unit = getIntent().getStringExtra("ingredient_unit");
        String expiry = getIntent().getStringExtra("ingredient_expiry");

        editName.setText(name == null ? "" : name);
        editQuantity.setText(
                quantity > 0 ? String.valueOf(quantity) : ""
        );
        editExpiry.setText(expiry == null ? "" : expiry);

        if (unit != null) {
            for (int i = 0; i < units.length; i++) {
                if (units[i].equalsIgnoreCase(unit)) {
                    spinnerUnit.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveIngredient() {

        String name = editName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();
        String expiry = editExpiry.getText().toString().trim();

        // Validate ingredient name
        if (name.isEmpty()) {
            editName.setError("Enter an ingredient name");
            editName.requestFocus();
            return;
        }

        // Validate quantity
        if (quantityText.isEmpty()) {
            editQuantity.setError("Enter a quantity");
            editQuantity.requestFocus();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);

            if (Double.isNaN(quantity)
                    || Double.isInfinite(quantity)
                    || quantity <= 0) {

                editQuantity.setError(
                        "Quantity must be greater than zero"
                );
                editQuantity.requestFocus();
                return;
            }

        } catch (NumberFormatException e) {
            editQuantity.setError("Enter a valid quantity");
            editQuantity.requestFocus();
            return;
        }

        // Validate unit
        if (spinnerUnit.getSelectedItemPosition() == 0) {
            Toast.makeText(
                    this,
                    "Please select a unit",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        long result;

        if (isEditing) {

            boolean rowsUpdated = databaseHelper.updateIngredient(
                    ingredientId,
                    name,
                    quantity,
                    unit,
                    expiry
            );

            result = rowsUpdated ? 1 : -1;

        } else {

            result = databaseHelper.addIngredient(
                    name,
                    quantity,
                    unit,
                    expiry
            ) ? 1 : -1;
        }

        if (result != -1) {

            String message = isEditing
                    ? "Ingredient updated successfully!"
                    : "Ingredient saved successfully!";

            Toast.makeText(
                    this,
                    message,
                    Toast.LENGTH_SHORT
            ).show();

            setResult(RESULT_OK);
            finish();

        } else {

            Toast.makeText(
                    this,
                    isEditing
                            ? "Could not update ingredient."
                            : "Could not save ingredient.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}

