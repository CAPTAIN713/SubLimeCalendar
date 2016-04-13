package com.example.oose.sublimecalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addFAB);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        final CaldroidListener calListener = new CaldroidListener() {
            @Override
            public void onSelectDate(java.util.Date date, View view) {
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String day_string = df.format(date);

                Fragment dayViewFragment = new FragmentDayView();
                Bundle selectDate = new Bundle();
                selectDate.putString("selectedDate", day_string);

                dayViewFragment.setArguments(selectDate);

                if (dayViewFragment != null) {
                    /** Kristina, this is where you would put the code to go to day view
                     * just change the above new fragment class to your day view java class **/
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainCalendarContainer, dayViewFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

            }
        };

        caldroidFragment.setCaldroidListener(calListener);
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainCalendarContainer, caldroidFragment);
        transaction .commit();
    }

    @Override
    public void onBackPressed() {
        //code for popBackExample found here:
        // https://github.com/CAPTAIN713/VitaCheck/blob/master/app/src/main/java/vitacheck/vitacheck/MainActivity.java

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(getFragmentManager().getBackStackEntryCount()>1) //if there is a saved fragment on the stack
            {
                //if at least one thing on fragment stack go back to that one
                getFragmentManager().popBackStack();
            }
            else{
                //if nothing else on stack exit app
                super.onBackPressed();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.month_view) {
            CaldroidFragment caldroidFragment = new CaldroidFragment();
            final CaldroidListener calListener = new CaldroidListener() {
                @Override
                public void onSelectDate(java.util.Date date, View view) {
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    String day_string = df.format(date);
                    Fragment dayViewFragment = new FragmentDayView();
                    Bundle selectDate = new Bundle();
                    selectDate.putString("selectedDate", day_string);
                    dayViewFragment.setArguments(selectDate);
                    if (dayViewFragment != null) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.mainCalendarContainer, dayViewFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }}};
            caldroidFragment.setCaldroidListener(calListener);
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            caldroidFragment.setArguments(args);
            FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainCalendarContainer, caldroidFragment);
            transaction .commit();

        } else if (id == R.id.week_view) {
            Fragment weekViewFragment = new FragmentWeekView();
            if (weekViewFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainCalendarContainer, weekViewFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        } else if (id == R.id.day_view) {
            Fragment dayViewFragment = new FragmentDayView();

            java.util.Date today_date = new java.util.Date();

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String day_string = df.format(today_date);

            Bundle dateToday = new Bundle();
            dateToday.putString("selectedDate", day_string);

            dayViewFragment.setArguments(dateToday);
            if (dayViewFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainCalendarContainer, dayViewFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        } else if (id == R.id.Event_list_view) {
            Fragment eventListFragment = new FragmentEventListView();
            if(eventListFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainCalendarContainer, eventListFragment);
                transaction.addToBackStack("eventListView");
                transaction.commit();
            }

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        /* for example on how to go from one fragment to another
        Fragment fragment = new FragAddEvent();
        if (fragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainCalendarContainer, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }*/
        switch (v.getId()){
            case R.id.addFAB:
                //make new intent; intent is used for activities, not fragments
                Intent myIntent = new Intent(this, ActivityAddEvent.class);
                this.startActivity(myIntent); //start new activity, old one does not go away
                break;
        }



    } //end of onClick method

} //end of activity class