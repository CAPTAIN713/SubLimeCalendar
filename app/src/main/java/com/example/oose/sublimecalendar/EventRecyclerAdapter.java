package com.example.oose.sublimecalendar;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * <code>RecyclerView.Adapter</code> is used by the <code>RecyclerView</code> to convert dynamic data
 * into viewable content on the screen. <code>RecyclerView</code> is designed to avoid making
 * frequent calls to the <code>findByID()</code> method, as this method is very taxing on the
 * system, and can cause significant performance issues when needing to display dynamic content.
 *
 * - Joseph Tompkins
 */
public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventRecyclerViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Event> eventList = new ArrayList<>();

    public EventRecyclerAdapter(Context context) {
        inflater = LayoutInflater.from(context);

        eventList = (ArrayList<Event>) Event.listAll(Event.class);
    }

    /**
     * An alternate constructor that allows the <code>Activity</code> or <code>Fragment</code>
     * containing the <code>RecyclerView</code> using this adapter to pass <code>Event</code>
     * objects via a <code>List</code>. Those <code>Event</code> objects are then used to inflate
     * the individual <code>View</code> objects in the <code>RecyclerView</code>.
     * @param context the <code>Context</code> that this <code>Adapter</code> belongs to
     * @param eventList the list of events to be displayed
     */
    public EventRecyclerAdapter(Context context, ArrayList<Event> eventList) {
        this(context);

        this.eventList = eventList;
    }

    @Override
    public EventRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_row, parent, false);

        EventRecyclerViewHolder holder = new EventRecyclerViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(EventRecyclerViewHolder holder, int position) {
        Event current = eventList.get(position);

        if(current != null) {
            holder.eventName.setText(current.name);

            holder.eventDate.setText(dateToString(current.date));
            holder.eventDuration.setText(timeToString(current.startTime) +
                    " - " + timeToString(current.finishTime));

            if(current.eventType != null) {
                switch (current.eventType) {
                    case "business":
                        holder.eventColorDot.getDrawable().setTint(Color.argb(255, 180, 100, 100));
                        break;
                    case "birthday":
                        holder.eventColorDot.getDrawable().setTint(Color.argb(255, 180, 180, 100));
                        break;
                    case "personal":
                        holder.eventColorDot.getDrawable().setTint(Color.argb(255, 100, 180, 100));
                        break;
                    default:
                        holder.eventColorDot.getDrawable().setTint(Color.argb(255, 100, 180, 100));
                        break;
                }
            }
            else holder.eventColorDot.getDrawable().setTint(Color.argb(255, 100, 180, 100));
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventRecyclerViewHolder extends RecyclerView.ViewHolder {
        protected TextView eventName;
        protected TextView eventDate;
        protected TextView eventDuration;
        protected ImageView eventColorDot;

        public EventRecyclerViewHolder(View itemView) {
            super(itemView);

            eventName = (TextView) itemView.findViewById(R.id.event_name);
            eventDate = (TextView) itemView.findViewById(R.id.event_date);
            eventDuration = (TextView) itemView.findViewById(R.id.event_duration);
            eventColorDot = (ImageView) itemView.findViewById(R.id.event_color_dot);
        }
    }

    /**
     * Converts <code>java.sql.Date()</code> to the <code>String</code> with format
     * <code><i>mm/dd/yyyy</i></code>.
     * @param date
     * @return <code>String</code>
     * of the format <code><i>mm/dd/yyyy</i></code>. If <code>date</code> parameter is
     * <code>null</code>, an empty string is returned.
     */
    private String dateToString(Date date) {
        if(date != null) {
            StringTokenizer tok = new StringTokenizer(date.toString(), "-");
            String year = tok.nextToken();
            int month = Integer.parseInt(tok.nextToken()); // strips month number of leading zeros
            int day = Integer.parseInt(tok.nextToken()); // strips day number of leading zeros

            return month + "/" + day + "/" + year;
        }

        return "";
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

        return "";
    }
}
