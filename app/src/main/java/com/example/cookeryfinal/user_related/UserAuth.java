package com.example.cookeryfinal.user_related;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAuth {

    private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private static User current_user;

    public UserAuth(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("signedInUserKey", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void signIn(User u) {
        editor.putString("signedInUserKey", u.getDatabase_key());
        editor.commit();
        current_user = u;
    }

    public void signOut() {
        editor.putString("signedInUserKey", null);
        editor.commit();
        current_user = null;
    }

    public User getSignedInUser() {
        return current_user;
    }

    public String getSignedInUserKey() {
        String key = sp.getString("signedInUserKey", null);
        return key;
    }

}
