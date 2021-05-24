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
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookeryfinal.GridSpacingItemDecoration;
import com.example.cookeryfinal.LogIn;
import com.example.cookeryfinal.MyRecipeEdit;
import com.example.cookeryfinal.R;
import com.example.cookeryfinal.Register;
import com.example.cookeryfinal.recipe_related.OnRecipeRetrievedListener;
import com.example.cookeryfinal.recipe_related.Recipe;
import com.example.cookeryfinal.recipe_related.RecipeDataProvider;
import com.example.cookeryfinal.recipe_related.SquareRecipeAdapter;
import com.example.cookeryfinal.user_related.UserAuth;

import java.util.ArrayList;

public class DraftsFragment extends Fragment implements SquareRecipeAdapter.OnRecipeListener{

    private UserAuth userAuth;
    private View root;
    private SquareRecipeAdapter sqr_recipe_adapter;
    private ArrayList<Recipe> recipeArrayList;
    private GridLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private RecipeDataProvider provider;

    public DraftsFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userAuth = UserAuth.getInstance();
        if(userAuth.getCurrentUser() != null){
            root = inflater.inflate(R.layout.fragment_drafts, container, false);


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
            provider.getDraftRecipes(listener);

        }else{
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
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userAuth = UserAuth.getInstance();
        if(userAuth.getCurrentUser() != null) {
            recyclerView = root.findViewById(R.id.recyclerView_Drafts);
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
        Intent intent = new Intent(getContext(), MyRecipeEdit.class);
        intent.putExtra("recipe_key", recipe.getRecipeId());
        startActivity(intent);
    }

    @Override
    public void onRecipeLongClick(int position) {

    }
}