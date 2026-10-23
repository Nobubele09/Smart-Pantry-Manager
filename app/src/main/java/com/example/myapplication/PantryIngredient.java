package com.example.myapplication;

public class PantryIngredient {

    private final int id;
    private final String name;
    private final double quantity;
    private final String unit;
    private final String expiry;

    public PantryIngredient(
            int id,
            String name,
            double quantity,
            String unit,
            String expiry
    ) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiry = expiry;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getExpiry() {
        return expiry;
    }
}

