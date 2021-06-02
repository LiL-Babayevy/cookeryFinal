package com.example.cookeryfinal.recipe_related;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookeryfinal.R;
import com.example.cookeryfinal.user_related.UserAuth;
import com.example.cookeryfinal.user_related.UserDataProvider;

import java.io.InputStream;
import java.util.ArrayList;

public class MyRecipeEdit extends AppCompatActivity {

    private TextView postChanges;
    private EditText editName;
    private EditText editCookingSteps;
    private ImageView recipe_image;
    private RecipeDataProvider recipeDataProvider;
    private String recipe_key, updated_name, updated_cookingSteps;
    private Spinner category_spinner;
    private LinearLayout ingredientList;
    private Recipe recipe = new Recipe();
    private UserAuth userAuth;

    private String[] categories = {"завтрак", "обед", "ужин", "десерт"};
    private TextView plus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        Intent intent = getIntent();
        recipe_key = intent.getStringExtra("recipe_key");
        recipeDataProvider = RecipeDataProvider.getInstance();
        editCookingSteps = findViewById(R.id.addCookingSteps);
        editName = findViewById(R.id.addRecipeName);
        ingredientList = findViewById(R.id.addPageIngredientList);
        postChanges = findViewById(R.id.addRecipePostBtn);
        recipe_image = findViewById(R.id.imageView);
        userAuth = new UserAuth(this);


        plus = findViewById(R.id.PlusTextView);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient();
            }
        });

        category_spinner = findViewById(R.id.CategorySpinner);
        category_spinner.setPrompt(recipe.getRecipe_Category());
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

        recipeDataProvider.getCurrentRecipe(recipe_key, new OnSingleRecipeRetrievedListener() {
            @Override
            public void onSingleRecipeRetrieved(Recipe retrieved_recipe) {
                try {
                    editName.setText(retrieved_recipe.getRecipe_name());
                    editCookingSteps.setText(retrieved_recipe.getCooking_steps());
                    ingredientList.removeAllViews();
                    for(Ingredient ingredient: retrieved_recipe.getIngredients()){
                        showIngredient(ingredient);
                    }
                    if(retrieved_recipe.getImage() != null){
                        byte[] decodedString = Base64.decode(retrieved_recipe.getImage(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString,0, decodedString.length);
                        recipe_image.setImageBitmap(bitmap);
                    }

                }catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(), "failed My REcipe Edit", Toast.LENGTH_LONG).show();
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

    public void btnPostClicked(View v){
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        int size = ingredientList.getChildCount();

        updated_name = editName.getText().toString();
        updated_cookingSteps = editCookingSteps.getText().toString();
        recipe.setRecipe_name(updated_name);
        recipe.setCooking_steps(updated_cookingSteps);
        recipe.setRecipeId(recipe_key);
        recipe.setOwnerID(userAuth.getSignedInUserKey());
        recipe.setStatus("my_recipes");
        recipe.setImage(recipe.getImage());

        for(int i = 0; i < size; i++){
            View frameIngredient = ingredientList.getChildAt(i);
            EditText ingTitle = frameIngredient.findViewById(R.id.editIngredientName);
            String ingTitleStr = ingTitle.getText().toString();

            EditText ingAmount = frameIngredient.findViewById(R.id.editIngredientAmount);
            String ingAmountStr = ingAmount.getText().toString();
            if(!ingAmountStr.isEmpty() && !ingTitleStr.isEmpty()){
                Ingredient ingredient = new Ingredient(ingTitleStr, ingAmountStr);
                ingredients.add(ingredient);
            }
            else{
                checkIsEmpty(ingTitleStr, ingTitle);
                checkIsEmpty(ingAmountStr, ingAmount);
                break;
            }
        }
        recipe.setIngredients(ingredients);
        recipeDataProvider.updateRecipe(recipe);
        MyRecipeEdit.this.finish();
    }

    public void btnSaveClicked(View v){
        recipe.setCooking_steps(editCookingSteps.getText().toString());
        EditText recipeName = findViewById(R.id.addRecipeName);
        String recipeNameStr = recipeName.getText().toString();
        recipe.setRecipe_name(recipeNameStr);
        recipe.setRecipeId(recipe_key);
        recipe.setIngredients(new ArrayList<>());
        recipe.setStatus("draft");
        recipe.setImage(recipe.getImage());
        recipe.getIngredients().clear();
        for(int i = 0; i < ingredientList.getChildCount(); i++){
            View frameIngredient = ingredientList.getChildAt(i);
            EditText ingTitle = frameIngredient.findViewById(R.id.editIngredientName);
            String ingTitleStr = ingTitle.getText().toString();

            EditText ingAmount = frameIngredient.findViewById(R.id.editIngredientAmount);
            String ingAmountStr = ingAmount.getText().toString();

            if(!ingAmountStr.isEmpty() && !ingTitleStr.isEmpty()){
                Ingredient ingredient = new Ingredient(ingTitleStr, ingAmountStr);
                recipe.getIngredients().add(ingredient);
            }
        }
        recipe.setOwnerID(userAuth.getSignedInUser().getDatabase_key());
        recipeDataProvider.updateRecipe(recipe);
        MyRecipeEdit.this.finish();
    }

    public void setImage(View v){
        String[] options = new String[]{"Сделать фото", "Загрузить из галереи"};
        AlertDialog.Builder image_set_dialog = new AlertDialog.Builder(this);
        image_set_dialog.setTitle("Изменить фото");
        image_set_dialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                }else if(which == 1){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                }
            }
        });
        image_set_dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){
            if(requestCode == 0){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                recipeDataProvider.saveImageToFB(bitmap, recipe);
                recipe_image.setImageBitmap(bitmap);
            } else if(requestCode == 1){
                try {
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    recipeDataProvider.saveImageToFB(bitmap, recipe);
                    recipe_image.setImageBitmap(bitmap);
                }catch (Exception e){}
            }
        }
    }

    public void removeIngredientClicked(View v){

    }
}