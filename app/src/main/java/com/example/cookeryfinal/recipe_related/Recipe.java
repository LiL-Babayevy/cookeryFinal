package com.example.cookeryfinal.recipe_related;

import com.example.cookeryfinal.recipe_related.Ingredient;

import java.util.ArrayList;

public class Recipe {
    private String recipe_name;
    private String recipeID;
    private String ownerID;
    private String cooking_steps;
    private int status; // recipe's status id
    private ArrayList<Ingredient> ingredients;

    public Recipe(){}

    public Recipe(String recipe_name, String cooking_steps, ArrayList<Ingredient> ingredients /*,String ownerID*/) {
        this.recipe_name = recipe_name;
        this.cooking_steps = cooking_steps;
        this.ingredients = ingredients;
        //this.ownerID = ownerID;

    }

    public String getRecipe_name() {
        if(recipe_name == null){
            return "lauraclass";
        }
        return recipe_name;
    }

    public String RecipeToString(){
        String recipe = "Ингредиенты:\n";
        for(Ingredient i: getIngredients()){
            recipe += i.ingredient_name + " " +i.amount+"\n";
        }
        recipe+="Приготовление:\n" + cooking_steps;
        return recipe;
    }


    public String getCooking_steps() {
        return cooking_steps;
    }
    public void setCooking_steps(String cooking_steps){
        this.cooking_steps = cooking_steps;
    }

    public void setRecipeId(String recipeID){
        this.recipeID = recipeID;
    }
    public String getRecipeId(){
        return this.recipeID;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }
}
