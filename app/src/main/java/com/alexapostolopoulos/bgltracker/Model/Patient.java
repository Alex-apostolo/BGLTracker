package com.alexapostolopoulos.bgltracker.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alexapostolopoulos.bgltracker.BGLMain;

import java.util.ArrayList;
import java.util.Collections;

public class Patient
{
    private int id;
    private String name;
    private ArrayList<Prescription> prescriptions;
    private float carbInsulinRatio;
    private float correctionDose;
    private float glucoseWarningUpper;
    private float glucoseWarningLower;

    public int getID() {return id;}
    public float getCIRatio() {return carbInsulinRatio;}
    public Patient(int id,
                   String name,
                   float carbInsulinRatio,
                   float correctionDose,
                   float glucoseWarningLower,
                   float glucoseWarningUpper)
    {
        this.id = id;
        this.name = name;
        this.carbInsulinRatio = carbInsulinRatio;
        this.correctionDose = correctionDose;
        this.glucoseWarningLower = glucoseWarningLower;
        this.glucoseWarningUpper = glucoseWarningUpper;
        populatePrescriptions(BGLMain.dbMain);
    }

    private void populatePrescriptions(SQLiteDatabase db) {
            prescriptions = new ArrayList<>();
            Cursor result = db.rawQuery("SELECT * FROM Prescription WHERE PatientID = ?", new String[]{String.valueOf(id)});
            while (result.moveToNext()) {
                prescriptions.add(new Prescription(result.getInt(0),
                        result.getString(2),
                        result.getInt(3),
                        result.getString(4),
                        result.getFloat(5),
                        result.getInt(6),
                        result.getFloat(7),
                        result.getString(8),
                        result.getInt(9),
                        result.getString(10)));
                Log.d("prescriptionID:",result.getString(4));
            }

    }

    public ArrayList<Prescription> getPrescriptions() {return prescriptions;}

    public float getCorrectionDose() {
        return correctionDose;
    }

    public int addPrescription(BGLMain appMain, Prescription prescription)
    {
        ContentValues row = new ContentValues();
        prescriptions.add(prescription);
        row.put("PatientID",id);
        row.put("Type",prescription.getType());
        row.put("InsulinTemplateID",prescription.getInsulinTemplateID());
        row.put("Name",prescription.getName());
        row.put("Frequency",prescription.getFrequency());
        row.put("FreqIndex",prescription.getFreqIndex());
        row.put("Quantity",prescription.getQuantity());
        row.put("Unit",prescription.getUnit());
        row.put("Advice",prescription.getAdvice());
        row.put("Comments",prescription.getComments());
        appMain.dbMain.insert("Prescription",null,row);
        Cursor test = appMain.dbMain.rawQuery("SELECT * FROM PRESCRIPTION",null);
        test.moveToFirst();
        Log.d("prescriptionID:",String.valueOf(test.getInt(0)));
        Cursor idVal = appMain.dbMain.rawQuery("SELECT max(ID) FROM Prescription",null);
        idVal.moveToFirst();
        return idVal.getInt(0);
    }

    public void updatePrescription(BGLMain appMain, Prescription prescription)
    {
        ContentValues row = new ContentValues();
        row.put("PatientID",id);
        row.put("Type",prescription.getType());
        row.put("InsulinTemplateID",prescription.getInsulinTemplateID());
        row.put("Name",prescription.getName());
        row.put("Frequency",prescription.getFrequency());
        row.put("FreqIndex",prescription.getFreqIndex());
        row.put("Quantity",prescription.getQuantity());
        row.put("Unit",prescription.getUnit());
        row.put("Advice",prescription.getAdvice());
        row.put("Comments",prescription.getComments());
        appMain.dbMain.update("Prescription",row,"ID = "+ id,null);
        for(int pos = 0; pos < prescriptions.size(); pos++)
        {
            if(prescriptions.get(pos).getID() == prescription.getID())
            {
                prescriptions.set(pos,prescription);
                break;
            }
        }
    }

    public void removePrescription(BGLMain appMain, int prescriptionID)
    {
        appMain.dbMain.execSQL("DELETE FROM Prescription WHERE ID = " + prescriptionID);
        for(int pos = 0; pos < prescriptions.size(); pos++)
        {
            if(prescriptions.get(pos).getID() == prescriptionID)
            {
                prescriptions.remove(pos);
                break;
            }
        }

    }

    public Prescription findPrescription(int id)
    {
        for(int pos = 0; pos < prescriptions.size(); pos++)
        {
            if(prescriptions.get(pos).getID() == id)
            {
                return prescriptions.get(pos);
            }
        }
        return null;
    }

    public ArrayList<Prescription> filterWhitelist(String type)
    {
        ArrayList<Prescription> filtered = new ArrayList<>();
        for(Prescription item: prescriptions)
        {
            if(item.getType().equals(type))
            {
                filtered.add(item.clone());
            }
        }
        return filtered;
    }

    public String getName() {return name;}

    public float getGlucoseWarningUpper() {
        return glucoseWarningUpper;
    }

    public void setGlucoseWarningUpper(float glucoseWarningUpper) {
        this.glucoseWarningUpper = glucoseWarningUpper;
    }

    public float getGlucoseWarningLower() {
        return glucoseWarningLower;
    }

    public void setGlucoseWarningLower(float glucoseWarningLower) {
        this.glucoseWarningLower = glucoseWarningLower;
    }
}
