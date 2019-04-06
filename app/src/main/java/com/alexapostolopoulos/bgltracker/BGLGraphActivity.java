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
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class BGLGraphActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String TAG="MainActivity";
    //ADD PointsGrapgSeries of DataPoint type
    PointsGraphSeries<DataPoint> xySeries;

    LineGraphSeries<DataPoint> lineSeries;

    //create graphview object

    SimpleDateFormat sdf[]={new SimpleDateFormat("k:mm"),new SimpleDateFormat("k:m d/M/y")};

    LinearLayout layout;
    int MaxY;
    int MinY=0;
    Date MaxX;
    Date MinX;
    Hashtable<Date,Integer> dateValInsulin;
    List<Date> datesInsulin;
    List<Date> datesBGL;
    List<Integer> insulinVal;
    List<Integer> bglVal;
    GraphView mScatterPlot;
    String text;
    boolean graphBlank;
    Calendar calendar=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        graphBlank=true;


        calendar.set(2019,0,0);
        MinX = calendar.getTime();
        calendar.set(2020,0,0);
        MaxX = calendar.getTime();
        MaxY=150;


        getData();

        List<String> periods=new ArrayList<>();
        periods.add("Select period");
        periods.add("Current year");
        periods.add("Last 30 days");
        periods.add("Today");






        layout=new LinearLayout(this);
        LinearLayout.LayoutParams paramsL=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        setContentView(layout,paramsL);
        layout.setOrientation(layout.VERTICAL);


        //updateGraph();


        LinearLayout.LayoutParams paramS = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                9.0f
        );

        Spinner periodsSpin= new Spinner(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, periods);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodsSpin.setAdapter(dataAdapter);
        periodsSpin.setOnItemSelectedListener(this);
        layout.addView(periodsSpin,paramS);



        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        createScatterPlot();

        // links the graphview to the layout xml
