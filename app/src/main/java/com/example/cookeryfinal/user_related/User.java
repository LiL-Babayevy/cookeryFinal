package com.example.cookeryfinal.user_related;

import com.example.cookeryfinal.recipe_related.Recipe;

import java.util.ArrayList;

public class User {
    private String name, email, auth_key, database_key;
    private ArrayList<Recipe> my_recipes;
    private ArrayList<Recipe> drafts;
    private ArrayList<String> liked;
    private ArrayList<String> shoppingList = new ArrayList<>();

    public User(){
    }

    public User(String name, String email){
        this.name = name;
        this.email = email;
        my_recipes = new ArrayList<>();
        drafts = new ArrayList<>();
        liked = new ArrayList<>();
        shoppingList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuth_key() {
        return auth_key;
    }
    public void setAuth_key(String auth_key) {
        this.auth_key = auth_key;
    }

    public ArrayList<Recipe> getMy_recipes() {
        return my_recipes;
    }

    public void setMy_recipes(ArrayList<Recipe> my_recipes) {
        this.my_recipes = my_recipes;
    }

    public ArrayList<Recipe> getDrafts() {
        return drafts;
    }

    public ArrayList<String> getLiked() {
        return liked;
    }

    public String getDatabase_key() {
        return database_key;
    }
    public void setDatabase_key(String database_key) {
        this.database_key = database_key;
    }

    public ArrayList<String> getShoppingList() {
        return shoppingList;
    }
    public void setShoppingList(ArrayList<String> shoppingList) {
        this.shoppingList = shoppingList;
    }
}
