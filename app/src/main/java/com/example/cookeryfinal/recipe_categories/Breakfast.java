package com.example.cookeryfinal.recipe_categories;

import com.example.cookeryfinal.recipe_related.Recipe;

import java.util.ArrayList;

public class Breakfast extends RecipeCategory {
    public static final String BreakfastQuote = "Breakfast ideas";
    public static final String name = "завтрак";
    public static final int BreakfastID = 1;
    public static final ArrayList<Recipe> BreakfastRecipes = new ArrayList<>();

    public static String getName() {
        return name;
    }

    public static int getBreakfastID() {
        return BreakfastID;
    }

    public static ArrayList<Recipe> getBreakfastRecipes() {
        return BreakfastRecipes;
    }

    @Override
    public void addRecipe(Recipe recipe) {
        super.addRecipe(recipe);
    }
}
