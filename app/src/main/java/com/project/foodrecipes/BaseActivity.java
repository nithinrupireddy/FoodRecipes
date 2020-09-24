package com.project.foodrecipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public abstract class BaseActivity extends AppCompatActivity {


    public ProgressBar mprogressBar;
    @Override
    public void setContentView(int layoutResID) {

        ConstraintLayout constraintLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base,null);
        FrameLayout frameLayout = constraintLayout.findViewById(R.id.activity_content);
        mprogressBar = constraintLayout.findViewById(R.id.progressBar);

         mprogressBar.setVisibility(View.GONE);
         getLayoutInflater().inflate(layoutResID,frameLayout,true);

        super.setContentView(constraintLayout);
    }

    public void showProgressBar(boolean visible){
        mprogressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }


}
