package com.alexapostolopoulos.bgltracker;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class BGLGraphActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String TAG="MainActivity";
    //ADD PointsGrapgSeries of DataPoint type
    PointsGraphSeries<DataPoint> xySeries;

    LineGraphSeries<DataPoint> lineSeries;

    //create graphview object

    SimpleDateFormat sdf[]={new SimpleDateFormat("k:mm"),new SimpleDateFormat("k:m d/M/y")};

    GraphView mScatterPlot1;
    Spinner spinner;
    String text;
    Calendar calendar=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bglgraph);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // links the graphview to the layout xml
        mScatterPlot1=findViewById(R.id.scatterPlot);
        mScatterPlot1.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        changeXLabels(1);
        calendar.set(2019,0,0);
        calendar.set(Calendar.MINUTE, 1);
        Date firstDate =  Calendar.getInstance().getTime();
        calendar.set(2020,0,0);
        calendar.set(Calendar.MILLISECOND, -1);
        Date date3 =  calendar.getTime();

        createScatterPlot(150,date3,0,firstDate);

        spinner=findViewById(R.id.spinner1);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.timePeriods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    private void createScatterPlot(int MaxY,Date MaxX,int MinY,Date MinX)
    {
        LinearLayout layout = findViewById(R.id.bglGraph_LinearLayout);
        layout.removeView(mScatterPlot1);

        Calendar calendar=Calendar.getInstance();
        Log.d(TAG, "createScatterPlot: Creating scatter plot");
        xySeries=new PointsGraphSeries();
        lineSeries= new LineGraphSeries<>(new DataPoint[]{});

        mScatterPlot1.onDataChanged(true,true);

        //data must be added in order from the least x value to the latter one

        calendar.set(2019,11,21);
        Date date =  calendar.getTime();
        //LocalDate localDate = LocalDate.of(2018, 11, 21);
        xySeries.appendData(new DataPoint(date,60.0),true,2);
        xySeries.appendData(new DataPoint(date,50.0),true,2);


        lineSeries.appendData(new DataPoint(date,60.0),true,2);
        lineSeries.appendData(new DataPoint(date,50.0),true,3);


        xySeries.setShape(PointsGraphSeries.Shape.RECTANGLE);
        xySeries.setColor(Color.BLUE);
        xySeries.setSize(20f);

        //set Scrollable and Scalable
//        mScatterPlot.getViewport().setScalable(true);
//        mScatterPlot.getViewport().setScrollable(true);
//        mScatterPlot.getViewport().setScalableY(true);
//        mScatterPlot.getViewport().setScrollableY(true);


        // set date label formatter

        mScatterPlot1.getGridLabelRenderer().setNumHorizontalLabels(16); // only 4 because of the space
        mScatterPlot1.getGridLabelRenderer().setHorizontalLabelsAngle(120);//set angle to..

        mScatterPlot1.getGridLabelRenderer().setNumVerticalLabels(5);
        //set manual y bounds
        mScatterPlot1.getViewport().setYAxisBoundsManual(true);
        mScatterPlot1.getViewport().setMaxY(MaxY);
        mScatterPlot1.getViewport().setMinY(MinY);

        //set manual x bounds
        mScatterPlot1.getViewport().setXAxisBoundsManual(true);

        mScatterPlot1.getViewport().setMaxX(MaxX.getTime());
        mScatterPlot1.getViewport().setMinX(MinX.getTime());

        mScatterPlot1.addSeries(xySeries);
        mScatterPlot1.addSeries(lineSeries);
        mScatterPlot1.getGridLabelRenderer().setHumanRounding(false);

        // the y bounds are always manual for second scale
        mScatterPlot1.getSecondScale().setMinY(0);
        mScatterPlot1.getSecondScale().setMaxY(100);

        mScatterPlot1.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        params.weight = 9;

        layout.addView(mScatterPlot1, params);
    }

    public final Activity getActivity (){return this;}


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Calendar calendar=Calendar.getInstance();

        text=parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + text,
                Toast.LENGTH_SHORT).show();

        if (text.equals("Today")) {

            changeXLabels(0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.HOUR,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.AM,0);
            Date minDate = calendar.getTime();

//            calendar.set(Calendar.HOUR,5);
//            Date dae2=calendar.getTime();

            //xySeries.appendData(new DataPoint(dae2,50.0),true,53);
            //mScatterPlot1.addSeries(xySeries);
            long d=minDate.getTime();
            d=d+86400000;
            Date maxDate=new Date(d);
            //Date maxDate = calendar.getTime();
            Toast.makeText(parent.getContext(),
                    minDate+""+maxDate,
                    Toast.LENGTH_SHORT).show();




            createScatterPlot(150, maxDate, 0, minDate);
        }
        else if (text.equals("Current year")) {
            changeXLabels(1);
            calendar.set(2019,0,0);
            Date minDate =  calendar.getTime();
            calendar.set(2020,0,0);
            Date maxDate =  calendar.getTime();
            createScatterPlot(150,maxDate,0,minDate);
        }
        else if (text.equals("Current month")) {
            changeXLabels(1);
            calendar.set(2019, 8, 0);
            Date minDate = calendar.getTime();
            calendar.set(2020, 0, 0);
            Date maxDate = calendar.getTime();

            createScatterPlot(150, maxDate, 0, minDate);
        }
    }
    public void changeXLabels(final int indexFormat)
    {
        mScatterPlot1.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
        {

            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX) {
                    return sdf[indexFormat].format(new Date(((long)value)));
                } else return super.formatLabel(value, isValueX);
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}