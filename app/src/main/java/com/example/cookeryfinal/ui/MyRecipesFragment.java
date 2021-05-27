package com.example.cookeryfinal.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookeryfinal.GridSpacingItemDecoration;
import com.example.cookeryfinal.LogIn;
import com.example.cookeryfinal.recipe_related.MyRecipeEdit;
import com.example.cookeryfinal.R;
import com.example.cookeryfinal.Register;
import com.example.cookeryfinal.recipe_related.AddRecipe;
import com.example.cookeryfinal.recipe_related.OnRecipeRetrievedListener;
import com.example.cookeryfinal.recipe_related.Recipe;
import com.example.cookeryfinal.recipe_related.RecipeDataProvider;
import com.example.cookeryfinal.recipe_related.RecipePage;
import com.example.cookeryfinal.recipe_related.SquareRecipeAdapter;
import com.example.cookeryfinal.user_related.UserAuth;
import com.example.cookeryfinal.user_related.UserDataProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyRecipesFragment extends Fragment implements SquareRecipeAdapter.OnRecipeListener {

    public MyRecipesFragment(){}

    private View root;
    private SquareRecipeAdapter sqr_recipe_adapter;
    private ArrayList<Recipe> recipeArrayList;
    private GridLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private RecipeDataProvider provider;
    private UserAuth userAuth;
    private UserDataProvider userDataProvider;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userAuth = UserAuth.getInstance();
        if(userAuth.getCurrentUser() == null){
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
        }else {
            root = inflater.inflate(R.layout.fragment_my_recipes, container, false);
            FloatingActionButton fab = root.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), AddRecipe.class);
                    startActivity(intent);
                }
            });

            recipeArrayList = new ArrayList<>();
            mLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
            sqr_recipe_adapter = new SquareRecipeAdapter(recipeArrayList, this);

            OnRecipeRetrievedListener listener = new OnRecipeRetrievedListener() {
                @Override
                public void onRecipeRetrieved(ArrayList<Recipe> recipes) {
                    recipeArrayList.clear();
                    recipeArrayList.addAll(recipes);
                    sqr_recipe_adapter.notifyDataSetChanged();
                }
            };

            provider = RecipeDataProvider.getInstance();
            provider.getMyRecipes(listener);
        }
        return root;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userAuth = UserAuth.getInstance();
        if(userAuth.getCurrentUser() != null) {
            recyclerView = root.findViewById(R.id.recyclerView_myRecipes);
            int spanCount = 2; // 3 columns
            int spacing = 40; // 50px
            boolean includeEdge = true;
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(sqr_recipe_adapter);
        }

    }

    @Override
    public void onRecipeClick(int position) {
        Recipe recipe = recipeArrayList.get(position);
        Intent intent = new Intent(getContext(), RecipePage.class);
        intent.putExtra("clicked_recipe", recipe.getRecipeId());
        startActivity(intent);
    }

    @Override
    public void onRecipeLongClick(int position) {
        Recipe recipe = recipeArrayList.get(position);
        AlertDialog.Builder recipe_dialog = new AlertDialog.Builder(getContext());
        recipe_dialog.setTitle(recipe.getRecipe_name());
        recipe_dialog
                .setPositiveButton("Редактировать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), MyRecipeEdit.class);
                        intent.putExtra("recipe_key", recipe.getRecipeId());
                        startActivity(intent);
                        }

                    });
        recipe_dialog.setNeutralButton("Удалить", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder recipe_delete_dialog = new AlertDialog.Builder(getContext());
                recipe_delete_dialog.setTitle(recipe.getRecipe_name());
                recipe_delete_dialog.setView(R.layout.dialog_delete);
                recipe_delete_dialog
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                provider.deleteRecipe(recipe);
//                            alertDialog.cancel();
                            }

                        });
                recipe_delete_dialog.setNeutralButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog2 = recipe_delete_dialog.create();
                alertDialog2.show();
            }

        });
        AlertDialog alertDialog = recipe_dialog.create();
        alertDialog.show();
    }
}