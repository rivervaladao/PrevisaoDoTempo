package com.river.app.previsaodotempo;

import java.sql.Time;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by fabrica on 01/03/16.
 */
public class Clima {

    public static final String ARG_DAY = "day";
    public static final String ARG_OUTLOOK = "text_outlook";
    public static final String ARG_IMAGE = "image_symbol";
    public static final String ARG_TEMP="text_temp";
    public static final String ARG_MIN="text_min";
    public static final String ARG_REAL_FEEL="text_real_feel";

    public final String dayOfWeek;
    public final String minTemp;
    public final String maxTemp;
    public final String humidity;
    public final String description;
    public final String iconUrl;

    public Clima(long timeStamp, double minTemp, double maxTemp, double humidity, String description, String iconName) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);

        this.dayOfWeek = convertTimeStampToDay(timeStamp);
        this.minTemp = numberFormat.format(minTemp) + "\u00B0F";
        this.maxTemp = numberFormat.format(maxTemp) + "\u00B0F";
        this.humidity = NumberFormat.getPercentInstance().format(humidity/100.0);
        this.description = description;
        this.iconUrl =
                "http://openweathermap.org/img/w/" + iconName + ".png";
    }

    private String convertTimeStampToDay(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp * 1000);
        TimeZone tz = TimeZone.getDefault();

        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE");

        return dateFormat.format(calendar.getTime());
    }
}
