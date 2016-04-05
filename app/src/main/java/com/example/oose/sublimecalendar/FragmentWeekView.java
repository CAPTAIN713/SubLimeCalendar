package com.example.oose.sublimecalendar;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.view.LayoutInflater;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;



/**
 * Created by sarah on 4/2/16.
 */
public class FragmentWeekView extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener{

    private WeekView mWeekView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View weekView = inflater.inflate(R.layout.fragment_week_view, container, false);
        //weekView.setContentView(R.layout.fragment_week_view);

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) weekView.findViewById(R.id.weekViewFull);

        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);

        return weekView;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        /*this could be a problem. you need to return an array of WeekViewEvent, not null.
        null is bad and causes headaches.
        * https://github.com/alamkanak/Android-Week-View/blob/master/sample/src/main/java/com/alamkanak/weekview/sample/BasicActivity.java
        * link to WeekViewEvent class:
        * https://github.com/alamkanak/Android-Week-View/blob/master/sample/src/main/java/com/alamkanak/weekview/sample/apiclient/Event.java */
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        return events;
    }
}
