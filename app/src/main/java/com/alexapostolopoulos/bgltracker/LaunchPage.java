package com.alexapostolopoulos.bgltracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alexapostolopoulos.bgltracker.MainActivity.MainActivity;
import com.alexapostolopoulos.bgltracker.Model.Patient;
import com.alexapostolopoulos.bgltracker.Model.Prescription;

import java.util.ArrayList;

public class LaunchPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrievePatient();
        setContentView(R.layout.activity_launch_page);
    }

    protected void retrievePatient() {
        BGLMain appMain = ((BGLMain) this.getApplication());
        Cursor result = appMain.dbMain.rawQuery("SELECT * FROM Patient", null);

        if (result.getCount() == 0) {
            startActivity(new Intent(this, AddPatientActivity.class));
        }

        if (result.getCount() > 0) {
            result.moveToFirst();
            int id = result.getInt(0);
            appMain.curPatient = new Patient(id,
                    result.getString(1),
                    result.getFloat(1),
                    result.getFloat(2),
                    result.getFloat(3),
                    result.getFloat(4));

            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
