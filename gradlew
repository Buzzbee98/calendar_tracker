package com.example.apitest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analytics extends Details {
    private EditText startDateEditText;
    private EditText endDateEditText;
    private Spinner spinner1;
    private Spinner spinner2;
    private Button chooseCalendarsButton;
    private LinearLayout linearLayout;
    private Button finishedBtn;
    private ConstraintLayout constraintLayout;

    public int selection;

    public List<DataEntry> dataList;
    //private ArrayList<String> allCals = allCalendars();

    public ArrayList<String> selectedCals = new ArrayList<>();
    public int calKey;
    public long avg1;
    public long startDate;
    public long endDate;



    public void addCals(String cal) {
        selectedCals.add(cal);
    }

    public void dataSetter(List<DataEntry> data) {
        dataList = data;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        SimpleDateFormat Format = new SimpleDateFormat("dd/MM/yyy");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Analytics.this, new String[] {Manifest.permission.READ_CALENDAR}, 100);

        }
        final ArrayList<String> allCals = allCalendars();
        startDateEditText = findViewById(R.id.start_date);
        endDateEditText = findViewById(R.id.end_date);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        chooseCalendarsButton = findViewById(R.id.choose_calendars);
        linearLayout = findViewById(R.id.linear_layout);
        finishedBtn = findViewById(R.id.finished);
        constraintLayout = findViewById(R.id.constrain_layout);


        selectedCals.addAll(allCals);
        calKey = 2;
        avg1 = 86400000;
        //
        //contents of spinner1 and spinner2
        //
        final String[] analyticsOptions = {"Total Time", "Total Occurrences", "Average Time", "Average Occurrences"};
        final String[] averageOptions = {"Per Day", "Per Week", "Per Month"};
        //
        //sets defaults for editTexts dates
        //
        final Calendar cal = Calendar.getInstance();
        endDateEditText.setText(Format.format(cal.getTimeInMillis()));
        cal.add(Calendar.MONTH,  -1);
        startDateEditText.setText(Format.format(cal.getTimeInMillis()));
        startDate = convertDateToMilli(startDateEditText.getText().toString());
        endDate = convertDateToMilli(endDateEditText.getText().toString());

        startDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                startDate = convertDateToMilli(startDateEditText.getText().toString());
                Log.d("MATH", "" + "" + startDate);
                calculatorCalculation();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        endDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                endDate = convertDateToMilli(endDateEditText.getText().toString());
                Log.d("MATH", "" + "" + startDate);
                calculatorCalculation();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        //Log.d("Math", )
        //HashMap<String, Double> map = getOccurrences(convertDateToMilli(startDate), convertDateToMilli(endDate));
