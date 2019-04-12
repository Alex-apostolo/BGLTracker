package com.alexapostolopoulos.bgltracker;

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

public class BGLGraphActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String TAG="MainActivity";
    BGLMain appMain;
    //ADD PointsGrapgSeries of DataPoint type
    PointsGraphSeries<DataPoint> xySeriesInsulin;

    LineGraphSeries<DataPoint> lineSeriesInsulin;

    List<PointsGraphSeries<DataPoint>> listXYseriesInsulins;
    PointsGraphSeries<DataPoint> xySeriesBGL;

    LineGraphSeries<DataPoint> lineSeriesBGL;

    LineGraphSeries<DataPoint> lineSeriesMin;
    LineGraphSeries<DataPoint> lineSeriesMax;

    //create graphview object

    SimpleDateFormat sdf[]={new SimpleDateFormat("k:mm"),new SimpleDateFormat("d/M/y")};

    LinearLayout layout;
    int[] colours={Color.MAGENTA,Color.GRAY,Color.DKGRAY,Color.LTGRAY,Color.BLACK,Color.BLUE,Color.RED,Color.YELLOW,Color.GREEN,Color.CYAN};
    double MaxY;
    int MinY=0;
    Date MaxX;
    Date MinX;
    int minGoal=30;
    int maxGoal=100;
    Hashtable<Date,Integer> dateValInsulin;
    Hashtable<Date,Integer> dateValBGL;
    Hashtable<Date,String> dateTypeInsulin;
    Hashtable<Date,String> datePrescriptionN;
    Hashtable<Date,String> dateNotes;

    double bglAvg;
    double insulinAvg;
    int maxPlottedY;
    double ratio;
    List<Date> datesInsulin;
    List<Date> datesBGL;
    List<String>prescriptions;
    List<String>typesInsulin;

    GraphView mScatterPlot;
    String text;
    String formatShown="year";

    Dialog insulinDialog;
    Dialog bglDialog;



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
        lineSeriesMin=new LineGraphSeries<>(new DataPoint[]{});
        lineSeriesMax=new LineGraphSeries<>(new DataPoint[]{});
        lineSeriesMin.setColor(Color.RED);
        lineSeriesMax.setColor(Color.RED);
        lineSeriesMin.appendData(new DataPoint(MinX,minGoal),true,4);
        lineSeriesMax.appendData(new DataPoint(MinX,maxGoal),true,4);


        lineSeriesMin.appendData(new DataPoint(MaxX,minGoal),true,4);
        lineSeriesMax.appendData(new DataPoint(MaxX,maxGoal),true,4);

        System.out.println(""+MaxX+MinX+minGoal+maxGoal);
        mScatterPlot.addSeries(lineSeriesMin);
        mScatterPlot.addSeries(lineSeriesMax);
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
        appMain=(BGLMain) this.getApplication();
        for(int i=0;i<appMain.insulinMasterList.size();i++)
            System.out.println("   safasgas222222222222222222222222222"+appMain.insulinMasterList.get(i));
        System.out.println("hello");
        float measurements=getIntent().getExtras().getFloat("measurements");
        System.out.println(measurements);

        //CustomRowData data =new CustomRowData();
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
        dateValInsulin.put(date,60);
        dateTypeInsulin.put(date,"type1");
        dateValBGL.put(date,60);

        calendar.set(2019, 01, 01);
        date=calendar.getTime();
        dateValInsulin.put(date,115);
        dateTypeInsulin.put(date,"type1");
        dateValBGL.put(date,115);

        calendar.set(2019, 01, 01);
        calendar.set(calendar.HOUR_OF_DAY,1);
        date=calendar.getTime();
        dateValInsulin.put(date,115);
        dateTypeInsulin.put(date,"type1");
        dateValBGL.put(date,115);

        calendar.set(2019, 04, 01);
        date=calendar.getTime();
        dateValInsulin.put(date,115);
        dateTypeInsulin.put(date,"type4");
        dateValBGL.put(date,115);

        calendar.set(2019, 10, 01);
        date=calendar.getTime();
        dateValInsulin.put(date,40);
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


    public void plottingInsulinBGLData()
    {

        int changerVal;

        int sumTotalInsulin=0;
        int counterTotalInsulin=0;
        int sumTotalBGL=0;
        int counterTotalBGL=0;
        int sum;
        int counter=0;
        maxPlottedY=0;
        int yVal;

        xySeriesInsulin=new PointsGraphSeries<>();
        xySeriesInsulin.setColor(Color.MAGENTA);
        lineSeriesInsulin=new LineGraphSeries<>(new DataPoint[]{});
        lineSeriesInsulin.setColor(Color.MAGENTA);

        xySeriesBGL=new PointsGraphSeries<>();
        xySeriesBGL.setColor(Color.RED);
        lineSeriesBGL=new LineGraphSeries<>(new DataPoint[]{});
        lineSeriesBGL.setColor(Color.RED);

        Date tempBeforeDate;
        Date tempAfterDate;
        Calendar calendar=Calendar.getInstance();
        calendar.set(calendar.HOUR_OF_DAY,0);
        calendar.set(calendar.MINUTE,0);
        calendar.set(calendar.SECOND,0);
        calendar.set(calendar.MILLISECOND,0);

        if(formatShown.equals("year")) {
            calendar.set(calendar.MONTH, 0);
            for (int k =10; k <= 20; k += 10)
            {
                calendar.set(calendar.MONTH,0);
                for (int j = 0; j < (12); j++) {
                    calendar.set(calendar.DATE, 1);
                    tempBeforeDate = calendar.getTime();
                    calendar.add(calendar.MONTH, 1);
                    tempAfterDate = calendar.getTime();
                    calendar.set(calendar.MONTH, j);
                    calendar.set(calendar.DATE, k);
                    System.out.println("safasgasgas"+calendar.getTime()+k);
                    sum = 0;
                    counter=0;
                    if(k==20) {
                        for (int i = 0; i < datesInsulin.size(); i++) {
                            if (datesInsulin.get(i).after(tempBeforeDate) &&
                                    datesInsulin.get(i).before(tempAfterDate)) {
                                counter++;
                                sum = sum + dateValInsulin.get(datesInsulin.get(i));
                            }
                        }
                    }else{
                        for (int i = 0; i < datesBGL.size(); i++) {

                            if (datesBGL.get(i).after(tempBeforeDate) &&
                                    datesBGL.get(i).before(tempAfterDate)) {
                                counter++;
                                sum = sum + dateValBGL.get(datesBGL.get(i));
                            }


                        }
                    }
                    if(k==20){
                        sumTotalInsulin = sumTotalInsulin + sum;
                        counterTotalInsulin = counterTotalInsulin + counter;
                    }else{
                        sumTotalBGL = sumTotalBGL + sum;
                        counterTotalBGL = counterTotalBGL + counter;
                    }

                    if (counter != 0) {
                        yVal=sum/counter;
                        if(yVal>maxPlottedY) maxPlottedY=yVal;
                    }else yVal=0;
                    if(k==20){
                        xySeriesInsulin.appendData(new DataPoint(calendar.getTime(), yVal), true, 12);
                        lineSeriesInsulin.appendData(new DataPoint(calendar.getTime(), yVal), true, 12);
                    } else {
                        xySeriesBGL.appendData(new DataPoint(calendar.getTime(), yVal), true, 12);
                        lineSeriesBGL.appendData(new DataPoint(calendar.getTime(), yVal), true, 12); }

                }

                if(k==20) {
                    dialogBoxInsulinListener();
                    mScatterPlot.addSeries(lineSeriesInsulin);
                    mScatterPlot.addSeries(xySeriesInsulin);
                    insulinAvg=sumTotalInsulin/counterTotalInsulin;

                }else{
                    dialogBoxBGLListener();
                    mScatterPlot.addSeries(lineSeriesBGL);
                    mScatterPlot.addSeries(xySeriesBGL);
                    bglAvg=sumTotalBGL/counterTotalBGL;
                }

            }


        }
        else if(formatShown.equals("month"))
        {
            calendar.add(calendar.DATE,-30);
            Date beginingD=calendar.getTime();
            System.out.println("ssgas"+calendar.getTime());
            for(int k =1; k <= 2; k += 1)
            {
                for(int j=0;j<31;j+=3)
                {

                }
            }
//            for (int k =1; k <= 2; k += 1)
//            {
//
//                for (int j = 0; j < (30); j+=3) {
//                    calendar.set(calendar.DATE, j*3+1);
//                    tempBeforeDate = calendar.getTime();
//                    calendar.add(calendar.DATE, 3);
//                    tempAfterDate = calendar.getTime();
//                    calendar.set(calendar.DATE, j*3+1);
//                    System.out.println("safasgasgas"+calendar.getTime()+k);
//                    sum = 0;
//                    counter=0;
//                    if(k==20) {
//                        for (int i = 0; i < datesInsulin.size(); i++) {
//                            if (datesInsulin.get(i).after(tempBeforeDate) &&
//                                    datesInsulin.get(i).before(tempAfterDate)) {
//                                counter++;
//                                sum = sum + dateValInsulin.get(datesInsulin.get(i));
//                            }
//                        }
//                    }else{
//                        for (int i = 0; i < datesBGL.size(); i++) {
//
//                            if (datesBGL.get(i).after(tempBeforeDate) &&
//                                    datesBGL.get(i).before(tempAfterDate)) {
//                                counter++;
//                                sum = sum + dateValBGL.get(datesBGL.get(i));
//                            }
//
//
//                        }
//                    }
//                    if(k==20){
//                        sumTotalInsulin = sumTotalInsulin + sum;
//                        counterTotalInsulin = counterTotalInsulin + counter;
//                    }else{
//                        sumTotalBGL = sumTotalBGL + sum;
//                        counterTotalBGL = counterTotalBGL + counter;
//                    }
//
//                    if (counter != 0) {
//                        yVal=sum/counter;
//                        if(yVal>maxPlottedY) maxPlottedY=yVal;
//                    }else yVal=0;
//                    if(k==20){
//                        xySeriesInsulin.appendData(new DataPoint(calendar.getTime(), yVal), true, 12);
//                        lineSeriesInsulin.appendData(new DataPoint(calendar.getTime(), yVal), true, 12);
//                    } else {
//                        xySeriesBGL.appendData(new DataPoint(calendar.getTime(), yVal), true, 12);
//                        lineSeriesBGL.appendData(new DataPoint(calendar.getTime(), yVal), true, 12); }
//
//                }
//
//                if(k==20) {
//                    dialogBoxInsulinListener();
//                    mScatterPlot.addSeries(lineSeriesInsulin);
//                    mScatterPlot.addSeries(xySeriesInsulin);
//                    if(counterTotalInsulin!=0)
//                        insulinAvg=sumTotalInsulin/counterTotalInsulin;
//                    else insulinAvg=0;
//
//                }else{
//                    dialogBoxBGLListener();
//                    mScatterPlot.addSeries(lineSeriesBGL);
//                    mScatterPlot.addSeries(xySeriesBGL);
//                    if(counterTotalBGL!=0)
//                        bglAvg=sumTotalBGL/counterTotalBGL;
//                    else bglAvg=0;
//                }
//
//            }

//            for(int j=0;j<(30/changerVal);j++) {
//                tempBeforeDate=calendar.getTime();
//                calendar.add(calendar.DATE,changerVal);
//                tempAfterDate=calendar.getTime();
//                for (int k=0;k<typesInsulin.size();k++) {
//                    sum=0;
//                    xySeriesInsulin=new PointsGraphSeries();
//                    xySeriesInsulin.setColor(colours[k]);
//                    for(int i = 0; i < datesInsulin.size(); i++)
//                    {
//                        if(dateTypeInsulin.get(datesInsulin.get(i)).equals(typesInsulin.get(i)) &&
//                                datesInsulin.get(i).after(tempBeforeDate) &&
//                                datesInsulin.get(i).before(tempAfterDate))
//                        {
//                            counter++;
//                            sum=sum+dateValInsulin.get(datesInsulin.get(i));
//                        }
//                    }
//                    calendar.add(calendar.DATE,-1*changerVal);
//                    calendar.add(calendar.DATE,changerVal/typesInsulin.size());
//                    if(counter!=0) {
//                        xySeriesInsulin.appendData(new DataPoint(calendar.getTime(), sum / counter), true, 180);
//                        lineSeriesInsulin.appendData(new DataPoint(calendar.getTime(), sum / counter), true, 180);
//                    }else
//                    {
//                        xySeriesInsulin.appendData(new DataPoint(calendar.getTime(), 0), true, 180);
//                        lineSeriesInsulin.appendData(new DataPoint(calendar.getTime(), 0), true, 180);
//                    }
//
//                    calendar.add(calendar.DATE,changerVal);
//                }
//                mScatterPlot.addSeries(xySeriesInsulin);
//                mScatterPlot.addSeries(lineSeriesInsulin);
//            }
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



    public void setBoundsX()
    {
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


        // set manual x bounds
        mScatterPlot.getViewport().setXAxisBoundsManual(true);
        //set manual y bounds
        mScatterPlot.getViewport().setYAxisBoundsManual(true);
        setBoundsX();

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

        mScatterPlot.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.MAGENTA);
        //mScatterPlot.getGridLabelRenderer().setHumanRounding(true);

        layout.addView(mScatterPlot,paramG);
        if(formatShown.equals("year") || formatShown.equals("month"))
            changeXLabels(1);
        else
            changeXLabels(0);

        //createScatterPlot();

    }
    public void dialogBoxInsulinListener() {
        xySeriesInsulin.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series xySeriesInsulin, DataPointInterface dataPoint) {
//                String dateFormatted = sdf[1].format((new Date((long) dataPoint.getX())).getTime());
                insulinDialog = new Dialog(BGLGraphActivity.this);
                insulinDialog.setContentView(R.layout.bglgraph_insulin_dialog);
                TextView prescrType = insulinDialog.findViewById(R.id.BGLGraph_prescrType_textView);
                //date and time
                TextView value = insulinDialog.findViewById(R.id.BGLGraph_insulin_textView);
                TextView notes = insulinDialog.findViewById(R.id.BGLGraph_note_inputField);

                //assign values to the above views
                insulinDialog.show();
            }
        });
    }
    public void dialogBoxBGLListener()
    {
        xySeriesBGL.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series xySeriesBGL, DataPointInterface dataPoint) {
//                String dateFormatted = sdf[1].format((new Date((long) dataPoint.getX())).getTime());
                bglDialog = new Dialog(BGLGraphActivity.this);
                bglDialog.setContentView(R.layout.bglgraph_bgl_dialog);
                TextView sugarConc = bglDialog.findViewById(R.id.BGLGraph_glucose_sugarCon_inputField);
                //date and time
                TextView notes = bglDialog.findViewById(R.id.BGLGraph_glucose_notes_inputField);

                //assign values to the above views
                bglDialog.show();
            }
        });
    }

    private void createScatterPlot()
    {
        int secAx;

//        lineSeriesInsulin= new LineGraphSeries<>(new DataPoint[]{});//as we always change the points on the graph
//        xySeriesInsulin=new PointsGraphSeries();


        updateGraph();//make the blank graph

        plottingInsulinBGLData();
        if(insulinAvg!=0)
            ratio=bglAvg/insulinAvg;
        else ratio=0;
        MaxY= maxPlottedY;
        mScatterPlot.getViewport().setMaxY(MaxY);
        mScatterPlot.getGridLabelRenderer().setHumanRounding(false);
        System.out.println(ratio);
        if(ratio!=0) secAx= (int) (MaxY/ratio);
        else secAx=0;
        mScatterPlot.setTitle("BGL graph"+" ratio:"+Math.round(ratio*10.0)/10.0);
        mScatterPlot.getSecondScale().setMaxY(secAx);


//        for(int i = 0; i < datesInsulin.size(); i++){
//            Date currDate=datesInsulin.get(i);
//            xySeriesInsulin.appendData(new DataPoint(currDate,dateValInsulin.get(currDate)),true,180);
//            lineSeriesInsulin.appendData(new DataPoint(currDate,dateValInsulin.get(currDate)),true,180);
//        }

//        mScatterPlot.addSeries(lineSeriesInsulin);
//        mScatterPlot.addSeries(xySeriesInsulin);

//        for(int i = 0; i < datesBGL.size(); i++){
//            Date currDate=datesBGL.get(i);
//            xySeriesBGL.appendData(new DataPoint(currDate,dateValBGL.get(currDate)),true,180);
//            lineSeriesBGL.appendData(new DataPoint(currDate,dateValBGL.get(currDate)),true,180);
//        }
//        lineSeriesBGL.setColor(Color.RED);
//        xySeriesBGL.setColor(Color.RED);
//        mScatterPlot.addSeries(lineSeriesBGL);
//        mScatterPlot.addSeries(xySeriesBGL);


        //SET listener for points



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
            lineSeriesMin=new LineGraphSeries<>(new DataPoint[]{});
            lineSeriesMax=new LineGraphSeries<>(new DataPoint[]{});
            lineSeriesMin.setColor(Color.RED);
            lineSeriesMax.setColor(Color.RED);
            //changeXLabels(0);
            calendar.set(calendar.MONTH,0);
            calendar.set(calendar.DATE,0);
            calendar.set(calendar.HOUR_OF_DAY,0);
            calendar.set(calendar.MINUTE,0);
            calendar.set(calendar.SECOND,0);
            MinX =  calendar.getTime();


            lineSeriesMin.appendData(new DataPoint(MinX,minGoal),true,4);
            lineSeriesMax.appendData(new DataPoint(MinX,maxGoal),true,4);

            calendar.add(calendar.YEAR,1);
            MaxX =  calendar.getTime();

            lineSeriesMin.appendData(new DataPoint(MaxX,minGoal),true,4);
            lineSeriesMax.appendData(new DataPoint(MaxX,maxGoal),true,4);

            System.out.println(""+MaxX+MinX+minGoal+maxGoal);
            mScatterPlot.addSeries(lineSeriesMin);
            mScatterPlot.addSeries(lineSeriesMax);
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