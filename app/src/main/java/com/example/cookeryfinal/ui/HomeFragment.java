package com.example.cookeryfinal.ui;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookeryfinal.R;
import com.example.cookeryfinal.recipe_related.OnRecipeRetrievedListener;
import com.example.cookeryfinal.recipe_related.Recipe;
import com.example.cookeryfinal.recipe_related.RecipeDataProvider;
import com.example.cookeryfinal.recipe_related.RecipePage;
import com.example.cookeryfinal.recipe_related.RectangleRecipeAdapter;
import com.example.cookeryfinal.recipe_related.SquareRecipeAdapter;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements RectangleRecipeAdapter.OnRecipeListener , SquareRecipeAdapter.OnRecipeListener{

    private View root;

    private ArrayList<Recipe> recipeArrayList;

    private ArrayList<Recipe> filtered_recipeArrayList;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView_breakfast;
    private RecyclerView mRecyclerView_lunch;
    private RecyclerView mRecyclerView_dinner;
    private RecyclerView mRecyclerView_dessert;
    private RectangleRecipeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private RecipeDataProvider provider;
    private  OnRecipeRetrievedListener listener;

    public HomeFragment(){
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        recipeArrayList = new ArrayList<>();

        mRecyclerView = root.findViewById(R.id.recyclerView_search);
        mRecyclerView_breakfast = root.findViewById(R.id.recyclerView_breakfast);
        mRecyclerView_lunch = root.findViewById(R.id.recyclerView_lunch);
        mRecyclerView_dinner = root.findViewById(R.id.recyclerView_dinner);
        mRecyclerView_dessert = root.findViewById(R.id.recyclerView_dessert);
        mRecyclerView.setHasFixedSize(true);
        provider = RecipeDataProvider.getInstance();


        setHorizontalRecyclerView(mRecyclerView_breakfast, "завтрак");
        setHorizontalRecyclerView(mRecyclerView_lunch, "обед");
        setHorizontalRecyclerView(mRecyclerView_dinner, "ужин");
        setHorizontalRecyclerView(mRecyclerView_dessert, "десерт");

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mAdapter = new RectangleRecipeAdapter(recipeArrayList, this);

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mRecyclerView_breakfast.setAdapter(null);
                mRecyclerView_dinner.setAdapter(null);
                mRecyclerView_dessert.setAdapter(null);
                mRecyclerView_lunch.setAdapter(null);

                mLayoutManager = new LinearLayoutManager(getContext());
                mRecyclerView.setLayoutManager(mLayoutManager);

                listener = new OnRecipeRetrievedListener(){
                    @Override
                    public void onRecipeRetrieved(ArrayList<Recipe> recipes) {
                        recipeArrayList.clear();
                        for(int i = recipes.size()-1; i>=0; i--){
                            recipeArrayList.add(recipes.get(i));
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                };

                provider.getRecipes(listener);

                if (searchItem != null) {
                    searchView = (SearchView) searchItem.getActionView();
                }
                if (searchView != null) {
                    searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

                    queryTextListener = new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextChange(String newText) {
                            filtered_recipeArrayList = new ArrayList<>(recipeArrayList);

                            if (newText == null || newText.trim().isEmpty()) {
                                mRecyclerView.setAdapter(mAdapter);
                                return false;
                            }

                            for(Recipe r : recipeArrayList){
                                if(!(r.getRecipe_name().contains(newText))){
                                    filtered_recipeArrayList.remove(r);
                                }
                            }

                            mAdapter = new RectangleRecipeAdapter(filtered_recipeArrayList,HomeFragment.this::onRecipeClick);
                            mRecyclerView.setAdapter(mAdapter);
                            return false;
                        }
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return true;
                        }
                    };
                    searchView.setOnQueryTextListener(queryTextListener);
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mRecyclerView.setAdapter(null);
                setHorizontalRecyclerView(mRecyclerView_breakfast, "завтрак");
                setHorizontalRecyclerView(mRecyclerView_lunch, "обед");
                setHorizontalRecyclerView(mRecyclerView_dinner, "ужин");
                setHorizontalRecyclerView(mRecyclerView_dessert, "десерт");

                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                mRecyclerView.setAdapter(mAdapter);
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }


    public void setHorizontalRecyclerView(RecyclerView recyclerView, String category){
        ArrayList<Recipe> recipesByCategory = new ArrayList<>();
        SquareRecipeAdapter squareRecipeAdapter = new SquareRecipeAdapter(recipesByCategory, this);

        OnRecipeRetrievedListener recipe_listener = new OnRecipeRetrievedListener() {
            @Override
            public void onRecipeRetrieved(ArrayList<Recipe> recipes) {
                recipesByCategory.clear();
                for(int i = recipes.size()-1; i>=0; i--){
                    if(recipes.get(i).getStatus().equals("my_recipes")){
                        recipesByCategory.add(recipes.get(i));
                    }
                }
                squareRecipeAdapter.notifyDataSetChanged();
            }
        };

        provider.getRecipesByCategory(recipe_listener, category);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setAdapter(squareRecipeAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(getContext(), RecipePage.class);
        intent.putExtra("clicked_recipe", recipe.getRecipeId());
        startActivity(intent);
    }

    @Override
    public void onRecipeLongClick(int position) {
    }

    @Override
    public void onRecipeClick(int position) {
        Recipe recipe = recipeArrayList.get(position);
        Intent intent = new Intent(getContext(), RecipePage.class);
        intent.putExtra("clicked_recipe", recipe.getRecipeId());
        startActivity(intent);
    }
}