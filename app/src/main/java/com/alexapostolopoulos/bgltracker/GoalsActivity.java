package com.alexapostolopoulos.bgltracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import com.alexapostolopoulos.bgltracker.MainActivity.MainActivity;
import com.alexapostolopoulos.bgltracker.Model.Patient;
import java.util.Calendar;
import java.util.Date;

public class GoalsActivity extends AppCompatActivity {

    EditText dailyMean;
    EditText weeklyMean;
    EditText dailyVariance;
    EditText weeklyVariance;
    TextView goalsText;
    BGLMain appMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        appMain = (BGLMain)this.getApplication();
        dailyMean = findViewById(R.id.dailyMean_editText);
        weeklyMean = findViewById(R.id.weeklyMean_editText);
        dailyVariance = findViewById(R.id.dailyVariance);
        weeklyVariance = findViewById(R.id.weeklyVariance);
        goalsText = findViewById(R.id.goalsText);
        populateField(dailyMean,appMain.curPatient.getDailyMean());
        populateField(weeklyMean,appMain.curPatient.getWeeklyMean());
        populateField(dailyVariance,appMain.curPatient.getDailyVariance());
        populateField(weeklyVariance,appMain.curPatient.getWeeklyVariance());
        if(appMain.curPatient.getDailyMean() >= 0 && appMain.curPatient.getDailyVariance() >= 0)
        {
            try {
                Date now = new Date();
                Date today = BGLMain.sqlDateFormat.parse(BGLMain.dateOnly.format(now));
                float avgVal = avgGlucose(today, now);
                if(avgVal == -1) {
                    goalsText.append("No glucose has been recorded today, record when glucose is taken to receive alerts.\n");
                }
                else if (Math.abs(avgVal - appMain.curPatient.getDailyMean()) <= appMain.curPatient.getDailyVariance()) {
                    goalsText.append("Daily glucose on target at " + avgVal + "! Congratulations!\n");
                } else {
                    goalsText.append("Daily glucose off target at " + avgVal + ". Do better.\n");
                }
            }
            catch (Exception e) {}

        }
        else
        {
            goalsText.append("Set a daily mean value to receive alerts on daily goal progress.\n");
        }

        if(appMain.curPatient.getWeeklyMean() >= 0 && appMain.curPatient.getWeeklyVariance() >= 0)
        {
            try {
                Date now = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                if(cal.get(Calendar.DAY_OF_WEEK) == 1)
                {
                    cal.add(Calendar.DATE,-6);
                }
                else
                {
                    cal.set(Calendar.DAY_OF_WEEK,2);
                }
                float avgVal = avgGlucose(BGLMain.sqlDateFormat.parse(BGLMain.dateOnly.format(cal.getTime())), now);
                if(avgVal == -1) {
                    goalsText.append("No glucose has been recorded this week, record when glucose is taken to receive alerts.\n");
                }
                else if (Math.abs(avgVal - appMain.curPatient.getWeeklyMean()) <= appMain.curPatient.getWeeklyVariance()) {
                    goalsText.append("Weekly glucose on target at " + avgVal + "! Congratulations!\n");
                } else {
                    goalsText.append("Weekly glucose off target at " + avgVal + ". Do better.\n");
                }
            }
            catch (Exception e) {}
        }
        else
        {
            goalsText.append("Set a weekly mean value to receive alerts on weekly goal progress.\n");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        //Do when user presses check
        try {
            Patient oldPatient = appMain.curPatient;
            String dMeanText = dailyMean.getText().toString();
            String wMeanText = weeklyMean.getText().toString();
            String dVarText = dailyVariance.getText().toString();
            String wVarText = weeklyVariance.getText().toString();

            appMain.curPatient = new Patient(oldPatient.getID(),
                    oldPatient.getName(),
                    oldPatient.getPrescriptions(),
                    oldPatient.getCIRatio(),
                    oldPatient.getCorrectionDose(),
                    oldPatient.getGlucoseWarningLower(),
                    oldPatient.getGlucoseWarningUpper(),
                    parseValue(dMeanText),
                    parseValue(wMeanText),
                    parseValue(dVarText),
                    parseValue(wVarText));
            updatePatient();
            startActivity(new Intent(this, MainActivity.class));

        } catch (Exception e) {
            Log.d("error_committing_goals",e.getMessage());
        }

        return super.onOptionsItemSelected(item);
    }

    float parseValue(String text)
    {
        return text.length() > 0 ? Float.parseFloat(text) : -1;
    }
    void populateField(EditText field, float value)
    {
        field.setText(value >= 0 ? String.valueOf(value) : "");

    }
    public void updatePatient()
    {
        ContentValues row = new ContentValues();
        row.put("Name",appMain.curPatient.getName());
        row.put("CarbInsulinRatio",appMain.curPatient.getCIRatio());
        row.put("CorrectionDose",appMain.curPatient.getCorrectionDose());
        row.put("GlucoseWarningLower",appMain.curPatient.getGlucoseWarningLower());
        row.put("GlucoseWarningUpper",appMain.curPatient.getGlucoseWarningUpper());
        row.put("DailyMean",appMain.curPatient.getDailyMean());
        row.put("WeeklyMean",appMain.curPatient.getWeeklyMean());
        row.put("DailyVariance",appMain.curPatient.getDailyVariance());
        row.put("WeeklyVariance",appMain.curPatient.getWeeklyVariance());
        appMain.dbMain.update("Patient",row,"ID="+String.valueOf(appMain.curPatient.getID()),null);
        Cursor test = appMain.dbMain.rawQuery("SELECT * FROM Patient WHERE ID = ?",new String[] {String.valueOf(appMain.curPatient.getID())});
        test.moveToFirst();
        Log.d("DailyMean",String.valueOf(test.getFloat(6)));
    }

    public float avgGlucose(Date dateMin, Date dateMax)
    {
        float avg = 0;
        Cursor result = appMain.dbMain.rawQuery("SELECT * FROM Glucose WHERE PatientID = ? " +
                        "AND Date >= ? " +
                        "AND Date <= ?",
                new String[]{String.valueOf(appMain.curPatient.getID()),
                        BGLMain.sqlDateFormat.format(dateMin),
                        BGLMain.sqlDateFormat.format(dateMax)});
        int count = result.getCount();
        if(count == 0)
        {
            return -1;
        }
        while (result.moveToNext()) {
            avg += result.getFloat(2);
        }
        avg /= count;
        return avg;
    }

}

