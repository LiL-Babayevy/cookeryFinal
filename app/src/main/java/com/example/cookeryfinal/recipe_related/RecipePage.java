package com.example.cookeryfinal.recipe_related;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookeryfinal.R;
import com.example.cookeryfinal.user_related.OnSingleUserRetrievedListener;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserAuth;
import com.example.cookeryfinal.user_related.UserDataProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecipePage extends AppCompatActivity {
    private ClipboardManager clipboardManager;
    private ClipData clipData;
    private TextView copyText;
    private LinearLayout IngLayout;
    private ImageView like_btn;

    private RecipeDataProvider recipeDataProvider;
    private String recipe_key;
    private ArrayList<String> liked_recipes = new ArrayList<>();

    private boolean liked_checker = true;

    private UserAuth userAuth;
    private UserDataProvider userDataProvider;
    private String currentUserId;
    private User current_user;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);
        Intent intent = getIntent();
        recipe_key = intent.getStringExtra("clicked_recipe");
        userAuth = new UserAuth(this);
        current_user = userAuth.getSignedInUser();
        userDataProvider = UserDataProvider.getInstance();
        like_btn = findViewById(R.id.LikeIcon);
        currentUserId = userAuth.getSignedInUserKey();

        if(current_user != null) {
            try{
                liked_recipes.addAll(current_user.getLiked());
            }catch (NullPointerException e) {
                liked_recipes = new ArrayList<>();
                current_user.setLiked(liked_recipes);
            }
        }

        // копирование рецепта
        clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        copyText = findViewById(R.id.CopyIcon);
        copyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeDataProvider.getCurrentRecipe(recipe_key, new OnSingleRecipeRetrievedListener() {
                    @Override
                    public void onSingleRecipeRetrieved(Recipe retrieved_recipe) {
                        String full_recipe = retrieved_recipe.RecipeToString();
                        clipData = ClipData.newPlainText("text",full_recipe);
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(getApplicationContext(), "рецепт скопирован!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if(current_user != null) {
            //проверка: юзер лайкал рецепт или нет
            getLikeButtonStatus(recipe_key);
        }
        IngLayout = findViewById(R.id.ListOfIngredients);
        recipeDataProvider = RecipeDataProvider.getInstance();

//      установка данных рецепта
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

                    ImageView recipe_image = findViewById(R.id.RecipePageImage);
                    if(retrieved_recipe.getImage() != null){
                        byte[] decodedString = Base64.decode(retrieved_recipe.getImage(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString,0, decodedString.length);
                        recipe_image.setImageBitmap(bitmap);
                    }

                    for(Ingredient ingredient: retrieved_recipe.getIngredients()) {
                        IngLayout.addView(addIngredient(ingredient));
                    }

                }catch(NullPointerException e){
                    Toast.makeText(getApplicationContext(), "failed Recipe Page", Toast.LENGTH_LONG).show();
                    }
            }
        });
    }

    //добавление frame layout для одного ингредиента
    public View addIngredient(Ingredient ingredient){
        View ingredientView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.ingredient_frame, null);
        TextView ing_name = ingredientView.findViewById(R.id.IngredientName);
        TextView ing_amount = ingredientView.findViewById(R.id.IngredientAmount);
        ing_name.setText(ingredient.getIngredient_name());
        ing_amount.setText(ingredient.getAmount());
        return ingredientView;
    }


    public void likeRecipe(View v){
        if(current_user != null) {
            if (liked_checker) {
                liked_checker = false;
                liked_recipes.remove(recipe_key);
                like_btn.setImageResource(R.drawable.ic_dislike);
                Toast.makeText(this, "Рецепт удален из 'Понравившихся'!", Toast.LENGTH_LONG).show();
            } else {
                liked_recipes.add(recipe_key);
                liked_checker = true;
                like_btn.setImageResource(R.drawable.ic_like_orange);
                Toast.makeText(this, "Рецепт добавлен в 'Понравившиеся'!", Toast.LENGTH_LONG).show();
            }
            current_user.setLiked(liked_recipes);
        }else {
            Toast.makeText(this, "Чтобы лайкнуть рецепт, необходимо зарегистироваться или войти", Toast.LENGTH_LONG).show();
        }
    }


    public void getLikeButtonStatus(String recipe_key){
        if(current_user.getLiked() != null) {
            if (current_user.getLiked().contains(recipe_key)) {
                liked_checker = true;
                like_btn.setImageResource(R.drawable.ic_like_orange);
            } else {
                liked_checker = false;
                like_btn.setImageResource(R.drawable.ic_dislike);
            }
        }else {
            liked_checker = false;
            like_btn.setImageResource(R.drawable.ic_dislike);
        }
    }
}
