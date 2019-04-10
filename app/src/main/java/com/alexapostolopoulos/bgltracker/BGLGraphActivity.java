package com.alexapostolopoulos.bgltracker;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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

public class BGLGraphActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String TAG="MainActivity";
    //ADD PointsGrapgSeries of DataPoint type
    PointsGraphSeries<DataPoint> xySeriesInsulin;

    LineGraphSeries<DataPoint> lineSeriesInsulin;

    List<PointsGraphSeries<DataPoint>> listXYseriesInsulins;
    PointsGraphSeries<DataPoint> xySeriesBGL;

    LineGraphSeries<DataPoint> lineSeriesBGL;

    //create graphview object

    SimpleDateFormat sdf[]={new SimpleDateFormat("k:mm"),new SimpleDateFormat("d/M/y")};

    LinearLayout layout;
    int[] colours={Color.MAGENTA,Color.GRAY,Color.DKGRAY,Color.LTGRAY,Color.BLACK,Color.BLUE,Color.RED,Color.YELLOW,Color.GREEN,Color.CYAN};
    int MaxY;
    int MinY=0;
    Date MaxX;
    Date MinX;
    Hashtable<Date,Integer> dateValInsulin;
    Hashtable<Date,Integer> dateValBGL;
    Hashtable<Date,String> dateTypeInsulin;
    Hashtable<Date,String> datePrescriptionN;
    Hashtable<Date,String> dateNotes;

    double bglAvg;
    double insulinAvg;
    double ratio= 1.5;
    List<Date> datesInsulin;
    List<Date> datesBGL;
    List<String>prescriptions;
    List<String>typesInsulin;

    GraphView mScatterPlot;
    String text;
    String formatShown="year";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calendar calendar=Calendar.getInstance();


        calendar.set(calendar.MONTH,0);
        calendar.set(calendar.DATE,0);
        calendar.set(calendar.HOUR_OF_DAY,0);
        calendar.set(calendar.MINUTE,0);
        calendar.set(calendar.SECOND,0);
        MinX = calendar.getTime();
        calendar.add(calendar.YEAR,1);
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
        Date date;
        //getting the insulin and bgl from the "database"
        prescriptions=new ArrayList<>();
        typesInsulin=new ArrayList<>();
        dateValInsulin=new Hashtable<>();
        dateTypeInsulin=new Hashtable<>();
        dateValBGL=new Hashtable<>();
        Calendar calendar=Calendar.getInstance();

        calendar.set(2019, 02, 05);
        date=calendar.getTime();
        dateValInsulin.put(date,40*3/2);
        dateTypeInsulin.put(date,"type1");
        dateValBGL.put(date,60);

        calendar.set(2019, 01, 01);
        date=calendar.getTime();
        dateValInsulin.put(date,90*3/2);
        dateTypeInsulin.put(date,"type1");
        dateValBGL.put(date,115);

        calendar.set(2019, 01, 01);
        calendar.set(calendar.HOUR_OF_DAY,1);
        date=calendar.getTime();
        dateValInsulin.put(date,70*3/2);
        dateTypeInsulin.put(date,"type1");
        dateValBGL.put(date,115);

        calendar.set(2019, 04, 01);
        date=calendar.getTime();
        dateValInsulin.put(date,70*3/2);
        dateTypeInsulin.put(date,"type4");
        dateValBGL.put(date,115);

        calendar.set(2019, 10, 01);
        date=calendar.getTime();
        dateValInsulin.put(date,100*3/2);
        dateTypeInsulin.put(date,"type2");
        dateValBGL.put(date,150);

        calendar.set(2018, 11, 01);
        date=calendar.getTime();
        dateValInsulin.put(date,20*3/2);
        dateTypeInsulin.put(date,"type3");
        dateValBGL.put(date,30);

        calendar.set(2018,01, 10);
        date=calendar.getTime();
        dateValInsulin.put(date,70*3/2);
        dateTypeInsulin.put(date,"type5");
        dateValBGL.put(date,105);

        Enumeration datesInsulin=dateValInsulin.keys();
        this.datesInsulin=Collections.list(datesInsulin);//convert
        Collections.sort(this.datesInsulin, new Comparator<Date>(){

            @Override
            public int compare(Date o1, Date o2) {
                return o1.compareTo(o2);
            }
        });

        Enumeration datesBGL=dateValBGL.keys();
        this.datesBGL=Collections.list(datesBGL);//convert
        Collections.sort(this.datesBGL, new Comparator<Date>(){

            @Override
            public int compare(Date o1, Date o2) {
                return o1.compareTo(o2);
            }
        });

        typesInsulin.add("type1");
        typesInsulin.add("type2");
        typesInsulin.add("type3");
        typesInsulin.add("type4");
        typesInsulin.add("type5");
        typesInsulin.add("type6");
        typesInsulin.add("type7");
