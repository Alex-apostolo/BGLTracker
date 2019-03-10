package com.alexapostolopoulos.bgltracker;

import java.time.LocalDate;
import java.time.LocalTime;

public class CustomRowData {

    private String title;
    private String subtitle;
    private LocalDate date;
    private LocalTime time;

    public CustomRowData(String title, String subtitle, LocalDate date, LocalTime time) {
        this.title = title;
        this.subtitle = subtitle;
        this.date = date;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
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