package com.example.cookeryfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserAuth;
import com.example.cookeryfinal.user_related.UserDataProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Register extends AppCompatActivity {

    private EditText email, name, password1, password2;
    private UserDataProvider userDataProvider;
    private UserAuth userAuth_sr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDataProvider = UserDataProvider.getInstance();

        userAuth_sr = new UserAuth(this);
        email = findViewById(R.id.email_signIn);
        name = findViewById(R.id.name_signIn);
        password1 = findViewById(R.id.password_signIn);
        password2 = findViewById(R.id.password_signIn2);
    }


    public void btnRegister_clicked(View v){
        String user_email = email.getText().toString();
        String user_name = name.getText().toString();
        String user_password1 = password1.getText().toString();
        String user_password2 = password2.getText().toString();

        if(user_email.equals("") || user_email == null){
            email.setError("Введите электронный адрес");
        }
        if(user_name.equals("") || user_name == null){
            name.setError("Введите имя");
        }
        if(user_password1.equals("") || user_password1 == null){
            password1.setError("Введите пароль");
        }
        if(user_password2.equals("") || user_password2 == null){
            password2.setError("Введите пароль повторно");
        }
        if(user_password1.length() < 6){
            password1.setError("Недостаточчное количество символов");
        }
        if(!user_password1.equals(user_password2)){
            password2.setError("Пароль введен неверно");
        }
        if(!isEmailValid(user_email)){
            email.setError("Неверный электронный адрес");
        }


        if((!user_email.equals("") || user_email != null) && (!user_name.equals("") || user_name != null)
                && (!user_password1.equals("") || user_password1 != null) && (user_password2.equals("") || user_password2 != null)
                && user_password1.equals(user_password2) && user_password1.length() >= 6 &&isEmailValid(user_email)) {
            User user = new User();
            user.setEmail(email.getText().toString());
            user.setName(name.getText().toString());
            user.setLiked(new ArrayList<>());
            user.setShoppingList(new ArrayList<>());
            user.setUser_password(user_password1);
            DatabaseReference push = userDataProvider.getUsers().push();
            user.setDatabase_key(push.getKey());
            push.setValue(user);
            Intent intent = new Intent(getApplicationContext(), ChooseUserIcon.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            userAuth_sr.signIn(user);
        }

    }

    public boolean isEmailValid(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}