package com.alexapostolopoulos.bgltracker;

import android.view.View;
import android.widget.AdapterView;

import com.alexapostolopoulos.bgltracker.Model.Prescription;

public class PrescriptionListListener implements AdapterView.OnItemClickListener
{

    public PrescriptionListListener(Prescribed prescribed)
    {
        parentForm = prescribed;
    }
    private int count = 0;
    private int lastPosition = -1;
    private Prescribed parentForm;
    public int getLastPosition() {return lastPosition;}
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(lastPosition == -1 || position == lastPosition)
        {
            count++;
        }
        lastPosition = position;
        if(count % 2 == 0) {
            parentForm.editPrescription((Prescription) parent.getItemAtPosition(position));
        }
    }
    public void reset()
    {
        if(lastPosition != -1) {
            lastPosition = -1;
            count = 0;
        }
    }
}
