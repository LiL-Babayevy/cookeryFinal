package com.example.cookeryfinal.user_related;

import androidx.annotation.NonNull;

import com.example.cookeryfinal.recipe_related.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserDataProvider{

//    private UserAuth userAuth = UserAuth.getInstance();
    private FirebaseDatabase db;
    private DatabaseReference users;

    private UserDataProvider() {
        db = FirebaseDatabase.getInstance();
        users = db.getReference().child("Users");

    }


    private static UserDataProvider provider = new UserDataProvider();

    public DatabaseReference getUsers() {
        return users;
    }

    public static UserDataProvider getInstance() {
        return provider;
    }


    public User getUser(String key, OnSingleUserRetrievedListener listener) {
        Query query = users.orderByChild("database_key").equalTo(key);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User current_user = null;
                for (DataSnapshot dataSnapshot_user : snapshot.getChildren()){
                    current_user = dataSnapshot_user.getValue(User.class);
                    break;
                }
                listener.OnSingleUserRetrieved(current_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return null;
    }



    public void getUsers(OnUsersRetrievedListener listener) {
        ArrayList<User> userArrayList = new ArrayList<>();
        Query query = users;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                for (DataSnapshot dataUser : snapshot.getChildren()) {
                    User user = dataUser.getValue(User.class);
                    userArrayList.add(user);
                }
                listener.OnUsersRetrieved(userArrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    public void updateUser(User u) {
        users.child(u.getDatabase_key()).setValue(u);
    }

    public void deleteUser(User u) {
        users.child(u.getDatabase_key()).removeValue();
    }

    public void deleteAll(){
        users.removeValue();
    }
}
