package com.example.cookeryfinal.user_related;

import com.example.cookeryfinal.recipe_related.DefaultRecipes;
import com.example.cookeryfinal.recipe_related.Recipe;

import java.util.ArrayList;

public class User {
    private String name, email, key;
    private ArrayList<String> my_recipes = new ArrayList<>();
    private ArrayList<Recipe> drafts = new ArrayList<>();
    private ArrayList<String> liked = new ArrayList<>();

    public User(){
    }

    public User(String name, String email){
        this.name = name;
        this.email = email;
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

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<String> getMy_recipes() {
        return my_recipes;
    }

    public ArrayList<Recipe> getDrafts() {
        return drafts;
    }

    public ArrayList<String> getLiked() {
        return liked;
    }
}
