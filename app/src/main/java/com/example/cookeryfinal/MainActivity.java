package com.example.cookeryfinal;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.cookeryfinal.ui.MyRecipesFragment;
import com.example.cookeryfinal.ui.HomeFragment;
import com.example.cookeryfinal.ui.DraftsFragment;
import com.example.cookeryfinal.ui.SettingsFragment;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserAuth;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.MenuItemCompat;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userAuth = UserAuth.getInstance();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.textView);
        FirebaseUser user = userAuth.getCurrentUser();
        navUsername.setText(user.getEmail());
        logOut = headerView.findViewById(R.id.LogOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAuth.singOut();
                try{
                    userAuth.getCurrentUser().getEmail();
                }catch (NullPointerException e){
                    Intent intent = new Intent(getApplicationContext(), LogIn.class);
                    startActivity(intent);
                }
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment()).commit();


//        mSlideTextView.setTextColor(getResources().getColor(R.color.purple_200));

//        Switch mGallerySwitch = (Switch) MenuItemCompat
//                .getActionView(navigationView.getMenu().findItem(R.id.nav_gallery));
//        mGallerySwitch.setGravity(Gravity.CENTER);
//        mGallerySwitch.setChecked(true);
//        mGallerySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), isChecked + "", Toast.LENGTH_LONG).show();
//            }
//        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment f = null;
                if (item.getItemId() == R.id.nav_home) {
                    setTitle(null);
                    f = new HomeFragment();
                } else if (item.getItemId() == R.id.nav_my_recipes) {
                    setTitle(item.getTitle());
                    f = new MyRecipesFragment();
                } else if (item.getItemId() == R.id.nav_slideshow) {
                    setTitle(item.getTitle());
                    f = new DraftsFragment();
                } else if (item.getItemId() == R.id.nav_settings) {
//                    toolbar.setBackgroundResource(R.color.white_blue);
                    setTitle(item.getTitle());
                    f = new SettingsFragment();
                }
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
}