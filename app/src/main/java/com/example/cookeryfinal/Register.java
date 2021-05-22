package com.example.cookeryfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cookeryfinal.recipe_related.DefaultRecipes;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserAuth;
import com.example.cookeryfinal.user_related.UserDataProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Register extends AppCompatActivity {

    private EditText email, name, password1, password2;
//    private FirebaseAuth mAuth;
//    FirebaseDatabase db;
//    DatabaseReference users;
    private UserDataProvider userDataProvider;
    private UserAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDataProvider = UserDataProvider.getInstance();
        userAuth = UserAuth.getInstance();
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseDatabase.getInstance();
//        users = db.getReference().child("Users");

        email = findViewById(R.id.email_signIn);
        name = findViewById(R.id.name_signIn);
        password1 = findViewById(R.id.password_signIn);
        password2 = findViewById(R.id.password_signIn2);
    }


    public void btnRegister_clicked(View v){
        User user = new User();
        user.setEmail(email.getText().toString());
        user.setName(name.getText().toString());

        userAuth.getmAuth().createUserWithEmailAndPassword(email.getText().toString(), password1.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser userAuth = userAuth.getmAuth().getCurrentUser();
                            FirebaseUser user_firebase = userAuth.getmAuth().getCurrentUser();
                            updateUI(user_firebase);
                            if(userAuth != null){
                                user.setAuth_key(userAuth.getmAuth().getUid());
                            }
                            DefaultRecipes.init();

                            DatabaseReference push = userDataProvider.getUsers().push();
                            user.setDatabase_key(push.getKey());
                            user.setMy_recipes(DefaultRecipes.defaultrecipes);
                            push.setValue(user);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = userAuth.getmAuth().getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }
}