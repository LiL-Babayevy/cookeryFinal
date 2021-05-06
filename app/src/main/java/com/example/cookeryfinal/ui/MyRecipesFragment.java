package com.example.cookeryfinal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookeryfinal.R;
import com.example.cookeryfinal.recipe_related.AddRecipe;
import com.example.cookeryfinal.recipe_related.OnRecipeRetrievedListener;
import com.example.cookeryfinal.recipe_related.Recipe;
import com.example.cookeryfinal.recipe_related.RecipeDataProvider;
import com.example.cookeryfinal.recipe_related.SquareRecipeAdapter;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserAuth;
import com.example.cookeryfinal.user_related.UserDataProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyRecipesFragment extends Fragment {

    public MyRecipesFragment(){}

    private View root;
    private SquareRecipeAdapter sqr_recipe_adapter;
    private ArrayList<Recipe> recipeArrayList;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_my_recipes, container, false);
//        final TextView textView = root.findViewById(R.id.text_gallery);
        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddRecipe.class);
                startActivity(intent);
            }
        });

        recipeArrayList = new ArrayList<>();
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView = root.findViewById(R.id.recyclerView_myRecipes);
        sqr_recipe_adapter = new SquareRecipeAdapter(recipeArrayList);
        recyclerView.setAdapter(sqr_recipe_adapter);
        recyclerView.setLayoutManager(mLayoutManager);

        OnRecipeRetrievedListener listener = new OnRecipeRetrievedListener() {
            @Override
            public void onRecipeRetrieved(ArrayList<Recipe> recipes) {
                recipeArrayList.clear();
                recipeArrayList.addAll(recipes);
                sqr_recipe_adapter.notifyDataSetChanged();
            }
        };

        RecipeDataProvider provider = RecipeDataProvider.getInstance();
        provider.getRecipes(listener);

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sqr_recipe_adapter.setClickListener();
    }
}