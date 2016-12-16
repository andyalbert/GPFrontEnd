package com.project.locateme.Utilities;

import java.util.Calendar;

/**
 * Created by Andrew on 12/11/2016.
 */

public class General {
    public static String calendarToString(Calendar calendar){
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
    }
}
