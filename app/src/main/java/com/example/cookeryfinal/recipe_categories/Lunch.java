package com.example.cookeryfinal.recipe_categories;

import com.example.cookeryfinal.recipe_related.Recipe;

import java.util.ArrayList;

public class Lunch extends RecipeCategory {
    public static final String LunchQuote = "lunch ideas";
    public static final String name = "обед";
    public static final int LunchID = 1;
    public static final ArrayList<Recipe> LunchRecipes = new ArrayList<>();

    @Override
    public void addRecipe(Recipe recipe) {
        super.addRecipe(recipe);
    }
}
