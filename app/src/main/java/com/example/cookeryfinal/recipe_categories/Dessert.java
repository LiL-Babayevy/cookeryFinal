package com.example.cookeryfinal.recipe_categories;

import com.example.cookeryfinal.recipe_related.Recipe;

import java.util.ArrayList;

public class Dessert extends RecipeCategory {
    public static final String DessetQuote = "Dessert ideas";
    public static final String name = "десерт";
    public static final int DessertID = 1;
    public static final ArrayList<Recipe> DessertRecipes = new ArrayList<>();

    @Override
    public void addRecipe(Recipe recipe) {
        super.addRecipe(recipe);
    }
}
