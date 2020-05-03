package buzzardsview.calendartracker;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Calendars;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Long.parseLong;

public class Queries extends AppCompatActivity {

    //
    //queries visible calendars on a device and returns an array of calendar names
    //
    public ArrayList<String> queryCalendarNames() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Queries.this, new String[] {Manifest.permission.READ_CALENDAR}, 100);
        }
        ArrayList<String> calNames = new ArrayList<>();
        final String[] projection = new String[]{Calendars.CALENDAR_DISPLAY_NAME};
        Cursor c;
        ContentResolver cr = getContentResolver();
        Uri uri = Calendars.CONTENT_URI;
        c = cr.query(uri, projection, Calendars.VISIBLE + "=1", null, null);
        while (c.moveToNext()) {
            String calName = c.getString(0);
            calNames.add(calName);
        }
        c.close();
        return calNames;
    }
    //
    //queries visible calendars on a device and returns an array of calendar ID's
    //
    public ArrayList<Long> queryCalendarID() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Queries.this, new String[] {Manifest.permission.READ_CALENDAR}, 100);
        }
        ArrayList<Long> calNames = new ArrayList<>();
        final String[] projection = new String[]{Calendars._ID};
        Cursor c;
        ContentResolver cr = getContentResolver();
        Uri uri = Calendars.CONTENT_URI;
        c = cr.query(uri, projection, Calendars.VISIBLE + "=1", null, null);
        while (c.moveToNext()) {
            long calID = c.getLong(0);
            calNames.add(calID);
        }
        c.close();
        return calNames;
    }
    //
    //queries visible calendars on a device and returns an array of calendar color integers
    //
    public ArrayList<Integer> queryCalendarColors() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Queries.this, new String[] {Manifest.permission.READ_CALENDAR}, 100);
        }
        ArrayList<Integer> calNames = new ArrayList<>();
        final String[] projection = new String[]{Calendars.CALENDAR_COLOR};
        Cursor c;
        ContentResolver cr = getContentResolver();
        Uri uri = Calendars.CONTENT_URI;
        c = cr.query(uri, projection, Calendars.VISIBLE + "=1", null, null);
        while (c.moveToNext()) {
            int calColor = c.getInt(0);
            calNames.add(calColor);
        }
        c.close();
        return calNames;
    }
    //
    //queries all events
    //
    public Cursor queryEvents() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Queries.this, new String[] {Manifest.permission.READ_CALENDAR}, 100);
        }
        final String[] projection = new String[]{
                Events.CALENDAR_DISPLAY_NAME,
                Events.DTSTART,
                Events.DTEND,
                Events.CALENDAR_COLOR
        };
        Cursor cur;
        ContentResolver cr = getContentResolver();
        Uri uri = Events.CONTENT_URI;
        cur = cr.query(uri, projection, null, null, null);
        while (cur.moveToNext()) {
            String eventTitle = cur.getString(0);
            String eventStart = cur.getString(1);
            String eventEnd = cur.getString(2);
            String calColor = cur.getString(3);
        }
        return cur;
    }

    //
    //Increases value in HashMap
    //
    public static<K> void incrementValue(Map<K,Double> map, K key, double durration) {
        Double count = map.get(key);
        if (count == null) {
            map.put(key, durration);
        }
        else {
            map.put(key, count + durration);
        }
    }
    //
    //converts dates to milliseconds
    //
    public long convertDateToMilli(String date) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Date formattedDate = null;
        try {
            formattedDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long miilis = formattedDate.getTime();
        return miilis;
    }

    //
    //gets array of colors for charts
    //
    public HashMap<String, Integer> getColorArray(long startMili, long endMili) {
        Cursor c = queryEvents();
        HashMap<String, Integer> map
                = new HashMap<>();
        c.moveToFirst();
        while (c.moveToNext()) {
            if(c.getString(1) != null && c.getString(2) != null) {
                long startTime = parseLong(c.getString(1));
                long endTime = parseLong(c.getString(2));
                if (startTime >= startMili && endTime <= endMili) {
                    String name = c.getString(0);
                    if (map.containsKey(name)) {
                    } else {
                        map.put(name, Integer.parseInt(c.getString(3)));
                    }
                }
            }
        }
        c.close();
        return map;
    }
    //
    //gets total occurrences of each calendar
    //
    public HashMap<String, Double> getOccurrences(long startMili, long endMili) {
        Cursor c = queryEvents();
        HashMap<String, Double> map
                = new HashMap<>();
        c.moveToFirst();
        while (c.moveToNext()) {
            if(c.getString(1) != null && c.getString(2) != null) {
                long startTime = parseLong(c.getString(1));
                long endTime = parseLong(c.getString(2));
                if (startTime >= startMili && endTime <= endMili) {
                    String name = c.getString(0);
                    if (map.containsKey(name)) {
                        incrementValue(map, name, 1);
                    } else {
                        map.put(name, 1.0);
                    }
                }
            }
        }
        c.close();
        return map;
    }

    //
    //gets average occurrences of each calendar
    //
    public HashMap<String, Double> getOccurrenceAverage(long startMili, long endMili, long avg) {
        Cursor c = queryEvents();
        HashMap<String, Double> map
                = new HashMap<>();
        c.moveToFirst();
        long chosenDuration = endMili - startMili;
        double numDays = chosenDuration / 86400000;
        double average = numDays / avg;
        while (c.moveToNext()) {
            if(c.getString(1) != null && c.getString(2) != null) {
                long startTime = parseLong(c.getString(1));
                long endTime = parseLong(c.getString(2));
                if (startTime >= startMili && endTime <= endMili) {
                    String name = c.getString(0);
                    if (map.containsKey(name)) {
                        incrementValue(map, name, 1);
                    } else {
                        map.put(name, 1.0);
                    }
                }
            }
        }
        for (Map.Entry<String, Double> entry: map.entrySet()){
            map.put(entry.getKey(), entry.getValue()/average);
        }
        c.close();
        return map;
    }

    //
    //gets average time spent of each calendar
    //
    public HashMap<String, Double> getTimeAverage(long startMili, long endMili, long avg) {
        Cursor c = queryEvents();
        HashMap<String, Double> map
                = new HashMap<>();
        DecimalFormat df2 = new DecimalFormat("0.00");
        c.moveToFirst();
        long chosenDuration = endMili - startMili;
        double numDays = chosenDuration / 86400000;
        double average = numDays / avg;
        while (c.moveToNext()) {
            if(c.getString(1) != null && c.getString(2) != null) {
                long startTime = parseLong(c.getString(1));
                long endTime = parseLong(c.getString(2));
                double duration = (endTime - startTime);
                double durationHours = Double.parseDouble(df2.format(duration / 3600000));
                if (startTime >= startMili && endTime <= endMili) {
                    String name = c.getString(0);
                    if (map.containsKey(name)) {
                        incrementValue(map, name, durationHours);
                    } else {
                        map.put(name, durationHours);
                    }
                }
            }
        }
        for (Map.Entry<String, Double> entry: map.entrySet()){
            map.put(entry.getKey(), entry.getValue()/average);
        }
        c.close();
        return map;
    }

    //
    //gets total time spent of each calendar
    //
    public HashMap<String, Double> getTotalTime(long startMili, long endMili) {
        Cursor c = queryEvents();
        DecimalFormat df2 = new DecimalFormat("0.00");
        HashMap<String, Double> map
                = new HashMap<>();
        c.moveToFirst();
        while (c.moveToNext()) {
            if(c.getString(1) != null && c.getString(2) != null) {
                long startTime = parseLong(c.getString(1));
                long endTime = parseLong(c.getString(2));
                double duration = (endTime - startTime);
                double durationHours = Double.parseDouble(df2.format(duration / 3600000));
                if (startTime >= startMili && endTime <= endMili) {
                    String name = c.getString(0);
                    if (map.containsKey(name)) {
                        incrementValue(map, name, durationHours);
                    } else {
                        map.put(name, durationHours);
                    }
                }
            }
        }

        c.close();
        return map;
    }
}
