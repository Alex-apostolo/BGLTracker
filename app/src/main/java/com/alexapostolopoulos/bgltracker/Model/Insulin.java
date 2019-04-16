package com.alexapostolopoulos.bgltracker.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Insulin implements Parcelable {
    private int id;
    private int prescriptionID;
    private float dosage;
    Date dateTime;
    private String notes;
    private static final SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final Parcelable.Creator<Insulin> CREATOR = new Parcelable.Creator<Insulin>() {
        @Override
        public Insulin createFromParcel(Parcel in) {return new Insulin(in); }
        @Override
        public Insulin[] newArray(int size) {return new Insulin[size];}
    };

    public int describeContents() {return 0;}

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeInt(prescriptionID);
        dest.writeFloat(dosage);
        dest.writeString(sqlDateFormat.format(dateTime));
        dest.writeString(notes);
    }

    public Insulin(int ID, int prescriptionID, float dosage, Date dateTime, String notes) {
        this.id = ID;
        this.prescriptionID = prescriptionID;
        this.dosage = dosage;
        this.dateTime = dateTime;
        this.notes = notes;
    }

    private Insulin(Parcel in)
    {
        try {
            id = in.readInt();
            prescriptionID = in.readInt();
            dosage = in.readFloat();
            dateTime = sqlDateFormat.parse(in.readString());
            notes = in.readString();
        }
        catch (Exception e) {}
    }
    public int getID() {return id;}
    public int getPrescriptionID () {return prescriptionID;}
    public float getDosage () {return dosage;}
    public Date getDateTime () {return dateTime;}
    public String getNotes () {return notes;}
}