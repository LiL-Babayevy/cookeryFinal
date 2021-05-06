package com.example.cookeryfinal.recipe_related;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cookeryfinal.R;

import java.util.ArrayList;

public class IngredientAdapter extends ArrayAdapter<Ingredient> {
    private Context context;
    private int resource;
    private ArrayList<Ingredient> ingredients;

    public IngredientAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Ingredient> ingredients) {
        super(context, resource, ingredients);
        this.context = context;
        this.resource = resource;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(this.context).inflate(resource, null);

        Ingredient r = ingredients.get(position);
        TextView ingredientName = view.findViewById(R.id.IngredientName);
        ingredientName.setText(r.getIngredient_name());

        TextView ingredientAmount = view.findViewById(R.id.IngredientAmount);
        ingredientAmount.setText(r.getAmount());
        return view;
    }
}
