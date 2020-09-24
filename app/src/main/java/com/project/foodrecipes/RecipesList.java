package com.project.foodrecipes;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class RecipesList extends BaseActivity {

    private static final String TAG = "Recipes_list";

    //UI Components
    private Button testButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        initializing_views();

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mprogressBar.getVisibility() == View.VISIBLE){
                    Log.d(TAG, "onClick: "+"hide it");
                    showProgressBar(false);
                }else{
                    Log.d(TAG, "onClick: "+"show it");
                    showProgressBar(true);
                }
            }
        });

    }

    private void initializing_views() {
        testButton = findViewById(R.id.testButton);
    }
}
