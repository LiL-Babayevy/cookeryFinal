package com.example.cookeryfinal.recipe_related;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookeryfinal.R;
import com.example.cookeryfinal.user_related.OnSingleUserRetrievedListener;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserAuth;
import com.example.cookeryfinal.user_related.UserDataProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddRecipe extends AppCompatActivity {
    private UserDataProvider userDataProvider;
    private String recipeCategory = null;
    private User currentUser;
    private LinearLayout ingredients;
    private TextView plus;
    private Spinner category_spinner;
    private EditText cooking_steps;
    private ArrayList<Ingredient> ingredient_list = new ArrayList<>();
    private String[] categories = {"завтрак", "обед", "ужин", "десерт"};
    private Recipe recipe = new Recipe();
    private RecipeDataProvider recipeDataProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Добавление рецепта");
        setContentView(R.layout.activity_add_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recipeDataProvider = RecipeDataProvider.getInstance();

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
        category_spinner = findViewById(R.id.CategorySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        category_spinner.setAdapter(adapter);


        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = (String)parent.getItemAtPosition(position);
                recipe.setRecipe_Category(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        recipe.setCooking_steps(cooking_steps.getText().toString());

        recipe.setIngredients(new ArrayList<>());
        recipe.getIngredients().clear();
        for(int i = 0; i < ingredients.getChildCount(); i++){
            View frameIngredient = ingredients.getChildAt(i);
            EditText ingTitle = frameIngredient.findViewById(R.id.editIngredientName);
            String ingTitleStr = ingTitle.getText().toString();

            EditText ingAmount = frameIngredient.findViewById(R.id.editIngredientAmount);
            String ingAmountStr = ingAmount.getText().toString();

            if(!ingAmountStr.isEmpty() && !ingTitleStr.isEmpty()){
                Ingredient ingredient = new Ingredient(ingTitleStr, ingAmountStr);
                recipe.getIngredients().add(ingredient);
            }
        }
        userDataProvider.setUserDatabaseKey(recipe);
        recipeDataProvider.pushRecipe(recipe);
//        recipe.setRecipe_Category(getCategoryFromSpinner());
////        currentUser.getDrafts().add(recipe);
//        userDataProvider.getUser(userDataProvider.getAuthUserKey(), new OnSingleUserRetrievedListener() {
//            @Override
//            public void OnSingleUserRetrieved(User user) {
//                user.getDrafts().add(recipe);
//            }
//        });
//        userDataProvider.updateUser(currentUser);
    }

    public void btnPostClicked(View view){
        for(int i = 0; i < ingredients.getChildCount(); i++){
            View frameIngredient = ingredients.getChildAt(i);
            EditText ingTitle = frameIngredient.findViewById(R.id.editIngredientName);
            String ingTitleStr = ingTitle.getText().toString();
            checkIsEmpty(ingTitleStr, ingTitle);

            EditText ingAmount = frameIngredient.findViewById(R.id.editIngredientAmount);
            String ingAmountStr = ingAmount.getText().toString();
            checkIsEmpty(ingAmountStr, ingAmount);

            if(!ingAmountStr.isEmpty() && !ingTitleStr.isEmpty()){
                Ingredient ingredient = new Ingredient(ingTitleStr, ingAmountStr);
                ingredient_list.add(ingredient);
            }else{
                checkIsEmpty(ingTitleStr, ingTitle);
                checkIsEmpty(ingAmountStr, ingAmount);
            }
        }
        EditText recipeName = findViewById(R.id.addRecipeName);
        String recipeNameStr = recipeName.getText().toString();
        checkIsEmpty(recipeNameStr, recipeName);

        String cookStepsStr = cooking_steps.getText().toString();
        checkIsEmpty(cookStepsStr, cooking_steps);

        getCategoryFromSpinner();

        if(!recipeNameStr.isEmpty() && !cookStepsStr.isEmpty()){
            recipe.setRecipe_name(recipeNameStr);
            recipe.setCooking_steps(cookStepsStr);
            recipe.setIngredients(ingredient_list);
            userDataProvider.setUserDatabaseKey(recipe);
            recipe.setStatus("my_recipes");

            userDataProvider.getUser(userDataProvider.getAuthUserKey(), new OnSingleUserRetrievedListener() {
                @Override
                public void OnSingleUserRetrieved(User user) {
                    user.getMy_recipes().add(recipe);
                    userDataProvider.updateUser(user);
                }
            });
            recipeDataProvider.pushRecipe(recipe);

        }else{
            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_LONG).show();
        }

    }

    public void getCategoryFromSpinner(){

    }


    public void checkIsEmpty(String s, EditText edit){
        if(s.isEmpty()){
            edit.setError("заполните поле");
        }
    }
}
