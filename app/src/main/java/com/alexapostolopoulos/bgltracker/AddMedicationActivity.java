package com.alexapostolopoulos.bgltracker;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
        managePrescDialog.setContentView(R.layout.manage_inputfields);
        managePrescDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        managePrescDialog.show();
    }

    public void addPrescriptionActivity(View v) {
        Intent intent = new Intent(this, AddPrescriptionActivity.class);
        startActivity(intent);
    }
}
