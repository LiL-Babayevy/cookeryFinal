package com.example.cookeryfinal.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cookeryfinal.R;
import com.example.cookeryfinal.user_related.OnSingleUserRetrievedListener;
import com.example.cookeryfinal.user_related.User;
import com.example.cookeryfinal.user_related.UserDataProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShoppingListFragment extends Fragment {

    private View root;
    private FloatingActionButton addItem;
    private LinearLayout itemList;
    private UserDataProvider userDataProvider;

    public ShoppingListFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_shop_list, container, false);
        userDataProvider = UserDataProvider.getInstance();

        itemList = root.findViewById(R.id.ItemList);
        addItem = root.findViewById(R.id.AddItemBtn);

//        userDataProvider.getUser(userDataProvider.getAuthUserKey(), new OnSingleUserRetrievedListener() {
//            @Override
//            public void OnSingleUserRetrieved(User user) {
//                for(String product : user.getShoppingList()){
//                    addItemToList(product);
//                }
//            }
//        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder addItem = new AlertDialog.Builder(getContext());
                addItem.setTitle("Добавление продукта");
                View dialogLayout = inflater.inflate(R.layout.dialog_layout, null);
                EditText item_nameEdit = dialogLayout.findViewById(R.id.addItemEditText);
                addItem.setView(dialogLayout)
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"yes", Toast.LENGTH_LONG).show();
                        String item_name =  item_nameEdit.getText().toString();
                        if(item_name.equals("") || item_name.isEmpty()){
                            item_nameEdit.setError("заполните поле");
                        }else{
                            addItemToList(item_name);
                            userDataProvider.getUser(userDataProvider.getAuthUserKey(), new OnSingleUserRetrievedListener() {
                                @Override
                                public void OnSingleUserRetrieved(User user) {
                                    user.getShoppingList().add(item_name);
                                    userDataProvider.updateUser(user);

                                }
                            });
//                            alertDialog.cancel();
                        }

                    }
                })
                .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"cancel", Toast.LENGTH_LONG).show();
//                        alertDialog.cancel();
                    }
                });
                addItem.setCancelable(false);
                AlertDialog alertDialog = addItem.create();
                alertDialog.show();
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userDataProvider.getUser(userDataProvider.getAuthUserKey(), new OnSingleUserRetrievedListener() {
            @Override
            public void OnSingleUserRetrieved(User user) {
                for(String product : user.getShoppingList()){
                    addItemToList(product);
                }
            }
        });
    }

    public void addItemToList(String name){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.shop_list_item, null);
        TextView name_textView = view.findViewById(R.id.TextViewItemName);
        name_textView.setText(name);
        itemList.addView(view);
    }
}
