package com.example.oose.sublimecalendar;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

/**
 * Created by Joseph on 4/11/2016.
 */
public class EventRecyclerViewHolder extends RecyclerView.ViewHolder {
    private long eventID;
    private TextView eventName;
    private TextView eventDate;
    private TextView eventDuration;
    private ImageView eventColorDot;

    public EventRecyclerViewHolder(View itemView) {
        super(itemView);

        eventName = (TextView) itemView.findViewById(R.id.event_name);
        eventDate = (TextView) itemView.findViewById(R.id.event_date);
        eventDuration = (TextView) itemView.findViewById(R.id.event_duration);
        eventColorDot = (ImageView) itemView.findViewById(R.id.event_color_dot);
    }

    /**
     * Get the event ID associated with this holder.
     * @return
     */
    public long getEventID() {
        return eventID;
    }

    /**
     * Sets the event ID, name, date, time duration, and event type.
     * @param event
     */
    public void setEvent(Event event) {
        if(event != null) {
            eventID = event.getId();
            eventName.setText(event.name);

            //holder.eventDate.setText(dateToString(current.date));
            //http://stackoverflow.com/questions/11755534/how-to-convert-long-date-value-to-mm-dd-yyyy-format
            Date d = new Date(event.date);
            SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
            eventDate.setText(df2.format(d));

            Time starttimestamp = new Time(event.startTime);
            Time finishtimestamp = new Time(event.finishTime);
            eventDuration.setText(timeToString(starttimestamp)
                    + " - " + timeToString(finishtimestamp));

            /**
             * Sets the color of the dot within the Event row based on the associated event's
             * type.
             */
            if (event.eventType != null) {
                switch (event.eventType) {
                    case "Work Event":
                        eventColorDot.getDrawable().setTint(Color.argb(255, 180, 100, 100));
                        break;
                    case "Birthday Event":
                        eventColorDot.getDrawable().setTint(Color.argb(255, 180, 180, 100));
                        break;
                    case "Personal Event":
                        eventColorDot.getDrawable().setTint(Color.argb(255, 100, 180, 100));
                        break;
                    default:
                        eventColorDot.getDrawable().setTint(Color.argb(255, 100, 180, 100));
                        break;
                }
            } else eventColorDot.getDrawable().setTint(Color.argb(255, 100, 180, 100));

            return;
        }

        eventID = -1;
        eventName.setText("!null Event!");
        eventColorDot.getDrawable().setTint(Color.argb(255, 100, 180, 100));
    }

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
