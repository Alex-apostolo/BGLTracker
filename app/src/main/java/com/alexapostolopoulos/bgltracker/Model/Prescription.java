package com.alexapostolopoulos.bgltracker.Model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.alexapostolopoulos.bgltracker.BGLMain;

public class Prescription  {
    private int id;
    private String type;
    private int insulinTemplateID;
    private String name;
    private float frequency;
    private int freqIndex;
    private float quantity;
    private String unit;
    private int advice;
    private String comments;

    public Prescription(int id,
                        String type,
                        int insulinTemplateID,
                        String name,
                        float frequency,
                        int index,
                        float quantity,
                        String unit,
                        int advice,
                        String comments)
    {
        this.id = id;
        this.type = type;
        this.insulinTemplateID = insulinTemplateID;
        this.name = name;
        this.frequency = frequency;
        this.freqIndex = index;
        this.quantity = quantity;
        this.unit = unit;
        this.advice = advice;
        this.comments = comments;
    }

    public Prescription(int id,
                        String type,
                        int insulinTemplateID,
                        String name,
                        int value,
                        int index,
                        float quantity,
                        String unit,
                        int advice,
                        String comments)
    {
        this.id = id;
        this.type = type;
        this.insulinTemplateID = insulinTemplateID;
        this.name = name;
        this.freqIndex = index;
        switch(freqIndex)
        {
            case 0:
                frequency = value/(float)60;
                break;
            case 1:
                frequency = value;
                break;
            case 2:
                frequency = (float)24/value;
                break;
            case 3:
                frequency = 168;
                break;
            case 4:
                frequency = 24 * value;
                break;
        }

        this.quantity = quantity;
        this.unit = unit;
        this.advice = advice;
        this.comments = comments;
    }

    public Prescription clone()
    {
            return new Prescription(id,type,insulinTemplateID,name,frequency,freqIndex,quantity,unit,advice,comments);
    }

    public String inferAdvice()
    {
        String[] arrAdvice;
        if(type.equals("General"))
        {
            arrAdvice = new String[]{"","Before meal","After meal"};
        }
        else if (type.equals("Insulin"))
        {
            arrAdvice = new String[]{"","Correction dose","With meal"};
        }
        else
        {
            return null;
        }
        return arrAdvice[advice];
    }

    public int getFreqIndex() {return freqIndex;}

    public String printFrequency()
    {
        switch(freqIndex)
        {
            case 0:
                return "Once every " + (frequency*60) + " mins";
            case 1:
                return "Once every " + frequency + " hours";
            case 2:
                return (24/frequency) + " times a day";
            case 3:
                return "Once a week";
            case 4:
                return "Once every " + (frequency/24) + " days";
        }
        return null;
    }

    public int getValue()
    {
        switch(freqIndex)
        {
            case 0:
                return (int)(frequency*60);
            case 1:
                return (int)frequency;
            case 2:
                return (int)(24/frequency);
            case 3:
                return 0;
            case 4:
                return (int)(frequency/24);
        }
        return 0;
    }

    public int getID() {return id;}

    public int describeContents() { return 0;}

    public int getInsulinTemplateID() {
        return insulinTemplateID;
    }

    public String getName() {
        return name;
    }

    public void setFrequency (float value) {frequency = value;}

    public float getFrequency() {
        return frequency;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public int getAdvice() {
        return advice;
    }

    public String getType() {return type;}

    public void setAdvice(int advice) {
        this.advice = advice;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String toString()
    {
        return name;
    }

}
