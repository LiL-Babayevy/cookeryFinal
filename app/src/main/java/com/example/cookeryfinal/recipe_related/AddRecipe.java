  package com.example.cookeryfinal.recipe_related;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookeryfinal.R;
import com.example.cookeryfinal.ui.MyRecipesFragment;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserAuth;
import com.example.cookeryfinal.user_related.UserDataProvider;

import java.io.InputStream;
import java.util.ArrayList;

public class AddRecipe extends AppCompatActivity {
    private ImageView set_image;
    private UserDataProvider userDataProvider;
    private LinearLayout ingredients;
    private TextView plus;
    private Spinner category_spinner;
    private EditText cooking_steps;
    private ArrayList<Ingredient> ingredient_list = new ArrayList<>();
    private String[] categories = {"завтрак", "обед", "ужин", "десерт"};
    private Recipe recipe = new Recipe();
    private RecipeDataProvider recipeDataProvider;
    private UserAuth userAuth;
    private User current_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Добавление рецепта");
        setContentView(R.layout.activity_add_recipe);
        set_image = findViewById(R.id.imageView);

        recipeDataProvider = RecipeDataProvider.getInstance();

        userAuth = new UserAuth(this);
        current_user = userAuth.getSignedInUser();

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

        //установка спиннера для выбора категории
        userDataProvider = UserDataProvider.getInstance();
        category_spinner = findViewById(R.id.CategorySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

    //добавление ингредиента
    public View addIngredient(){
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.edit_ingredient, null);
        EditText name = view.findViewById(R.id.editIngredientName);
        name.setHint("Ингредиент");
        Button delete = view.findViewById(R.id.DeleteIngredient);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredients.removeView(view);
            }
        });
        return view;
    }

    //сохранение рецепта в черновики
    public void btnSaveClicked(View view){
        recipe.setCooking_steps(cooking_steps.getText().toString());
        EditText recipeName = findViewById(R.id.addRecipeName);
        String recipeNameStr = recipeName.getText().toString();
        recipe.setRecipe_name(recipeNameStr);

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
        recipe.setOwnerID(current_user.getDatabase_key());
        recipeDataProvider.pushRecipe(recipe);
        AddRecipe.this.finish();
    }

    //публикация рецепта
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

        if(!recipeNameStr.isEmpty() && !cookStepsStr.isEmpty()){
            recipe.setRecipe_name(recipeNameStr);
            recipe.setCooking_steps(cookStepsStr);
            recipe.setIngredients(ingredient_list);
            recipe.setOwnerID(current_user.getDatabase_key());
            recipe.setStatus("my_recipes");

            recipeDataProvider.pushRecipe(recipe);
            AddRecipe.this.finish();
        }else{
            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_LONG).show();
        }
    }


    //проверка(заполнено поле или нет)
    public void checkIsEmpty(String s, EditText edit){
        if(s.isEmpty()){
            edit.setError("заполните поле");
        }
    }


    //выбор фото
    public void setImage(View v){
        String[] options = new String[]{"Сделать фото", "Загрузить из галереи"};
        AlertDialog.Builder image_set_dialog = new AlertDialog.Builder(this);
        image_set_dialog.setTitle("Добавление фото");
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

    //сохранение фото в рецепт
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){
            if(requestCode == 0){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                recipeDataProvider.saveImageToFB(bitmap, recipe);
                set_image.setImageBitmap(bitmap);
                recipeDataProvider.pushRecipe(recipe);
            } else if(requestCode == 1){
                try {
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    recipeDataProvider.saveImageToFB(bitmap, recipe);
                    set_image.setImageBitmap(bitmap);
                    recipeDataProvider.pushRecipe(recipe);
                }catch (Exception e){}
            }
        }
    }
}
