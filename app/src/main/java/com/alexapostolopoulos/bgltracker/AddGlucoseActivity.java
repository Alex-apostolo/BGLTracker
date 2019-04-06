package com.alexapostolopoulos.bgltracker;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alexapostolopoulos.bgltracker.MainActivity.MainActivity;
import com.alexapostolopoulos.bgltracker.Model.Glucose;

import java.util.Date;

public class AddGlucoseActivity extends AppCompatActivity {

    BGLMain appMain;
    Glucose curGlucose;
    boolean isNew;
    EditText txtValue;
    DateField dateField;
    EditText txtNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_glucose);
        appMain = (BGLMain)this.getApplication();
        Bundle data = getIntent().getExtras();
        isNew = data.getBoolean("isNew");
        txtValue = findViewById(R.id.addGlucose_sugarCon_inputField);
        dateField = new DateField(findViewById(R.id.addGlucose_dateTime_inputLayout));
        txtNotes = findViewById(R.id.addGlucose_notes_inputField);

        if (!isNew) {
            curGlucose = data.getParcelable("glucose");
            String date = BGLMain.sqlDateFormat.format(curGlucose.getDateTime());
            txtValue.setText(String.valueOf(curGlucose.getValue()));
            dateField.showDate(curGlucose.getDateTime());
            txtNotes.setText(curGlucose.getNotes());
        }
        dateField.initEvents();
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
            Date dateVal = dateField.parseDate();
            String valueEntered = txtValue.getText().toString();
            if(dateVal != null && BGLMain.chkFloat(valueEntered)) {
                if (isNew) {
                    curGlucose = new Glucose(0,
                            Float.parseFloat(txtValue.getText().toString()),
                            dateVal,
                            txtNotes.getText().toString());
                    insertGlucose();
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    int id = curGlucose.getID();
                    curGlucose = new Glucose(id,
                            Float.parseFloat(txtValue.getText().toString()),
                            dateVal,
                            txtNotes.getText().toString());
                    updateGlucose();
                    startActivity(new Intent(this, MainActivity.class));
                }
            }
        }
        catch (Exception e) {
            Log.d("error_committing_glucose", e.getMessage());
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertGlucose() {
        ContentValues row = new ContentValues();
        row.put("PatientID", appMain.curPatient.getID());
        row.put("Value", curGlucose.getValue());
        row.put("Date", BGLMain.sqlDateFormat.format(curGlucose.getDateTime()));
        row.put("Comments", curGlucose.getNotes());
        appMain.dbMain.insert("Glucose", null, row);
    }

    public void updateGlucose() {
        ContentValues row = new ContentValues();
        row.put("PatientID", appMain.curPatient.getID());
        row.put("Value", curGlucose.getValue());
        row.put("Date", BGLMain.sqlDateFormat.format(curGlucose.getDateTime()));
        row.put("Comments", curGlucose.getNotes());
        Log.d("ID:", String.valueOf(curGlucose.getID()));
        appMain.dbMain.update("Glucose", row, "ID =" + String.valueOf(curGlucose.getID()), null);
    }
}
