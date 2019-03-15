package com.alexapostolopoulos.bgltracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AddGlucoseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_glucose);
        EditText date = findViewById(R.id.date_inputField);
        EditText time = findViewById(R.id.time);
        date.setText(LocalDate.now().toString());
        time.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add,menu);
        return true;
    }

    public void saveChanges(MenuItem menuItem) {
        EditText date = findViewById(R.id.date_inputField);
        EditText time = findViewById(R.id.time_inputField);
        EditText sugarConcentration = findViewById(R.id.sugarCon_inputField);
        EditText notes = findViewById(R.id.note_inputField);
    }
}
