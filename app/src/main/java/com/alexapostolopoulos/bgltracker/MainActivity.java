package com.alexapostolopoulos.bgltracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.time.LocalDate;
import java.time.LocalTime;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomRowData firstRow = new CustomRowData("Insulin", "5670 mmlg", LocalDate.of(2018,12,20), LocalTime.of(20,20));
        CustomRowData secondRow = new CustomRowData("Glucose", "lol mmlg", LocalDate.of(2018,12,10), LocalTime.of(20,20));
        CustomRowData[] myData = {firstRow,secondRow};
        ListAdapter adapter = new CustomAdapter(this, myData);
        ListView BGLlist = findViewById(R.id.BGLlist);
        BGLlist.setAdapter(adapter);
        BGLlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

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
        Intent intent = new Intent(this, AddMedicationActivity.class);
        startActivity(intent);
    }
}
