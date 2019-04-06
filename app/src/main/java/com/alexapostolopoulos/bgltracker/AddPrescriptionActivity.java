package com.alexapostolopoulos.bgltracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alexapostolopoulos.bgltracker.MainActivity.MainActivity;
import com.alexapostolopoulos.bgltracker.Model.Insulin;
import com.alexapostolopoulos.bgltracker.Model.Prescription;
import com.alexapostolopoulos.bgltracker.Model.TemplateInsulin;

import java.util.Date;

public class AddPrescriptionActivity extends AppCompatActivity {


    boolean isNew;
    Prescription curPrescription;
    BGLMain appMain;
    String type;
    Spinner typeSpinner;
    Spinner freqSpinner;
    Spinner adviceSpinner;
    EditText txtName;
    Spinner insulinSpinner;
    EditText txtFreq;
    EditText txtQuantity;
    EditText txtNotes;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prescription);
        appMain = (BGLMain) this.getApplication();
        Bundle data = getIntent().getExtras();
        isNew = data.getBoolean("isNew");
        type = data.getString("type");
        txtFreq = findViewById(R.id.addPresc_X_editText);
        txtQuantity = findViewById(R.id.addPrescr_quantity_editText);
        txtNotes = findViewById(R.id.addPrescr_notes_editText);
        adviceSpinner = findViewById(R.id.addPrescr_advice_spinner);

        //Prescription input fields
        configureTypeSpinner();
        configureFrequencySpinner();

        if (!isNew) {
            curPrescription = appMain.curPatient.findPrescription(data.getInt("prescriptionID")).clone();
            type = curPrescription.getType();
            switch (type) {
                case "General":
                    typeSpinner.setSelection(0);
                    break;
                case "Insulin":
                    typeSpinner.setSelection(1);
                    configureInsulinView();
                    insulinSpinner.setSelection(curPrescription.getInsulinTemplateID() - 1);
                    break;
            }
            freqSpinner.setSelection(curPrescription.getFreqIndex());
            txtFreq.setText(String.valueOf(curPrescription.getValue()));
            txtQuantity.setText(String.valueOf(curPrescription.getQuantity()));
            adviceSpinner.setSelection(curPrescription.getAdvice());
            txtNotes.setText(curPrescription.getComments());
        } else {
            if (type.equals("Insulin")) {
                typeSpinner.setSelection(1);
                configureInsulinView();
                refreshAdviceSpinner(1);
            }
            else
            {
                refreshAdviceSpinner(0);
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (BGLMain.chkFloat(txtQuantity.getText().toString())) {
            try {
                int id;
                switch (type) {
                    case "General":
                        if (isNew) {
                            curPrescription = new Prescription(0,
                                    "General",
                                    0,
                                    txtName.getText().toString(),
                                    Integer.parseInt(txtFreq.getText().toString()),
                                    freqSpinner.getSelectedItemPosition(),
                                    Float.parseFloat(txtQuantity.getText().toString()),
                                    "unit",
                                    adviceSpinner.getSelectedItemPosition(),
                                    txtNotes.getText().toString());
                            id = appMain.curPatient.addPrescription(appMain, curPrescription);
                        } else {
                            id = curPrescription.getID();
                            curPrescription = new Prescription(id,
                                    "General",
                                    0,
                                    txtName.getText().toString(),
                                    Integer.parseInt(txtFreq.getText().toString()),
                                    freqSpinner.getSelectedItemPosition(),
                                    Float.parseFloat(txtQuantity.getText().toString()),
                                    "unit",
                                    adviceSpinner.getSelectedItemPosition(),
                                    txtNotes.getText().toString());
                            appMain.curPatient.updatePrescription(appMain, curPrescription);
                        }
                        Intent startGeneral = new Intent(this, AddMedicationActivity.class);
                        startGeneral.putExtra("callingForm", "Prescription");
                        startGeneral.putExtra("selected", id);
                        startGeneral.putExtra("isNew",true);
                        startActivity(startGeneral);
                        break;
                    case "Insulin":
                        if (isNew) {
                            curPrescription = new Prescription(0,
                                    "Insulin",
                                    insulinSpinner.getSelectedItemPosition() + 1,
                                    insulinSpinner.getSelectedItem().toString(),
                                    Integer.parseInt(txtFreq.getText().toString()),
                                    freqSpinner.getSelectedItemPosition(),
                                    Float.parseFloat(txtQuantity.getText().toString()),
                                    "unit",
                                    adviceSpinner.getSelectedItemPosition(),
                                    txtNotes.getText().toString());
                            id = appMain.curPatient.addPrescription(appMain, curPrescription);
                        } else {
                            id = curPrescription.getID();
                            curPrescription = new Prescription(id,
                                    "Insulin",
                                    insulinSpinner.getSelectedItemPosition() + 1,
                                    insulinSpinner.getSelectedItem().toString(),
                                    Integer.parseInt(txtFreq.getText().toString()),
                                    freqSpinner.getSelectedItemPosition(),
                                    Float.parseFloat(txtQuantity.getText().toString()),
                                    "unit",
                                    adviceSpinner.getSelectedItemPosition(),
                                    txtNotes.getText().toString());
                            appMain.curPatient.updatePrescription(appMain, curPrescription);
                        }
                        Intent startInsulin = new Intent(this, AddInsulinActivity.class);
                        startInsulin.putExtra("callingForm", "Prescription");
                        startInsulin.putExtra("selected", id);
                        startInsulin.putExtra("isNew", true);
                        startActivity(startInsulin);
                }


            } catch (Exception e) {
                Log.d("error_committing_prescription", e.getMessage());
            }
        }
        return super.onOptionsItemSelected(item);
    }


        private void configureTypeSpinner () {
            typeSpinner = findViewById(R.id.addPrescr_type_spinner);
            ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.addPresc_type_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            typeSpinner.setAdapter(adapter);
            typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    type = parent.getItemAtPosition(position).toString();
                    switch (type) {
                        case "General":
                            configureGeneralView();
                            break;

                        case "Insulin":
                            configureInsulinView();

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        public void configureGeneralView ()
        {
            LinearLayout prescLayout = findViewById(R.id.addPrescr_presc_layout);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            params.weight = 1;
            params.gravity = Gravity.CENTER_VERTICAL;
            prescLayout.removeAllViews();
            TextView nameTextView = new TextView(AddPrescriptionActivity.this);
            txtName = new EditText(AddPrescriptionActivity.this);

            nameTextView.setText("Name");
            nameTextView.setTextAppearance(R.style.CustomTextTitle);
            prescLayout.addView(nameTextView, params);

            txtName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            prescLayout.addView(txtName, params);
        }

        public void configureInsulinView ()
        {
            LinearLayout prescLayout = findViewById(R.id.addPrescr_presc_layout);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            params.weight = 1;
            params.gravity = Gravity.CENTER_VERTICAL;
            prescLayout.removeAllViews();
            TextView selectInsulinTextView = new TextView(AddPrescriptionActivity.this);
            insulinSpinner = new Spinner(AddPrescriptionActivity.this);
            ArrayAdapter<TemplateInsulin> adapter =
                    new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, appMain.insulinMasterList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            insulinSpinner.setAdapter(adapter);


            selectInsulinTextView.setText("Select Insulin");
            selectInsulinTextView.setTextAppearance(R.style.CustomTextTitle);
            prescLayout.addView(selectInsulinTextView, params);

            //Set custom adapter to spinner before adding it to the view and also populate all its fields
            prescLayout.addView(insulinSpinner);
        }

        private void configureFrequencySpinner () {
            freqSpinner = findViewById(R.id.addPrescr_freq_spinner);
            ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.addPresc_freq_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            freqSpinner.setAdapter(adapter);
        }

        private void refreshAdviceSpinner(int typeSelection) {
            String[][] adviceText = new String[][]{{"","Before meal","After meal"},{"","Correction dose","With meal"}};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, adviceText[typeSelection]);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adviceSpinner.setAdapter(adapter);
        }
    }

