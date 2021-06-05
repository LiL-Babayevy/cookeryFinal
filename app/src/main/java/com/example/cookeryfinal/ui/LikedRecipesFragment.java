package com.example.cookeryfinal.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookeryfinal.GridSpacingItemDecoration;
import com.example.cookeryfinal.LogIn;
import com.example.cookeryfinal.R;
import com.example.cookeryfinal.Register;
import com.example.cookeryfinal.recipe_related.AddRecipe;
import com.example.cookeryfinal.recipe_related.OnRecipeRetrievedListener;
import com.example.cookeryfinal.recipe_related.Recipe;
import com.example.cookeryfinal.recipe_related.RecipeDataProvider;
import com.example.cookeryfinal.recipe_related.RecipePage;
import com.example.cookeryfinal.recipe_related.SquareRecipeAdapter;
import com.example.cookeryfinal.user_related.OnSingleUserRetrievedListener;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserAuth;
import com.example.cookeryfinal.user_related.UserDataProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class LikedRecipesFragment extends Fragment implements SquareRecipeAdapter.OnRecipeListener {

    private View root;
    private SquareRecipeAdapter sqr_recipe_adapter;
    private ArrayList<Recipe> liked_recipes;
    private GridLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private RecipeDataProvider provider;
    private UserDataProvider user_data_provider;
    private UserAuth userAuth;
    private User user;

    public LikedRecipesFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userAuth = new UserAuth(getContext());
        user = userAuth.getSignedInUser();
        user_data_provider = UserDataProvider.getInstance();
        if (user == null) {
            root = inflater.inflate(R.layout.blank_fragments, container, false);
            TextView logInTxt = root.findViewById(R.id.logInText);
            logInTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent Intent = new Intent(getContext(), LogIn.class);
                    startActivity(Intent);
                }
            });
            TextView registerTxt = root.findViewById(R.id.registerText);
            registerTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent Intent = new Intent(getContext(), Register.class);
                    startActivity(Intent);
                }
            });
        } else {
            root = inflater.inflate(R.layout.fragment_liked_recipes, container, false);

            liked_recipes = new ArrayList<>();
            mLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
            sqr_recipe_adapter = new SquareRecipeAdapter(liked_recipes, this);

            OnRecipeRetrievedListener listener = new OnRecipeRetrievedListener() {
                @Override
                public void onRecipeRetrieved(ArrayList<Recipe> recipes) {
                    liked_recipes.clear();
                    for(int i = recipes.size()-1; i>=0; i--) {
                        liked_recipes.add(recipes.get(i));
                    }
                    sqr_recipe_adapter.notifyDataSetChanged();
                }
            };
            provider = RecipeDataProvider.getInstance();
            provider.getLikedRecipes(user.getLiked(), listener);
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userAuth = new UserAuth(getContext());
        user = userAuth.getSignedInUser();
        if(user != null) {
            recyclerView = root.findViewById(R.id.recyclerView_likedRecipes);
            int spanCount = 2;
            int spacing = 40;
            boolean includeEdge = true;
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(sqr_recipe_adapter);
        }

    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(getContext(), RecipePage.class);
        intent.putExtra("clicked_recipe", recipe.getRecipeId());
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRecipeLongClick(Recipe recipe) {
        AlertDialog.Builder liked_dialog = new AlertDialog.Builder(getContext());
        liked_dialog.setTitle(recipe.getRecipe_name());
        liked_dialog.setView(R.layout.dialog_delete_from_fav);
        liked_dialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user_data_provider = UserDataProvider.getInstance();
                user.getLiked().remove(recipe.getRecipeId());
                liked_recipes.remove(recipe);
                sqr_recipe_adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "рецепт удален из 'Понравившихся'!", Toast.LENGTH_SHORT).show();
            }
        });
        liked_dialog.setNeutralButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = liked_dialog.create();
        alertDialog.show();
    }
}