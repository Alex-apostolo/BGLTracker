package com.alexapostolopoulos.bgltracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class AddPrescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prescription);
        configureTypeSpinner();
        configureFrequencySpinner();
        configureAdviceSpinner();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add,menu);
        return true;
    }

    private void configureTypeSpinner() {
        Spinner typeSpinner = findViewById(R.id.addPrescr_type_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.addPresc_type_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(parent.getItemAtPosition(position).toString()) {
                    case "General":
//                        GridLayout layout = findViewById(R.id.addPrescr_gridLayout);
//                        TextView nameView = new TextView(getBaseContext());
//                        nameView.setText("Name");
//                        nameView.setTextAppearance(R.style.CustomTextTitle);
//                        EditText editName = new EditText(getBaseContext());
//                        editName.setText("");
//
//                        GridLayout.LayoutParams params = new GridLayout.LayoutParams(layout.getLayoutParams());
//                        params.height = 0;
//                        params.width = 0;
//                        params.rowSpec = GridLayout.spec(2, 2, 1f);
//                        nameView.setLayoutParams(params);
//                        params.columnSpec = GridLayout.spec(3, 2, 1f);
//                        editName.setLayoutParams(params);
//
//                        layout.addView(nameView);
//                        layout.addView(editName);
                        break;
                    case "Insulin":break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void configureFrequencySpinner() {
        Spinner freqSpinner = findViewById(R.id.addPrescr_freq_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.addPresc_freq_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        freqSpinner.setAdapter(adapter);
        freqSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void configureAdviceSpinner() {
        Spinner adviceSpinner = findViewById(R.id.addPrescr_advice_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.addPresc_advice_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adviceSpinner.setAdapter(adapter);
        adviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
