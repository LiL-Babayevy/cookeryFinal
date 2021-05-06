package com.example.cookeryfinal.recipe_related;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecipeDataProvider {
    private FirebaseDatabase db;
    private DatabaseReference recipes_dr;


    private RecipeDataProvider() {
        db = FirebaseDatabase.getInstance();
        recipes_dr = db.getReference().child("Recipes");

    }

    private static RecipeDataProvider provider = new RecipeDataProvider();

    public static RecipeDataProvider getInstance() {
        return provider;
    }


    public Recipe getRecipe(String key) {
        ArrayList<Recipe> recipe_arrayList = new ArrayList<>();
        OnRecipeRetrievedListener recipe_listener = new OnRecipeRetrievedListener() {
            @Override
            public void onRecipeRetrieved(ArrayList<Recipe> recipes) {
                recipe_arrayList.clear();
                recipe_arrayList.addAll(recipes);
            }
        };
        getRecipes(recipe_listener);
        for (Recipe recipe : recipe_arrayList){
            if(recipe.getRecipeId() == key){
                return recipe;
            }
        }
        return null;
    }

    public void getRecipes(OnRecipeRetrievedListener listener) {
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
        Query query = recipes_dr;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipeArrayList.clear();
                for (DataSnapshot dataRecipe : snapshot.getChildren()) {
                    Recipe recipe = dataRecipe.getValue(Recipe.class);
                    recipeArrayList.add(recipe);
                }
                listener.onRecipeRetrieved(recipeArrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    public User getCurrentUserByKey(){
//        User user = getUser(userAuth.getmAuth().getCurrentUser().getUid());
//        return user;
//    }

    public void updateRecipe(Recipe r) {
        recipes_dr.child(r.getRecipeId()).setValue(r);

    }

    public void deleteRecipe(Recipe r) {
        recipes_dr.child(r.getRecipeId()).removeValue();
    }
}
