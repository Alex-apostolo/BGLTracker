package com.alexapostolopoulos.bgltracker.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.alexapostolopoulos.bgltracker.BGLMain;

import java.util.Date;

public class Glucose implements Parcelable{
    private int id;
    private float value;
    private Date dateTime;
    private String notes;

    public static final Parcelable.Creator<Glucose> CREATOR = new Parcelable.Creator<Glucose>() {
        public Glucose createFromParcel(Parcel in) {return new Glucose(in);}
        public Glucose[] newArray(int size) {return new Glucose[size];}
    };

    public Glucose(int ID, float value,
                   Date dateTime, String notes) {
        this.id = ID;
        this.value = value;
        this.notes = notes;
        this.dateTime = dateTime;
    }

    private Glucose(Parcel in)
    {
        try {
            id = in.readInt();
            value = in.readFloat();
            dateTime = BGLMain.sqlDateFormat.parse(in.readString());
            notes = in.readString();
        }
        catch (Exception e) {}
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeFloat(value);
        dest.writeString(BGLMain.sqlDateFormat.format(dateTime));
        dest.writeString(notes);
    }

    public int describeContents() {return 0;}

    public int getID() {
        return id;
    }

    public float getValue() {
        return value;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public String getNotes() { return notes; }
}
