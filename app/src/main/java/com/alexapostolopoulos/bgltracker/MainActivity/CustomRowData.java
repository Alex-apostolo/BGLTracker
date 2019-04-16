package com.alexapostolopoulos.bgltracker.MainActivity;

import java.util.Comparator;
import java.util.Date;

public class CustomRowData implements Comparable<CustomRowData> {

    private String title;
    private String subtitle;
    private Date dateTime;
    private Object data;

    public CustomRowData(String title, String subtitle, Date date, Object data) {
        this.title = title;
        this.subtitle = subtitle;
        this.dateTime = date;
        this.data = data;
    }

    public int compareTo(CustomRowData row)
    {
        return (int)(row.getDate().getTime() - dateTime.getTime());
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Date getDate() {
        return dateTime;
    }

    public void setDate(Date dateTime) {
        this.dateTime = dateTime;
    }
}