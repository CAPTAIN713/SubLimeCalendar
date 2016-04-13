package com.example.oose.sublimecalendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
/**
 * <code>RecyclerView.Adapter</code> is used by the <code>RecyclerView</code> to convert dynamic data
 * into viewable content on the screen. <code>RecyclerView</code> is designed to avoid making
 * frequent calls to the <code>findByID()</code> method, as this method is very taxing on the
 * system, and can cause significant performance issues when needing to display dynamic content.
 *
 * - Joseph Tompkins
 */
public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Event> eventList = new ArrayList<>();
    private View.OnClickListener mOnClickListener;
    private int eventRowViewID = R.id.event_row;

    public EventRecyclerAdapter(Context context, View.OnClickListener clickListener) {
        inflater = LayoutInflater.from(context);

        mOnClickListener = clickListener;

//        eventList = (ArrayList<Event>) Event.listAll(Event.class, "date asc");
        updateData();
    }

    /**
     * An alternate constructor that allows the <code>Activity</code> or <code>Fragment</code>
     * containing the <code>RecyclerView</code> using this adapter to pass <code>Event</code>
     * objects via a <code>List</code>. Those <code>Event</code> objects are then used to inflate
     * the individual <code>View</code> objects in the <code>RecyclerView</code>.
     *
     * @param context   the <code>Context</code> that this <code>Adapter</code> belongs to
     * @param eventList the list of events to be displayed
     */
    public EventRecyclerAdapter(Context context, View.OnClickListener clickListener, ArrayList<Event> eventList) {
        this(context, clickListener);

        this.eventList = eventList;
    }

    @Override
    public EventRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_row, parent, false);

        EventRecyclerViewHolder holder = new EventRecyclerViewHolder(view);

        view.setOnClickListener(mOnClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(EventRecyclerViewHolder holder, int position) {
        Event current = eventList.get(position);

        if (current != null) {
            holder.setEvent(current);
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void updateData() {
        eventList = (ArrayList<Event>) Event.listAll(Event.class, "date asc, start_time asc, finish_time asc");

        notifyDataSetChanged();
    }
}
