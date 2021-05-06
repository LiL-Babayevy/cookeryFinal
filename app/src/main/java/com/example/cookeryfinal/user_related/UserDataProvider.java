package com.example.cookeryfinal.user_related;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserDataProvider{

    private UserAuth userAuth = UserAuth.getInstance();
    private FirebaseDatabase db;
    private DatabaseReference users;


    private UserDataProvider() {
        db = FirebaseDatabase.getInstance();
        users = db.getReference().child("Users");

    }

    private static UserDataProvider provider = new UserDataProvider();

    public static UserDataProvider getInstance() {
        return provider;
    }


    public User getUser(String key) {
        ArrayList<User> user_arrayList = new ArrayList<>();
        OnUsersRetrievedListener user_listener = new OnUsersRetrievedListener() {
            @Override
            public void OnUsersRetrieved(ArrayList<User> users) {
                user_arrayList.clear();
                user_arrayList.addAll(users);
            }
        };
        getUsers(user_listener);
        for (User user : user_arrayList){
            if(user.getKey() == key){
                return user;
            }
        }
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

    public User getCurrentUserByKey(){
        User user = getUser(userAuth.getmAuth().getCurrentUser().getUid());
        return user;
    }

    public void updateUser(User u) {
        users.child(u.getKey()).setValue(u);

    }

    public void deleteUser(User u) {
        users.child(u.getKey()).removeValue();
    }
}
