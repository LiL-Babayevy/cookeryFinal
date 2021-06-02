package com.example.cookeryfinal;

public class LikedRecipe {
    private String recipe_id;
    private int status_liked; //if status = 0, recipe is liked; if status = 2, recipe is not liked

    public LikedRecipe(){}
    public LikedRecipe(String recipe_id, int status_liked){
        this.recipe_id = recipe_id;
        this.status_liked = status_liked;
    }

    public String getRecipe_id() {
        return recipe_id;
    }
    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public int getStatus_liked() {
        return status_liked;
    }
    public void setStatus_liked(int status_liked) {
        this.status_liked = status_liked;
    }
}
