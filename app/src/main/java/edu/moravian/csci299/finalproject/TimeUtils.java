package edu.moravian.csci299.finalproject;

public class TimeUtils {

    public static int getSeconds(long time){
        int totalSeconds = (int) (time / 1000);
        return totalSeconds % 60;
    }
    public static int getMinutes(long time){
        int totalSeconds = (int) (time / 1000);
        return totalSeconds / 60;
    }
    public static int getMilliSeconds(long time){
        int totalMilliSeconds = (int) (time / 10);
        return totalMilliSeconds % 100;
    }
}
