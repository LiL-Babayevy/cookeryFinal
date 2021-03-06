package com.example.cookeryfinal.recipe_related;

import java.util.ArrayList;

public class Recipe {
    private String recipe_name;
    private String recipeID;
    private String ownerID;
    private String cooking_steps;
    private String status;
    private ArrayList<Ingredient> ingredients;
    private String recipe_Category;
    private final static String DRAFT = "draft";
    private String image;

    public Recipe(){
        this.status = DRAFT;
        image = null;
    }

    public Recipe(String recipe_name, String cooking_steps, ArrayList<Ingredient> ingredients) {
        this.recipe_name = recipe_name;
        this.cooking_steps = cooking_steps;
        this.ingredients = ingredients;
        this.status = DRAFT;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public String RecipeToString(){
        String recipe = recipe_name+"\n" + "Ингредиенты:\n";
        for(Ingredient i: getIngredients()){
            recipe += i.ingredient_name + " " +i.amount+"\n";
        }
        recipe+="Приготовление:\n" + cooking_steps;
        return recipe;
    }

    public String getOwnerID() {
        return ownerID;
    }
    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
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


    public void setIngredients(ArrayList<Ingredient> ingredients){
        this.ingredients = ingredients;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getRecipe_Category() {
        return recipe_Category;
    }
    public void setRecipe_Category(String recipe_Category) {
        this.recipe_Category = recipe_Category;
    }

    public void addIngredientToList(Ingredient ingredient){
        this.getIngredients().add(ingredient);
    }

    public static String getDRAFT() {
        return DRAFT;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
