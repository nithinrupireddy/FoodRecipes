package com.project.foodrecipes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.foodrecipes.adapters.RecipeRecyclerAdapter;
import com.project.foodrecipes.adapters.onRecipeListener;
import com.project.foodrecipes.models.Recipe;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        initializing_views();
        init_recyclerview();

        recipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);

        subscribeObservers();
        test_retrofit_response();

    }

    private void init_recyclerview() {
        recyclerView = findViewById(R.id.recipe_list);
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
                      recipeRecyclerAdapter.setRecipes(recipes);
                  }
              }
          }
      });
    }

    private void initializing_views() {
    }

    private void test_retrofit_response(){
        recipeListViewModel.searchRecipesApi("Chicken",1);
    }

    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {

    }
}
