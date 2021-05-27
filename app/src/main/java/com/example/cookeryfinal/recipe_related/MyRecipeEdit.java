package com.example.cookeryfinal.recipe_related;

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

import com.example.cookeryfinal.R;
import com.example.cookeryfinal.recipe_related.Ingredient;
import com.example.cookeryfinal.recipe_related.OnSingleRecipeRetrievedListener;
import com.example.cookeryfinal.recipe_related.Recipe;
import com.example.cookeryfinal.recipe_related.RecipeDataProvider;
import com.example.cookeryfinal.recipe_related.RecipePage;
import com.example.cookeryfinal.ui.MyRecipesFragment;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserDataProvider;

import java.util.ArrayList;

public class MyRecipeEdit extends AppCompatActivity {

    private Button saveChanges;
    private EditText editName;
    private EditText editCookingSteps;
    private RecipeDataProvider recipeDataProvider;
    private UserDataProvider userDataProvider;
    private String recipe_key, updated_name, updated_cookingSteps;
    private Spinner category_spinner;
    private LinearLayout ingredientList;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
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

    public void btnSaveChangesClicked(View v){
        int size = ingredientList.getChildCount();
        recipeDataProvider.getCurrentRecipe(recipe_key, new OnSingleRecipeRetrievedListener() {
            @Override
            public void onSingleRecipeRetrieved(Recipe retrieved_recipe) {
                updated_name = editName.getText().toString();
                updated_cookingSteps = editCookingSteps.getText().toString();
                for(int i = 0; i < size; i++){
                    View frameIngredient = ingredientList.getChildAt(i);
                    EditText ingTitle = frameIngredient.findViewById(R.id.editIngredientName);
                    String ingTitleStr = ingTitle.getText().toString();

                    EditText ingAmount = frameIngredient.findViewById(R.id.editIngredientAmount);
                    String ingAmountStr = ingAmount.getText().toString();

//                                ingredients.clear();
                    if(!ingAmountStr.isEmpty() && !ingTitleStr.isEmpty()){
                        Ingredient ingredient = new Ingredient(ingTitleStr, ingAmountStr);
//                        if(!retrieved_recipe.getIngredients().isEmpty()) {
//                            for (Ingredient ingredient1 : retrieved_recipe.getIngredients()) {
//                                if(!ingredient1.getIngredient_name().equals(ingredient.getIngredient_name())
//                                        && !ingredient1.getAmount().equals(ingredient.getAmount())) {
//                                    retrieved_recipe.addIngredientToList(ingredient);
//                                    break;
//                                }
//                            }
//                        }else{
//                            retrieved_recipe.addIngredientToList(ingredient);
//                        }

                        ingredients.add(ingredient);

                    }else{
                        checkIsEmpty(ingTitleStr, ingTitle);
                        checkIsEmpty(ingAmountStr, ingAmount);
                        break;
                    }
                }
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

                if(!updated_name.isEmpty() && !updated_cookingSteps.isEmpty()){
                    retrieved_recipe.setRecipe_name(updated_name);
                    retrieved_recipe.setCooking_steps(updated_cookingSteps);

//                                    retrieved_recipe.getIngredients().clear();
//                                    recipeDataProvider.getRecipes_dr().child(recipe_key).child("recipe_name").setValue(name);
//                                    recipeDataProvider.getRecipes_dr().child(recipe_key).child("cooking_steps").setValue(cookingSteps);
//                                    recipeDataProvider.getRecipes_dr().child(recipe_key).child("category").setValue(retrieved_recipe.getRecipe_Category());
//                                    recipeDataProvider.getRecipes_dr().child(recipe_key).child("ingredients").setValue(ingredients);
                }
                retrieved_recipe.setIngredients(ingredients);
                recipeDataProvider.updateRecipe(retrieved_recipe);
            }

        });
        Intent intent1 = new Intent(getApplicationContext(), RecipePage.class);
//        Intent intent1 = new Intent(getApplicationContext(), MyRecipesFragment.class);
        intent1.putExtra("clicked_recipe", recipe_key);
        startActivity(intent1);
    }




}