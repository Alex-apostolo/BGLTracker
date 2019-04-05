package com.alexapostolopoulos.bgltracker.Model;

public class TemplateInsulin {
    private int id;
    private String name;
    private float onsetMin;
    private float onsetMax;
    private float peakMin;
    private float peakMax;
    private float durMin;
    private float durMax;
    public TemplateInsulin(int id, String name, float onsetMin, float onsetMax,
                            float peakMin, float peakMax, float durMin, float durMax)
    {
        this.id = id;
        this.name = name;
        this.onsetMin = onsetMin;
        this.onsetMax = onsetMax;
        this.peakMin = peakMin;
        this.peakMax = peakMax;
        this.durMin = durMin;
        this.durMax = durMax;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getOnsetMin() {
        return onsetMin;
    }

    public float getOnsetMax() {
        return onsetMax;
    }

    public float getPeakMin() {
        return peakMin;
    }

    public float getPeakMax() {
        return peakMax;
    }

    public float getDurMin() {
        return durMin;
    }

    public float getDurMax() {
        return durMax;
    }

    @Override
    public String toString()
    {
        return name;
    }
    
    public String printOnset()
    {
        if(onsetMin < 1 && onsetMax < 1)
        {
            return (onsetMin*60) + "-" + (onsetMax*60);
        }
        else
        {
            return onsetMin + "-" + onsetMax;
        }
    }

    public String printPeak()
    {
        if(peakMin < 1 && peakMax < 1)
        {
            return (peakMin*60) + "-" + (peakMax*60);
        }
        else
        {
            return peakMin + "-" + peakMax;
        }
    }

    public String printDuration()
    {
        if(durMin < 1 && durMax < 1)
        {
            return (durMin*60) + "-" + (durMax*60);
        }
        else
        {
            return durMin + "-" + durMax;
        }
    }

    public String inferPeriod()
    {
        if(onsetMax <= 0.5 && peakMax <= 1.5 && durMax <= 5)
        {
            return "Rapid-acting";
        }
        else if (onsetMax <= 1 && peakMax <= 5 && durMax <= 8)
        {
            return "Fast-acting";
        }
        else if (onsetMax <= 2 && peakMax <= 12 && durMax <= 24)
        {
            return "Intermediate-acting";
        }
        else if (onsetMax <= 2 && peakMax <= 8 && durMax <= 42)
        {
            return "Long-acting";
        }
        else
        {
            return null;
        }
    }
}
