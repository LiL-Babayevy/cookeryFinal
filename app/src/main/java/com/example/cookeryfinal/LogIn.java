package com.example.cookeryfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookeryfinal.user_related.OnUsersRetrievedListener;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserAuth;
import com.example.cookeryfinal.user_related.UserDataProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class LogIn extends AppCompatActivity {

//    private UserAuth userAuth = UserAuth.getInstance();
    private EditText userEmail, userPassword;
    private UserAuth userAuth;
    private User current_user;
    private UserDataProvider userDataProvider;

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        if(userAuth.getSignedInUser() != null){
//            reload();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        userAuth = new UserAuth(this);
        userEmail = findViewById(R.id.email_logIn);
        userPassword = findViewById(R.id.password_logIn);
        current_user = userAuth.getSignedInUser();
        userDataProvider = UserDataProvider.getInstance();

        TextView no_user = findViewById(R.id.continue_without_sgnIn);
        no_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
    }

    public void register_clicked(View v){
        Intent Intent = new Intent(this, Register.class);
        Intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(Intent);
    }

    public void logIn_clicked(View view){
        String passwrd = userPassword.getText().toString();
        String email = userEmail.getText().toString();
        userDataProvider.getUsers(new OnUsersRetrievedListener() {
            @Override
            public void OnUsersRetrieved(ArrayList<User> users) {
                for(User single_user: users){
                    if(single_user.getEmail().equals(email) && single_user.getUser_password().equals(passwrd)){
                        userAuth.signIn(single_user);
                        Intent mainPage = new Intent(getApplicationContext(), MainActivity.class);
                        mainPage.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(mainPage);
                    }else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LogIn.this, "электронный адрес или пароль введен неверно",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }




    private void reload() { }
    private void updateUI(FirebaseUser user) {
    }
}