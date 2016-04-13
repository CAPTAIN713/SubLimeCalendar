package com.example.oose.sublimecalendar;

import com.orm.SugarRecord;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by ERIC on 3/31/2016.
 * link to how to create this class that extends Sugar ORM:
 * http://satyan.github.io/sugar/getting-started.html
 */
public class Event extends SugarRecord {

    String name;
    Long date;
    Long startTime;
    Long finishTime;
    String location;
    String emailList;
    String eventType;
    String eventNote;

    public Event(){
    }

    public Event(String name,Long date,Long startTime, Long finishTime,
                 String location,String emailList,String eventType,String eventNote){
        this.name=name;
        this.date=date;
        this.startTime=startTime;
        this.finishTime=finishTime;
        this.location=location;
        this.emailList=emailList;
        this.eventType=eventType;
        this.eventNote=eventNote;
    }

}