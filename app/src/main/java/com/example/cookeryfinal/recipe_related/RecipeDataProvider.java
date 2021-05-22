package com.example.cookeryfinal.recipe_related;

import androidx.annotation.NonNull;

import com.example.cookeryfinal.user_related.OnSingleUserRetrievedListener;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserDataProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecipeDataProvider {
    private FirebaseDatabase db;
    private DatabaseReference recipes_dr, recipe_push;


    private RecipeDataProvider() {
        db = FirebaseDatabase.getInstance();
        recipes_dr = db.getReference().child("Recipes");
        recipe_push = recipes_dr.push();

    }

    public DatabaseReference getRecipes_dr() {
        return recipes_dr;
    }

    private static RecipeDataProvider provider = new RecipeDataProvider();

    public static RecipeDataProvider getInstance() {
        return provider;
    }


//    public Recipe getRecipe(String key) {
//        ArrayList<Recipe> recipe_arrayList = new ArrayList<>();
//        OnRecipeRetrievedListener recipe_listener = new OnRecipeRetrievedListener() {
//            @Override
//            public void onRecipeRetrieved(ArrayList<Recipe> recipes) {
//                recipe_arrayList.clear();
//                recipe_arrayList.addAll(recipes);
//
//            }
//        };
//        getRecipes(recipe_listener);
//        for (Recipe recipe : recipe_arrayList){
//            if(recipe.getRecipeId().equals(key)){
//                return recipe;
//            }
//        }
//        return null;
//    }

    public void getMyRecipes(OnRecipeRetrievedListener listener) {
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
        Query query = recipes_dr.orderByChild("status").equalTo("my_recipes");
        UserDataProvider userdata = UserDataProvider.getInstance();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipeArrayList.clear();
                for (DataSnapshot dataRecipe : snapshot.getChildren()) {
                    Recipe recipe = dataRecipe.getValue(Recipe.class);
                    try {
                        if(recipe.getOwnerID().equals(userdata.getAuthUserKey())){
                            recipeArrayList.add(recipe);
                        }
                    }catch (NullPointerException e){
                        recipeArrayList.clear();
                    }
                }
                listener.onRecipeRetrieved(recipeArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getDraftRecipes(OnRecipeRetrievedListener listener) {
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
        Query query = recipes_dr.orderByChild("status").equalTo("draft");
        UserDataProvider userdata = UserDataProvider.getInstance();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipeArrayList.clear();
                for (DataSnapshot dataRecipe : snapshot.getChildren()) {
                    Recipe recipe = dataRecipe.getValue(Recipe.class);
                    try {
                        userdata.getUser(userdata.getAuthUserKey(), new OnSingleUserRetrievedListener() {
                            @Override
                            public void OnSingleUserRetrieved(User user) {
//                               if(recipe.getOwnerID().equals(userdata.getUsers().child(user.getDatabase_key()).getKey())){
//                                   recipeArrayList.add(recipe);
//                               }
                            }
                        });
                    }catch (NullPointerException e){
                        recipeArrayList.clear();
                    }
                }
                listener.onRecipeRetrieved(recipeArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getRecipes(OnRecipeRetrievedListener listener) {
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
        Query query = recipes_dr.orderByChild("status").equalTo("my_recipes");

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


    public void getRecipesByCategory(OnCategoryRecipesRetrievedListener listener, String category) {
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
        Query query = recipes_dr.orderByChild("recipe_Category").equalTo(category);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipeArrayList.clear();
                for (DataSnapshot dataRecipe : snapshot.getChildren()) {
                    Recipe recipe = dataRecipe.getValue(Recipe.class);
                    recipeArrayList.add(recipe);
                }
                listener.onCategoryRecipeRetrieved(recipeArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public Recipe getCurrentRecipe(String key, OnSingleRecipeRetrievedListener listener) {
        Query query = recipes_dr.orderByChild("recipeId").equalTo(key);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Recipe recipe = null;
                for (DataSnapshot dataSnapshot_recipe : snapshot.getChildren()) {
                    recipe = dataSnapshot_recipe.getValue(Recipe.class);
                    break;
                }
                listener.onSingleRecipeRetrieved(recipe);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return null;
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

    public void pushRecipe(Recipe recipe){
        recipe_push.setValue(recipe);
        String recipeId = recipe_push.getKey();
        recipe.setRecipeId(recipeId);
        recipe_push.setValue(recipe);
    }
}
