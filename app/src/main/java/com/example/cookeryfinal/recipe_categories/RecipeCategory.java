package com.example.cookeryfinal.recipe_categories;

import com.example.cookeryfinal.recipe_related.Recipe;

import java.util.ArrayList;

public class RecipeCategory {
    private String categoryName;
    private int categoryID;
    public static final ArrayList<Recipe> recipesInCategory = new ArrayList<>();

    public void addRecipe(Recipe recipe){
        recipesInCategory.add(recipe);
        recipe.setStatus(categoryID);
    }
}
