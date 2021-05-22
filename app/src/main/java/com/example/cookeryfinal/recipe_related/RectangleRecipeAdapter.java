package com.example.cookeryfinal.recipe_related;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookeryfinal.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RectangleRecipeAdapter extends RecyclerView.Adapter<RectangleRecipeAdapter.RecipeViewHolder>{

    private ArrayList<Recipe> recipes;
    private OnRecipeListener onRecipeListener;


    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView image;
        OnRecipeListener listener;

        RecipeViewHolder(View recipeView, OnRecipeListener listener){
            super(recipeView);
            title = recipeView.findViewById(R.id.recipeRect_title);
            image = recipeView.findViewById(R.id.recipeRect_image);
            this.listener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onRecipeClick(getAdapterPosition());
        }
    }

    public RectangleRecipeAdapter(ArrayList<Recipe> recipes, OnRecipeListener onRecipeListener){
        this.recipes = recipes;
        this.onRecipeListener = onRecipeListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recipe_rectangle, parent, false);
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(v, onRecipeListener);
        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe current_recipe = recipes.get(position);

        holder.image.setImageResource(R.drawable.no_image);
        holder.title.setText(current_recipe.getRecipe_name());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public interface OnRecipeListener{
        void onRecipeClick(int position);
    }
}
