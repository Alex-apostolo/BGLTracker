package com.alexapostolopoulos.bgltracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.addPatient_button) {
            Intent intent = new Intent(this, AddPatientActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
        Intent intent = new Intent(this, AddMedicationActivity.class);
        startActivity(intent);
    }
}
