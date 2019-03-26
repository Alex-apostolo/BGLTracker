package com.alexapostolopoulos.bgltracker.MainActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alexapostolopoulos.bgltracker.AddGlucoseActivity;
import com.alexapostolopoulos.bgltracker.AddInsulinActivity;
import com.alexapostolopoulos.bgltracker.AddMedicationActivity;
import com.alexapostolopoulos.bgltracker.AddPatientActivity;
import com.alexapostolopoulos.bgltracker.BGLGraphActivity;
import com.alexapostolopoulos.bgltracker.R;

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
        switch (item.getItemId()) {

            case R.id.addPatient_button:
            {
                Intent intent = new Intent(this, AddPatientActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.BGLGraph_button:
            {
                Intent intent = new Intent(this, BGLGraphActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.filter:
                //filter code goes here
                break;
            case R.id.sort:
                //sort code goes here
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onButtonOptionsItemSelected(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.fab_menu_glucose:
                intent = new Intent(this, AddGlucoseActivity.class);
                startActivity(intent);
                break;
            case R.id.fab_menu_insulin:
                intent = new Intent(this, AddInsulinActivity.class);
                startActivity(intent);
                break;
            case R.id.fab_menu_medication:
                intent = new Intent(this, AddMedicationActivity.class);
                startActivity(intent);
        }
    }
}
