package edu.moravian.csci299.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        findViewById(R.id.backButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.backButton)){
            finish();
        }
    }
}