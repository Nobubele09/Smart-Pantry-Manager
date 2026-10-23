package com.example.myapplication;

public class Recipe {

    private int id;
    private String name;
    private String description;
    private String instructions;
    private int prepTime;

    public Recipe(int id, String name, String description,
                  String instructions, int prepTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.instructions = instructions;
        this.prepTime = prepTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructions() {
        return instructions;
    }

    public int getPrepTime() {
        return prepTime;
    }
}


