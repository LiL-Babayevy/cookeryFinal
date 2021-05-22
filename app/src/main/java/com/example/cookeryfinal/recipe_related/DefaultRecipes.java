package com.example.cookeryfinal.recipe_related;


import java.util.ArrayList;

public class DefaultRecipes {
    public static final ArrayList<Recipe> defaultrecipes = new ArrayList<>();
    public static void init(){
        defaultrecipes.clear();

        Ingredient oats = new Ingredient("oats", "30 g");
        Ingredient egg = new Ingredient("egg", "1");
        Ingredient milk = new Ingredient("milk", "30 tbsp");
        ArrayList<Ingredient> oatcrpIng = new ArrayList<>();
        oatcrpIng.add(oats);
        oatcrpIng.add(egg);
        oatcrpIng.add(milk);
        String cook =
                "Слегка перемолоть овсяные хлопья\n" +
                        "Перемешать все ингредиенты\n" +
                        "Вылить тесто на разогретую сковороду и жарить на среднем огне с двух сторон";
        Recipe oatCrepe = new Recipe("oatCrepe", cook, oatcrpIng);
        oatCrepe.setRecipeId("1");
        DefaultRecipes.defaultrecipes.add(oatCrepe);


        Ingredient manka = new Ingredient("манка", "5 tbsp");
        Ingredient milk1 = new Ingredient("milk", "2 cups");
        Ingredient water = new Ingredient("water", "1 cup");
        ArrayList<Ingredient> mannayakashaIng = new ArrayList<>();
        mannayakashaIng.add(manka);
        mannayakashaIng.add(milk1);
        mannayakashaIng.add(water);
        String steps = "В кастрюлю налить молоко, воду и засыпать манку\n" +
                "Поставить на сильный огонь, довести до кипения, уменьшить огонь\n" +
                "Добавить сахар и варить до желаемой густоты\n" +
                "готово!";
        Recipe porridge = new Recipe("mannaya kasha", steps, mannayakashaIng);
        porridge.setRecipeId("2");
        DefaultRecipes.defaultrecipes.add(porridge);

        Ingredient babana = new Ingredient("banana", "1");
        Ingredient milk3 = new Ingredient("milk", "1/3 cup");
        Ingredient strawberries = new Ingredient("strawberries", "100 g");
        ArrayList<Ingredient> smoothieIng = new ArrayList<>();
        smoothieIng.add(babana);
        smoothieIng.add(strawberries);
        smoothieIng.add(milk3);
        String smoothieCook = "перемолоть все ингредиенты в миктере. Готово!";
        Recipe smoothie = new Recipe();
        smoothie.setCooking_steps(smoothieCook);
        smoothie.setRecipeId("3");
        DefaultRecipes.defaultrecipes.add(smoothie);
    }
}
