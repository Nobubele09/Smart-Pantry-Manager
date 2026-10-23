package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PantryAdapter
        extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private final Context context;
    private final List<PantryIngredient> ingredientList;
    private final DatabaseHelper databaseHelper;
    private final Runnable refreshPantry;

    public PantryAdapter(
            Context context,
            List<PantryIngredient> ingredientList,
            DatabaseHelper databaseHelper,
            Runnable refreshPantry
    ) {
        this.context = context;
        this.ingredientList = ingredientList;
        this.databaseHelper = databaseHelper;
        this.refreshPantry = refreshPantry;
    }

    @Override
    public PantryViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_pantry_ingredient,
                        parent,
                        false
                );

        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            PantryViewHolder holder,
            int position
    ) {
        PantryIngredient ingredient = ingredientList.get(position);

        holder.txtIngredientName.setText(ingredient.getName());

        holder.txtIngredientQuantity.setText(
                "Quantity: "
                        + formatQuantity(ingredient.getQuantity())
                        + " "
                        + ingredient.getUnit()
        );

        String expiryStatus = getExpiryStatus(ingredient.getExpiry());

        holder.txtIngredientExpiry.setText(expiryStatus);

        if (expiryStatus.startsWith("Expired")) {
            holder.txtIngredientExpiry.setTextColor(
                    Color.rgb(190, 65, 55)
            );
        } else if (expiryStatus.startsWith("Expiring Soon")) {
            holder.txtIngredientExpiry.setTextColor(
                    Color.rgb(180, 100, 30)
            );
        } else {
            holder.txtIngredientExpiry.setTextColor(
                    Color.rgb(36, 92, 67)
            );
        }

        // Edit ingredient
        holder.btnEditIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(
                    context,
                    AddIngredientActivity.class
            );

            intent.putExtra("ingredient_id", ingredient.getId());
            intent.putExtra("ingredient_name", ingredient.getName());
            intent.putExtra(
                    "ingredient_quantity",
                    ingredient.getQuantity()
            );
            intent.putExtra("ingredient_unit", ingredient.getUnit());
            intent.putExtra("ingredient_expiry", ingredient.getExpiry());

            if (context instanceof PantryActivity) {
                ((PantryActivity) context).editIngredient(intent);
            }
        });

        // Delete ingredient
        holder.btnDeleteIngredient.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Ingredient")
                    .setMessage(
                            "Are you sure you want to delete "
                                    + ingredient.getName()
                                    + "?"
                    )
                    .setNegativeButton("Cancel", (dialog, which) ->
                            dialog.dismiss())
                    .setPositiveButton("Delete", (dialog, which) -> {

                        databaseHelper.deleteIngredient(
                                ingredient.getId()
                        );

                        Toast.makeText(
                                context,
                                "Ingredient deleted.",
                                Toast.LENGTH_SHORT
                        ).show();

                        refreshPantry.run();
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    private String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity)) {
            return String.valueOf((long) quantity);
        }

        return String.valueOf(quantity);
    }

    private String getExpiryStatus(String expiry) {
        if (expiry == null || expiry.trim().isEmpty()) {
            return "No expiry date";
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

            long difference = expiryCalendar.getTimeInMillis()
                    - today.getTimeInMillis();

            long daysRemaining = difference
                    / (24 * 60 * 60 * 1000L);

            if (daysRemaining < 0) {
                return "Expired";
            } else if (daysRemaining <= 7) {
                return "Expiring Soon — "
                        + daysRemaining
                        + " day(s) remaining";
            } else {
                return "Fresh — "
                        + daysRemaining
                        + " day(s) remaining";
            }

        } catch (ParseException e) {
            return "Invalid expiry date";
        }
    }

    private void clearTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {

        TextView txtIngredientName;
        TextView txtIngredientQuantity;
        TextView txtIngredientExpiry;

        Button btnEditIngredient;
        Button btnDeleteIngredient;

        PantryViewHolder(View itemView) {
            super(itemView);

            txtIngredientName = itemView.findViewById(
                    R.id.txtIngredientName
            );

            txtIngredientQuantity = itemView.findViewById(
                    R.id.txtIngredientQuantity
            );

            txtIngredientExpiry = itemView.findViewById(
                    R.id.txtIngredientExpiry
            );

            btnEditIngredient = itemView.findViewById(
                    R.id.btnEditIngredient
            );

            btnDeleteIngredient = itemView.findViewById(
                    R.id.btnDeleteIngredient
            );
        }
    }
}


