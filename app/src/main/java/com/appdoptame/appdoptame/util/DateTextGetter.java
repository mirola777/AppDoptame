package com.appdoptame.appdoptame.util;

import android.annotation.SuppressLint;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.appdoptame.appdoptame.AppDoptameApp;
import com.appdoptame.appdoptame.R;

public class DateTextGetter {
    @SuppressLint("StringFormatMatches")
    public static String getDateText(Date date){
        Date currentTime = Calendar.getInstance().getTime();
        long diff = currentTime.getTime() - date.getTime();

        TimeUnit timeDays = TimeUnit.DAYS;
        long difference = timeDays.convert(diff, TimeUnit.MILLISECONDS);

        if(difference >= 7){
            return date.toString();
        } else if(difference < 7 && difference >= 1) {
            return String.format(
                    AppDoptameApp.getContext().getString(R.string.days_ago),
                    difference);
        } else {
            TimeUnit timeHours = TimeUnit.HOURS;
            difference = timeHours.convert(diff, TimeUnit.MILLISECONDS);

            if(difference >= 1){
                return String.format(
                        AppDoptameApp.getContext().getString(R.string.hours_ago),
                        difference);
            } else {
                TimeUnit timeMinutes = TimeUnit.MINUTES;
                difference = timeMinutes.convert(diff, TimeUnit.MILLISECONDS);

                if(difference > 1){
                    return String.format(
                            AppDoptameApp.getContext().getString(R.string.minutes_ago),
                            difference);
                } else {
                    return AppDoptameApp.getContext().getString(R.string.few_moments_ago);
                }
            }
        }
    }
}