//        typesInsulin.add("type8");
//        typesInsulin.add("type9");
//        typesInsulin.add("type10");


        prescriptions.add("presc1");//these should be get once we get the date.
        prescriptions.add("presc2");
        prescriptions.add("presc3");
        prescriptions.add("presc4");
        prescriptions.add("presc5");
        prescriptions.add("presc6");
        prescriptions.add("presc7");




    }
    public void plottingBGLData()
    {
        int changerVal;
        int sumTotal=0;
        int counterTotal=0;
        int sum;
        int counter=0;
        listXYseriesInsulins=new ArrayList<>();
        lineSeriesInsulin=new LineGraphSeries<>(new DataPoint[]{});
        Date tempBeforeDate;
        Date tempAfterDate;
        Calendar calendar=Calendar.getInstance();
        calendar.set(calendar.HOUR_OF_DAY,0);
        calendar.set(calendar.MINUTE,0);
        calendar.set(calendar.SECOND,0);
        calendar.set(calendar.MILLISECOND,0);

        if(formatShown.equals("year")) {
            calendar.set(calendar.MONTH, 0);
            calendar.set(calendar.DATE, 1);
            System.out.println(calendar.getTime());
            if (typesInsulin.size() < 3) {
                changerVal = 1;
            } else if (typesInsulin.size() < 6) {
                changerVal = 2;

            } else changerVal = 3;


        }
        else if(formatShown.equals("month"))
        {

        }
        else;


    }
    public void plottingInsulinData()
    {
        int changerVal;
        int sumTotal=0;
        int counterTotal=0;
        int sum;
        int counter=0;
        listXYseriesInsulins=new ArrayList<>();
        lineSeriesInsulin=new LineGraphSeries<>(new DataPoint[]{});
        Date tempBeforeDate;
        Date tempAfterDate;
        Calendar calendar=Calendar.getInstance();
        calendar.set(calendar.HOUR_OF_DAY,0);
        calendar.set(calendar.MINUTE,0);
        calendar.set(calendar.SECOND,0);
        calendar.set(calendar.MILLISECOND,0);

        if(formatShown.equals("year"))
        {
            calendar.set(calendar.MONTH,0);
            calendar.set(calendar.DATE,1);
            System.out.println(calendar.getTime());
            if(typesInsulin.size()<3) {
                changerVal=1;
            }
            else if(typesInsulin.size()<6)
            {
                changerVal=2;

            }
            else changerVal=3;

            for(int j=0;j<(12/changerVal);j++) {
                tempBeforeDate=calendar.getTime();
                calendar.add(calendar.MONTH,changerVal);
                calendar.set(calendar.DATE,1);
                tempAfterDate=calendar.getTime();
                calendar.set(calendar.MONTH,changerVal*j);
                for (int k=0;k<typesInsulin.size();k++) {
                    System.out.println(typesInsulin.get(k));
                    sum=0;
                    counter=0;
                    PointsGraphSeries<DataPoint> xySeriesInsulin=new PointsGraphSeries();
                    xySeriesInsulin.setColor(colours[k]);

                    for(int i = 0; i < datesInsulin.size(); i++)
                    {
//                        if((dateTypeInsulin.get(datesInsulin.get(i)).equals(typesInsulin.get(k)) &&
//                                datesInsulin.get(i).after(tempBeforeDate) &&
//                                datesInsulin.get(i).before(tempAfterDate)))
//                            System.out.println((dateTypeInsulin.get(datesInsulin.get(i)).equals(typesInsulin.get(k)) &&
//                                    datesInsulin.get(i).after(tempBeforeDate) &&
//                                    datesInsulin.get(i).before(tempAfterDate))+"   "+
//                                            dateTypeInsulin.get(datesInsulin.get(i)).equals(typesInsulin.get(k))+"   "+
//                                            typesInsulin.get(k)+"   "+datesInsulin.get(i)+"     "+
//                                            datesInsulin.get(i).after(tempBeforeDate)+"     "+
//                                            datesInsulin.get(i).before(tempAfterDate)+"      "+tempBeforeDate+"    "+
//                                            tempAfterDate
//                                    );
                        if(dateTypeInsulin.get(datesInsulin.get(i)).equals(typesInsulin.get(k)) &&
                        datesInsulin.get(i).after(tempBeforeDate) &&
                                datesInsulin.get(i).before(tempAfterDate))
                        {

                            counter++;
                            sum=sum+dateValInsulin.get(datesInsulin.get(i));
                            System.out.println(typesInsulin.get(k)+" "+sum+" "+ datesInsulin.get(i));
                            System.out.println(dateValInsulin.get(datesInsulin.get(i)));
                        }
                    }
                    sumTotal=sumTotal+sum;
                    counterTotal=counterTotal+counter;
                    //calendar.set(calendar.MONTH,j*changerVal);
                    System.out.println(calendar.getTime());
                    //System.out.println(typesInsulin.get(k)+"vsdv"+calendar.getTime());

                    System.out.println(calendar.getTime()+"  "+(j*changerVal));
                    if(counter!=0) {
                        xySeriesInsulin.appendData(new DataPoint(calendar.getTime(), sum / counter), true, 180);
                        lineSeriesInsulin.appendData(new DataPoint(calendar.getTime(), sum / counter), true, 180);
                    }else
                    {
                        xySeriesInsulin.appendData(new DataPoint(calendar.getTime(), 0), true, 180);
                        lineSeriesInsulin.appendData(new DataPoint(calendar.getTime(), 0), true, 180);
                    }
                    mScatterPlot.addSeries(xySeriesInsulin);
                    //calendar.set(calendar.DATE,0);

                    listXYseriesInsulins.add(xySeriesInsulin);
                    calendar.add(calendar.DATE,28*changerVal/typesInsulin.size());

                }
                //calendar.add(calendar.MONTH,changerVal);
                mScatterPlot.addSeries(lineSeriesInsulin);
            }
            insulinAvg=sumTotal/counterTotal;




        }
        else if(formatShown.equals("month"))
        {
            calendar.set(calendar.DATE,0);
            if(typesInsulin.size()<3) {
                changerVal=3;
            }
            else if(typesInsulin.size()<6)
            {
                changerVal=4;

            }
            else changerVal=6;

            for(int j=0;j<(30/changerVal);j++) {
                tempBeforeDate=calendar.getTime();
                calendar.add(calendar.DATE,changerVal);
                tempAfterDate=calendar.getTime();
                for (int k=0;k<typesInsulin.size();k++) {
                    sum=0;
                    xySeriesInsulin=new PointsGraphSeries();
                    xySeriesInsulin.setColor(colours[k]);
                    for(int i = 0; i < datesInsulin.size(); i++)
                    {
                        if(dateTypeInsulin.get(datesInsulin.get(i)).equals(typesInsulin.get(i)) &&
                                datesInsulin.get(i).after(tempBeforeDate) &&
                                datesInsulin.get(i).before(tempAfterDate))
                        {
                            counter++;
                            sum=sum+dateValInsulin.get(datesInsulin.get(i));
                        }
                    }
                    calendar.add(calendar.DATE,-1*changerVal);
                    calendar.add(calendar.DATE,changerVal/typesInsulin.size());
                    if(counter!=0) {
                        xySeriesInsulin.appendData(new DataPoint(calendar.getTime(), sum / counter), true, 180);
                        lineSeriesInsulin.appendData(new DataPoint(calendar.getTime(), sum / counter), true, 180);
                    }else
                    {
                        xySeriesInsulin.appendData(new DataPoint(calendar.getTime(), 0), true, 180);
                        lineSeriesInsulin.appendData(new DataPoint(calendar.getTime(), 0), true, 180);
                    }

                    calendar.add(calendar.DATE,changerVal);
                }
                mScatterPlot.addSeries(xySeriesInsulin);
                mScatterPlot.addSeries(lineSeriesInsulin);
            }
        }
        else
        {
            tempBeforeDate=calendar.getTime();
            calendar.add(calendar.DATE,1);
            tempAfterDate=calendar.getTime();
            for (int k=0;k<typesInsulin.size();k++) {
                xySeriesInsulin=new PointsGraphSeries();
                xySeriesInsulin.setColor(colours[k]);
                for(int i = 0; i < datesInsulin.size(); i++)
                {

                    if(dateTypeInsulin.get(datesInsulin.get(i)).equals(typesInsulin.get(i)) &&
                            datesInsulin.get(i).after(tempBeforeDate) &&
                            datesInsulin.get(i).before(tempAfterDate))
                    {
                        xySeriesInsulin.appendData(new DataPoint(calendar.getTime(),dateValInsulin.get(datesInsulin.get(i))),true,180);
                        lineSeriesInsulin.appendData(new DataPoint(calendar.getTime(),dateValInsulin.get(datesInsulin.get(i))),true,180);
                    }
                }
            }
            mScatterPlot.addSeries(xySeriesInsulin);
            mScatterPlot.addSeries(lineSeriesInsulin);
        }


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
        //mScatterPlot.getGridLabelRenderer().setHumanRounding(true);

        layout.addView(mScatterPlot,paramG);
        if(formatShown.equals("year") || formatShown.equals("month"))
            changeXLabels(1);
        else
            changeXLabels(0);

        //createScatterPlot();

    }
    private void createScatterPlot()
    {

//        lineSeriesInsulin= new LineGraphSeries<>(new DataPoint[]{});//as we always change the points on the graph
//        xySeriesInsulin=new PointsGraphSeries();
        lineSeriesBGL= new LineGraphSeries<>(new DataPoint[]{});
        xySeriesBGL=new PointsGraphSeries();

        updateGraph();//make the blank graph

        plottingInsulinData();

//        for(int i = 0; i < datesInsulin.size(); i++){
//            Date currDate=datesInsulin.get(i);
//            xySeriesInsulin.appendData(new DataPoint(currDate,dateValInsulin.get(currDate)),true,180);
//            lineSeriesInsulin.appendData(new DataPoint(currDate,dateValInsulin.get(currDate)),true,180);
//        }

//        mScatterPlot.addSeries(lineSeriesInsulin);
//        mScatterPlot.addSeries(xySeriesInsulin);

        for(int i = 0; i < datesBGL.size(); i++){
            Date currDate=datesBGL.get(i);
            xySeriesBGL.appendData(new DataPoint(currDate,dateValBGL.get(currDate)),true,180);
            lineSeriesBGL.appendData(new DataPoint(currDate,dateValBGL.get(currDate)),true,180);
        }
        lineSeriesBGL.setColor(Color.RED);
        xySeriesBGL.setColor(Color.RED);
        mScatterPlot.addSeries(lineSeriesBGL);
        mScatterPlot.addSeries(xySeriesBGL);


        //SET listener for points
        for(int i=0;i<listXYseriesInsulins.size();i++){
            listXYseriesInsulins.get(i).setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series xySeriesInsulin, DataPointInterface dataPoint) {
                    String dateFormatted = sdf[1].format((new Date((long) dataPoint.getX())).getTime());
                    Toast.makeText(getActivity(), "Xander is the guy "+dateFormatted, Toast.LENGTH_SHORT).show();
                }
            });
        }

        xySeriesBGL.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series xySeriesBGL, DataPointInterface dataPoint) {
                String dateFormatted = sdf[1].format((new Date((long) dataPoint.getX())).getTime());
                Toast.makeText(getActivity(), "Xander is the guy2 "+dateFormatted, Toast.LENGTH_SHORT).show();
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
            formatShown="days";
            //changeXLabels(0);
            calendar.set(calendar.HOUR_OF_DAY,0);
            calendar.set(calendar.MINUTE,0);
            calendar.set(calendar.SECOND,0);
//            calendar.set(Calendar.MINUTE,0);
//            calendar.set(Calendar.HOUR,0);
//            calendar.set(Calendar.SECOND,0);
//            calendar.set(Calendar.AM,0);
            MinX = calendar.getTime();

//            calendar.set(Calendar.HOUR,5);
//            Date dae2=calendar.getTime();

            //xySeries.appendData(new DataPoint(dae2,50.0),true,53);
            //mScatterPlot1.addSeries(xySeries);
            calendar.add(calendar.DATE,1);
            MaxX=calendar.getTime();
            //Date maxDate = calendar.getTime();
            /*Toast.makeText(parent.getContext(),
                    minDate+""+maxDate,
                    Toast.LENGTH_SHORT).show();*/




            createScatterPlot();
        }
        else if (text.equals("Current year")) {
            formatShown="year";
            //changeXLabels(0);
            calendar.set(calendar.MONTH,0);
            calendar.set(calendar.DATE,0);
            calendar.set(calendar.HOUR_OF_DAY,0);
            calendar.set(calendar.MINUTE,0);
            calendar.set(calendar.SECOND,0);
            MinX =  calendar.getTime();
            calendar.add(calendar.YEAR,1);
            MaxX =  calendar.getTime();

            createScatterPlot();
            //createScatterPlot(150,maxDate,0,minDate);
        }
        else if (text.equals("Last 30 days")) {
            formatShown="month";
            //changeXLabels(0);
            calendar.set(calendar.DATE,0);
            calendar.set(calendar.HOUR_OF_DAY,0);
            calendar.set(calendar.MINUTE,0);
            calendar.set(calendar.SECOND,0);
            MinX =  calendar.getTime();
            calendar.add(calendar.MONTH,1);
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