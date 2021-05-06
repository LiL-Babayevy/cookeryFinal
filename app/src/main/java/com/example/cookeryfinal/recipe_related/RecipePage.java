package com.example.cookeryfinal.recipe_related;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookeryfinal.R;

public class RecipePage extends AppCompatActivity {
    IngredientAdapter adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);
        DefaultRecipes.init();
        TextView recipeName = findViewById(R.id.RecipePageTitle);
        TextView cookingSteps = findViewById(R.id.CookStepsTxt);
        cookingSteps.setText(DefaultRecipes.defaultrecipes.get(1).getCooking_steps());
        recipeName.setText(DefaultRecipes.defaultrecipes.get(1).getRecipe_name());
//        ListView ingredientListView = findViewById(R.id.IngredientListView);
//        IngredientAdapter adapter = new IngredientAdapter(this, R.layout.ingredient,
//                DefaultRecipes.defaultrecipes.get(1).getIngredients());
//        ingredientListView.setAdapter(adapter);
//        int min = DefaultRecipes.defaultrecipes.get(1).getIngredients().size()*30;
//        ingredientListView.setMinimumHeight(min);
        // LinearLayout line = findViewById(R.id.layoutLine);


        LinearLayout linlay = findViewById(R.id.LinLayIng);
        for(Ingredient i: DefaultRecipes.defaultrecipes.get(2).getIngredients()){
            FrameLayout fr = new FrameLayout(this);
            TextView name = new TextView(this);
            name.setGravity(Gravity.LEFT);
            name.setText(i.getIngredient_name());
            TextView amount = new TextView(this);
            amount.setGravity(Gravity.RIGHT);
            amount.setText(i.getAmount());
            fr.addView(name);
            fr.addView(amount);
            linlay.addView(fr);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void copyRecipe(View v){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", DefaultRecipes.defaultrecipes.get(2).RecipeToString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show();
    }

    public void addToLikedRecipes(View v){
        Toast.makeText(this, "Добавлен в понравившиеся", Toast.LENGTH_SHORT).show();
    }
}
