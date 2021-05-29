package com.example.cookeryfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class ChooseUserIcon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user_icon);
    }

    public void onIconSelected(View v){
        switch (v.getId()){
            case R.id.ImageBoyChef:

                break;
            case R.id.ImageBossChef:
                break;
            case R.id.ImageGirlChef:
                break;
            case R.id.ImageRollingPin:
                break;
            case R.id.ImageBeater:
                break;
            case R.id.ImageOven:
                break;
            case R.id.ImageIceCream:
                break;
            case R.id.ImageCookies:
                break;
            case R.id.ImageCroissant:
                break;
            case R.id.ImageSausage:
                break;
            case R.id.ImagePizza:
                break;
            case R.id.ImageSushi:
                break;
        }
    }
}