package buzzardsview.calendartracker;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<CalendarObject> {
    private Context context;
    private ArrayList<CalendarObject> calendars;

    public SpinnerAdapter(Context context, int textViewResourceId, ArrayList<CalendarObject> calendars) {
        super(context, textViewResourceId, calendars);
        this.context = context;
        this.calendars = calendars;
    }

    @Override
    public int getCount(){
        return calendars.size();
    }

    @Override
    public CalendarObject getItem(int position){
        return calendars.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(calendars.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(calendars.get(position).getName());

        return label;
    }
}
