package com.alexapostolopoulos.bgltracker;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddInsulinActivity extends AppCompatActivity {

    Dialog manageInsulinDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_insulin);
        manageInsulinDialog = new Dialog(this);

        //Insulin input fields
        Spinner prescriptionSpinner = findViewById(R.id.addInsulin_selectPresc_spinner2);
        EditText value = findViewById(R.id.addInsulin_insulin_inputField);
        EditText dateTime = findViewById(R.id.addInsulin_dateTime_inputLayout);
        EditText notes = findViewById(R.id.addInsulin_note_inputField);

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

    public void showInsulinDialog(View v) {
        manageInsulinDialog.setContentView(R.layout.manage_entries_inputfield);
        manageInsulinDialog.show();

        Button addBtn = manageInsulinDialog.findViewById(R.id.addButton);
        Button rmvBtn = manageInsulinDialog.findViewById(R.id.removeButton);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddInsulinActivity.this, AddCustomInsulinActivity.class);
                startActivity(intent);
            }
        });

        rmvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove field
            }
        });
    }
}
