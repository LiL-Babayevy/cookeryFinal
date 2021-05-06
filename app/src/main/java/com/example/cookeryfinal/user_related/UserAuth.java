package com.example.cookeryfinal.user_related;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAuth {

    private FirebaseAuth mAuth;

    private UserAuth(){
        mAuth = FirebaseAuth.getInstance();
    }
    private static UserAuth userAuth = new UserAuth();
    public static UserAuth getInstance(){
        return userAuth;
    }

    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }

    public void singOut(){
        mAuth.signOut();
    }

    public FirebaseAuth getmAuth(){
        return this.mAuth;
    }
}
