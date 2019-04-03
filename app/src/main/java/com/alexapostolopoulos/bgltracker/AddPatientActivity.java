package com.alexapostolopoulos.bgltracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class AddPatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        //Patient entry fields
        EditText patientName = findViewById(R.id.addPatient_name_editText);
        EditText carbInsulinRatio = findViewById(R.id.addPatient_CarbInsulin_editText);
        EditText correctionDose = findViewById(R.id.addPatient_correctionDose_editText);
        EditText highBGLThreshold = findViewById(R.id.addPatient_highBGL_editText);
        EditText lowBGLThreshold = findViewById(R.id.addPatient_lowBGL_editText);
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
