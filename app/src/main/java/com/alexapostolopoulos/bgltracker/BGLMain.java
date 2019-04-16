package com.alexapostolopoulos.bgltracker;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import com.alexapostolopoulos.bgltracker.Model.Prescription;
import com.alexapostolopoulos.bgltracker.Model.TemplateInsulin;
import com.alexapostolopoulos.bgltracker.Model.Patient;

public class BGLMain extends Application {
    public static SQLiteDatabase dbMain;
    public ArrayList<TemplateInsulin> insulinMasterList;
    public Patient curPatient;
    public static final SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    public void onCreate()
    {
        super.onCreate();
        populateDatabase();
        //populateSample();
        initialiseArrInsulin();
    }
    public void populateDatabase()
    {
        Log.d("DATABASE_CREATION","Database being created");
        dbMain = openOrCreateDatabase("BGL_Local",MODE_PRIVATE,null);
        dbMain.execSQL("DROP TABLE TemplateInsulin");
        dbMain.execSQL("CREATE TABLE IF NOT EXISTS Patient(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name VARCHAR(20)," +
                "CarbInsulinRatio FLOAT," +
                "CorrectionDose FLOAT, " +
                "GlucoseWarningLower FLOAT, " +
                "GlucoseWarningUpper FLOAT," +
                "DailyMean FLOAT," +
                "WeeklyMean FLOAT," +
                "DailyVariance FLOAT," +
                "WeeklyVariance FLOAT)");
        dbMain.execSQL("CREATE TABLE IF NOT EXISTS TemplateInsulin(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name VARCHAR(30) NOT NULL," +
                "onsetMin FLOAT," +
                "onsetMax FLOAT," +
                "peakMin FLOAT," +
                "peakMax FLOAT," +
                "durMin FLOAT," +
                "durMax FLOAT)");
        dbMain.execSQL("CREATE TABLE IF NOT EXISTS Prescription(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PatientID INTEGER NOT NULL," +
                "Type CHAR(7)," +
                "InsulinTemplateID INTEGER," +
                "Name VARCHAR(30)," +
                "Frequency FLOAT," +
                "freqIndex INT," +
                "Quantity FLOAT," +
                "Unit VARCHAR(6)," +
                "Advice INTEGER," +
                "Comments TEXT," +
                "FOREIGN KEY(InsulinTemplateID) REFERENCES TemplateInsulin(ID)," +
                "FOREIGN KEY(PatientID) REFERENCES Patient(ID))");
        dbMain.execSQL("CREATE TABLE IF NOT EXISTS Glucose(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PatientID INTEGER NOT NULL," +
                "Value FLOAT," +
                "Date DATETIME," +
                "Comments TEXT," +
                "FOREIGN KEY(PatientID) REFERENCES Patient(ID))");
        dbMain.execSQL("CREATE TABLE IF NOT EXISTS Insulin(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PatientID INTEGER," +
                "PrescriptionID INTEGER," +
                "Dosage FLOAT," +
                "Date DATETIME," +
                "Comments TEXT," +
                "FOREIGN KEY(PatientID) REFERENCES Patient(ID)," +
                "FOREIGN KEY(PrescriptionID) REFERENCES Prescription(ID))");
        dbMain.execSQL("CREATE TABLE IF NOT EXISTS Medication(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PatientID INTEGER," +
                "PrescriptionID INTEGER," +
                "Name VARCHAR(30)," +
                "Dosage FLOAT," +
                "Unit VARCHAR(6)," +
                "Date DATETIME," +
                "Comments TEXT," +
                "FOREIGN KEY(PatientID) REFERENCES Patient(ID)," +
                "FOREIGN KEY(PrescriptionID) REFERENCES Prescription(ID))");
        populateInsulin();
    }
    public void populateSample()
    {
        ContentValues row = new ContentValues();
        row.put("Name","Test 1");
        row.put("onsetMin",0.5);
        row.put("onsetMax",1);
        row.put("peakMin",2);
        row.put("peakMax",3);
        row.put("durMin",4);
        row.put("durMax",5);
        dbMain.insert("TemplateInsulin",null,row);
        row.put("Name","Test 2");
        row.put("onsetMin",0.18);
        row.put("onsetMax",0.5);
        row.put("peakMin",1);
        row.put("peakMax",1.5);
        row.put("durMin",2);
        row.put("durMax",3);
        dbMain.insert("TemplateInsulin",null,row);
        row.put("Name","Test 3");
        row.put("onsetMin",1);
        row.put("onsetMax",2);
        row.put("peakMin",3);
        row.put("peakMax",4);
        row.put("durMin",5);
        row.put("durMax",6);
        dbMain.insert("TemplateInsulin",null,row);
        /*row = new ContentValues();
        row.put("PatientID",1);
        row.put("PrescriptionID",-1);
        row.put("Dosage",12);
        row.put("Date","2019-03-14 17:06:14");
        row.put("Comments","Peter");
        dbMain.insert("Insulin",null,row);*/
    }

