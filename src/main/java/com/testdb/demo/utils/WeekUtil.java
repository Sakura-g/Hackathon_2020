package com.testdb.demo.utils;

public class WeekUtil {

    public static String last(String currentDay){
        switch(currentDay){
            case "Sun": return "Sat";
            case "Mon": return "Sun";
            case "Tue": return "Mon";
            case "Wed": return "Tue";
            case "Thu": return "Wed";
            case "Fri": return "Thu";
            case "Sat": return "Fri";
        }
        return null;
    }

    public static String next(String currentDay){
        switch(currentDay){
            case "Sun": return "Mon";
            case "Mon": return "Tue";
            case "Tue": return "Wed";
            case "Wed": return "Thu";
            case "Thu": return "Fri";
            case "Fri": return "Sat";
            case "Sat": return "Sun";
        }
        return null;
    }

}
