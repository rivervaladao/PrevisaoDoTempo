package com.river.app.previsaodotempo;

/**
 * Created by fabrica on 29/02/16.
 */
public class ClimaData {
    static String[] outlookArray = {"Developing snow storms",
            "Partly sunny and breezy", "Mostly sunny", "Afternoon storms", "Increasing cloudiness"};
    static Integer[] symbolArray = {R.drawable.snowy,
            R.drawable.partly_sunny, R.drawable.sunny,
            R.drawable.stormy, R.drawable.cloudy};
    static Integer[] tempsArray = {0, 1, 3, 2, 4};
    static Integer[] minArray = {-5,-3,-2,0,2};
    static Integer[] realFeelArray = {-1, 2, 0, 1, 3};
}
