package com.example.cookeryfinal.recipe_categories;

import com.example.cookeryfinal.recipe_related.Recipe;

import java.util.ArrayList;

public class Dinner extends RecipeCategory {
    public static final String DinnerQuote = "Dinner ideas";
    public static final String name = "ужин";
    public static final int DinnerID = 1;
    public static final ArrayList<Recipe> DinnerRecipes = new ArrayList<>();

    @Override
    public void addRecipe(Recipe recipe) {
        super.addRecipe(recipe);
    }
}
