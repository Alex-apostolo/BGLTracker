package com.alexapostolopoulos.bgltracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

public class GoalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        EditText dailyMean = findViewById(R.id.dailyMean_editText);
        EditText weeklyMean = findViewById(R.id.weeklyMean_editText);

        EditText dailyVariance = findViewById(R.id.dailyVariance);
        EditText weeklyVariance = findViewById(R.id.weeklyVariance);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add,menu);
        return true;
    }
}
