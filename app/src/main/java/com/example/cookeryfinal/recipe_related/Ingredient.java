package com.example.cookeryfinal.recipe_related;

public class Ingredient {
    String ingredient_name;
    String amount;

    public Ingredient(){

    }

    public Ingredient(String ingredient_name, String amount) {
        this.ingredient_name = ingredient_name;
        this.amount = amount;
    }

    public String getIngredient_name() {
        return ingredient_name;
    }
    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
}
