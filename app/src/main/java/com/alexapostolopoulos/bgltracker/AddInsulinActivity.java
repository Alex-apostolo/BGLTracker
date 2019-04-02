package com.alexapostolopoulos.bgltracker;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AddInsulinActivity extends AppCompatActivity {

    Dialog manageInsulinDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_insulin);
        manageInsulinDialog = new Dialog(this);
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
        manageInsulinDialog.setContentView(R.layout.manage_inputfields);
        manageInsulinDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        manageInsulinDialog.show();
    }
}
