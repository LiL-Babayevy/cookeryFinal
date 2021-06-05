package com.example.cookeryfinal.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cookeryfinal.LogIn;
import com.example.cookeryfinal.R;
import com.example.cookeryfinal.Register;
import com.example.cookeryfinal.ShoppingItem;
import com.example.cookeryfinal.user_related.OnSingleUserRetrievedListener;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserAuth;
import com.example.cookeryfinal.user_related.UserDataProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class ShoppingListFragment extends Fragment {

    private View root;
    private FloatingActionButton addItem;
    private LinearLayout itemList;
    private UserDataProvider userDataProvider;
    private String user_databaseId;
    private ArrayList<ShoppingItem> shoppingList = new ArrayList<>();
    private UserAuth userAuth;
    private User current_user;

    public ShoppingListFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userAuth = new UserAuth(getContext());
        current_user = userAuth.getSignedInUser();
        if(current_user == null){
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
        }else{
            root = inflater.inflate(R.layout.fragment_shop_list, container, false);
            userDataProvider = UserDataProvider.getInstance();

            itemList = root.findViewById(R.id.ItemList);
            addItem = root.findViewById(R.id.AddItemBtn);

            if(current_user.getShoppingList() != null) {
                for (int i = 0; i < current_user.getShoppingList().size(); i++) {
                    ShoppingItem item = current_user.getShoppingList().get(i);
                    shoppingList.add(item);
                    addItemToList(item);
                }
            }else{
                current_user.setShoppingList(shoppingList);
            }


            addItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder addItem = new AlertDialog.Builder(getContext());
                    addItem.setTitle("Добавление продукта");
                    View dialogLayout = inflater.inflate(R.layout.dialog_layout, null);
                    EditText item_nameEdit = dialogLayout.findViewById(R.id.addItemEditText);
                    addItem.setView(dialogLayout)
                            .setPositiveButton("ДОБАВИТЬ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String item_name =  item_nameEdit.getText().toString();
                                    if(item_name.equals("") || item_name.isEmpty()){
                                        item_nameEdit.setError("заполните поле");
                                    }else{
                                        ShoppingItem item = new ShoppingItem(item_name, false);
                                        addItemToList(item);
                                        shoppingList.add(item);
                                    }
                                }
                            })
                            .setNeutralButton("ПРОПУСТИТЬ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    addItem.setCancelable(false);
                    AlertDialog alertDialog = addItem.create();
                    alertDialog.show();
                    current_user.setShoppingList(shoppingList);
                }
            });
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemList.removeAllViews();
        if(!shoppingList.isEmpty()){
            for(ShoppingItem item : shoppingList){
                addItemToList(item);
            }
        }
    }

    public void addItemToList(ShoppingItem item){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.shop_list_item, null);
        TextView name_textView = view.findViewById(R.id.TextViewItemName);
        Button deleteItem = view.findViewById(R.id.DeleteItem);
        CheckBox itemCheck = view.findViewById(R.id.itemCheck);
        name_textView.setText(item.getTitle());
        itemCheck.setChecked(item.isIs_checked());
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.removeView(view);
                shoppingList.remove(item);
                current_user.setShoppingList(shoppingList);
            }
        });

        itemCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setIs_checked(isChecked);
                current_user.setShoppingList(shoppingList);
            }
        });
        itemList.addView(view);
    }

}
