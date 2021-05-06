package com.example.cookeryfinal.recipe_related;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookeryfinal.R;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserDataProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddRecipe extends AppCompatActivity {
    private UserDataProvider userDataProvider;
    private User currentUser;
    private LinearLayout ingredients;
    private TextView plus;
    private EditText cooking_steps;
    private ArrayList<Ingredient> ingredient_list = new ArrayList<>();
    private ArrayList<View> frameList = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference recipes = database.getReference("Recipes");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Добавление рецепта");
        setContentView(R.layout.activity_add_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ingredients = findViewById(R.id.addPageIngredientList);
        ingredients.addView(addIngredient());
        plus = findViewById(R.id.PlusTextView);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredientToList();
            }
        });

        cooking_steps = findViewById(R.id.addCookingSteps);

        userDataProvider = UserDataProvider.getInstance();
        currentUser = userDataProvider.getCurrentUserByKey();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void addIngredientToList(){
        ingredients.addView(addIngredient());
    }

    public View addIngredient(){
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.edit_ingredient, null);
        EditText name = view.findViewById(R.id.editIngredientName);
        name.setHint("ingredient");
        return view;
    }

    public void btnSaveClicked(View view){
        Recipe recipe = new Recipe();
        recipe.setCooking_steps(cooking_steps.getText().toString());
        currentUser.getDrafts().add(recipe);
        userDataProvider.updateUser(currentUser);
    }

    public void btnPostClicked(View view){
        for(int i = 0; i < ingredients.getChildCount(); i++){
            View frameIngredient = ingredients.getChildAt(i);
            EditText ingTitle = frameIngredient.findViewById(R.id.editIngredientName);
            String ingTitleStr = ingTitle.getText().toString();
            EditText ingAmount = frameIngredient.findViewById(R.id.editIngredientAmount);
            String ingAmountStr = ingAmount.getText().toString();
            Ingredient ingredient = new Ingredient(ingTitleStr, ingAmountStr);
            ingredient_list.add(ingredient);
        }
        EditText recipeName = findViewById(R.id.addRecipeName);
        String recipeNameStr = recipeName.getText().toString();
        String cookStepsStr = cooking_steps.getText().toString();
        Recipe recipe = new Recipe(recipeNameStr, cookStepsStr, ingredient_list);
        DatabaseReference push = recipes.push();
        push.setValue(recipe);
    }
}
