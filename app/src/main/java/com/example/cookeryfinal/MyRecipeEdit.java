package com.example.cookeryfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookeryfinal.recipe_related.Ingredient;
import com.example.cookeryfinal.recipe_related.OnSingleRecipeRetrievedListener;
import com.example.cookeryfinal.recipe_related.Recipe;
import com.example.cookeryfinal.recipe_related.RecipeDataProvider;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserDataProvider;

import java.util.ArrayList;

public class MyRecipeEdit extends AppCompatActivity {

    private Button saveChanges;
    private EditText editName;
    private EditText editCookingSteps;
    private RecipeDataProvider recipeDataProvider;
    private UserDataProvider userDataProvider;
    private String recipe_key;
    private Spinner category_spinner;
    private LinearLayout ingredientList;
    private ArrayList<Ingredient> ingredients;
    private String[] categories = {"завтрак", "обед", "ужин", "десерт"};
    private TextView plus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe_edit);
        Intent intent = getIntent();
        recipe_key = intent.getStringExtra("recipe_key");
        recipeDataProvider = RecipeDataProvider.getInstance();
        editCookingSteps = findViewById(R.id.editCookingSteps);
        editName = findViewById(R.id.editRecipeName);
        userDataProvider = UserDataProvider.getInstance();
        ingredientList = findViewById(R.id.EditIngredientList);
        saveChanges = findViewById(R.id.SaveChanges);

        plus = findViewById(R.id.PlusIngredient);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient();
            }
        });

        category_spinner = findViewById(R.id.editCategorySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        category_spinner.setAdapter(adapter);

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeDataProvider.getCurrentRecipe(recipe_key, new OnSingleRecipeRetrievedListener() {
                    @Override
                    public void onSingleRecipeRetrieved(Recipe retrieved_recipe) {
                        try {
                            String name = editName.getText().toString();
                            String cookingSteps = editCookingSteps.getText().toString();
                            retrieved_recipe.getIngredients().clear();
                            for(int i = 0; i < ingredientList.getChildCount(); i++){
                                View frameIngredient = ingredientList.getChildAt(i);
                                EditText ingTitle = frameIngredient.findViewById(R.id.editIngredientName);
                                String ingTitleStr = ingTitle.getText().toString();
                                checkIsEmpty(ingTitleStr, ingTitle);

                                EditText ingAmount = frameIngredient.findViewById(R.id.editIngredientAmount);
                                String ingAmountStr = ingAmount.getText().toString();
                                checkIsEmpty(ingAmountStr, ingAmount);

                                if(!ingAmountStr.isEmpty() && !ingTitleStr.isEmpty()){
                                    Ingredient ingredient = new Ingredient(ingTitleStr, ingAmountStr);
                                    retrieved_recipe.getIngredients().add(ingredient);
                                    category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            String category = (String)parent.getItemAtPosition(position);
                                            retrieved_recipe.setRecipe_Category(category);
                                        }
                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                }else{
                                    checkIsEmpty(ingTitleStr, ingTitle);
                                    checkIsEmpty(ingAmountStr, ingAmount);
                                }
                                if(!name.isEmpty() && !cookingSteps.isEmpty()){
                                    recipeDataProvider.getRecipes_dr().child(recipe_key).child("recipe_name").setValue(name);
                                    recipeDataProvider.getRecipes_dr().child(recipe_key).child("cooking_steps").setValue(cookingSteps);
                                    recipeDataProvider.getRecipes_dr().child(recipe_key).child("category").setValue(retrieved_recipe.getRecipe_Category());
                                    recipeDataProvider.getRecipes_dr().child(recipe_key).child("ingredients").setValue(retrieved_recipe.getIngredients());
                                }

                            }
                        }catch (NullPointerException e){
                            Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        recipeDataProvider.getCurrentRecipe(recipe_key, new OnSingleRecipeRetrievedListener() {
            @Override
            public void onSingleRecipeRetrieved(Recipe retrieved_recipe) {
                try {
                    editName.setText(retrieved_recipe.getRecipe_name());
                    editCookingSteps.setText(retrieved_recipe.getCooking_steps());
                    for(Ingredient ingredient: retrieved_recipe.getIngredients()){
                        showIngredient(ingredient);
                    }
                }catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void showIngredient(Ingredient current){
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.edit_ingredient, null);
        EditText name = view.findViewById(R.id.editIngredientName);
        name.setText(current.getIngredient_name());
        EditText amount = view.findViewById(R.id.editIngredientAmount);
        amount.setText(current.getAmount());
        ingredientList.addView(view);
    }

    public void checkIsEmpty(String s, EditText edit){
        if(s.isEmpty()){
            edit.setError("заполните поле");
        }
    }


    public void addIngredient(){
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.edit_ingredient, null);
        EditText name = view.findViewById(R.id.editIngredientName);
        name.setHint("ingredient");
         ingredientList.addView(view);
    }
}