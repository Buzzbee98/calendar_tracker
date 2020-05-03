package buzzardsview.calendartracker;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analytics extends Queries {

    private BottomNavigationView bottomNavigationView;
    private PieChart pieChart;
    private BarChart barChart;
    private Button chooseCalendarsButton;
    private Button finishedBtn;
    private ToggleButton toggleChart;
    private EditText startDateEditText;
    private EditText endDateEditText;
    private Spinner spinner1;
    private Spinner spinner2;
    private ConstraintLayout constraintLayout;
    private LinearLayout linearLayout;

    private List<PieEntry> pieDataList;
    private List<BarEntry> barDataList;
    private ArrayList<String> selectedCals = new ArrayList<>();
    private ArrayList<String> barChartLabels;
    private ArrayList<Integer> colorArray;

    private int calKey;
    private long avg1;
    private long startDate;
    private long endDate;
    private int chartChoiceKey;

    public void openHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Analytics.this, new String[] {Manifest.permission.READ_CALENDAR}, 100);

        }

        //
        //makes bottom navigation functional
        //
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_timer:
                        openHome();
                        break;
                    case R.id.navigation_analytics:
                        break;
                }
                return true;
            }
        });

        chooseCalendarsButton = findViewById(R.id.choose_calendars);
        finishedBtn = findViewById(R.id.finished);
        toggleChart = findViewById(R.id.toggle_chart);
        startDateEditText = findViewById(R.id.start_date);
        endDateEditText = findViewById(R.id.end_date);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        constraintLayout = findViewById(R.id.constrain_layout);
        linearLayout = findViewById(R.id.linear_layout);

        //
        //sets defaults of variables
        //
        final ArrayList<String> allCals = queryCalendarNames();
        selectedCals.addAll(allCals);
        calKey = 1;
        avg1 = 1;
        chartChoiceKey = 1;

        //
        //sets defaults for editTexts dates
        //
        SimpleDateFormat Format = new SimpleDateFormat("MM/dd/yyy");
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        endDateEditText.setText(Format.format(cal.getTimeInMillis()));
        cal.add(Calendar.MONTH,  -1);
        startDateEditText.setText(Format.format(cal.getTimeInMillis()));
        startDate = convertDateToMilli(startDateEditText.getText().toString());
        endDate = convertDateToMilli(endDateEditText.getText().toString());

        //
        //contents of spinner1 and spinner2
        //
        final String[] analyticsOptions = {
                getResources().getString(R.string.total_time),
                getResources().getString(R.string.total_occurrences),
                getResources().getString(R.string.average_time),
                getResources().getString(R.string.average_occurrences)
        };
        final String[] averageOptions = {
                getResources().getString(R.string.per_day),
                getResources().getString(R.string.per_week),
                getResources().getString(R.string.per_month)
        };



        //
        //opens view with all calendars checkboxes
        //
        chooseCalendarsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.VISIBLE);
            }
        });
        //
        //closes and confirms checkbox selection
        //
        finishedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.INVISIBLE);
                calculatorCalculation();
            }
        });

        //
        //sets checkboxes and checkbox listen functionality
        //
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < allCals.size(); i++) {
            CheckBox ch = new CheckBox(this);
            ch.setText(allCals.get(i));
            ch.setLayoutParams(lparams);
            ch.setGravity(Gravity.CENTER);
            ch.setChecked(true);
            linearLayout.addView(ch);
            linearLayout.setBackgroundColor(Color.BLACK);
            final int index = i;

            final CompoundButton.OnCheckedChangeListener checkboxListener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        addCals(allCals.get(index));
                    } else {
                        selectedCals.remove(allCals.get(index));
                    }

                }
            };
            ch.setOnCheckedChangeListener(checkboxListener);
        }

        //
        //sets listener and functionality of toggle chart
        //
        toggleChart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chartChoiceKey = 2;
                    calculatorCalculation();
                } else {
                    chartChoiceKey = 1;
                    calculatorCalculation();
                }
            }
        });

        //
        //sets listener for edit text change
        //
        startDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                startDate = convertDateToMilli(startDateEditText.getText().toString());
                calculatorCalculation();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        endDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                endDate = convertDateToMilli(endDateEditText.getText().toString());
                calculatorCalculation();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        //
        //makes calendar picker appear when you click editText
        //
        MyEditTextDatePicker startDateCalPicker = new MyEditTextDatePicker(this, R.id.start_date);
        MyEditTextDatePicker endDateCalPicker = new MyEditTextDatePicker(this, R.id.end_date);

        //
        //sets content for first spinner
        //
        ArrayAdapter adapter1 = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                analyticsOptions);

        //
        //sets content of second spinner
        //
        ArrayAdapter adapter2 = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                averageOptions);
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);

        //
        //checks contents of of first spinner
        //
        OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        calKey = 1;
                        spinner2.setVisibility(View.INVISIBLE);
                        calculatorCalculation();
                        break;
                    case 1:
                        calKey = 2;
                        spinner2.setVisibility(View.INVISIBLE);
                        calculatorCalculation();
                        break;
                    case 2:
                        calKey = 3;
                        spinner2.setVisibility(View.VISIBLE);
                        calculatorCalculation();
                        break;
                    case 3:
                        calKey = 4;
                        spinner2.setVisibility(View.VISIBLE);
                        calculatorCalculation();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        //
        //checks contents of of second spinner
        //
        OnItemSelectedListener spinner2Listener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        avg1 = 1;
                        calculatorCalculation();
                        break;
                    case 1:
                        avg1 = 7;
                        calculatorCalculation();
                        break;
                    case 2:
                        avg1 = 30;
                        calculatorCalculation();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner2.setOnItemSelectedListener(spinner2Listener);
        spinner1.setOnItemSelectedListener(spinnerListener);

    }

    //
    //calculates and sets data depending on user selected options
    //
    public void calculatorCalculation() {
        switch (calKey) {
            case 1:
                HashMap<String, Double> map = getTotalTime(startDate, endDate);
                List<BarEntry> barData1 = new ArrayList<>();
                List<PieEntry> data1 = new ArrayList<>();
                float i1 = 0;
                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    for (String calendar : selectedCals) {
                        if (entry.getKey().equals(calendar)) {
                            float value = entry.getValue().floatValue();
                            switch(chartChoiceKey){
                                case 1:
                                    data1.add(new PieEntry(value, entry.getKey()));
                                    break;
                                case 2:
                                    barData1.add(new BarEntry(i1, value));
                                    i1++;
                                    break;
                            }
                        }
                    }
                }
                switch (chartChoiceKey) {
                    case 1:
                        pieDataSetter(data1);
                        break;
                    case 2:
                        barDataSetter(barData1);
                        break;
                }
                break;
            case 2:
                HashMap<String, Double> map2 = getOccurrences(startDate, endDate);
                List<BarEntry> barData2 = new ArrayList<>();
                List<PieEntry> data2 = new ArrayList<>();
                float i2 = 0;
                for (Map.Entry<String, Double> entry : map2.entrySet()) {
                    for (String calendar : selectedCals) {
                        if (entry.getKey().equals(calendar)) {
                            float value = entry.getValue().floatValue();
                            switch(chartChoiceKey){
                                case 1:
                                    data2.add(new PieEntry(value, entry.getKey()));
                                    break;
                                case 2:
                                    barData2.add(new BarEntry(i2, value));
                                    i2++;
                                    break;
                            }
                        }
                    }
                }
                switch (chartChoiceKey) {
                    case 1:
                        pieDataSetter(data2);
                        break;
                    case 2:
                        barDataSetter(barData2);
                        break;
                }
                break;
            case 3:
                HashMap<String, Double> map3 = getTimeAverage(startDate, endDate, avg1);
                List<BarEntry> barData3 = new ArrayList<>();
                List<PieEntry> data3 = new ArrayList<>();
                float i3 = 0;
                for (Map.Entry<String, Double> entry : map3.entrySet()) {
                    for (String calendar : selectedCals) {
                        if (entry.getKey().equals(calendar)) {
                            float value = entry.getValue().floatValue();
                            switch(chartChoiceKey){
                                case 1:
                                    data3.add(new PieEntry(value, entry.getKey()));
                                    break;
                                case 2:
                                    barData3.add(new BarEntry(i3, value));
                                    i3++;
                                    break;
                            }
                        }
                    }
                }
                switch (chartChoiceKey) {
                    case 1:
                        pieDataSetter(data3);
                        break;
                    case 2:
                        barDataSetter(barData3);
                        break;
                }
                break;
            case 4:
                HashMap<String, Double> map4 = getOccurrenceAverage(startDate, endDate, avg1);
                List<PieEntry> data4 = new ArrayList<>();
                List<BarEntry> barData4 = new ArrayList<>();
                float i4 = 0;
                for (Map.Entry<String, Double> entry : map4.entrySet()) {
                    for (String calendar : selectedCals) {
                        if (entry.getKey().equals(calendar)) {
                            float value = entry.getValue().floatValue();
                            switch(chartChoiceKey){
                                case 1:
                                    data4.add(new PieEntry(value, entry.getKey()));
                                    break;
                                case 2:
                                    barData4.add(new BarEntry(i4, value));
                                    i4++;
                                    break;
                            }
                        }
                    }
                }
                switch (chartChoiceKey) {
                    case 1:
                        pieDataSetter(data4);
                        break;
                    case 2:
                        barDataSetter(barData4);
                        break;
                }
                break;
        }
        HashMap<String, Integer> color = getColorArray(startDate, endDate);
        ArrayList<Integer> colorArray1 = new ArrayList<>();
        ArrayList<String> barChartLabels1 = new ArrayList<>();
        for(Map.Entry<String, Integer> entry: color.entrySet()) {
            for(String calendar: selectedCals) {
                if (entry.getKey().equals(calendar)) {
                    colorArray1.add(entry.getValue());
                    barChartLabels1.add(entry.getKey());
                }
            }
        }
        setBarChartLabels(barChartLabels1);
        setColorArray(colorArray1);
        switch (chartChoiceKey) {
            case 1:
                drawPie();
                break;
            case 2:
                drawBar();
                break;
        }
    }

    public void drawPie(){
        pieChart = findViewById(R.id.chart);
        barChart = findViewById(R.id.bar_chart);
        pieChart.setVisibility(View.VISIBLE);
        barChart.setVisibility(View.INVISIBLE);
        PieDataSet set = new PieDataSet(pieDataList, "ANALYTICS");
        PieData data = new PieData(set);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setData(data);
        set.setColors(colorArray);
        set.setValueTextColor(Color.parseColor("#000000"));
        pieChart.invalidate();
    }

    public void drawBar(){
        pieChart.setVisibility(View.INVISIBLE);
        barChart = findViewById(R.id.bar_chart);
        barChart.setVisibility(View.VISIBLE);
        BarDataSet barSet = new BarDataSet(barDataList, "ANALYTICS");
        BarData barData = new BarData(barSet);
        barChart.setData(barData);
        Description description = new Description();
        description.setText("");
        barChart.setDrawValueAboveBar(false);
        barChart.setFitBars(true);
        barSet.setColors(colorArray);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(barChartLabels));
        barChart.invalidate();
    }

    //
    //add selected calendars to selectedCals list
    //
    public void addCals(String cal) {
        selectedCals.add(cal);
    }

    //
    //used to set pie chart data
    //
    public void pieDataSetter(List<PieEntry> data) {
        pieDataList = data;
    }

    //
    //used to set bar graph data
    //
    public void barDataSetter(List<BarEntry> data) {
        barDataList = data;
    }

    //
    //set color Array to coresponding selected calendars
    //
    public void setColorArray(ArrayList<Integer> color){
        colorArray = color;
    }

    //
    //set correct labels for bar chart and corresponding calendars
    //
    public void setBarChartLabels(ArrayList<String> labels) {
        barChartLabels = labels;
    }
}
