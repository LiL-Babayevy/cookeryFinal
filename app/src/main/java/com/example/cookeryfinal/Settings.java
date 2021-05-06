package com.example.cookeryfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cookeryfinal.user_related.UserAuth;

public class Settings extends AppCompatActivity {

    private UserAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        userAuth = UserAuth.getInstance();
    }

    public void logOut_clicked(View view){
        userAuth.singOut();
        try{
            userAuth.getmAuth().getCurrentUser().getEmail();
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            this.finish();
        }catch (NullPointerException e){
            Intent intent = new Intent(this, LogIn.class);
            startActivity(intent);
            this.finish();
        }
    }
}