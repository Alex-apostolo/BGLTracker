package com.alexapostolopoulos.bgltracker;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alexapostolopoulos.bgltracker.MainActivity.MainActivity;
import com.alexapostolopoulos.bgltracker.Model.Medication;
import com.alexapostolopoulos.bgltracker.Model.Prescription;
import com.alexapostolopoulos.bgltracker.Model.TemplateInsulin;

import java.util.ArrayList;
import java.util.Date;

public class AddMedicationActivity extends AppCompatActivity implements Prescribed{

    boolean isNew;
    Medication curMedication;
    BGLMain appMain;
    ArrayList<Prescription> generalPrescriptions;
    EditText txtName;
    EditText txtDosage;
    DateField dateField;
    EditText txtNotes;
    Spinner prescriptionSpinner;
    String callingForm;
    ListView prescriptionList;
    Dialog manageMedicationDialog;
    PrescriptionListListener listListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);
        appMain = (BGLMain)this.getApplication();
        txtName = findViewById(R.id.addMedication_name_inputField);
        txtDosage = findViewById(R.id.addMedication_dosage_editText);
        dateField = new DateField(findViewById(R.id.addMedication_dateTime_inputLayout));
        manageMedicationDialog = new Dialog(this);
        manageMedicationDialog.setContentView(R.layout.manage_entries_inputfield);
        txtNotes = findViewById(R.id.addMedication_notes_inputField);
        prescriptionSpinner = findViewById(R.id.addMedication_selectPresc_spinner);
        prescriptionList = manageMedicationDialog.findViewById(R.id.prescriptionList);
        listListener = new PrescriptionListListener(this);
        prescriptionList.setOnItemClickListener(listListener);
        Bundle data = this.getIntent().getExtras();
        isNew = data.getBoolean("isNew");
        callingForm = data.getString("callingForm");
        repopulateLists();
        if(!isNew)
        {
            curMedication = data.getParcelable("medication");
            String date = BGLMain.sqlDateFormat.format(curMedication.getDateTime());
            txtName.setText(curMedication.getName());
            txtDosage.setText(String.valueOf(curMedication.getDosage()));
            dateField.showDate(curMedication.getDateTime());
            txtNotes.setText(curMedication.getNotes());
            int selectedID = curMedication.getPrescriptionID();
            for(int pos = 0; pos < generalPrescriptions.size(); pos++)
            {
                if(generalPrescriptions.get(pos).getID() == selectedID)
                {
                    prescriptionSpinner.setSelection(pos);
                }
            }
        }

        switch(callingForm)
        {
            case "Prescription":
                int selectedID = data.getInt("selected");
                for(int pos = 0; pos < generalPrescriptions.size(); pos++)
                {
                    if(generalPrescriptions.get(pos).getID() == selectedID)
                    {
                        prescriptionSpinner.setSelection(pos);
                    }
                }
        }
    }

    public void addItem(View v)
    {
        Intent startPrescription = new Intent(this,AddPrescriptionActivity.class);
        startPrescription.putExtra("isNew",true);
        startPrescription.putExtra("callingForm","Insulin");
        startPrescription.putExtra("type","General");
        listListener.reset();
        startActivity(startPrescription);
    }

    public void removeItem(View v)
    {
        Prescription p = (Prescription)prescriptionList.getItemAtPosition(listListener.getLastPosition());
        appMain.curPatient.removePrescription(appMain,p.getID());
        listListener.reset();
        repopulateLists();
    }

    public void showPrescDialog(View v)
    {
        manageMedicationDialog.show();
    }

    public void repopulateLists()
    {
        generalPrescriptions = appMain.curPatient.filterWhitelist("General");
        ListAdapter adapter = new PrescriptionAdapter(this, generalPrescriptions.toArray(new Prescription[generalPrescriptions.size()]));
        prescriptionList.setAdapter(adapter);
        ArrayAdapter<Prescription> prescriptionAdapter =
                new ArrayAdapter<>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, generalPrescriptions);
        prescriptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prescriptionSpinner.setAdapter(prescriptionAdapter);
    }

    public void editPrescription(Prescription prescription)
    {
        Intent startPrescription = new Intent(this,AddPrescriptionActivity.class);
        startPrescription.putExtra("isNew",false);
        startPrescription.putExtra("prescriptionID",prescription.getID());
        startPrescription.putExtra("callingForm","General");
        startPrescription.putExtra("type","General");
        listListener.reset();
        startActivity(startPrescription);
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
            String enteredDosage = txtDosage.getText().toString();
            Date fullDate = dateField.parseDate();
            int prescriptionID = prescriptionSpinner.getSelectedItem() == null ? 0 : ((Prescription)prescriptionSpinner.getSelectedItem()).getID();


            if (fullDate != null && BGLMain.chkFloat(enteredDosage)) {
                if (isNew) {
                    curMedication = new Medication(0,
                            prescriptionID,
                            txtName.getText().toString(),
                            Float.parseFloat(txtDosage.getText().toString()),
                            "",
                            fullDate,
                            txtNotes.getText().toString());
                    insertMedication();
                    startActivity(new Intent(this,MainActivity.class));
                } else {
                    int id = curMedication.getID();
                    curMedication = new Medication(id,
                            prescriptionID,
                            txtName.getText().toString(),
                            Float.parseFloat(txtDosage.getText().toString()),
                            "",
                            fullDate,
                            txtNotes.getText().toString());
                    updateMedication();
                    startActivity(new Intent(this,MainActivity.class));
                }
            }
        }

        catch (Exception e) {
            Log.d("error_committing_medication",e.getMessage());}
        return super.onOptionsItemSelected(item);
    }

    public void insertMedication()
    {
        ContentValues row = new ContentValues();
        row.put("PatientID",appMain.curPatient.getID());
        row.put("PrescriptionID",curMedication.getPrescriptionID());
        row.put("Name",curMedication.getName());
        row.put("Dosage",curMedication.getDosage());
        row.put("Unit",curMedication.getUnit());
        row.put("Date",BGLMain.sqlDateFormat.format(curMedication.getDateTime()));
        row.put("Comments",curMedication.getNotes());
        appMain.dbMain.insert("Medication",null,row);
    }
    public void updateMedication()
    {
        ContentValues row = new ContentValues();
        row.put("PatientID",appMain.curPatient.getID());
        row.put("PrescriptionID",curMedication.getPrescriptionID());
        row.put("Name",curMedication.getName());
        row.put("Dosage",curMedication.getDosage());
        row.put("Unit",curMedication.getUnit());
        row.put("Date",BGLMain.sqlDateFormat.format(curMedication.getDateTime()));
        row.put("Comments",curMedication.getNotes());
        appMain.dbMain.update("Medication",row,"ID = "+ String.valueOf(curMedication.getID()),null);
    }

    public class PrescriptionAdapter extends ArrayAdapter<Prescription>
    {
        PrescriptionAdapter(Context context, Prescription[] prescriptions) {
            super(context, R.layout.prescription_row,prescriptions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View customView = inflater.inflate(R.layout.prescription_row, parent, false);

            Prescription prescription = getItem(position);

            TextView title = customView.findViewById(R.id.prescription_title);
            TextView dosage = customView.findViewById(R.id.prescription_sub2);
            TextView frequency = customView.findViewById(R.id.prescription_sub3);
            TextView advice = customView.findViewById(R.id.prescription_sub7);

            title.setText(prescription.getName());
            dosage.setText(String.valueOf(prescription.getQuantity()));
            frequency.setText(prescription.printFrequency());
            advice.setText(prescription.inferAdvice());

            return customView;
        }
    }

}

