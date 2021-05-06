package com.example.cookeryfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cookeryfinal.user_related.UserAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {

    private UserAuth userAuth = UserAuth.getInstance();
    private EditText userEmail, userPassword;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(userAuth.getCurrentUser() != null){
            reload();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        userEmail = findViewById(R.id.email_logIn);
        userPassword = findViewById(R.id.password_logIn);
    }

    public void register_clicked(View v){
        Intent Intent = new Intent(this, Register.class);
        startActivity(Intent);
    }

    public void logIn_clicked(View view){
        String passwrd = userPassword.getText().toString();
        String email = userEmail.getText().toString();
        userAuth.getmAuth().signInWithEmailAndPassword(email, passwrd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent mainPage = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(mainPage);
                            updateUI(userAuth.getCurrentUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LogIn.this, "электронный адрес или пароль введен неверно",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
//        Intent mainPage = new Intent(this, MainActivity.class);
//        startActivity(mainPage);
//        this.finish();
    }




    private void reload() { }
    private void updateUI(FirebaseUser user) {
    }
}