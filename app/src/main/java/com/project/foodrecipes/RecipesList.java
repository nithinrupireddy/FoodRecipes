package com.project.foodrecipes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));


    }

    private void init_recyclerview() {
        recyclerView = findViewById(R.id.recipe_list);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        recyclerView.addItemDecoration(itemDecorator);
        recipeRecyclerAdapter = new RecipeRecyclerAdapter(this);
        recyclerView.setAdapter(recipeRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if(!recyclerView.canScrollVertically(1)){
                    //search the next page
                    recipeListViewModel.searchNextPage();
                }

            }
        });

    }

    private void subscribeObservers(){
      recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
          @Override
          public void onChanged(List<Recipe> recipes) {
              if(recipes!=null){
                  for(Recipe recipe:recipes){
                      if(recipeListViewModel.isViewingRecipes()){
                          Log.d(TAG, "getting recipes :"+recipe.getTitle());

                          recipeListViewModel.setisPerformingQuery(false);
                          recipeRecyclerAdapter.setRecipes(recipes);
                      }
                  }
              }
          }
      });

      recipeListViewModel.isQueryExhausted().observe(this, new Observer<Boolean>() {
          @Override
          public void onChanged(Boolean aBoolean) {
              if(aBoolean){
                  recipeRecyclerAdapter.setQueryExhausted();
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
                recipeListViewModel.searchRecipesApi(query,1);
                searchView.clearFocus();
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

        Intent intent = new Intent(this,RecipeActivity.class);
        intent.putExtra("recipe",recipeRecyclerAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        searchView.clearFocus();
        recipeRecyclerAdapter.displayLoading();
        recipeListViewModel.searchRecipesApi(category,1);
    }

    private void displaySearchCategories(){

        recipeListViewModel.setIsViewingRecipes(false);
        recipeRecyclerAdapter.displaySearchCategories();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){
            case R.id.action_categories:
                displaySearchCategories();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {

        if(recipeListViewModel.isPerformingQuery()){
            //cancel the query
            recipeListViewModel.cancelRequest();
            recipeListViewModel.setisPerformingQuery(false);
        }else if(recipeListViewModel.isViewingRecipes()) {
            displaySearchCategories();
        }else{
            finish();
        }
    }
}
