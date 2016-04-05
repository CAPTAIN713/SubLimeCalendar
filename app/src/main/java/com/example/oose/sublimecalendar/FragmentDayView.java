package com.example.oose.sublimecalendar;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekView.EventLongPressListener;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by kristina on 3/30/2016.
 */
public class FragmentDayView extends Fragment implements WeekView.EventClickListener, WeekView.EventLongPressListener, MonthLoader.MonthChangeListener,
        WeekView.EmptyViewLongPressListener{


    // Get a reference for the week view in the layout.
    private WeekView mWeekView;
    //private WeekView.EventClickListener mEventClickListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_view, container, false);
        Date dateSelected;
        Calendar day = Calendar.getInstance();
        String date = getArguments().getString("selectedDate");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
            dateSelected = df.parse(date);
            day.setTime(dateSelected);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mWeekView = (WeekView) view.findViewById(R.id.weekView);

        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setEventLongPressListener(this);
        mWeekView.goToDate(day);

        return view;
    }
    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }


    @Override
    public List<WeekViewEvent> onMonthChange(int i, int i2) {
        List<WeekViewEvent> events = new ArrayList<>(1);
        return events;

    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {

    }

}