//        mScatterPlot1=findViewById(R.id.scatterPlot);
//        mScatterPlot1.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
//
//        calendar.set(2019,0,0);
//        calendar.set(Calendar.MINUTE, 1);
//        Date firstDate =  Calendar.getInstance().getTime();
//        calendar.set(2020,0,0);
//        calendar.set(Calendar.MILLISECOND, -1);
//        Date date3 =  calendar.getTime();
//
//        createScatterPlot(150,date3,0,firstDate);
//
//        spinner=findViewById(R.id.spinner1);
//
//        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.timePeriods, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(this);

    }

    public void getData()
    {

        //getting the insulin and bgl from the "database"

        dateValInsulin=new Hashtable<>();
        Calendar calendar=Calendar.getInstance();
        calendar.set(2019, 03, 01);
        dateValInsulin.put(calendar.getTime(),40);
        calendar.set(2019, 01, 01);
        dateValInsulin.put(calendar.getTime(),120);
        calendar.set(2019, 10, 01);
        dateValInsulin.put(calendar.getTime(),110);
        calendar.set(2018, 11, 01);
        dateValInsulin.put(calendar.getTime(),20);
        calendar.set(2018,01, 10);
        dateValInsulin.put(calendar.getTime(),70);

        Enumeration datesInsulin=dateValInsulin.keys();
        this.datesInsulin=Collections.list(datesInsulin);//convert
        Collections.sort(this.datesInsulin, new Comparator<Date>(){

            @Override
            public int compare(Date o1, Date o2) {
                return o1.compareTo(o2);
            }
        });




        // do the same with insulin dates
    }
    public void setBounds()
    {
        mScatterPlot.getViewport().setMaxY(MaxY);
        mScatterPlot.getViewport().setMinY(MinY);

        mScatterPlot.getViewport().setMaxX(MaxX.getTime());
        mScatterPlot.getViewport().setMinX(MinX.getTime());
    }
    public void setMinMax(int MaxY,Date MaxX,int MinY,Date MinX)
    {
        this.MinX=MinX;
        this.MaxX=MaxX;
        this.MaxY=MaxY;
        this.MinY=MinY;
    }

    public void updateGraph()
    {
        if(mScatterPlot!=null) layout.removeView(mScatterPlot);

        graphBlank=true;

        mScatterPlot=new GraphView(this);
        mScatterPlot.setTitle("BGL graph");

        // set manual x bounds
        mScatterPlot.getViewport().setXAxisBoundsManual(true);
        //set manual y bounds
        mScatterPlot.getViewport().setYAxisBoundsManual(true);
        setBounds();

        mScatterPlot.getGridLabelRenderer().setHorizontalLabelsAngle(110);//set angle to..
        mScatterPlot.getGridLabelRenderer().setNumHorizontalLabels(10); // only 16 because of the space

        //we cannot set a label for the second axis
        LinearLayout.LayoutParams paramG = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        //get the second axis
        mScatterPlot.getSecondScale().setMinY(0);
        mScatterPlot.getSecondScale().setMaxY(100);
        mScatterPlot.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED);
        mScatterPlot.getGridLabelRenderer().setHumanRounding(true);

        layout.addView(mScatterPlot,paramG);
        changeXLabels(1);

        //createScatterPlot();

    }
    private void createScatterPlot()
    {

        lineSeries= new LineGraphSeries<>(new DataPoint[]{});//as we always change the points on the graph
        xySeries=new PointsGraphSeries();

        updateGraph();//make the blank graph
        graphBlank=false;


        for(int i = 0; i < datesInsulin.size(); i++){
            Date currDate=datesInsulin.get(i);
            xySeries.appendData(new DataPoint(currDate,dateValInsulin.get(currDate)),true,180);
            lineSeries.appendData(new DataPoint(currDate,dateValInsulin.get(currDate)),true,180);
        }
        //do the same for he insulinDates
        mScatterPlot.addSeries(lineSeries);
        mScatterPlot.addSeries(xySeries);
        // do the same with BGL


        //SET listener for points
        xySeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series xySeries, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "Xander is the guy"+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

        //do the same for he insulinDates
    }
    /*private void createScatterPlot(int MaxY,Date MaxX,int MinY,Date MinX)
    {
        LinearLayout layout = new LinearLayout(this);
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
    }*/

    public final Activity getActivity (){return this;}


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Calendar calendar=Calendar.getInstance();

        text=parent.getItemAtPosition(position).toString();


        // set everything as they were before u dumb fak!

        if(text.equals("Select period"))
        {

            Toast.makeText(parent.getContext(),
                    "Please select a suitable period of time.",
                    Toast.LENGTH_SHORT).show();
        }
        if (text.equals("Today")) {

            changeXLabels(0);
            calendar.set(calendar.YEAR,0,Calendar.DATE,0,0,0);
//            calendar.set(Calendar.MINUTE,0);
//            calendar.set(Calendar.HOUR,0);
//            calendar.set(Calendar.SECOND,0);
//            calendar.set(Calendar.AM,0);
            MinX = calendar.getTime();

//            calendar.set(Calendar.HOUR,5);
//            Date dae2=calendar.getTime();

            //xySeries.appendData(new DataPoint(dae2,50.0),true,53);
            //mScatterPlot1.addSeries(xySeries);
            long d=MinX.getTime();
            d=d+86400000;
            MaxX=new Date(d);
            //Date maxDate = calendar.getTime();
            /*Toast.makeText(parent.getContext(),
                    minDate+""+maxDate,
                    Toast.LENGTH_SHORT).show();*/




            createScatterPlot();
        }
        else if (text.equals("Current year")) {

            calendar.set(calendar.YEAR,0,0,0,0,0);
            MinX =  calendar.getTime();
            calendar.set(calendar.YEAR+1,0,0,0,0,0);
            MaxX =  calendar.getTime();

            createScatterPlot();
            //createScatterPlot(150,maxDate,0,minDate);
        }
        else if (text.equals("Last 30 days")) {

            calendar.set(calendar.YEAR,calendar.MONTH,0,0,0,0);
            MinX = calendar.getTime();
            calendar.add(calendar.DATE, 30);
            MaxX = calendar.getTime();

            createScatterPlot();

            //createScatterPlot(150, maxDate, 0, minDate);
        }
    }
    public void changeXLabels(final int indexFormat)
    {
        mScatterPlot.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
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