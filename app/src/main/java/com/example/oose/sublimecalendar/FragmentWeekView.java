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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;



/**
 * Created by sarah on 4/2/16.
 */
public class FragmentWeekView extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener{

    private WeekView mWeekView;
    private Long cDate;

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

        Date dateSelected;
        Calendar day= Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = df.format(day.getTime());

        // Now formattedDate have current date/time
        try{
            dateSelected=df.parse(formattedDate);
            day.setTime(dateSelected);
            cDate=dateSelected.getTime();
        }
        catch(ParseException e){
            e.printStackTrace();
        }

        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.goToDate(day);
        return weekView;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {


    }

    @Override
    public List<WeekViewEvent> onMonthChange(int i, int i2) {
        /*i==newYear, i2=newMonth*/
        List<WeekViewEvent> wEvents = new ArrayList<>(1);

        //link on using sugar query: https://github.com/satyan/sugar/issues/376
        //Long l=(cDate-31556926);
        //Long ll=(cDate+31556926);
        //List<Event> events = Event.find(Event.class,"date >= ? AND date < ?",l+"",ll+"");
        //List<Event> events = Event.find(Event.class,"date = ?",cDate+"");
        List<Event> events = Event.listAll(Event.class); //grab all events from data base

        for(Event e : events){ /*for each event*/
            //set up start, end, and date calendar objects
            Calendar startTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();

            //get the date the event is taking place on
            Date d= new Date(e.date);

            /*if the Calendar month we are on is not equal to the current month break out of this loop
            the reason behind this is that it would display the same event 3 times because it would call
            the function 3 times. once for past, current and next month. this is a way around it to only
            display the event once for every day*/
            if(cal.get(Calendar.MONTH)!=i2)
            {break;}

            //grab the start and finish time hours
            Date stDate= new Date(e.startTime);
            Date ftDate= new Date(e.finishTime);

            //set calendar cal equal to the start date and set the start/end times equals to that date
            cal.setTime(d);
            startTime.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
            startTime.set(Calendar.MONTH, cal.get(Calendar.MONTH));
            startTime.set(Calendar.YEAR, cal.get(Calendar.YEAR));
            endTime.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
            endTime.set(Calendar.MONTH, cal.get(Calendar.MONTH));
            endTime.set(Calendar.YEAR, cal.get(Calendar.YEAR));

            //set cal equal to event start time and set the startTime equal to the event start time
            cal.setTime(stDate);
            startTime.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
            startTime.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));

            //set cal equal to event finish time and set the finishTime equal to the event finish time
            cal.setTime(ftDate);
            endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
            endTime.set(Calendar.MINUTE,cal.get(Calendar.MINUTE));

            //transfer event data to built in WeekViewEvent class
            WeekViewEvent event = new WeekViewEvent(e.getId(),e.name,startTime,endTime);
            //assign color to be displayed on day view, according to event type
            if(e.eventType.equals("Personal Event")){/*green*/
                event.setColor(getResources().getColor(R.color.green));
            } else if(e.eventType.equals("Birthday Event")){/*yellow*/
                event.setColor(getResources().getColor(R.color.yellow));
            } else/*work event*/{/*red*/
                event.setColor(getResources().getColor(R.color.caldroid_light_red));
            }
            wEvents.add(event); //add to WeekViewEvent array list
        }

        return wEvents;
    }


}
