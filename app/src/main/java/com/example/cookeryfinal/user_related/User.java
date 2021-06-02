package com.example.cookeryfinal.user_related;

import com.example.cookeryfinal.ShoppingItem;
import com.example.cookeryfinal.recipe_related.Recipe;

import java.util.ArrayList;

public class User {
    private String name, email, database_key, user_password;
    private ArrayList<String> liked;
    private ArrayList<ShoppingItem> shoppingList;
    private int image;

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


    public void setLiked(ArrayList<String> liked) {
        this.liked = liked;
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

    public ArrayList<ShoppingItem> getShoppingList() {
        return shoppingList;
    }
    public void setShoppingList(ArrayList<ShoppingItem> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }

    public String getUser_password() {
        return user_password;
    }
    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
}
