package com.example.daniel.ogmail.application;

import java.util.Calendar;
import java.util.Date;

public class DateManager {



    public static Date create(){
        return new Date();
    }

    public static Date create(int day, int month, int year, int hour, int minute){
        return new Date(year, month, day, hour, minute);
    }

    public static String weekday(Date date){
        String[] days = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public static String time(Date date){
        int hour = date.getHours();
        int min = date.getMinutes();
        return (hour < 10? "0" : "") + Integer.toString(hour) + ":" + (min < 10? "0" : "") + Integer.toString(min);
    }

    public static String date(Date date){
        int day = date.getDay();
        int month = date.getMonth() + 1;
        int year = date.getYear() % 100;
        return (day < 10? "0" : "") + Integer.toString(day) +
                "/" + (month < 10? "0" : "") + Integer.toString(month) +
                "/" + (year < 10? "0" : "") + Integer.toString(year);
    }
}
