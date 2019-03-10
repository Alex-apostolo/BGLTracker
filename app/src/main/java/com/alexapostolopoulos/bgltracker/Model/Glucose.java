package com.alexapostolopoulos.bgltracker.Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Glucose {

    private int level;
    private LocalDate date;
    private LocalTime time;

    public Glucose(int level, LocalDate date, LocalTime time) {
        this.level = level;
        this.date = date;
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
