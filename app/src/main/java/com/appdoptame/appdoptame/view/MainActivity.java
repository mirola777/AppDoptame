package com.appdoptame.appdoptame.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.view.fragmentcontroller.FragmentController;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            FragmentController.init(this);
        } else {
            FragmentController.reload(this);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentController.onBackPressed();
    }
}