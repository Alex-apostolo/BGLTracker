package com.alexapostolopoulos.bgltracker;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AddMedicationActivity extends AppCompatActivity {

    Dialog managePrescDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);
        managePrescDialog = new Dialog(this);
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

    public void showPrescDialog(View v) {
        managePrescDialog.setContentView(R.layout.manage_inputfield);
        managePrescDialog.show();

        Button addBtn = managePrescDialog.findViewById(R.id.addButton);
        Button rmvBtn = managePrescDialog.findViewById(R.id.removeButton);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMedicationActivity.this, AddPrescriptionActivity.class);
                startActivity(intent);
            }
        });

        rmvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove entry field
            }
        });
    }
}
