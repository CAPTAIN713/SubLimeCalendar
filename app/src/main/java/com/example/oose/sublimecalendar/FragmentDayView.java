package com.example.oose.sublimecalendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekView.EventLongPressListener;
import com.alamkanak.weekview.WeekViewEvent;
import com.orm.query.Condition;
import com.orm.query.Select;

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
public class FragmentDayView extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.ScrollListener {


    // Get a reference for the week view in the layout.
    private WeekView mWeekView;
    //private WeekView.EventClickListener mEventClickListener;
    private Long cDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    /**when the user comes back to the main activity and this fragment from another activity
     * the code for updating the database is called so that the event data on the view is updated**/
    public void onStart(){
        super.onStart();
        Log.wtf("day view onStart","update event view");
        mWeekView.notifyDatasetChanged(); //update here
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
            cDate=dateSelected.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mWeekView = (WeekView) view.findViewById(R.id.weekView);

        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setScrollListener(this);

        mWeekView.goToDate(day);

        return view;
    }

    @Override
    /**
     * used when returning from another event view activity. this will cause the day view to refreash
     * in case data was changed.
     * link on using set/return result: http://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
     * link on using set/return result with fragment and activity:
     *      http://stackoverflow.com/questions/17085729/startactivityforresult-from-a-fragment-and-finishing-child-activity-doesnt-c **/
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        //if we get the refreash result code (this is always true, if statement is for error checking)
        if( (requestCode==1) && (resultCode== Activity.RESULT_OK)){
            Log.wtf("day view", "On activity result");
            mWeekView.notifyDatasetChanged(); //update here
        }
    }


    @Override
    /**
     * when you click on an event go to the event view activity. use the startActivityForResult
     * so that we can refresh the view page with the onActivityResult method. **/
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        /*link on bundles: http://stackoverflow.com/questions/3913592/start-an-activity-with-a-parameter */
        Intent intent = new Intent(getContext(), ActivitySingleEventView.class);
        Bundle b = new Bundle();
        b.putLong("eventID", event.getId()); //event id
        intent.putExtras(b); //put id in our next intent
        startActivityForResult(intent, 1);
    }

    @Override
    /** when you go to the day view for the first time or when you go to a differnt month this method
     * is called. it grabs all the vents for the current month**/
    public List<WeekViewEvent> onMonthChange(int i, int i2) {
        Log.wtf("Day View onMonthChange","grabbing events");
        /*i==newYear (yyyy), i2=newMonth (mm)*/
        List<WeekViewEvent> wEvents = new ArrayList<>(1);

        //link on using sugar query: https://github.com/satyan/sugar/issues/376
        //Long l=(cDate-31556926);
        //Long ll=(cDate+31556926);
        //List<Event> events = Event.find(Event.class,"date >= ? AND date < ?",l+"",ll+"");
        //List<Event> events = Event.find(Event.class,"date = ?",cDate+"");
        List<Event> events = Event.listAll(Event.class); //grab all events from database

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


    @Override
    public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {
        //when you scroll to the left or right this method is called. not used because
        //i used on month change mehtod instead.
    }
}