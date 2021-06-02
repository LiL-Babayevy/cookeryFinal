package com.example.cookeryfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cookeryfinal.user_related.OnSingleUserRetrievedListener;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserAuth;
import com.example.cookeryfinal.user_related.UserDataProvider;

public class ChooseUserIcon extends AppCompatActivity {
    private UserDataProvider userDataProvider;
    private UserAuth userAuth;
    private User current_user;
    private TextView ready;
    private int resID;
    private LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user_icon);
        userDataProvider = UserDataProvider.getInstance();
        userAuth = new UserAuth(this);
        current_user = userAuth.getSignedInUser();
        ready = findViewById(R.id.ReadyTxt);
        ready.setVisibility(View.INVISIBLE);
        linear = findViewById(R.id.chooseIconLinear);
        for(int i =0; i< linear.getChildCount(); i++){
            LinearLayout icons = (LinearLayout) linear.getChildAt(i);
            for(int j = 0; j<icons.getChildCount(); j++) {
                View icon = icons.getChildAt(j);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ready.setVisibility(View.VISIBLE);
                        switch (v.getId()) {
                            case R.id.ImageBoyChef:
                                resID = R.drawable.boy_chef;
                                setBackground(R.id.ImageBoyChef);
                                break;
                            case R.id.ImageBossChef:
                                resID = R.drawable.chef;
                                setBackground(R.id.ImageBossChef);
                                break;
                            case R.id.ImageGirlChef:
                                resID = R.drawable.girl_chef;
                                setBackground(R.id.ImageGirlChef);
                                break;
                            case R.id.ImageRollingPin:
                                resID = R.drawable.rolling_pin;
                                setBackground(R.id.ImageRollingPin);
                                break;
                            case R.id.ImageBeater:
                                resID = R.drawable.beater;
                                setBackground(R.id.ImageBeater);
                                break;
                            case R.id.ImageOven:
                                resID = R.drawable.oven;
                                setBackground(R.id.ImageOven);
                                break;
                            case R.id.ImageIceCream:
                                resID = R.drawable.ice_cream;
                                setBackground(R.id.ImageIceCream);
                                break;
                            case R.id.ImageCookies:
                                resID = R.drawable.cookies;
                                setBackground(R.id.ImageCookies);
                                break;
                            case R.id.ImageCroissant:
                                resID = R.drawable.croissant;
                                setBackground(R.id.ImageCroissant);
                                break;
                            case R.id.ImageSausage:
                                resID = R.drawable.sausage;
                                setBackground(R.id.ImageSausage);
                                break;
                            case R.id.ImagePizza:
                                resID = R.drawable.pizza;
                                setBackground(R.id.ImagePizza);
                                break;
                            case R.id.ImageSushi:
                                resID = R.drawable.sushi;
                                setBackground(R.id.ImageSushi);
                                break;
                        }
                    }
                });
            }
        }
    }

    public void setBackground(int id){
        for(int i = 0; i < linear.getChildCount(); i++){
            LinearLayout icons = (LinearLayout) linear.getChildAt(i);
            for(int j = 0; j<icons.getChildCount(); j++) {
                View v = icons.getChildAt(j);
                if(v.getId() == id){
                    v.setBackgroundResource(R.color.white_blue);
                }else{
                    v.setBackgroundResource(R.color.white);
                }
            }
        }
    }

    public void setUserImage(View v){
        if(current_user != null){
            current_user.setImage(resID);
            userDataProvider.updateUser(current_user);
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}