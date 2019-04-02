package com.alexapostolopoulos.bgltracker;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
        manageInsulinDialog.setContentView(R.layout.manage_inputfield);
        manageInsulinDialog.show();

        Button addBtn = manageInsulinDialog.findViewById(R.id.addButton);
        Button rmvBtn = manageInsulinDialog.findViewById(R.id.removeButton);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddInsulinActivity.this, CustomInsulinActivity.class);
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
