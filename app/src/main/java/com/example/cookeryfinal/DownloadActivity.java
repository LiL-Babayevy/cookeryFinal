package com.example.cookeryfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.cookeryfinal.user_related.OnSingleUserRetrievedListener;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserAuth;
import com.example.cookeryfinal.user_related.UserDataProvider;

public class DownloadActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 2300;
    private UserDataProvider userDataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cover);
//        UserAuth userAuth = UserAuth.getInstance();
        UserAuth userAuth = new UserAuth(this);
        userDataProvider = UserDataProvider.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                try{
//                User user = userAuth.getSignedInUser();
                String key = userAuth.getSignedInUserKey();
                if(key != null){
                    userDataProvider.getUser(key, new OnSingleUserRetrievedListener() {
                        @Override
                        public void OnSingleUserRetrieved(User user) {
                            if(user != null) {
                                userAuth.signIn(user);
                                Intent mainIntent = new Intent(DownloadActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                DownloadActivity.this.finish();
                            }else{
                                Intent intent = new Intent(DownloadActivity.this, LogIn.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                                DownloadActivity.this.finish();
                            }
                        }
                    });
                } else{
                    Intent intent = new Intent(DownloadActivity.this, LogIn.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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