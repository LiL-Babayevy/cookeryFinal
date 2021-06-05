package com.example.cookeryfinal.recipe_related;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cookeryfinal.GridSpacingItemDecoration;
import com.example.cookeryfinal.MainActivity;
import com.example.cookeryfinal.R;

import java.util.ArrayList;

public class RecipesByCategory extends AppCompatActivity implements SquareRecipeAdapter.OnRecipeListener{

    private SquareRecipeAdapter sqr_recipe_adapter;
    private ArrayList<Recipe> recipeArrayList;
    private GridLayoutManager mLayoutManager;
    private RecipeDataProvider provider;
    private RecyclerView recyclerView;
    private TextView categoryTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_by_category);
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");

        categoryTxt = findViewById(R.id.categoryTxt);
//        categoryTxt.setText(category);
        switch (category){
            case "завтрак":
                categoryTxt.setText("Завтраки");
                break;
            case "обед":
                categoryTxt.setText("Обеды");
                break;
            case "ужин":
                categoryTxt.setText("Ужины");
                break;
            case "десерт":
                categoryTxt.setText("Десерты");
                break;
        }

        recipeArrayList = new ArrayList<>();
        mLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        sqr_recipe_adapter = new SquareRecipeAdapter(recipeArrayList, this);

        OnRecipeRetrievedListener listener = new OnRecipeRetrievedListener() {
            @Override
            public void onRecipeRetrieved(ArrayList<Recipe> recipes) {
                recipeArrayList.clear();
                for(int i = recipes.size()-1; i>=0; i--){
                    if(recipes.get(i).getStatus().equals("my_recipes")) {
                        recipeArrayList.add(recipes.get(i));
                    }
                }
                sqr_recipe_adapter.notifyDataSetChanged();
            }
        };

        provider = RecipeDataProvider.getInstance();
        provider.getRecipesByCategory(listener, category);
        recyclerView = findViewById(R.id.recyclerView_CategoryRecipes);
        int spanCount = 2;
        int spacing = 40;
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(sqr_recipe_adapter);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipePage.class);
        intent.putExtra("clicked_recipe", recipe.getRecipeId());
        startActivity(intent);
    }

    @Override
    public void onRecipeLongClick(Recipe recipe) {

    }

    public void backArrowClicked(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        RecipesByCategory.this.finish();
    }
}