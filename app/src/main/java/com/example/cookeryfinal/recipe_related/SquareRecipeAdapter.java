package com.example.cookeryfinal.recipe_related;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookeryfinal.R;

import java.util.ArrayList;

public class SquareRecipeAdapter extends RecyclerView.Adapter<SquareRecipeAdapter.ViewHolder>{

    private ArrayList<Recipe> recipeList;
    private OnRecipeListener onRecipeListener;

    public SquareRecipeAdapter(ArrayList<Recipe> recipes, OnRecipeListener onRecipeListener){
        this.recipeList = recipes;
        this.onRecipeListener = onRecipeListener;

    }


    @NonNull
    @Override
    public SquareRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recipe_square, parent, false);
        SquareRecipeAdapter.ViewHolder recipeViewHolder = new ViewHolder(v, onRecipeListener);
        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SquareRecipeAdapter.ViewHolder holder, int position) {
        Recipe current_recipe = recipeList.get(position);

        holder.imageView.setImageResource(R.drawable.no_image);

        holder.title.setText(current_recipe.getRecipe_name());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView imageView;
        OnRecipeListener onRecipeListener;

        ViewHolder(View itemView, OnRecipeListener onRecipeListener) {
            super(itemView);
            title = itemView.findViewById(R.id.recipeSquare_name);
            imageView = itemView.findViewById(R.id.recipeSquare_image);
            int height = getScreenHeight()/11 * 2; // ((display.getWidth()*20)/100)
            int width = getScreenWidth()/7*2;
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(height, width);
            imageView.setLayoutParams(parms);
            this.onRecipeListener = onRecipeListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecipeListener.onRecipeClick(recipeList.get(getAdapterPosition()));
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onRecipeListener.onRecipeLongClick(getAdapterPosition());
                    return false;
                }
            });
        }


    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return recipeList.get(id).getRecipe_name();
    }

    public interface OnRecipeListener{
        void onRecipeClick(Recipe recipe);
        void onRecipeLongClick(int position);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
