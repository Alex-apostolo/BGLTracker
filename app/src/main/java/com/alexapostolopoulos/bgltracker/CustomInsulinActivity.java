package com.alexapostolopoulos.bgltracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class CustomInsulinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_insulin);

        //Insulin name
        EditText insulinName = findViewById(R.id.customInsulin_name);

        //Onset
        View onsetView = findViewById(R.id.customInsulin_onset);
        EditText onsetMax = onsetView.findViewById(R.id.customInsulin_max_editText);
        EditText onsetMin = onsetView.findViewById(R.id.customInsulin_min_editText);
        Spinner onsetUnitSpinner = onsetView.findViewById(R.id.customInsulin_unit_spinner);

        //Peak
        View peakView = findViewById(R.id.customInsulin_peak);
        EditText peakMax = peakView.findViewById(R.id.customInsulin_max_editText);
        EditText peakMin = peakView.findViewById(R.id.customInsulin_min_editText);
        Spinner peakUnitSpinner = peakView.findViewById(R.id.customInsulin_unit_spinner);

        //Duration
        View durationView = findViewById(R.id.customInsulin_duration);
        EditText durationMax = durationView.findViewById(R.id.customInsulin_max_editText);
        EditText durationMin = durationView.findViewById(R.id.customInsulin_min_editText);
        Spinner durationUnitSpinner = durationView.findViewById(R.id.customInsulin_unit_spinner);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Do when user presses check
        return super.onOptionsItemSelected(item);
    }
}
