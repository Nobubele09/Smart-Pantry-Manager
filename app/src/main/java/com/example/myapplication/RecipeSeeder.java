package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RecipeSeeder {

    private final DatabaseHelper databaseHelper;

    public RecipeSeeder(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void seedRecipes() {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM recipes", null);

        int recipeCount = 0;

        if (cursor.moveToFirst()) {
            recipeCount = cursor.getInt(0);
        }

        cursor.close();

        // Do not insert the recipes more than once.
        if (recipeCount > 0) {
            return;
        }

        db.beginTransaction();

        try {

            addRecipe(db, "Vegetable Fried Rice",
                    "A quick rice meal with vegetables.", 20,
                    "1. Cook the rice.\n2. Chop the vegetables.\n3. Fry the vegetables in a little oil.\n4. Add the rice and stir well.",
                    new String[]{"rice", "carrot", "peas", "oil"},
                    new double[]{2, 1, 1, 1},
                    new String[]{"cups", "pieces", "cups", "tablespoons"});

            addRecipe(db, "Tomato Pasta",
                    "Pasta with a simple tomato sauce.", 25,
                    "1. Cook the pasta.\n2. Chop the tomatoes and onion.\n3. Cook them with a little oil.\n4. Mix with the pasta.",
                    new String[]{"pasta", "tomato", "onion", "oil"},
                    new double[]{200, 150, 50, 10},
                    new String[]{"g", "g", "g", "ml"});

            addRecipe(db, "Scrambled Eggs",
                    "Soft scrambled eggs for breakfast.", 10,
                    "1. Crack the eggs into a bowl.\n2. Beat them.\n3. Heat a little oil in a pan.\n4. Stir the eggs until cooked.",
                    new String[]{"eggs", "oil"},
                    new double[]{2, 5},
                    new String[]{"pieces", "ml"});

            addRecipe(db, "Banana Pancakes",
                    "Simple homemade banana pancakes.", 15,
                    "1. Mash the banana.\n2. Mix with flour, egg and milk.\n3. Cook small portions in a lightly oiled pan.",
                    new String[]{"banana", "flour", "eggs", "milk", "oil"},
                    new double[]{1, 100, 1, 100, 5},
                    new String[]{"pieces", "g", "pieces", "ml", "ml"});

            addRecipe(db, "Potato Wedges",
                    "Oven-baked seasoned potato wedges.", 35,
                    "1. Wash and cut the potatoes.\n2. Toss with oil and seasoning.\n3. Bake until golden and tender.",
                    new String[]{"potato", "oil", "salt"},
                    new double[]{300, 15, 2},
                    new String[]{"g", "ml", "g"});

            addRecipe(db, "Tomato Omelette",
                    "Eggs cooked with fresh tomato.", 12,
                    "1. Beat the eggs.\n2. Chop the tomato.\n3. Cook the tomato briefly, add eggs and cook until set.",
                    new String[]{"eggs", "tomato", "oil"},
                    new double[]{2, 80, 5},
                    new String[]{"pieces", "g", "ml"});

            addRecipe(db, "Peanut Butter Sandwich",
                    "A quick sandwich with peanut butter.", 5,
                    "1. Spread peanut butter on the bread.\n2. Close the sandwich and serve.",
                    new String[]{"bread", "peanut butter"},
                    new double[]{2, 30},
                    new String[]{"pieces", "g"});

            addRecipe(db, "Garlic Pasta",
                    "Pasta tossed with garlic and oil.", 20,
                    "1. Cook the pasta.\n2. Chop the garlic.\n3. Warm the garlic in oil.\n4. Toss with pasta.",
                    new String[]{"pasta", "garlic", "oil"},
                    new double[]{200, 10, 15},
                    new String[]{"g", "g", "ml"});

            addRecipe(db, "Carrot Soup",
                    "A warming carrot soup.", 30,
                    "1. Chop carrots and onion.\n2. Simmer in water until soft.\n3. Blend carefully until smooth.\n4. Season and serve.",
                    new String[]{"carrot", "onion", "salt"},
                    new double[]{250, 50, 2},
                    new String[]{"g", "g", "g"});

            addRecipe(db, "Apple Oatmeal",
                    "Warm oats topped with apple.", 10,
                    "1. Cook the oats with milk or water.\n2. Chop the apple.\n3. Add the apple and serve.",
                    new String[]{"oats", "apple", "milk"},
                    new double[]{50, 1, 150},
                    new String[]{"g", "pieces", "ml"});

            addRecipe(db, "Cheese Toast",
                    "Crispy toast with melted cheese.", 8,
                    "1. Place cheese on the bread.\n2. Toast in a suitable appliance until the cheese melts.",
                    new String[]{"bread", "cheese"},
                    new double[]{2, 40},
                    new String[]{"pieces", "g"});

            addRecipe(db, "Chicken and Rice",
                    "A filling chicken and rice meal.", 40,
                    "1. Cook the chicken thoroughly.\n2. Cook the rice separately.\n3. Season and serve together.",
                    new String[]{"chicken", "rice", "salt"},
                    new double[]{200, 150, 2},
                    new String[]{"g", "g", "g"});

            addRecipe(db, "Tuna Sandwich",
                    "A simple tuna sandwich.", 10,
                    "1. Drain the tuna.\n2. Mix with a little mayonnaise if available.\n3. Place between slices of bread.",
                    new String[]{"bread", "tuna"},
                    new double[]{2, 100},
                    new String[]{"pieces", "g"});

            addRecipe(db, "Spinach Eggs",
                    "Eggs cooked with spinach.", 15,
                    "1. Wash and chop the spinach.\n2. Cook briefly in a pan.\n3. Add the eggs and cook thoroughly.",
                    new String[]{"eggs", "spinach", "oil"},
                    new double[]{2, 100, 5},
                    new String[]{"pieces", "g", "ml"});

            addRecipe(db, "Sweet Corn Salad",
                    "A quick corn and tomato salad.", 10,
                    "1. Drain the corn if canned.\n2. Chop the tomato.\n3. Mix together and season.",
                    new String[]{"sweetcorn", "tomato", "salt"},
                    new double[]{100, 100, 2},
                    new String[]{"g", "g", "g"});

            addRecipe(db, "Mashed Potatoes",
                    "Creamy mashed potatoes.", 25,
                    "1. Boil the potatoes until tender.\n2. Mash thoroughly.\n3. Add milk and season.",
                    new String[]{"potato", "milk", "salt"},
                    new double[]{300, 50, 2},
                    new String[]{"g", "ml", "g"});

            addRecipe(db, "Flour Flatbread",
                    "Simple pan-cooked flatbread.", 20,
                    "1. Mix flour, water and a little salt.\n2. Knead into a dough.\n3. Roll flat and cook in a hot pan.",
                    new String[]{"flour", "salt"},
                    new double[]{200, 2},
                    new String[]{"g", "g"});

            addRecipe(db, "Yoghurt Fruit Bowl",
                    "Fruit served with yoghurt.", 5,
                    "1. Wash and cut the fruit.\n2. Add yoghurt and serve chilled.",
                    new String[]{"banana", "apple", "yoghurt"},
                    new double[]{1, 1, 100},
                    new String[]{"pieces", "pieces", "g"});

            addRecipe(db, "Bean Tomato Stew",
                    "Beans cooked in a tomato sauce.", 25,
                    "1. Chop the onion and tomato.\n2. Cook with the beans and a little water.\n3. Simmer until hot and tender.",
                    new String[]{"beans", "tomato", "onion"},
                    new double[]{200, 100, 50},
                    new String[]{"g", "g", "g"});

            addRecipe(db, "Lemon Water",
                    "Refreshing lemon-infused water.", 5,
                    "1. Squeeze the lemon into the water.\n2. Stir and serve.",
                    new String[]{"lemon", "water"},
                    new double[]{1, 250},
                    new String[]{"pieces", "ml"});

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
    }

    private void addRecipe(SQLiteDatabase db,
                           String name,
                           String description,
                           int prepTime,
                           String instructions,
                           String[] ingredientNames,
                           double[] quantities,
                           String[] units) {

        android.content.ContentValues recipeValues =
                new android.content.ContentValues();

        recipeValues.put("name", name);
        recipeValues.put("description", description);
        recipeValues.put("instructions", instructions);
        recipeValues.put("prep_time", prepTime);

        long recipeId = db.insert("recipes", null, recipeValues);

        if (recipeId == -1) {
            throw new IllegalStateException(
                    "Could not insert recipe: " + name);
        }

        for (int i = 0; i < ingredientNames.length; i++) {

            android.content.ContentValues ingredientValues =
                    new android.content.ContentValues();

            ingredientValues.put("recipe_id", recipeId);
            ingredientValues.put("ingredient_name", ingredientNames[i]);
            ingredientValues.put("required_quantity", quantities[i]);
            ingredientValues.put("unit", units[i]);

            long result = db.insert(
                    "recipe_ingredients",
                    null,
                    ingredientValues);

            if (result == -1) {
                throw new IllegalStateException(
                        "Could not insert ingredient for: " + name);
            }
        }
    }
}


