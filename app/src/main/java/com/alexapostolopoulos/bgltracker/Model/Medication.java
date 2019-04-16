package com.alexapostolopoulos.bgltracker.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.alexapostolopoulos.bgltracker.BGLMain;

import java.util.Date;

public class Medication implements Parcelable{
    private int id;
    private int prescriptionID;
    private String name;
    private float dosage;
    private String unit;
    Date dateTime;
    private String notes;

    public int describeContents() {return 0;}

    public static final Parcelable.Creator<Medication>  CREATOR = new Parcelable.Creator<Medication>() {
        public Medication createFromParcel(Parcel in) {return new Medication(in);}
        public Medication[] newArray(int size) {return new Medication[size];}
    };

    public Medication(int id, int prescriptionID, String name, float dosage, String unit, Date dateTime, String notes)
    {
        this.id = id;
        this.prescriptionID = prescriptionID;
        this.name = name;
        this.dosage = dosage;
        this.unit = unit;
        this.dateTime = dateTime;
        this.notes = notes;
    }
    private Medication(Parcel in)
    {
        try {
            id = in.readInt();
            prescriptionID = in.readInt();
            name = in.readString();
            dosage = in.readFloat();
            unit = in.readString();
            dateTime = BGLMain.sqlDateFormat.parse(in.readString());
            notes = in.readString();
        }
        catch(Exception e) {}
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeInt(prescriptionID);
        dest.writeString(name);
        dest.writeFloat(dosage);
        dest.writeString(unit);
        dest.writeString(BGLMain.sqlDateFormat.format(dateTime));
        dest.writeString(notes);
    }

    public int getID() {return id;}
    public int getPrescriptionID(){return prescriptionID;}
    public String getName() {return name;}
    public float getDosage(){return dosage;}
    public String getUnit() {return unit;}
    public Date getDateTime() {return dateTime;}
    public String getNotes() {return notes;}
}
