package com.example.oose.sublimecalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ActivityEditEvent extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private EditText nameTB,dateTB,locationTB,emailListTB,startTimeTB,finishTimeTB,noteTB;
    private Button saveButton;
    private TextView startTime,finishTime,date;
    private String name,selectedDate="",location="",emailList="",eventType="",selectedStartTime="",selectedFinishTime="",note="";
    private String selectedDateString,selectedStartTimeString,selectedFinishTimeString;
    private Long microSecDate,microSecStartTime,microSecFinishTime;
    private Bundle extrasBundle;
    private Long selectedEventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //link on getting bundle: http://stackoverflow.com/questions/3913592/start-an-activity-with-a-parameter
        extrasBundle = getIntent().getExtras();

        if( !(extrasBundle.isEmpty()) && (extrasBundle.containsKey("eventID")) ){
            //checks if bundle is empty and if it has the event id
            selectedEventID=extrasBundle.getLong("eventID");
        }
        else{
            //either bundle was empty or did not have parse id. should find a way to go back to previous activity
            //put finish()
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        nameTB=(EditText) findViewById(R.id.editEventNameTB);
        locationTB=(EditText) findViewById(R.id.editEventLocationTB);
        emailListTB=(EditText) findViewById(R.id.editEventEmailListTB);
        noteTB=(EditText) findViewById(R.id.editEventNoteTB);
        /*code on date and time picker: http://stackoverflow.com/questions/17901946/timepicker-dialog-from-clicking-edittext
         * use this for later: http://android--examples.blogspot.com/2015/04/timepickerdialog-in-android.html */
        date=(TextView) findViewById(R.id.editEventDateTB);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(ActivityEditEvent.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        selectedDate=("" + selectedmonth + "-" + selectedday + "-" + selectedyear);
                        //Code for convert date to long micro seconds:
                        // http://stackoverflow.com/questions/8427169/converting-a-date-string-into-milliseconds-in-java
                        try {
                            selectedDateString= selectedmonth+" "+selectedday+" "+selectedyear;
                            Date date = new SimpleDateFormat("MM dd yyyy", Locale.ENGLISH).parse(selectedDateString);
                            microSecDate=date.getTime();
                            Log.wtf("work pls", date.getTime() + "");
                        }catch(Exception e){
                            //error_lol
                        }
                        date.setText("" + selectedmonth + "/" + selectedday + "/" + selectedyear);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });
        //startTimeTB=(EditText) findViewById(R.id.editEventStartTimeTB);
        //finishTimeTB=(EditText) findViewById(R.id.editEventFinishTimeTB);
        startTime=(TextView) findViewById(R.id.editEventStartTimeTB);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ActivityEditEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        selectedStartTime=(selectedHour + ":" + selectedMinute);
                        selectedStartTimeString=selectedHour + ":" + selectedMinute;
                        try {
                            Date stime = new SimpleDateFormat("hh:mm", Locale.ENGLISH).parse(selectedStartTimeString);
                            microSecStartTime=stime.getTime();
                        } catch (ParseException e) {
                            //error_lol
                        }
                        startTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        finishTime =(TextView) findViewById(R.id.editEventFinishTimeTB);
        finishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ActivityEditEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        selectedFinishTime=(selectedHour + ":" + selectedMinute);
                        selectedFinishTimeString=selectedHour + ":" + selectedMinute;
                        try {
                            Date ftime = new SimpleDateFormat("hh:mm", Locale.ENGLISH).parse(selectedFinishTimeString);
                            microSecFinishTime=ftime.getTime();
                        } catch (ParseException e) {
                            //error_lol
                        }
                        finishTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        //dateTB=(EditText) findViewById(R.id.editEventDateTB);

        /*sites on spinners: http://developer.android.com/guide/topics/ui/controls/spinner.html
        http://www.tutorialspoint.com/android/android_spinner_control.htm  */
        Spinner spinner = (Spinner) findViewById(R.id.editEventTypeSpinner);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.eventTypes,android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Event e= Event.findById(Event.class,selectedEventID);
        Calendar calendarStartTime = Calendar.getInstance();
        Calendar calendarEndTime = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

        saveButton = (Button) findViewById(R.id.editEventSaveButton);
        saveButton.setOnClickListener(this);
        nameTB.setText(e.name);
        calendar.setTime(new java.util.Date(e.date));
        int temp=calendar.get(Calendar.MONTH)+1;
        date.setText(temp + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));
        microSecDate=calendar.getTimeInMillis();
        calendar.setTime(new java.util.Date(e.startTime));
        startTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        microSecStartTime=calendar.getTimeInMillis();
        calendar.setTime(new java.util.Date(e.finishTime));
        finishTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        microSecFinishTime=calendar.getTimeInMillis();
        locationTB.setText(e.location);
        emailListTB.setText(e.emailList);
        int spinnerPosition = adapter.getPosition(e.eventType);
        spinner.setSelection(spinnerPosition);
        eventType=e.eventType;
        //eventTypeTB.setText(e.eventType);
        noteTB.setText(e.eventNote);

    }


    @Override
        /*link on how to handle mutiple button clicks
         http://stackoverflow.com/questions/21827046/handle-multiple-button-click-in-view-onclicklistener-in-android*/
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.editEventSaveButton: //if save button was pressed
                //pull the inputed user data from the edit fields, no need to pull the times and dates
                //because they are auto grabed when selected
                name=nameTB.getText().toString();
                location=locationTB.getText().toString();
                emailList=emailListTB.getText().toString();
                note=noteTB.getText().toString();

                //error check; make sure user inputed data into the name, date and time fields
                if(name.compareTo("")==0){
                    Toast.makeText(getApplicationContext(), "Please fill out the name field", Toast.LENGTH_SHORT).show();
                    break;}

                try {
                    //Event e = new Event(name, microSecDate, microSecStartTime, microSecFinishTime, location, emailList, eventType, note);
                    Event e= Event.findById(Event.class,selectedEventID);
                    e.name=name;
                    e.date=microSecDate;
                    e.startTime=microSecStartTime;
                    e.finishTime=microSecFinishTime;
                    e.location=location;
                    e.emailList=emailList;
                    e.eventType=eventType;
                    e.eventNote=note;
                    e.save();
                    Toast.makeText(getApplicationContext(), "Saved Changes", Toast.LENGTH_SHORT).show();

                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                    break;
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Unable to save event", Toast.LENGTH_SHORT).show();
                    super.onBackPressed(); //same effect as pressing the back button
                    break;
                }


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        String item = parent.getItemAtPosition(position).toString();
        eventType=item;
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback. Required method for spinner.
    }

}
