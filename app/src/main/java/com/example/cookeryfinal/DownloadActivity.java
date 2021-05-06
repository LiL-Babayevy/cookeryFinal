package com.example.cookeryfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.cookeryfinal.user_related.UserAuth;

public class DownloadActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 2300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cover);
        UserAuth userAuth = UserAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    userAuth.getmAuth().getCurrentUser().getEmail();
                    Intent mainIntent = new Intent(DownloadActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    DownloadActivity.this.finish();
                }catch (NullPointerException e){
                    Intent intent = new Intent(DownloadActivity.this, LogIn.class);
                    startActivity(intent);
                    DownloadActivity.this.finish();
                }

            }
        }, SPLASH_DISPLAY_LENGHT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}