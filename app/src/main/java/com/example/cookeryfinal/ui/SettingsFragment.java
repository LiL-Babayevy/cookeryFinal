package com.example.cookeryfinal.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.cookeryfinal.LogIn;
import com.example.cookeryfinal.MainActivity;
import com.example.cookeryfinal.R;
import com.example.cookeryfinal.Register;
import com.example.cookeryfinal.recipe_related.OnRecipeRetrievedListener;
import com.example.cookeryfinal.recipe_related.Recipe;
import com.example.cookeryfinal.recipe_related.RecipeDataProvider;
import com.example.cookeryfinal.user_related.OnSingleUserRetrievedListener;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserAuth;
import com.example.cookeryfinal.user_related.UserDataProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;

public class SettingsFragment extends Fragment{

    private View root;
    private UserAuth userAuth;
    private ImageView user_icon;
    private TextView my_name, my_email, emailEdit, passwordEdit, nameEdit,
            imageEdit, deleteUserTxt, my_password, saveChangesTxt;
    private UserDataProvider userDataProvider;
    private String newEmail = "";
    private String newName = "";
    private String newPassword = "";
    private int resID;
    private RecipeDataProvider recipeDataProvider;
    private User current_user;

    public SettingsFragment(){}


    public View onCreateView(@NonNull LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {


        userAuth = new UserAuth(getContext());
        current_user = userAuth.getSignedInUser();
        userDataProvider = UserDataProvider.getInstance();

        if(current_user != null){
            root = inflater.inflate(R.layout.fragment_settings, container, false);
            my_email = root.findViewById(R.id.myEmail);
            my_name = root.findViewById(R.id.myName);
            user_icon  = root.findViewById(R.id.userPhotoSettings);
            my_password = root.findViewById(R.id.myPassword);
            recipeDataProvider = RecipeDataProvider.getInstance();
            my_email.setText(current_user.getEmail());
            my_name.setText(current_user.getName());
            user_icon.setImageResource(current_user.getImage());
            resID = current_user.getImage();

            String secret_password="";
            for(int i = 0; i < current_user.getUser_password().length(); i++){
                secret_password += "*";
            }
            my_password.setText(secret_password);

            deleteUserTxt = root.findViewById(R.id.deleteUserTxt);
            deleteUserTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder delete_user = new AlertDialog.Builder(getContext());
                    delete_user.setTitle("Вы уверены, что хотите удалить Ваш аккаунт?");
                    delete_user.setPositiveButton("да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteUser();
                        }
                    });
                    delete_user.setNeutralButton("нет", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    delete_user.setCancelable(false);
                    AlertDialog alertDialog2 = delete_user.create();
                    alertDialog2.show();
                }
            });

            saveChangesTxt = root.findViewById(R.id.saveUserChangesTxt);
            saveChangesTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),"Изменения сохранены!", Toast.LENGTH_LONG).show();
                    userDataProvider.updateUser(current_user);
                }
            });

            emailEdit = root.findViewById(R.id.editEmail);
            emailEdit.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder change_email = new AlertDialog.Builder(getContext());
                    change_email.setTitle("Изменение электронного адреса");
                    View dialogLayout = inflater.inflate(R.layout.dialog_layout, null);
                    EditText edit = dialogLayout.findViewById(R.id.addItemEditText);
                    edit.setHint("введите новый электронный адрес");
                    change_email.setView(dialogLayout);
                    change_email.setPositiveButton("изменить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    newEmail = edit.getText().toString();
                                    onEmailEditClicked(v);
                                }
                            });
                    change_email.setNeutralButton("пропустить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    change_email.setCancelable(false);
                    AlertDialog alertDialog2 = change_email.create();
                    alertDialog2.show();
                }
            });

            nameEdit = root.findViewById(R.id.editName);
            nameEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder change_name = new AlertDialog.Builder(getContext());
                    change_name.setTitle("Изменение имени");

                    View dialogLayout = inflater.inflate(R.layout.dialog_layout, null);
                    EditText edit = dialogLayout.findViewById(R.id.addItemEditText);
                    edit.setHint("введите новое имя");
                    change_name.setView(dialogLayout);
                    change_name.setPositiveButton("изменить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            newName = edit.getText().toString();
                            onNameEditClicked();
                        }
                    });
                    change_name.setNeutralButton("пропустить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    change_name.setCancelable(false);
                    AlertDialog alertDialog2 = change_name.create();
                    alertDialog2.show();
                }
            });


            imageEdit = root.findViewById(R.id.editImage);
            imageEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder change_icon = new AlertDialog.Builder(getContext());
                    View dialogLayout = inflater.inflate(R.layout.activity_choose_user_icon, null);
                    change_icon.setView(dialogLayout);
                    TextView choose = dialogLayout.findViewById(R.id.chooseIconTxt);
                    choose.setText("Выберите новую иконку");
                    TextView readyTxt = dialogLayout.findViewById(R.id.ReadyTxt);
                    readyTxt.setVisibility(View.INVISIBLE);
                    LinearLayout linLay= dialogLayout.findViewById(R.id.chooseIconLinear);
                    for(int i=0; i< linLay.getChildCount(); i++){
                        LinearLayout linear = (LinearLayout) linLay.getChildAt(i);
                        for(int j = 0; j< linear.getChildCount(); j++){
                            View icon = linear.getChildAt(j);
                            icon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    iconSelectedInDialog(icon);
                                }
                            });
                        }

                    }
                    change_icon.setPositiveButton("сохранить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            changeUserIcon();
                        }
                    });
                    change_icon.setNeutralButton("пропустить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    change_icon.setCancelable(false);
                    AlertDialog alertDialog2 = change_icon.create();
                    alertDialog2.show();
                }
            });


            passwordEdit = root.findViewById(R.id.editPassword);
            passwordEdit.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setTitle("Изменение пароля");
                    View dialog_changePass = inflater.inflate(R.layout.dialog_password, null);
                    EditText enterPassword = dialog_changePass.findViewById(R.id.dialog_newPassword1);
                    EditText enterPassword2 = dialog_changePass.findViewById(R.id.dialog_newPassword2);
                    EditText edit = dialog_changePass.findViewById(R.id.dialog_oldPassword);
                    enterPassword.setOnTouchListener(new View.OnTouchListener() {
                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            String oldPassword = edit.getText().toString();
                            if(!oldPassword.equals(current_user.getUser_password())) {
                                edit.setError("старый пароль введен неверно");
                            }else{
                                return false;
                            }
                            return true;
                        }
                    });
                    enterPassword2.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            String str_pass1 = enterPassword.getText().toString();
                            if (str_pass1.length() < 6) {
                                enterPassword.setError("недостаточное количество символов(минимум: 6)");
                            } else {
                                return false;
                            }
                            return true;
                        }
                    });
                    builder.setView(dialog_changePass);
                    builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String str_pass1 = enterPassword.getText().toString();
                            String str_pass2 = enterPassword2.getText().toString();
                            if(str_pass1.length() >= 6 && str_pass2.equals(str_pass1)){
                                newPassword = str_pass1;
                                onPasswordEditClicked();
                            }else{
                                Snackbar.make(getContext(), getView(), "пароль введен неверно", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Пропустить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
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
            current_user.setEmail(newEmail);
            my_email.setText(newEmail);
            saveChangesTxt.setTextColor(getResources().getColor(R.color.orange_red));
//            Toast.makeText(getContext(), "электронный адрес изменен", Toast.LENGTH_LONG).show();
        }
    }

    public void onNameEditClicked(){
        if(!newName.isEmpty()){
            current_user.setName(newName);
            my_name.setText(newName);
            saveChangesTxt.setTextColor(getResources().getColor(R.color.orange_red));
//            Toast.makeText(getContext(), "имя изменено", Toast.LENGTH_LONG).show();
        }
    }

    public void onPasswordEditClicked(){
        if(!newPassword.isEmpty() || newPassword.length() < 6){
            current_user.setUser_password(newPassword);
            String secret_password="";
            for(int i = 0; i < current_user.getUser_password().length(); i++){
                secret_password += "*";
            }
            my_password.setText(secret_password);
            saveChangesTxt.setTextColor(getResources().getColor(R.color.orange_red));
//            Toast.makeText(getContext(), "пароль изменен", Toast.LENGTH_LONG).show();
        }
    }

    public void iconSelectedInDialog(View icon){
        icon.setBackgroundResource(R.color.white_blue);
        switch (icon.getId()){
            case R.id.ImageBoyChef:
                resID = R.drawable.boy_chef;
                break;
            case R.id.ImageBossChef:
                resID = R.drawable.chef;
                break;
            case R.id.ImageGirlChef:
                resID = R.drawable.girl_chef;
                break;
            case R.id.ImageRollingPin:
                resID = R.drawable.rolling_pin;
                break;
            case R.id.ImageBeater:
                resID = R.drawable.beater;
                break;
            case R.id.ImageOven:
                resID = R.drawable.oven;
                break;
            case R.id.ImageIceCream:
                resID = R.drawable.ice_cream;
                break;
            case R.id.ImageCookies:
                resID = R.drawable.cookies;
                break;
            case R.id.ImageCroissant:
                resID = R.drawable.croissant;
                break;
            case R.id.ImageSausage:
                resID = R.drawable.sausage;
                break;
            case R.id.ImagePizza:
                resID = R.drawable.pizza;
                break;
            case R.id.ImageSushi:
                resID = R.drawable.sushi;
                break;
        }
    }

    public void changeUserIcon(){
        current_user.setImage(resID);
        user_icon.setImageResource(resID);
//        Toast.makeText(getContext(), "иконка изменена", Toast.LENGTH_LONG).show();
        saveChangesTxt.setTextColor(getResources().getColor(R.color.orange_red));
    }

    public void deleteUser(){
        recipeDataProvider.getMyRecipes(userAuth.getSignedInUserKey(), new OnRecipeRetrievedListener() {
            @Override
            public void onRecipeRetrieved(ArrayList<Recipe> recipes) {
                for(Recipe r : recipes){
                    recipeDataProvider.deleteRecipe(r);
                }
            }
        });
        recipeDataProvider.getDraftRecipes(userAuth.getSignedInUserKey(), new OnRecipeRetrievedListener() {
            @Override
            public void onRecipeRetrieved(ArrayList<Recipe> recipes) {
                for(Recipe r : recipes){
                    recipeDataProvider.deleteRecipe(r);
                }
            }
        });
        userDataProvider.deleteUser(current_user);
        userAuth.signOut();
        Intent intent = new Intent(getActivity(), LogIn.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        getActivity().finish();
        startActivity(intent);
    }
}