    public void populateInsulin()
    {
        Cursor q = dbMain.rawQuery("SELECT * FROM TemplateInsulin",null);
        if(q.getCount() == 0)
        {
            ContentValues row = new ContentValues();
            row.put("Name","Insulin glulisine (Apidra)");
            row.put("onsetMin",(float)1/(float)12);
            row.put("onsetMax",0.25);
            row.put("peakMin",1);
            row.put("peakMax",3);
            row.put("durMin",3);
            row.put("durMax",5);
            dbMain.insert("TemplateInsulin",null,row);
            row = new ContentValues();
            row.put("Name","Insulin aspart (Novolog)");
            row.put("onsetMin",(float)1/(float)6);
            row.put("onsetMax",0.25);
            row.put("peakMin",1);
            row.put("peakMax",3);
            row.put("durMin",3);
            row.put("durMax",5);
            dbMain.insert("TemplateInsulin",null,row);
            row = new ContentValues();
            row.put("Name","Insulin lispro (Humalog)");
            row.put("onsetMin",(float)1/(float)6);
            row.put("onsetMax",0.25);
            row.put("peakMin",1);
            row.put("peakMax",3);
            row.put("durMin",3);
            row.put("durMax",5);
            dbMain.insert("TemplateInsulin",null,row);
            row = new ContentValues();
            row.put("Name","Regular insulin (Novolin R, Humulin R)");
            row.put("onsetMin",0.5);
            row.put("onsetMax",1);
            row.put("peakMin",2);
            row.put("peakMax",4);
            row.put("durMin",5);
            row.put("durMax",8);
            dbMain.insert("TemplateInsulin",null,row);
            row = new ContentValues();
            row.put("Name","NPH insulin (Novolin N, Humulin N)");
            row.put("onsetMin",1);
            row.put("onsetMax",2);
            row.put("peakMin",4);
            row.put("peakMax",12);
            row.put("durMin",14);
            row.put("durMax",24);
            dbMain.insert("TemplateInsulin",null,row);
            row = new ContentValues();
            row.put("Name","Insulin detemir (Levemir)");
            row.put("onsetMin",1);
            row.put("onsetMax",1);
            row.put("peakMin",3);
            row.put("peakMax",14);
            row.put("durMin",24);
            row.put("durMax",24);
            dbMain.insert("TemplateInsulin",null,row);
            row = new ContentValues();
            row.put("Name","Insulin U-100 (Lantus, Basaglar)");
            row.put("onsetMin",3);
            row.put("onsetMax",4);
            row.put("peakMin",0);
            row.put("peakMax",0);
            row.put("durMin",24);
            row.put("durMax",24);
            dbMain.insert("TemplateInsulin",null,row);
            row = new ContentValues();
            row.put("Name","Insulin glargine U-300 (Toujeo)");
            row.put("onsetMin",6);
            row.put("onsetMax",6);
            row.put("peakMin",0);
            row.put("peakMax",0);
            row.put("durMin",36);
            row.put("durMax",36);
            dbMain.insert("TemplateInsulin",null,row);
            row = new ContentValues();
            row.put("Name","Insulin degludec U-100/U-200");
            row.put("onsetMin",1);
            row.put("onsetMax",1);
            row.put("peakMin",0);
            row.put("peakMax",0);
            row.put("durMin",42);
            row.put("durMax",42);
            dbMain.insert("TemplateInsulin",null,row);
            row = new ContentValues();
            row.put("Name","Pre-mix (Any)");
            row.put("onsetMin",(float)1/(float)12);
            row.put("onsetMax",(float)1/(float)3);
            row.put("peakMin",1);
            row.put("peakMax",2);
            row.put("durMin",10);
            row.put("durMax",16);
            dbMain.insert("TemplateInsulin",null,row);
        }
    }

    public static boolean chkFloat(String testVal)
    {
        if(testVal.length() == 0)
        {
            return false;
        }
        int dpCount = 0;
        for(char c : testVal.toCharArray())
        {
            if(c == '.')
            {
                if(dpCount == 0)
                {
                    dpCount++;
                }
                else
                {
                    return false;
                }
            }
            else if(c < 48 || c > 57)
            {
                return false;
            }
        }
        if(dpCount > 1) {
            return false;
        }
        return true;
    }

    public void initialiseArrInsulin() {
        try {
            insulinMasterList = new ArrayList<>();
            Cursor result = dbMain.rawQuery("SELECT * FROM TemplateInsulin", null);
            while (result.moveToNext()) {
                insulinMasterList.add(new TemplateInsulin(result.getInt(0),
                        result.getString(1),
                        result.getFloat(2),
                        result.getFloat(3),
                        result.getFloat(4),
                        result.getFloat(5),
                        result.getFloat(6),
                        result.getFloat(7)));
                Log.d("ID",String.valueOf(result.getInt(0)));
            }
            result.close();
        } catch (Exception e) {
        }
    }

}

