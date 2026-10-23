package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_INGREDIENTS = "ingredients";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS =
            "recipe_ingredients";

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_EXPIRY = "expiry";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create database tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_NAME + " TEXT NOT NULL, " +
                        COL_QUANTITY + " REAL NOT NULL, " +
                        COL_UNIT + " TEXT NOT NULL, " +
                        COL_EXPIRY + " TEXT)"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_RECIPES + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_NAME + " TEXT NOT NULL, " +
                        "description TEXT, " +
                        "instructions TEXT, " +
                        "prep_time INTEGER)"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "recipe_id INTEGER NOT NULL, " +
                        "ingredient_name TEXT NOT NULL, " +
                        "required_quantity REAL NOT NULL, " +
                        "unit TEXT NOT NULL, " +
                        "FOREIGN KEY(recipe_id) REFERENCES " +
                        TABLE_RECIPES + "(" + COL_ID + ") ON DELETE CASCADE)"
        );
    }

    // Upgrade database without deleting existing pantry ingredients
    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        if (oldVersion < 2) {

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS " + TABLE_RECIPES + " (" +
                            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COL_NAME + " TEXT NOT NULL, " +
                            "description TEXT, " +
                            "instructions TEXT, " +
                            "prep_time INTEGER)"
            );

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS " +
                            TABLE_RECIPE_INGREDIENTS + " (" +
                            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "recipe_id INTEGER NOT NULL, " +
                            "ingredient_name TEXT NOT NULL, " +
                            "required_quantity REAL NOT NULL, " +
                            "unit TEXT NOT NULL, " +
                            "FOREIGN KEY(recipe_id) REFERENCES " +
                            TABLE_RECIPES + "(" + COL_ID +
                            ") ON DELETE CASCADE)"
            );
        }
    }

    // Add a pantry ingredient
    public boolean addIngredient(
            String name,
            double quantity,
            String unit,
            String expiry
    ) {

        if (name == null || name.trim().isEmpty()
                || unit == null || unit.trim().isEmpty()
                || !Double.isFinite(quantity) || quantity <= 0) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name.trim());
        values.put(COL_QUANTITY, quantity);
        values.put(COL_UNIT, unit.trim());
        values.put(COL_EXPIRY, expiry);

        long result = db.insert(TABLE_INGREDIENTS, null, values);

        return result != -1;
    }

    // Get all pantry ingredients
    public Cursor getAllIngredients() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                TABLE_INGREDIENTS,
                null,
                null,
                null,
                null,
                null,
                COL_NAME + " ASC"
        );
    }

    // Delete a pantry ingredient
    public boolean deleteIngredient(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_INGREDIENTS,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }

    // Update a pantry ingredient
    public boolean updateIngredient(
            int id,
            String name,
            double quantity,
            String unit,
            String expiry
    ) {

        if (name == null || name.trim().isEmpty()
                || unit == null || unit.trim().isEmpty()
                || !Double.isFinite(quantity) || quantity <= 0) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name.trim());
        values.put(COL_QUANTITY, quantity);
        values.put(COL_UNIT, unit.trim());
        values.put(COL_EXPIRY, expiry);

        int result = db.update(
                TABLE_INGREDIENTS,
                values,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }

    // Add a recipe
    public boolean addRecipe(
            String name,
            String description,
            String instructions,
            int prepTime
    ) {

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name.trim());
        values.put("description", description);
        values.put("instructions", instructions);
        values.put("prep_time", prepTime);

        long result = db.insert(TABLE_RECIPES, null, values);

        return result != -1;
    }

    // Add an ingredient required by a recipe
    public boolean addRecipeIngredient(
            int recipeId,
            String ingredientName,
            double requiredQuantity,
            String unit
    ) {

        if (ingredientName == null || ingredientName.trim().isEmpty()
                || unit == null || unit.trim().isEmpty()
                || !Double.isFinite(requiredQuantity)
                || requiredQuantity <= 0) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("recipe_id", recipeId);
        values.put("ingredient_name", ingredientName.trim());
        values.put("required_quantity", requiredQuantity);
        values.put("unit", unit.trim());

        long result = db.insert(
                TABLE_RECIPE_INGREDIENTS,
                null,
                values
        );

        return result != -1;
    }

    // Get all recipes
    public Cursor getAllRecipes() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                COL_NAME + " ASC"
        );
    }

    // Get ingredients required by a particular recipe
    public Cursor getRecipeIngredients(int recipeId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                "recipe_id = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                "ingredient_name ASC"
        );
    }

    // Update Vegetable Fried Rice requirements
    public void updateFriedRiceRequirements() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        try (Cursor cursor = db.query(
                TABLE_RECIPES,
                new String[]{COL_ID},
                "name = ?",
                new String[]{"Vegetable Fried Rice"},
                null,
                null,
                null
        )) {

            if (cursor.moveToFirst()) {

                int recipeId = cursor.getInt(0);

                updateRecipeIngredient(
                        db, recipeId, "rice", 2, "cups"
                );

                updateRecipeIngredient(
                        db, recipeId, "carrot", 1, "pieces"
                );

                updateRecipeIngredient(
                        db, recipeId, "peas", 1, "cups"
                );

                updateRecipeIngredient(
                        db, recipeId, "oil", 1, "tablespoons"
                );
            }

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
    }

    // Update one recipe ingredient requirement
    private void updateRecipeIngredient(
            SQLiteDatabase db,
            int recipeId,
            String ingredientName,
            double quantity,
            String unit
    ) {

        ContentValues values = new ContentValues();
        values.put("required_quantity", quantity);
        values.put("unit", unit);

        db.update(
                TABLE_RECIPE_INGREDIENTS,
                values,
                "recipe_id = ? AND LOWER(TRIM(ingredient_name)) = ?",
                new String[]{
                        String.valueOf(recipeId),
                        ingredientName.trim().toLowerCase(Locale.ROOT)
                }
        );
    }

    // Convert common unit spellings to a standard unit
    private String normalizeUnit(String unit) {

        if (unit == null) {
            return "";
        }

        String value = unit.trim().toLowerCase(Locale.ROOT);

        switch (value) {

            case "g":
            case "gram":
            case "grams":
                return "g";

            case "kg":
            case "kilogram":
            case "kilograms":
                return "kg";

            case "ml":
            case "millilitre":
            case "millilitres":
            case "milliliter":
            case "milliliters":
                return "ml";

            case "l":
            case "litre":
            case "litres":
            case "liter":
            case "liters":
                return "l";

            case "piece":
            case "pieces":
            case "pc":
            case "pcs":
                return "pieces";

            case "cup":
            case "cups":
                return "cups";

            case "tablespoon":
            case "tablespoons":
            case "tbsp":
            case "tbsps":
                return "tablespoons";

            case "teaspoon":
            case "teaspoons":
            case "tsp":
            case "tsps":
                return "teaspoons";

            case "packet":
            case "packets":
                return "packets";

            default:
                return value;
        }
    }

    // Convert compatible measurements to a common base quantity.
    // Mass is converted to grams; volume is converted to millilitres.
    private double toBaseQuantity(double quantity, String unit) {

        switch (unit) {

            case "kg":
                return quantity * 1000.0;

            case "g":
                return quantity;

            case "l":
                return quantity * 1000.0;

            case "ml":
                return quantity;

            case "cups":
                return quantity * 250.0;

            case "tablespoons":
                return quantity * 15.0;

            case "teaspoons":
                return quantity * 5.0;

            default:
                // Pieces, packets and other units remain unchanged.
                return quantity;
        }
    }

    // Return the measurement category for a unit
    private String getUnitCategory(String unit) {

        switch (unit) {

            case "g":
            case "kg":
                return "mass";

            case "ml":
            case "l":
            case "cups":
            case "tablespoons":
            case "teaspoons":
                return "volume";

            case "pieces":
                return "count";

            case "packets":
                return "packets";

            default:
                // Unknown units only match the same normalized unit.
                return "other:" + unit;
        }
    }

    // Normalise common ingredient name variations.
    // This intentionally avoids broad fuzzy matching.
    private String normalizeIngredientName(String name) {

        if (name == null) {
            return "";
        }

        String value = name.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");

        // Handle a few common irregular plurals.
        switch (value) {
            case "tomatoes":
                return "tomato";
            case "potatoes":
                return "potato";
            case "leaves":
                return "leaf";
            case "loaves":
                return "loaf";
            default:
                break;
        }

        // Handle common regular plurals.
        if (value.length() > 3 && value.endsWith("ies")) {
            return value.substring(0, value.length() - 3) + "y";
        }

        if (value.length() > 3
                && (value.endsWith("ches")
                || value.endsWith("shes")
                || value.endsWith("xes")
                || value.endsWith("zes"))) {

            return value.substring(0, value.length() - 2);
        }

        if (value.length() > 3
                && value.endsWith("s")
                && !value.endsWith("ss")) {

            return value.substring(0, value.length() - 1);
        }

        return value;
    }

    // Check whether every required ingredient is available.
    // A recipe is suggested only when ALL requirements are satisfied.
    public boolean canMakeRecipe(int recipeId) {

        SQLiteDatabase db = this.getReadableDatabase();

        boolean hasRequiredIngredients = false;

        try (Cursor recipeCursor = getRecipeIngredients(recipeId)) {

            // Recipes without requirements must never be suggested.
            if (!recipeCursor.moveToFirst()) {
                return false;
            }

            do {

                hasRequiredIngredients = true;

                String requiredName = recipeCursor.getString(
                        recipeCursor.getColumnIndexOrThrow(
                                "ingredient_name"
                        )
                );

                double requiredQuantity = recipeCursor.getDouble(
                        recipeCursor.getColumnIndexOrThrow(
                                "required_quantity"
                        )
                );

                String requiredUnit = normalizeUnit(
                        recipeCursor.getString(
                                recipeCursor.getColumnIndexOrThrow("unit")
                        )
                );

                // Invalid recipe requirements must not pass matching.
                if (requiredName == null
                        || requiredName.trim().isEmpty()
                        || !Double.isFinite(requiredQuantity)
                        || requiredQuantity <= 0
                        || requiredUnit.isEmpty()) {
                    return false;
                }

                String normalizedRequiredName =
                        normalizeIngredientName(requiredName);

                String requiredCategory =
                        getUnitCategory(requiredUnit);

                double requiredBaseQuantity =
                        toBaseQuantity(requiredQuantity, requiredUnit);

                double availableBaseQuantity = 0;

                boolean ingredientFound = false;

                // Read all pantry entries to support name normalisation
                // and combine duplicate entries with compatible units.
                try (Cursor pantryCursor = db.query(
                        TABLE_INGREDIENTS,
                        new String[]{
                                COL_NAME,
                                COL_QUANTITY,
                                COL_UNIT
                        },
                        null,
                        null,
                        null,
                        null,
                        null
                )) {

                    while (pantryCursor.moveToNext()) {

                        String pantryName = pantryCursor.getString(
                                pantryCursor.getColumnIndexOrThrow(
                                        COL_NAME
                                )
                        );

                        double pantryQuantity = pantryCursor.getDouble(
                                pantryCursor.getColumnIndexOrThrow(
                                        COL_QUANTITY
                                )
                        );

                        String pantryUnit = normalizeUnit(
                                pantryCursor.getString(
                                        pantryCursor.getColumnIndexOrThrow(
                                                COL_UNIT
                                        )
                                )
                        );

                        // Ignore invalid pantry entries.
                        if (pantryName == null
                                || pantryUnit.isEmpty()
                                || !Double.isFinite(pantryQuantity)
                                || pantryQuantity < 0) {
                            continue;
                        }

                        // Ingredient names must match after normalisation.
                        if (!normalizeIngredientName(pantryName)
                                .equals(normalizedRequiredName)) {
                            continue;
                        }

                        // Units must belong to compatible categories.
                        if (!getUnitCategory(pantryUnit)
                                .equals(requiredCategory)) {
                            continue;
                        }

                        ingredientFound = true;

                        availableBaseQuantity += toBaseQuantity(
                                pantryQuantity,
                                pantryUnit
                        );
                    }
                }

                // Strict rule: reject if the ingredient is absent
                // or the total available quantity is insufficient.
                if (!ingredientFound
                        || availableBaseQuantity + 0.000001
                        < requiredBaseQuantity) {
                    return false;
                }

            } while (recipeCursor.moveToNext());
        }

        return hasRequiredIngredients;
    }
}

