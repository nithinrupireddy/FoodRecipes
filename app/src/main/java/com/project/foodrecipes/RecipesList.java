package com.project.foodrecipes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.project.foodrecipes.adapters.RecipeRecyclerAdapter;
import com.project.foodrecipes.adapters.onRecipeListener;
import com.project.foodrecipes.models.Recipe;
import com.project.foodrecipes.util.VerticalSpacingItemDecorator;
import com.project.foodrecipes.viewmodels.RecipeListViewModel;

import java.util.List;

//this is recipesList Activity
public class RecipesList extends BaseActivity implements onRecipeListener {

    private static final String TAG = "Recipes_list";
    private RecipeListViewModel recipeListViewModel;
    private Context context = this;



    //UI Components
    private RecyclerView recyclerView;
    private RecipeRecyclerAdapter recipeRecyclerAdapter;
    private SearchView searchView;
    private LottieAnimationView rocket_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        initializing_views();
        init_recyclerview();

        recipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);

        subscribeObservers();
        init_search_view();

        if(!recipeListViewModel.isViewingRecipes()){
            //display search categories
            displaySearchCategories();
        }


    }

    private void init_recyclerview() {
        recyclerView = findViewById(R.id.recipe_list);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        recyclerView.addItemDecoration(itemDecorator);
        recipeRecyclerAdapter = new RecipeRecyclerAdapter(this);
        recyclerView.setAdapter(recipeRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void subscribeObservers(){
      recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
          @Override
          public void onChanged(List<Recipe> recipes) {
              if(recipes!=null){
                  for(Recipe recipe:recipes){
                      Log.d(TAG, "getting recipes :"+recipe.getTitle());
                      //rocket_spinner.setVisibility(View.GONE);
                      recipeRecyclerAdapter.setRecipes(recipes);
                  }
              }
          }
      });
    }

    private void initializing_views() {
        searchView = findViewById(R.id.searchview);
        rocket_spinner = findViewById(R.id.rocket_spinner);
    }

    private void init_search_view(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                recipeRecyclerAdapter.displayLoading();
                //rocket_spinner.setVisibility(View.VISIBLE);
                recipeListViewModel.searchRecipesApi(query,1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {
        recipeRecyclerAdapter.displayLoading();
        //rocket_spinner.setVisibility(View.VISIBLE);
        recipeListViewModel.searchRecipesApi(category,1);
    }

    private void displaySearchCategories(){
        //rocket_spinner.setVisibility(View.GONE);
        recipeListViewModel.setIsViewingRecipes(false);
        recipeRecyclerAdapter.displaySearchCategories();
    }
}
