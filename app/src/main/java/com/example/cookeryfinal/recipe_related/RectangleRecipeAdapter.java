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

public class RectangleRecipeAdapter extends RecyclerView.Adapter<RectangleRecipeAdapter.RecipeViewHolder> implements Filterable {

//    private Context context;
//    private int resource;
    private List<Recipe> recipes;
    private List<Recipe> recipeArrayList_full;

    class RecipeViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView image;

        RecipeViewHolder(View recipeView){
            super(recipeView);
            title = recipeView.findViewById(R.id.recipeRect_title);
            image = recipeView.findViewById(R.id.recipeRect_image);
        }
    }

    public RectangleRecipeAdapter(List<Recipe> recipes){
        this.recipes = recipes;
        recipeArrayList_full = new ArrayList<>(recipes);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recipe_rectangle, parent, false);
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(v);
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



//    public RectangleRecipeAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Recipe> recipes) {
//        super(context, resource, recipes);
//        this.context = context;
//        this.resource = resource;
//        this.recipes = recipes;
//        recipeArrayList_full = new ArrayList<>(this.recipes);
//    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View view = LayoutInflater.from(this.context).inflate(resource, null);
//
//        Recipe r = recipes.get(position);
//
////        ImageView image = view.findViewById(R.id.recipeRect_image);
//        TextView recipeName = view.findViewById(R.id.recipeRect_title);
////        image.setImageResource(R.drawable.no_image);
//        recipeName.setText(r.getRecipe_name());
//        return view;
//    }


    @Override
    public Filter getFilter() {
        return recipeFilter;
    }

    private Filter recipeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Recipe> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(recipeArrayList_full);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Recipe recipe : recipeArrayList_full){
                    if(recipe.getRecipe_name().toLowerCase().contains(filterPattern)){
                        filteredList.add(recipe);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            recipes.clear();
            recipes.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
