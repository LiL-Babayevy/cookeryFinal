package com.example.cookeryfinal.recipe_related;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookeryfinal.R;

public class RecipePage extends AppCompatActivity {
    private LinearLayout IngLayout;
    private RecipeDataProvider recipeDataProvider;
    private String recipe_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);
        Intent intent = getIntent();
        recipe_key = intent.getStringExtra("clicked_recipe");
        IngLayout = findViewById(R.id.ListOfIngredients);
        recipeDataProvider = RecipeDataProvider.getInstance();

        recipeDataProvider.getCurrentRecipe(recipe_key, new OnSingleRecipeRetrievedListener() {
            @Override
            public void onSingleRecipeRetrieved(Recipe retrieved_recipe) {
                try{
                    TextView recipe_title = findViewById(R.id.RecipePageTitle);
                    recipe_title.setText(retrieved_recipe.getRecipe_name());
                    TextView cookingSteps = findViewById(R.id.CookStepsTxt);
                    cookingSteps.setText(retrieved_recipe.getCooking_steps());
                    TextView category = findViewById(R.id.Category);
                    category.setText(retrieved_recipe.getRecipe_Category());

                    for(Ingredient ingredient: retrieved_recipe.getIngredients()) {
                        IngLayout.addView(addIngredient(ingredient));
                    }
                }catch(NullPointerException e){
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                    }

            }
        });
    }

    public View addIngredient(Ingredient ingredient){
        View ingredientView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.ingredient_frame, null);
        TextView ing_name = ingredientView.findViewById(R.id.IngredientName);
        TextView ing_amount = ingredientView.findViewById(R.id.IngredientAmount);
        ing_name.setText(ingredient.getIngredient_name());
        ing_amount.setText(ingredient.getAmount());
        return ingredientView;
    }
}
