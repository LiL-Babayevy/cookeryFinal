package com.example.cookeryfinal.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.cookeryfinal.LogIn;
import com.example.cookeryfinal.R;
import com.example.cookeryfinal.Register;
import com.example.cookeryfinal.user_related.OnSingleUserRetrievedListener;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserAuth;
import com.example.cookeryfinal.user_related.UserDataProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SettingsFragment extends Fragment{

    private View root;
    private UserAuth userAuth;
    private TextView my_name, my_email, my_password, emailEdit, passwordEdit, nameEdit;
    private UserDataProvider userDataProvider;
    private String newEmail = "";
    public SettingsFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {

        userAuth = UserAuth.getInstance();
        userDataProvider = UserDataProvider.getInstance();
        if(userAuth.getCurrentUser() != null){
            root = inflater.inflate(R.layout.fragment_settings, container, false);
            my_email = root.findViewById(R.id.myEmail);
            my_name = root.findViewById(R.id.myName);
            my_password = root.findViewById(R.id.myPassword);

            userDataProvider.getUser(userAuth.getCurrentUser().getUid(), new OnSingleUserRetrievedListener() {
                @Override
                public void OnSingleUserRetrieved(User user) {
                    my_email.setText(user.getEmail());
                    my_name.setText(user.getName());
                }
            });

            emailEdit = root.findViewById(R.id.editEmail);
            emailEdit.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder chage_email = new AlertDialog.Builder(getContext());
                    chage_email.setTitle("Изменение электронного адреса");

                    View dialogLayout = inflater.inflate(R.layout.dialog_layout, null);
                    EditText edit = dialogLayout.findViewById(R.id.addItemEditText);
                    chage_email.setView(dialogLayout);
                    chage_email.setPositiveButton("изменить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    newEmail = edit.getText().toString();
                                    onEmailEditClicked(v);
                                }
                            });
                    chage_email.setNeutralButton("пропустить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    chage_email.setCancelable(false);
                    AlertDialog alertDialog2 = chage_email.create();
                    alertDialog2.show();
                }
            });


        }else{
            root = inflater.inflate(R.layout.blank_fragments, container, false);
            TextView logInTxt = root.findViewById(R.id.logInText);
            logInTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent Intent = new Intent(getContext(), LogIn.class);
                    startActivity(Intent);
                }
            });
            TextView registerTxt = root.findViewById(R.id.registerText);
            registerTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent Intent = new Intent(getContext(), Register.class);
                    startActivity(Intent);
                }
            });
        }
        return root;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onEmailEditClicked(View v){
        if(!newEmail.isEmpty()){
            userDataProvider.getUser(userAuth.getCurrentUser().getUid(), new OnSingleUserRetrievedListener() {
                @Override
                public void OnSingleUserRetrieved(User user) {
                    user.setEmail(newEmail);
                    userDataProvider.updateUser(user);
                    Toast.makeText(getContext(), "changed", Toast.LENGTH_LONG).show();
                }
            });
            userAuth.getCurrentUser().updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getContext(), "changed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
