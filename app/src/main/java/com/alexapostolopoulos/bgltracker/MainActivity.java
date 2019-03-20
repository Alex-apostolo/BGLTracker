package com.alexapostolopoulos.bgltracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    public void createInsulinActivity(View v) {
        Intent intent = new Intent(this, AddInsulinActivity.class);
        startActivity(intent);
    }

    public void createGlucoseActivity(View v) {
        Intent intent = new Intent(this, AddGlucoseActivity.class);
        startActivity(intent);
    }

    public void createMedicationActivity(View v) {
        try {
            Intent intent = new Intent(this, AddMedicationActivity.class);
            startActivity(intent);
        }
        catch (Exception e) {
            Log.d("cannot_open_medication",e.getMessage());}
    }
}
