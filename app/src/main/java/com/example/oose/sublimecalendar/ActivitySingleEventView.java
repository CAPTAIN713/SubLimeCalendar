package com.example.oose.sublimecalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

public class ActivitySingleEventView extends AppCompatActivity implements View.OnClickListener {

    private TextView name,date,startTime,finishTime,location,emailList, eventType, note;
    private Button editButton, shareButton, deleteButton;
    private Bundle extrasBundle;
    private Long selectedEventID;
    private String emList="";
    private Event e;

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
        setContentView(R.layout.activity_single_event_view);

        e = Event.findById(Event.class, selectedEventID);
        Calendar calendarStartTime = Calendar.getInstance();
        Calendar calendarEndTime = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

        //java.util.Date d= new java.util.Date(e.date);
        //java.util.Date stDate= new java.util.Date(e.startTime);
        //java.util.Date ftDate= new java.util.Date(e.finishTime);

        name =(TextView) findViewById(R.id.singleEventNameField);
        date =(TextView) findViewById(R.id.singleEventDateField);
        startTime =(TextView) findViewById(R.id.singleEventStartTimeField);
        finishTime =(TextView) findViewById(R.id.singleEventFinishTimeField);
        location =(TextView) findViewById(R.id.singleEventLocationField);
        emailList =(TextView) findViewById(R.id.singleEventEmailListField);
        eventType =(TextView) findViewById(R.id.singleEventEventTypeField);
        note =(TextView) findViewById(R.id.singleEventNoteField);

        calendar.setTime(new java.util.Date(e.date));
        name.setText(e.name);
        date.setText(calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));
        calendar.setTime(new java.util.Date(e.startTime));
        startTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        calendar.setTime(new java.util.Date(e.finishTime));
        finishTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        location.setText(e.location);
        emailList.setText(e.emailList);
        emList=e.emailList;
        eventType.setText(e.eventType);
        note.setText(e.eventNote);

        editButton = (Button) findViewById(R.id.singleEventEditButton);
        shareButton = (Button) findViewById(R.id.singleEventShareButton);
        deleteButton = (Button) findViewById(R.id.singleEventDeleteButton);

        editButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

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
        if( (requestCode==1) && (resultCode== Activity.RESULT_OK)){
            Log.wtf("singleEvent", "On activity result");
            //redownload event and update all fields incase they were changed in the edit activity
            Event e= Event.findById(Event.class, selectedEventID);

            Calendar calendarStartTime = Calendar.getInstance();
            Calendar calendarEndTime = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new java.util.Date(e.date));
            name.setText(e.name);
            date.setText(calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));
            calendar.setTime(new java.util.Date(e.startTime));
            startTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
            calendar.setTime(new java.util.Date(e.finishTime));
            finishTime.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
            location.setText(e.location);
            emailList.setText(e.emailList);
            emList=e.emailList;
            eventType.setText(e.eventType);
            note.setText(e.eventNote);
        }
    }

    @Override
    public void onBackPressed(){
        //link on using onBackPressed method: http://stackoverflow.com/questions/4778754/kill-activity-on-back-button
        /*link on using set/return result: http://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
        link on using backPressed and setResult: http://stackoverflow.com/questions/2679250/setresult-does-not-work-when-back-button-pressed */
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onClick(View v) {
        Intent myIntent=null;
        switch (v.getId()){
            case R.id.singleEventEditButton:
                //link on starting new activity:http://stackoverflow.com/questions/4186021/how-to-start-new-activity-on-button-click
                myIntent = new Intent(this, ActivityEditEvent.class);
                myIntent.putExtra("eventID", selectedEventID); //Optional parameters
                this.startActivityForResult(myIntent, 1);
                break;

            case R.id.singleEventShareButton:
                //example on sending an email: https://github.com/CAPTAIN713/VitaCheck/blob/master/app/src/main/java/vitacheck/vitacheck/fragments/DoctorFragmentIndividualPage.java
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emList});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, e.name);

                Date date = new Date(e.date);
                Time startTime = new Time(e.startTime);
                Time finishTime = new Time(e.finishTime);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

                // Building email body
                String emailBody =    "Event Name:  " + e.name + "\n"
                                    + "Event Type:  " + e.eventType + "\n"
                                    + "Event Date:  " + dateFormat.format(date) + "\n"
                                    + "Start Time:  " + timeToString(startTime) + "\n"
                                    + "Finish Time:  " + timeToString(finishTime) + "\n"
                                    + "Location:  " + e.location + "\n"
                                    + "Notes:   " + e.eventNote + "\n";

                emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
                try{
                    startActivity(Intent.createChooser(emailIntent,"Send mail..."));
                }
                catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(this,"Unable to send email", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.singleEventDeleteButton:
                //link on using the alert dialog builder: http://www.tutorialspoint.com/android/android_alert_dialoges.htm
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure you want to delete this event?\nIt will be deleted forever! (A really long time!)");

                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(ActivitySingleEventView.this, "You clicked yes button", Toast.LENGTH_LONG).show();
                        Event e= Event.findById(Event.class, selectedEventID);
                        e.delete();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                });
                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
        }
    } //end of onClick method

    /**
     * Converts <code>java.sql.Date()</code> to the <code>String</code> with format
     * <code><i>hh:mm[am|pm]</i></code>.
     * @param time
     * @return <code>String</code> of format <code>hh:mm[am|pm]</code>. If <code>time</code>
     * is <code>null</code>, an empty string is returned.
     */
    private String timeToString(Time time) {
        if(time != null) {
            StringTokenizer tok = new StringTokenizer(time.toString(), ":");

            int hours = Integer.parseInt(tok.nextToken());
            String minutes = tok.nextToken();

            if (hours > 11 && hours < 24) {
                if(hours != 12) hours -= 12;
                return hours + ":" + minutes + "pm";
            }

            if(hours == 0) hours = 12;
            return hours + ":" + minutes + "am";
        }

        return "\"You Broke It!\" - timeToString ";
    }
}
