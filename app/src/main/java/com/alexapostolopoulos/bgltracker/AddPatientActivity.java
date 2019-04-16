package com.alexapostolopoulos.bgltracker;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.alexapostolopoulos.bgltracker.MainActivity.MainActivity;
import com.alexapostolopoulos.bgltracker.Model.Patient;

public class AddPatientActivity extends AppCompatActivity {

    BGLMain appMain;
    Patient curPatient;
    EditText txtName;
    EditText txtCIRatio;
    EditText txtCorrection;
    EditText txtGlucoseLo;
    EditText txtGlucoseHi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        appMain = (BGLMain)this.getApplication();
        txtName = findViewById(R.id.addPatient_name_editText);
        txtCIRatio = findViewById(R.id.addPatient_CarbInsulin_editText);
        txtCorrection = findViewById(R.id.addPatient_correctionDose_editText);
        txtGlucoseLo = findViewById(R.id.addPatient_lowBGL_editText);
        txtGlucoseHi = findViewById(R.id.addPatient_highBGL_editText);
        if(appMain.curPatient != null)
        {
            curPatient = appMain.curPatient;
            txtName.setText(curPatient.getName());
            txtCIRatio.setText(String.valueOf(curPatient.getCIRatio()));
            txtCorrection.setText(String.valueOf(curPatient.getCorrectionDose()));
            txtGlucoseLo.setText(String.valueOf(curPatient.getGlucoseWarningLower()));
            txtGlucoseHi.setText(String.valueOf(curPatient.getGlucoseWarningUpper()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Do when user presses check
        try {
            String enteredName = txtName.getText().toString();
            String enteredRatio = txtCIRatio.getText().toString();
            String enteredCorrection = txtCorrection.getText().toString();
            String enteredLo = txtGlucoseLo.getText().toString();
            String enteredHi = txtGlucoseHi.getText().toString();
            if (enteredName.length() <= 20 &&
                    BGLMain.chkFloat(enteredRatio) &&
                    BGLMain.chkFloat(enteredCorrection) &&
                    BGLMain.chkFloat(enteredLo) &&
                    BGLMain.chkFloat(enteredHi)) {

                if (appMain.curPatient == null) {
                    curPatient = new Patient(1,
                            enteredName,
                            Float.parseFloat(txtCIRatio.getText().toString()),
                            Float.parseFloat(txtCorrection.getText().toString()),
                            Float.parseFloat(txtGlucoseLo.getText().toString()),
                            Float.parseFloat(txtGlucoseHi.getText().toString()),
                            -1,
                            -1,
                            -1,
                            -1);
                    appMain.curPatient = curPatient;
                    insertPatient();
                } else {
                    int id = curPatient.getID();
                    curPatient = new Patient(id,
                            enteredName,
                            Float.parseFloat(txtCIRatio.getText().toString()),
                            Float.parseFloat(txtCorrection.getText().toString()),
                            Float.parseFloat(txtGlucoseLo.getText().toString()),
                            Float.parseFloat(txtGlucoseHi.getText().toString()),
                            appMain.curPatient.getDailyMean(),
                            appMain.curPatient.getWeeklyMean(),
                            appMain.curPatient.getDailyVariance(),
                            appMain.curPatient.getWeeklyVariance());
                    appMain.curPatient = curPatient;
                    updatePatient();
                }
                startActivity(new Intent(this, MainActivity.class));
            }
        }
        catch (Exception e) {}
        return super.onOptionsItemSelected(item);
    }

    public void insertPatient()
    {
        ContentValues row = new ContentValues();
        row.put("Name",curPatient.getName());
        row.put("CarbInsulinRatio",curPatient.getCIRatio());
        row.put("CorrectionDose",curPatient.getCorrectionDose());
        row.put("GlucoseWarningLower",curPatient.getGlucoseWarningLower());
        row.put("GlucoseWarningUpper",curPatient.getGlucoseWarningUpper());
        row.put("DailyMean",-1);
        row.put("WeeklyMean",-1);
        row.put("DailyVariance",-1);
        row.put("WeeklyVariance",-1);
        appMain.dbMain.insert("Patient",null,row);
    }

    public void updatePatient()
    {
        ContentValues row = new ContentValues();
        row.put("Name",curPatient.getName());
        row.put("CarbInsulinRatio",curPatient.getCIRatio());
        row.put("CorrectionDose",curPatient.getCorrectionDose());
        row.put("GlucoseWarningLower",curPatient.getGlucoseWarningLower());
        row.put("GlucoseWarningUpper",curPatient.getGlucoseWarningUpper());
        appMain.dbMain.update("Patient",row,"ID="+String.valueOf(curPatient.getID()),null);
    }

}