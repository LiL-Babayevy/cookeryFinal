package com.example.cookeryfinal.recipe_related;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookeryfinal.R;

import java.util.List;

public class SquareRecipeAdapter extends RecyclerView.Adapter<SquareRecipeAdapter.ViewHolder>{

    private List<Recipe> recipeList;
    private ItemClickListener mClickListener;
    private LayoutInflater layoutInflater;

    public SquareRecipeAdapter(List<Recipe> recipes){
        this.recipeList = recipes;
    }


    @NonNull
    @Override
    public SquareRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recipe_rectangle, parent, false);
        SquareRecipeAdapter.ViewHolder recipeViewHolder = new ViewHolder(v);
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

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipeRect_title);
            imageView = itemView.findViewById(R.id.recipeSquare_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return recipeList.get(id).getRecipe_name();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
