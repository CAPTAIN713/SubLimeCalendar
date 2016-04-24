package com.example.oose.sublimecalendar;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Joseph on 4/4/2016.
 */
public class FragmentEventListView extends Fragment implements View.OnClickListener {
    private RecyclerView mEventRecycler;
    private EventRecyclerAdapter mAdapter;
    private DividerItemDecoration mDividerDecoration;
    private Bundle extrasBundle;
    private String searchKeyWord,searchEventType;
    private Boolean searchFlag=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        extrasBundle =this.getArguments();
        if(extrasBundle!=null) {
            if (!(extrasBundle.isEmpty()) && (extrasBundle.containsKey("searchKeyword")) && (extrasBundle.containsKey("searchEventType"))) {
                //checks if bundle is empty and if it has the search criteria
                searchKeyWord = extrasBundle.getString("searchKeyword");
                searchEventType = extrasBundle.getString("searchEventType");
                searchFlag = true;
                //Toast.makeText(getContext(),serachEventType, Toast.LENGTH_SHORT).show();
            } else {
                //bundle was empty and user is not searching for an event
                searchFlag = false;
            }
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_event_list_view, container, false);

        mEventRecycler = (RecyclerView) view.findViewById(R.id.eventListRecycler);
        if(!searchFlag){
            mAdapter = new EventRecyclerAdapter(getContext(), this);
        }
        else{
            if(searchEventType.equals("All Event")){
                //ArrayList<Event> eventList = (ArrayList<Event>) Event.find(Event.class,"name like ?","%"+searchKeyWord+"%", "date asc, start_time asc, finish_time asc");
                ArrayList<Event> eventList = (ArrayList<Event>) Event.find(Event.class,"name like ?","%"+searchKeyWord+"%");
                mAdapter = new EventRecyclerAdapter(getContext(), this, eventList);
            }
            else{
                ArrayList<Event> eventList = (ArrayList<Event>) Event.find(Event.class,"name like ?","%"+searchKeyWord+"%");
                for(int i=eventList.size()-1;i>=0;i--){
                    String s=eventList.get(i).eventType;
                    if(!s.equals(searchEventType)){
                        eventList.remove(i);
                    }
                }
                mAdapter = new EventRecyclerAdapter(getContext(), this, eventList);
            }
        }
        mEventRecycler.setAdapter(mAdapter);

        mEventRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mDividerDecoration = new DividerItemDecoration(getContext());
        mEventRecycler.addItemDecoration(mDividerDecoration);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.event_row) {
            /*link on bundles: http://stackoverflow.com/questions/3913592/start-an-activity-with-a-parameter */
            Intent intent = new Intent(getContext(), ActivitySingleEventView.class);
            Bundle b = new Bundle();

            EventRecyclerViewHolder holder = (EventRecyclerViewHolder) mEventRecycler.getChildViewHolder(view);

            b.putLong("eventID", holder.getEventID()); //event id
            intent.putExtras(b); //put id in our next intent
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.updateData();
    }

    /**
     * The below code was taken from:
     * http://stackoverflow.com/questions/24618829/how-to-add-dividers-and-spaces-between-items-in-recyclerview
     */
    private class DividerItemDecoration extends RecyclerView.ItemDecoration {
        private final int[] ATTRS = new int[]{android.R.attr.listDivider};

        private Drawable mDivider;

        /**
         * Default divider will be used
         */
        public DividerItemDecoration(Context context) {
            final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
            mDivider = styledAttributes.getDrawable(0);
            styledAttributes.recycle();
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
