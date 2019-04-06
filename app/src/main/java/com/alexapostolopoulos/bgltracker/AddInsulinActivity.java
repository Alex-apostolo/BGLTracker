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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alexapostolopoulos.bgltracker.MainActivity.CustomAdapter;
import com.alexapostolopoulos.bgltracker.MainActivity.CustomRowData;
import com.alexapostolopoulos.bgltracker.MainActivity.MainActivity;
import com.alexapostolopoulos.bgltracker.Model.Glucose;
import com.alexapostolopoulos.bgltracker.Model.Insulin;
import com.alexapostolopoulos.bgltracker.Model.Medication;
import com.alexapostolopoulos.bgltracker.Model.Patient;
import com.alexapostolopoulos.bgltracker.Model.Prescription;
import com.alexapostolopoulos.bgltracker.Model.TemplateInsulin;

import java.util.ArrayList;
import java.util.Date;

public class AddInsulinActivity extends AppCompatActivity {

    boolean isNew;
    String callingForm;
    Insulin curInsulin;
    ArrayList<Prescription> arrPrescription;
    Spinner prescriptionSpinner;
    BGLMain appMain;
    EditText txtDosage;
    DateField dateField;
    EditText txtNotes;
    Dialog manageInsulinDialog;
    ListView prescriptionList;
    PrescriptionListListener listListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_insulin);
        arrPrescription = new ArrayList<>();
        appMain = (BGLMain)this.getApplication();
        txtDosage = findViewById(R.id.addInsulin_insulin_inputField);
        dateField = new DateField(findViewById(R.id.addInsulin_dateTime_inputLayout));
        txtNotes = findViewById(R.id.addInsulin_note_inputField);
        manageInsulinDialog = new Dialog(this);
        manageInsulinDialog.setContentView(R.layout.manage_entries_inputfield);
        prescriptionList = manageInsulinDialog.findViewById(R.id.prescriptionList);
        listListener = new PrescriptionListListener();
        prescriptionList.setOnItemClickListener(listListener);
        prescriptionSpinner = findViewById(R.id.addInsulin_selectPresc_spinner2);
        Bundle data = getIntent().getExtras();
        isNew = data.getBoolean("isNew");
        callingForm = data.getString("callingForm");
        repopulateLists();
        if(!isNew)
        {
            curInsulin = data.getParcelable("insulin");
            String date = BGLMain.sqlDateFormat.format(curInsulin.getDateTime());
            txtDosage.setText(String.valueOf(curInsulin.getDosage()));
            dateField.showDate(curInsulin.getDateTime());
            txtNotes.setText(curInsulin.getNotes());
            int selectedID = curInsulin.getPrescriptionID();
            for(int pos = 0; pos < arrPrescription.size(); pos++)
            {
                if(arrPrescription.get(pos).getID() == selectedID)
                {
                    prescriptionSpinner.setSelection(pos);
                }
            }
        }
        switch(callingForm)
        {
            case "Prescription":
                int selectedID = data.getInt("selected");
                for(int pos = 0; pos < arrPrescription.size(); pos++)
                {
                    if(arrPrescription.get(pos).getID() == selectedID)
                    {
                        prescriptionSpinner.setSelection(pos);
                    }
                }
        }
        dateField.initEvents();
    }

    private class PrescriptionListListener implements AdapterView.OnItemClickListener
    {
        private int count = 0;
        private int lastPosition = -1;
        public int getLastPosition() {return lastPosition;}
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(lastPosition == -1 || position == lastPosition)
            {
                count++;
                lastPosition = position;
            }
            if(count % 2 == 0) {
                Prescription prescription = (Prescription) parent.getItemAtPosition(position);
                editPrescription(prescription);
            }
        }
    }

    public void editPrescription(Prescription prescription)
    {
        Intent startPrescription = new Intent(this,AddPrescriptionActivity.class);
        startPrescription.putExtra("isNew",false);
        startPrescription.putExtra("prescriptionID",prescription.getID());
        startPrescription.putExtra("callingForm","Insulin");
        startPrescription.putExtra("type","Insulin");
        startActivity(startPrescription);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add,menu);
        return true;
    }

    public void showInsulinDialog(View v) {
        manageInsulinDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Do when user presses check
        try{
            String enteredDosage = txtDosage.getText().toString();
            Date fullDate = dateField.parseDate();
            int prescriptionID = prescriptionSpinner.getSelectedItem() == null ? 0 : ((Prescription)prescriptionSpinner.getSelectedItem()).getID();
            if(fullDate != null && BGLMain.chkFloat(enteredDosage)) {
                if (isNew) {
                    curInsulin = new Insulin(0, prescriptionID, Float.parseFloat(txtDosage.getText().toString()),
                            fullDate,
                            txtNotes.getText().toString());
                    insertInsulin();
                    startActivity(new Intent(this,MainActivity.class));
                } else {
                    int id = curInsulin.getID();
                    curInsulin = new Insulin(id, prescriptionID, Float.parseFloat(txtDosage.getText().toString()),
                            fullDate,
                            txtNotes.getText().toString());
                    updateInsulin();
                    startActivity(new Intent(this,MainActivity.class));
                }
            }
        }
        catch (Exception e) {
            Log.d("error_committing_insulin",e.getMessage());}
        return super.onOptionsItemSelected(item);
    }

    public void addItem(View v)
    {
        Intent startPrescription = new Intent(this,AddPrescriptionActivity.class);
        startPrescription.putExtra("isNew",true);
        startPrescription.putExtra("callingForm","Insulin");
        startPrescription.putExtra("type","Insulin");
        startActivity(startPrescription);
    }

    public void removeItem(View v)
    {
        Prescription p = (Prescription)prescriptionList.getItemAtPosition(listListener.getLastPosition());
        appMain.curPatient.removePrescription(appMain,p.getID());
        repopulateLists();
    }

    public void insertInsulin()
    {
        ContentValues row = new ContentValues();
        row.put("PatientID",appMain.curPatient.getID());
        row.put("PrescriptionID",curInsulin.getPrescriptionID());
        row.put("Dosage",curInsulin.getDosage());
        row.put("Date",BGLMain.sqlDateFormat.format(curInsulin.getDateTime()));
        row.put("Comments",curInsulin.getNotes());
        appMain.dbMain.insert("Insulin",null,row);
    }

    public void updateInsulin()
    {
        ContentValues row = new ContentValues();
        row.put("PatientID",appMain.curPatient.getID());
        row.put("PrescriptionID",curInsulin.getPrescriptionID());
        row.put("Dosage",curInsulin.getDosage());
        row.put("Date",BGLMain.sqlDateFormat.format(curInsulin.getDateTime()));
        row.put("Comments",curInsulin.getNotes());
        appMain.dbMain.update("Insulin",row,"ID = "+String.valueOf(curInsulin.getID()),null);
    }

    public void repopulateLists()
    {
        arrPrescription = appMain.curPatient.filterWhitelist("Insulin");
        ListAdapter adapter = new PrescriptionAdapter(this, arrPrescription.toArray(new Prescription[arrPrescription.size()]));
        prescriptionList.setAdapter(adapter);
        ArrayAdapter<Prescription> prescriptionAdapter =
                new ArrayAdapter<>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, arrPrescription);
        prescriptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prescriptionSpinner.setAdapter(prescriptionAdapter);
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
            TextView period = customView.findViewById(R.id.prescription_sub1);
            TextView dosage = customView.findViewById(R.id.prescription_sub2);
            TextView frequency = customView.findViewById(R.id.prescription_sub3);
            TextView onset = customView.findViewById(R.id.prescription_sub4);
            TextView peak = customView.findViewById(R.id.prescription_sub5);
            TextView duration = customView.findViewById(R.id.prescription_sub6);
            TextView advice = customView.findViewById(R.id.prescription_sub7);

            TemplateInsulin curTemplate = appMain.insulinMasterList.get(prescription.getInsulinTemplateID()-1);

            title.setText(prescription.getName());
            period.setText(curTemplate.inferPeriod());
            dosage.setText(String.valueOf(prescription.getQuantity()));
            frequency.setText(prescription.printFrequency());
            onset.setText(curTemplate.printOnset());
            peak.setText(curTemplate.printPeak());
            duration.setText(curTemplate.printDuration());
            advice.setText(prescription.inferAdvice());

            return customView;
        }
    }
}
