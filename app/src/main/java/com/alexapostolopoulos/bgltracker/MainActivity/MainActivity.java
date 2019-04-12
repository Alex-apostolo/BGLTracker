package com.alexapostolopoulos.bgltracker.MainActivity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alexapostolopoulos.bgltracker.MainActivity.CustomRowData;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alexapostolopoulos.bgltracker.AddGlucoseActivity;
import com.alexapostolopoulos.bgltracker.AddInsulinActivity;
import com.alexapostolopoulos.bgltracker.AddMedicationActivity;
import com.alexapostolopoulos.bgltracker.AddPatientActivity;
import com.alexapostolopoulos.bgltracker.BGLGraphActivity;
import com.alexapostolopoulos.bgltracker.BGLMain;
import com.alexapostolopoulos.bgltracker.Model.Glucose;
import com.alexapostolopoulos.bgltracker.Model.Insulin;
import com.alexapostolopoulos.bgltracker.Model.Medication;
import com.alexapostolopoulos.bgltracker.R;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BGLMain appMain;
    ArrayList<CustomRowData> measurements;
    ListView BGLList;
    ArrayList<CustomRowData> measurementsDisplay;
    String typeFilter;

    public static void addDays(Date d, int days)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, days);
        d.setTime( c.getTime().getTime() );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appMain = (BGLMain)this.getApplication();
        BGLList = findViewById(R.id.entry_list);
        typeFilter = "General";

        BGLList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomRowData selectedRow = (CustomRowData)parent.getItemAtPosition(position);
                switch(selectedRow.getTitle())
                {
                    case "Glucose":
                        editGlucose((Glucose)selectedRow.getData());
                        break;
                    case "Insulin":
                        editInsulin((Insulin)selectedRow.getData());
                        break;
                    case "Medication":
                        editMedication((Medication)selectedRow.getData());
                        break;
                }

            }
        });
        LastDayClick(null);
    }


    public void editGlucose(Glucose glucose)
    {
        Intent startGlucose = new Intent(this,AddGlucoseActivity.class);
        startGlucose.putExtra("isNew",false);
        startGlucose.putExtra("glucose",glucose);
        startActivity(startGlucose);
    }

    public void editInsulin (Insulin insulin)
    {
        Intent startInsulin = new Intent(this,AddInsulinActivity.class);
        startInsulin.putExtra("isNew",false);
        startInsulin.putExtra("insulin",insulin);
        startInsulin.putExtra("callingForm","Main");
        startActivity(startInsulin);
    }

    public void editMedication (Medication medication)
    {
        Intent startMedication = new Intent(this,AddMedicationActivity.class);
        startMedication.putExtra("isNew",false);
        startMedication.putExtra("medication",medication);
        startMedication.putExtra("callingForm","Main");
        startActivity(startMedication);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    public void addPatientClick(MenuItem item)
    {
        Intent addPatient = new Intent(this,AddPatientActivity.class);
        startActivity(addPatient);
    }

    public void addBGLGraphClick(MenuItem item)
    {
        Calendar calendar=Calendar.getInstance();
        calendar.set(calendar.MONTH,0);
        calendar.set(calendar.DATE,1);
        calendar.set(calendar.HOUR_OF_DAY,0);
        calendar.set(calendar.MINUTE,0);
        calendar.set(calendar.SECOND,0);
        Date minDate=calendar.getTime();
        calendar.set(calendar.MONTH,11);
        calendar.set(calendar.DATE,31);
        Date maxDate= calendar.getTime();
        Intent addBGLGraph = new Intent(this, BGLGraphActivity.class);
        System.out.println(" asfasfasgasg1211111111");
        retrieveEntries(minDate,maxDate);
        System.out.println(appMain.curPatient.getGlucoseWarningLower());
        System.out.println(appMain.curPatient.getGlucoseWarningUpper());
        for(int i=0;i<measurements.size();i++)
        {
            System.out.println("hey");
            System.out.println(measurements.get(i).getData());
            if(measurements.get(i).getTitle().equals("Insulin"))
            {
                Insulin obj = (Insulin)measurements.get(i).getData();
                System.out.println(obj.getDosage());
                addBGLGraph.putExtra("measurements",obj.getDosage());
            }
        }
        //System.out.println(measurements.get(0));
        //addBGLGraph.putExtra("measurements",measurements);
        startActivity(addBGLGraph);
    }

    public void filterAllClick(MenuItem item)
    {
        typeFilter = "General";
        refreshFilter();
    }

    public void filterGlucoseClick(MenuItem item)
    {
        typeFilter = "Glucose";
        refreshFilter();
    }

    public void filterInsulinClick(MenuItem item)
    {
        typeFilter = "Insulin";
        refreshFilter();
    }

    public void filterMedicationClick(MenuItem item)
    {
        typeFilter = "Medication";
        refreshFilter();
    }

    public void LastDayClick(MenuItem item)
    {
        try {
            Date now = new Date();
            Date today = BGLMain.sqlDateFormat.parse(BGLMain.dateOnly.format(now));
            retrieveEntries(today,now);
        }
        catch (Exception e) { Log.d("error_parsing_date",e.getMessage());}
    }

    public void LastWeekClick(MenuItem item)
    {
        try {
            Date now = new Date();
            Date weekAgo = new Date(now.getTime());
            addDays(weekAgo,-7);
            Log.d("now:",BGLMain.sqlDateFormat.format(now));
            retrieveEntries(weekAgo,now);
        }
        catch (Exception e) {Log.d("error_parsing_date",e.getMessage());}
    }

    public void onButtonOptionsItemSelected(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.fab_menu_glucose:
                intent = new Intent(this, AddGlucoseActivity.class);
                intent.putExtra("isNew",true);
                startActivity(intent);
                break;
            case R.id.fab_menu_insulin:
                intent = new Intent(this, AddInsulinActivity.class);
                intent.putExtra("isNew",true);
                intent.putExtra("callingForm","Main");
                startActivity(intent);
                break;
            case R.id.fab_menu_medication:
                intent = new Intent(this, AddMedicationActivity.class);
                intent.putExtra("isNew",true);
                intent.putExtra("callingForm","Main");
                startActivity(intent);
        }
    }

    public void refreshFilter()
    {
        if(typeFilter.equals("General"))
        {
            measurementsDisplay = measurements;
        }
        else {
            measurementsDisplay = new ArrayList<>();
            for (CustomRowData measurement : measurements) {
                if (measurement.getTitle().equals(typeFilter)) {
                    measurementsDisplay.add(measurement);
                }
            }
        }
        refreshList();
    }

    public void refreshList()
    {
        ListAdapter adapter = new CustomAdapter(this, measurementsDisplay.toArray(new CustomRowData[measurementsDisplay.size()]));
        BGLList.setAdapter(adapter);
    }

    public void retrieveEntries(Date dateMin, Date dateMax)
    {
        Log.d("min",BGLMain.sqlDateFormat.format(dateMin));
        Log.d("max",BGLMain.sqlDateFormat.format(dateMax));
        measurements = new ArrayList<>();
        try {
            Cursor result = appMain.dbMain.rawQuery("SELECT * FROM Glucose WHERE PatientID = ? " +
                            "AND Date >= ? " +
                            "AND Date <= ?",
                    new String[]{String.valueOf(appMain.curPatient.getID()),
                            BGLMain.sqlDateFormat.format(dateMin),
                            BGLMain.sqlDateFormat.format(dateMax)});
            while (result.moveToNext()) {
                Glucose data = new Glucose(result.getInt(0),
                        result.getFloat(2),
                        BGLMain.sqlDateFormat.parse(result.getString(3)),
                        result.getString(4));
                CustomRowData row = new CustomRowData("Glucose", String.valueOf(data.getValue()) + " mmlg", data.getDateTime(), data);
                measurements.add(row);
                Log.d("GlucoseID:",String.valueOf(result.getInt(0)));
            }
            result = appMain.dbMain.rawQuery("SELECT * FROM Insulin WHERE PatientID = ?" +
                            "AND Date >= ?" +
                            "AND Date <= ?",
                    new String[]{String.valueOf(appMain.curPatient.getID()),
                            BGLMain.sqlDateFormat.format(dateMin),
                            BGLMain.sqlDateFormat.format(dateMax)});
            while(result.moveToNext())
            {
                Insulin data = new Insulin(result.getInt(0),
                        result.getInt(2),
                        result.getFloat(3),
                        BGLMain.sqlDateFormat.parse(result.getString(4)),
                        result.getString(5));
                measurements.add(new CustomRowData("Insulin",String.valueOf(data.getDosage()) + " IU",data.getDateTime(),data));
            }

            result = appMain.dbMain.rawQuery("SELECT * FROM Medication WHERE PatientID = ?" +
                            "AND Date >= ?" +
                            "AND Date <= ?",
                    new String[] {String.valueOf(appMain.curPatient.getID()),
                            BGLMain.sqlDateFormat.format(dateMin),
                            BGLMain.sqlDateFormat.format(dateMax)});
            while(result.moveToNext())
            {
                Medication data = new Medication(result.getInt(0),
                        result.getInt(2),
                        result.getString(3),
                        result.getFloat(4),
                        result.getString(5),
                        BGLMain.sqlDateFormat.parse(result.getString(6)),
                        result.getString(7));
                measurements.add(new CustomRowData("Medication",String.valueOf(data.getDosage()) + data.getUnit(),data.getDateTime(),data));
            }

            Collections.sort(measurements);
            refreshFilter();

        }

        catch (Exception e) {
            Log.d("error_retrieving_data",e.getMessage());

        }

    }


}


