package com.alexapostolopoulos.bgltracker.Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Insulin {

    private String type;
    private int dosage;
    private LocalDate date;
    private LocalTime time;

    public Insulin(String type, int dosage, LocalDate date, LocalTime time) {
        this.type = type;
        this.dosage = dosage;
        this.date = date;
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
