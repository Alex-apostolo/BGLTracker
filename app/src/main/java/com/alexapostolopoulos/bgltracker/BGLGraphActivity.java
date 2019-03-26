package com.alexapostolopoulos.bgltracker;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class BGLGraphActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";
    //ADD PointsGrapgSeries of DataPoint type
    PointsGraphSeries<DataPoint> xySeries;

    //create graphview object
    GraphView mScatterPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bglgraph);

        // links the graphview to the layout xml
        mScatterPlot=(GraphView) findViewById(R.id.scatterPlot);

        createScatterPlot();
    }

    private void createScatterPlot()
    {
        Log.d(TAG, "createScatterPlot: Creating scatter plot");
        xySeries=new PointsGraphSeries();


        //data must be added in order from the least x value to the latter one
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018,11,21);
        Date date =  calendar.getTime();
        //LocalDate localDate = LocalDate.of(2018, 11, 21);
        xySeries.appendData(new DataPoint(date,60.0),true,1000124);
        xySeries.appendData(new DataPoint(date,50.0),true,100014);

        xySeries.setShape(PointsGraphSeries.Shape.RECTANGLE);
        xySeries.setColor(Color.BLUE);
        xySeries.setSize(20f);

        //set Scrollable and Scalable
        mScatterPlot.getViewport().setScalable(true);
        mScatterPlot.getViewport().setScrollable(true);
        mScatterPlot.getViewport().setScalableY(true);
        mScatterPlot.getViewport().setScrollableY(true);


        // set date label formatter
        mScatterPlot.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        mScatterPlot.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        //set manual y bounds
        mScatterPlot.getViewport().setYAxisBoundsManual(true);
        mScatterPlot.getViewport().setMaxY(150);
        mScatterPlot.getViewport().setMinY(0);

        //set manual x bounds
        mScatterPlot.getViewport().setXAxisBoundsManual(true);
        calendar.set(2018,1,1);
        Date date2 =  calendar.getTime();
        calendar.set(2019,1,1);
        Date date3 =  calendar.getTime();
        mScatterPlot.getViewport().setMaxX(date3.getTime());
        mScatterPlot.getViewport().setMinX(date2.getTime());

        mScatterPlot.addSeries(xySeries);

        mScatterPlot.getGridLabelRenderer().setHumanRounding(false);
    }
    public final Activity getActivity (){return this;}
}