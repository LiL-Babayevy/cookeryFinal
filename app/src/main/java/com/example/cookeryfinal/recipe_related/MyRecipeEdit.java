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

import com.example.cookeryfinal.MainActivity;
import com.example.cookeryfinal.R;
import com.example.cookeryfinal.recipe_related.Ingredient;
import com.example.cookeryfinal.recipe_related.OnSingleRecipeRetrievedListener;
import com.example.cookeryfinal.recipe_related.Recipe;
import com.example.cookeryfinal.recipe_related.RecipeDataProvider;
import com.example.cookeryfinal.recipe_related.RecipePage;
import com.example.cookeryfinal.ui.MyRecipesFragment;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserDataProvider;

import java.io.InputStream;
import java.util.ArrayList;

public class MyRecipeEdit extends AppCompatActivity {

    private Button saveChanges;
    private EditText editName;
    private EditText editCookingSteps;
    private ImageView recipe_image;
    private RecipeDataProvider recipeDataProvider;
    private UserDataProvider userDataProvider;
    private String recipe_key, updated_name, updated_cookingSteps, current_image, updated_image;
    private Spinner category_spinner;
    private LinearLayout ingredientList;

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
        recipe_image = findViewById(R.id.EditImageView);

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
                    if(retrieved_recipe.getImage() != null){
                        byte[] decodedString = Base64.decode(retrieved_recipe.getImage(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString,0, decodedString.length);
                        recipe_image.setImageBitmap(bitmap);
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
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        int size = ingredientList.getChildCount();
        recipeDataProvider.getCurrentRecipe(recipe_key, new OnSingleRecipeRetrievedListener() {
            @Override
            public void onSingleRecipeRetrieved(Recipe retrieved_recipe) {
//                updated_name = editName.getText().toString();
//                updated_cookingSteps = editCookingSteps.getText().toString();
                for(int i = 0; i < size; i++){
                    View frameIngredient = ingredientList.getChildAt(i);
                    EditText ingTitle = frameIngredient.findViewById(R.id.editIngredientName);
                    String ingTitleStr = ingTitle.getText().toString();

                    EditText ingAmount = frameIngredient.findViewById(R.id.editIngredientAmount);
                    String ingAmountStr = ingAmount.getText().toString();

                    if(!ingAmountStr.isEmpty() && !ingTitleStr.isEmpty()){
                        Ingredient ingredient = new Ingredient(ingTitleStr, ingAmountStr);
//                        if(!retrieved_recipe.getIngredients().isEmpty()) {
//                            for (Ingredient ingredient1 : retrieved_recipe.getIngredients()) {
//                                if(!ingredient1.getIngredient_name().equals(ingredient.getIngredient_name())
//                                        && !ingredient1.getAmount().equals(ingredient.getAmount())) {
////                                    retrieved_recipe.addIngredientToList(ingredient);
//                                    break;
//                                }
//                            }
//                        }else{
//                            retrieved_recipe.addIngredientToList(ingredient);
//                        }
                        ingredients.add(ingredient);

                    }
                    else{
                        checkIsEmpty(ingTitleStr, ingTitle);
                        checkIsEmpty(ingAmountStr, ingAmount);
                        break;
                    }
                }
//                category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        String category = (String)parent.getItemAtPosition(position);
//                        retrieved_recipe.setRecipe_Category(category);
//                    }
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });

//                if(!updated_name.isEmpty() && !updated_cookingSteps.isEmpty()){
//                    retrieved_recipe.setRecipe_name(updated_name);
//                    retrieved_recipe.setCooking_steps(updated_cookingSteps);

//                }
                retrieved_recipe.setIngredients(ingredients);
                recipeDataProvider.updateRecipe(retrieved_recipe);
//                Recipe recipe = new Recipe();
//                recipe.setOwnerID(retrieved_recipe.getOwnerID());
//                recipe.setIngredients(ingredients);
//                recipe.setRecipe_name(retrieved_recipe.getRecipe_name());
//                recipe.setCooking_steps(retrieved_recipe.getCooking_steps());
//                recipe.setRecipe_Category(retrieved_recipe.getRecipe_Category());
//                recipe.setImage(retrieved_recipe.getImage());
//                recipe.setStatus("my_recipes");
//                recipeDataProvider.deleteRecipe(retrieved_recipe);
//                recipeDataProvider.pushRecipe(recipe);
                System.out.println(ingredients.size());
            }

        });
        MyRecipeEdit.this.finish();
    }

    public void updateRecipeImage(View v){
        String[] options = new String[]{"Сделать фото", "Загрузить из галереи"};
        AlertDialog.Builder image_set_dialog = new AlertDialog.Builder(this);
        image_set_dialog.setTitle("set photo");
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
                recipeDataProvider.getCurrentRecipe(recipe_key, new OnSingleRecipeRetrievedListener() {
                    @Override
                    public void onSingleRecipeRetrieved(Recipe retrieved_recipe) {
                        recipeDataProvider.saveImageToFB(bitmap, retrieved_recipe);
                    }
                });
                recipe_image.setImageBitmap(bitmap);
            } else if(requestCode == 1){
                try {
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    recipeDataProvider.getCurrentRecipe(recipe_key, new OnSingleRecipeRetrievedListener() {
                        @Override
                        public void onSingleRecipeRetrieved(Recipe retrieved_recipe) {
                            recipeDataProvider.saveImageToFB(bitmap, retrieved_recipe);
                        }
                    });
                    recipe_image.setImageBitmap(bitmap);
                }catch (Exception e){}
            }
        }
    }
}