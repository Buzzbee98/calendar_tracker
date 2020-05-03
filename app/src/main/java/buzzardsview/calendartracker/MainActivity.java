package buzzardsview.calendartracker;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.provider.CalendarContract.Events;
import android.widget.Spinner;
import android.widget.ToggleButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Queries {

    private ArrayList<String> calNames;
    private long[] startTimes = new long[8];
    private ToggleButton[] toggleArray;
    private Spinner[] spinnerArray;
    private Chronometer[] chronometerArray;
    private ConstraintLayout[] backgroundArray;
    private ConstraintLayout[] calendarContainerArray;


    private ToggleButton cal1ToggleBtn;
    private Spinner cal1Spinner;
    private Chronometer cal1Chronometer;
    private ConstraintLayout cal1Background;
    private ConstraintLayout cal1Container;

    private ToggleButton cal2ToggleBtn;
    private Spinner cal2Spinner;
    private Chronometer cal2Chronometer;
    private ConstraintLayout cal2Background;
    private ConstraintLayout cal2Container;

    private ToggleButton cal3ToggleBtn;
    private Spinner cal3Spinner;
    private Chronometer cal3Chronometer;
    private ConstraintLayout cal3Background;
    private ConstraintLayout cal3Container;

    private ToggleButton cal4ToggleBtn;
    private Spinner cal4Spinner;
    private Chronometer cal4Chronometer;
    private ConstraintLayout cal4Background;
    private ConstraintLayout cal4Container;

    private ToggleButton cal5ToggleBtn;
    private Spinner cal5Spinner;
    private Chronometer cal5Chronometer;
    private ConstraintLayout cal5Background;
    private ConstraintLayout cal5Container;

    private ToggleButton cal6ToggleBtn;
    private Spinner cal6Spinner;
    private Chronometer cal6Chronometer;
    private ConstraintLayout cal6Background;
    private ConstraintLayout cal6Container;

    private ToggleButton cal7ToggleBtn;
    private Spinner cal7Spinner;
    private Chronometer cal7Chronometer;
    private ConstraintLayout cal7Background;
    private ConstraintLayout cal7Container;

    private ToggleButton cal8ToggleBtn;
    private Spinner cal8Spinner;
    private Chronometer cal8Chronometer;
    private ConstraintLayout cal8Background;
    private ConstraintLayout cal8Container;

    //used to tell spinner in what states i'm specifying. in this case all true
    int[][] states = new int[][] {
            new int[] { android.R.attr.state_enabled}, // enabled
            new int[] { android.R.attr.state_enabled}, // disabled
            new int[] { android.R.attr.state_checked}, // unchecked
            new int[] { android.R.attr.state_pressed}  // pressed
    };

    private SpinnerAdapter spinnerAdapter;
    private OnItemSelectedListener onItemSelectedListener;
    private OnCheckedChangeListener onCheckedChangeListener;
    private ObjectAnimator animation;
    private BottomNavigationView bottomNavigationView;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String[] spinnerPositionPrefs = {
            "spinner1",
            "spinner2",
            "spinner3",
            "spinner4",
            "spinner5",
            "spinner6",
            "spinner7",
            "spinner8",
    };
    public static final String[] togglePositionPrefs = {
            "toggleBtn1",
            "toggleBtn2",
            "toggleBtn3",
            "toggleBtn4",
            "toggleBtn5",
            "toggleBtn6",
            "toggleBtn7",
            "toggleBtn8",
    };
    public static final String[] startTimePrefs = {
            "startTime1",
            "startTime2",
            "startTime3",
            "startTime4",
            "startTime5",
            "startTime6",
            "startTime7",
            "startTime8",
    };

    public void openAnalytics() {
        Intent intent = new Intent(this, Analytics.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("test123", "test");

        //
        //checks for read permissions
        //
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CALENDAR}, 100);
            return;
        } else {
            activity();
        }
    }

    public void activity() {

        //
        //makes bottom navigation functional
        //
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_timer:
                        break;
                    case R.id.navigation_analytics:
                        openAnalytics();
                        break;
                }
                return true;
            }
        });

        cal1ToggleBtn = findViewById(R.id.calendar1_toggle_btn);
        cal1Spinner = findViewById(R.id.calendar1_spinner);
        cal1Chronometer = findViewById(R.id.chronometer1);
        cal1Background = findViewById(R.id.calendar1_background);
        cal1Container = findViewById(R.id.calendar1_constraint_layout);

        cal2ToggleBtn = findViewById(R.id.calendar2_toggle_btn);
        cal2Spinner = findViewById(R.id.calendar2_spinner);
        cal2Chronometer = findViewById(R.id.chronometer2);
        cal2Background = findViewById(R.id.calendar2_background);
        cal2Container = findViewById(R.id.calendar2_constraint_layout);

        cal3ToggleBtn = findViewById(R.id.calendar3_toggle_btn);
        cal3Spinner = findViewById(R.id.calendar3_spinner);
        cal3Chronometer = findViewById(R.id.chronometer3);
        cal3Background = findViewById(R.id.calendar3_background);
        cal3Container = findViewById(R.id.calendar3_constraint_layout);

        cal4ToggleBtn = findViewById(R.id.calendar4_toggle_btn);
        cal4Spinner = findViewById(R.id.calendar4_spinner);
        cal4Chronometer = findViewById(R.id.chronometer4);
        cal4Background = findViewById(R.id.calendar4_background);
        cal4Container = findViewById(R.id.calendar4_constraint_layout);

        cal5ToggleBtn = findViewById(R.id.calendar5_toggle_btn);
        cal5Spinner = findViewById(R.id.calendar5_spinner);
        cal5Chronometer = findViewById(R.id.chronometer5);
        cal5Background = findViewById(R.id.calendar5_background);
        cal5Container = findViewById(R.id.calendar5_constraint_layout);

        cal6ToggleBtn = findViewById(R.id.calendar6_toggle_btn);
        cal6Spinner = findViewById(R.id.calendar6_spinner);
        cal6Chronometer = findViewById(R.id.chronometer6);
        cal6Background = findViewById(R.id.calendar6_background);
        cal6Container = findViewById(R.id.calendar6_constraint_layout);

        cal7ToggleBtn = findViewById(R.id.calendar7_toggle_btn);
        cal7Spinner = findViewById(R.id.calendar7_spinner);
        cal7Chronometer = findViewById(R.id.chronometer7);
        cal7Background = findViewById(R.id.calendar7_background);
        cal7Container = findViewById(R.id.calendar7_constraint_layout);

        cal8ToggleBtn = findViewById(R.id.calendar8_toggle_btn);
        cal8Spinner = findViewById(R.id.calendar8_spinner);
        cal8Chronometer = findViewById(R.id.chronometer8);
        cal8Background = findViewById(R.id.calendar8_background);
        cal8Container = findViewById(R.id.calendar8_constraint_layout);

        toggleArray = new ToggleButton[]{
                cal1ToggleBtn,
                cal2ToggleBtn,
                cal3ToggleBtn,
                cal4ToggleBtn,
                cal5ToggleBtn,
                cal6ToggleBtn,
                cal7ToggleBtn,
                cal8ToggleBtn
        };

        spinnerArray = new Spinner[]{
                cal1Spinner,
                cal2Spinner,
                cal3Spinner,
                cal4Spinner,
                cal5Spinner,
                cal6Spinner,
                cal7Spinner,
                cal8Spinner
        };

        chronometerArray = new Chronometer[]{
                cal1Chronometer,
                cal2Chronometer,
                cal3Chronometer,
                cal4Chronometer,
                cal5Chronometer,
                cal6Chronometer,
                cal7Chronometer,
                cal8Chronometer
        };

        backgroundArray = new ConstraintLayout[]{
                cal1Background,
                cal2Background,
                cal3Background,
                cal4Background,
                cal5Background,
                cal6Background,
                cal7Background,
                cal8Background
        };

        calendarContainerArray = new ConstraintLayout[]{
                cal1Container,
                cal2Container,
                cal3Container,
                cal4Container,
                cal5Container,
                cal6Container,
                cal7Container,
                cal8Container
        };

        loadData();


        //
        //initializes each calendar as a calendar object
        //
        final ArrayList<CalendarObject> calArray = new ArrayList<>();
        calNames = queryCalendarNames();
        ArrayList<Long> calID = queryCalendarID();
        ArrayList<Integer> calColors = queryCalendarColors();
        for (int i = 0; i < calNames.size(); i++) {
            CalendarObject calendarObject = new CalendarObject(calNames.get(i), calID.get(i), calColors.get(i));
            calArray.add(calendarObject);
        }

        setCalContainers();
        chronometerSetup();
        setAnimation();

        //
        //sets spinner dropdown list to have the names of each calendar
        //
        spinnerAdapter = new SpinnerAdapter(this, R.layout.spinner_item, calArray);
        setSpinnerAdapters();

        //
        //set spinner listener. handles changing colors
        //
        onItemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int spinnerIndex = 0;
                for(Spinner item: spinnerArray) {
                    if (parent.equals(item)) {
                        spinnerIndex = Arrays.asList(spinnerArray).indexOf(item);
                    }
                }
                CalendarObject calendarObject = spinnerAdapter.getItem(position);
                int colorCode = (0xff000000 + calendarObject.getColor());
                ColorStateList myList = new ColorStateList(states, new int[] {colorCode});
                backgroundArray[spinnerIndex].setBackgroundTintList(myList);
                spinnerArray[spinnerIndex].setBackgroundTintList(myList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        setSpinnerListener();
        loadData();

        //
        //handles events for toggle button change
        //
        onCheckedChangeListener = new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int toggleIndex = 0;
                for (ToggleButton btn: toggleArray) {
                    if(buttonView.equals(btn)) {
                        toggleIndex = Arrays.asList(toggleArray).indexOf(btn);
                    }
                }
                if (isChecked) {
                    chronometerArray[toggleIndex].setBase(SystemClock.elapsedRealtime());
                    chronometerArray[toggleIndex].start();
                    setTime(toggleIndex);
                    backgroundArray[toggleIndex].setVisibility(View.VISIBLE);
                    animation = ObjectAnimator.ofFloat(backgroundArray[toggleIndex], "translationX", 0);
                    animation.setDuration(500);
                    animation.start();
                } else {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_CALENDAR}, 200 );
                        return;
                    }
                    CalendarObject calendarObject = spinnerAdapter.getItem(spinnerArray[toggleIndex].getSelectedItemPosition());
                    chronometerArray[toggleIndex].stop();
                    chronometerArray[toggleIndex].setBase(SystemClock.elapsedRealtime());
                    ContentResolver cr = getContentResolver();
                    ContentValues values = new ContentValues();
                    values.put(Events.DTSTART, startTimes[toggleIndex]);
                    values.put(Events.DTEND, Calendar.getInstance().getTimeInMillis());
                    values.put(Events.TITLE, calendarObject.getName());
                    values.put(Events.CALENDAR_ID, calendarObject.getId());
                    values.put(Events.EVENT_TIMEZONE, "America/New_York");
                    cr.insert(Events.CONTENT_URI, values);
                    animation = ObjectAnimator.ofFloat(backgroundArray[toggleIndex], "translationX", 1000);
                    animation.setDuration(500);
                    animation.start();
                    final int index = toggleIndex;
                    backgroundArray[toggleIndex].postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            backgroundArray[index].setVisibility(View.INVISIBLE);
                        }
                    }, 500);
                }
            }
        };
        setToggleListener();
        for (ToggleButton btn: toggleArray) {
            onCheckedChanged(btn, btn.isChecked());
        }
    }

    public void onStop(){
        super.onStop();
        saveData();
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < spinnerPositionPrefs.length; i++) {
            editor.putInt(spinnerPositionPrefs[i], spinnerArray[i].getSelectedItemPosition());
        }
        for (int i = 0; i < togglePositionPrefs.length; i++) {
            editor.putBoolean(togglePositionPrefs[i], toggleArray[i].isChecked());
        }
        for (int i = 0; i < startTimePrefs.length; i++) {
            editor.putLong(startTimePrefs[i], startTimes[i]);
        }
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        for (int i = 0; i < spinnerArray.length; i++) {
            int index = i;
            spinnerArray[i].setSelection(sharedPreferences.getInt(spinnerPositionPrefs[i], index));
        }
        for (int i = 0; i < toggleArray.length; i++) {
            toggleArray[i].setChecked(sharedPreferences.getBoolean(togglePositionPrefs[i], false));
        }
        for (int i = 0; i < startTimes.length; i++) {
            startTimes[i] = sharedPreferences.getLong(startTimePrefs[i], System.currentTimeMillis());
        }
    }

    //
    //makes unused calendar containers invisible
    //
    public void setCalContainers() {
        if (calNames.size() < calendarContainerArray.length) {
            for (int i = 0; i < calendarContainerArray.length; i++) {
                if (i >= calNames.size()) {
                    calendarContainerArray[i].setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    //
    //sets each spinner to the adapter and sets it's position
    //
    public void setSpinnerAdapters() {
        for (int i = 0; i < spinnerAdapter.getCount(); i++) {
            if (i >= spinnerArray.length) {} else {
                spinnerArray[i].setAdapter(spinnerAdapter);
            }
        }
    }

    //
    //set each spinner listener
    //
    public void setSpinnerListener() {
        for (int i = 0; i < spinnerArray.length; i++) {
            spinnerArray[i].setOnItemSelectedListener(onItemSelectedListener);
        }
    }

    //
    //set toggle listener
    //
    public void setToggleListener() {
        for (ToggleButton toggleButton: toggleArray) {
            toggleButton.setOnCheckedChangeListener(onCheckedChangeListener);
        }
    }

    //
    //set chronometer settings
    //
    public void chronometerSetup() {
        for (Chronometer chronometer: chronometerArray) {
            chronometer.setFormat("00:%s");
        }
    }

    //
    //set time
    //
    public void setTime(int index){
        long time = Calendar.getInstance().getTimeInMillis();
        startTimes[index] = time;
    }

    //
    //sets start position of backgrounds
    //
    public void setAnimation() {
        for (ConstraintLayout layout: backgroundArray) {
            layout.setVisibility(View.INVISIBLE);
            animation = ObjectAnimator.ofFloat(layout, "translationX", 1000);
            animation.setDuration(50);
            animation.start();
        }
    }

    //
    //used to recover state of chronometer
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int toggleIndex = 0;
        for (ToggleButton btn: toggleArray) {
            if(buttonView.equals(btn)) {
                toggleIndex = Arrays.asList(toggleArray).indexOf(btn);
            }
        }
        if (isChecked) {
            long time = System.currentTimeMillis() - startTimes[toggleIndex];
            Long timeDifference = SystemClock.elapsedRealtime() - time;
            chronometerArray[toggleIndex].setBase(timeDifference);
            chronometerArray[toggleIndex].start();
            backgroundArray[toggleIndex].setVisibility(View.VISIBLE);
            animation = ObjectAnimator.ofFloat(backgroundArray[toggleIndex], "translationX", 0);
            animation.setDuration(500);
            animation.start();
        } else {}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    activity();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
