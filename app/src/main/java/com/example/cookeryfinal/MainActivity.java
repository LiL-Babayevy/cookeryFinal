package com.example.cookeryfinal;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookeryfinal.recipe_related.RecipesByCategory;
import com.example.cookeryfinal.ui.LikedRecipesFragment;
import com.example.cookeryfinal.ui.MyRecipesFragment;
import com.example.cookeryfinal.ui.HomeFragment;
import com.example.cookeryfinal.ui.DraftsFragment;
import com.example.cookeryfinal.ui.SettingsFragment;
import com.example.cookeryfinal.ui.ShoppingListFragment;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserAuth;
import com.example.cookeryfinal.user_related.UserDataProvider;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView logOut;
    private UserAuth userAuth;
    private UserDataProvider userDataProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userAuth = new UserAuth(this);
        userDataProvider = UserDataProvider.getInstance();
        User current_user = userAuth.getSignedInUser();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_name);
        TextView navUserEmail = headerView.findViewById(R.id.nav_email);
        ImageView nav_image = headerView.findViewById(R.id.userPhotoHeader);

        if(current_user != null){
            navUsername.setText(current_user.getName());
            navUserEmail.setText(current_user.getEmail());
            nav_image.setImageResource(current_user.getImage());
        }else{
            navUsername.setText("Гость");
            navUserEmail.setText("");
        }



//      Выход из аккаунта
        logOut = headerView.findViewById(R.id.LogOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDataProvider.updateUser(userAuth.getSignedInUser());
                userAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment()).commit();
        setTitle("Главная");



        navigationView.setCheckedItem(R.id.nav_home);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment f = null;
                if (item.getItemId() == R.id.nav_home) {
                    f = new HomeFragment();
                } else if (item.getItemId() == R.id.nav_my_recipes) {
                    f = new MyRecipesFragment();
                } else if (item.getItemId() == R.id.nav_slideshow) {
                    f = new DraftsFragment();
                } else if (item.getItemId() == R.id.nav_settings) {
                    f = new SettingsFragment();
                } else if (item.getItemId() == R.id.nav_shop_list){
                    f = new ShoppingListFragment();
                } else if (item.getItemId() == R.id.nav_liked_recipes){
                    f = new LikedRecipesFragment();
                }
                setTitle(item.getTitle());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, f).commit();
                drawer.close();

                return true;
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        new AlertDialog.Builder(this).setMessage("Вы уверены, что хотите выйти из приложения?")
                .setPositiveButton("да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userDataProvider.updateUser(userAuth.getSignedInUser());
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })
                .setNeutralButton("нет", null)
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(userAuth.getSignedInUser() != null){
            userDataProvider.updateUser(userAuth.getSignedInUser());
        }
    }

    public void breakfastClicked(View v){
        Intent intent = new Intent(getApplicationContext(), RecipesByCategory.class);
        intent.putExtra("category", "завтрак");
        startActivity(intent);
    }

    public void lunchClicked(View v){
        Intent intent = new Intent(getApplicationContext(), RecipesByCategory.class);
        intent.putExtra("category", "обед");
        startActivity(intent);
    }

    public void dinnerClicked(View v){
        Intent intent = new Intent(getApplicationContext(), RecipesByCategory.class);
        intent.putExtra("category", "ужин");
        startActivity(intent);
    }
    public void dessertClicked(View v){
        Intent intent = new Intent(getApplicationContext(), RecipesByCategory.class);
        intent.putExtra("category", "десерт");
        startActivity(intent);
    }
}