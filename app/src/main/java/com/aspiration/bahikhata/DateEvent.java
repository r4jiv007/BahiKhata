package com.aspiration.bahikhata;

/**
 * Created by abhi on 27/03/15.
 */
public class DateEvent {
    public final int year;
    public final int monthOfYear;
    public final int dayOfMonth;
    public final int minute;
    public final int hourOfDay;
    public final boolean isDate;

    public DateEvent(int dayOfMonth,int monthOfYear,int year,boolean type){
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
        this.hourOfDay = 0;
        this.minute = 0;
        this.isDate = type;
    }

    public DateEvent(int hourOfDay,int minute,boolean type){
        this.year = 0;
        this.monthOfYear = 0;
        this.dayOfMonth = 0;
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.isDate = type;
    }
}
