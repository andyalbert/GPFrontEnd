package com.project.locateme.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * @author Andrew
 * @since 12/11/2017
 * @version 1.0
 */

public class General {
    /**
     * this function takes a calender object and return it in the format DD/MM/YYYY
     * @param calendar
     * @return string, the formatted date
     */
    public static String calendarToString(Calendar calendar){
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
    }

    /**
     * This function check if there is an internet connection or not
     * @param context
     * @return boolean, true for online, else false
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    /**
     * this function is used for converting a view to a bitmap, used mainly for the Google map marker shape
     * @param view the view to be converted
     * @return bitmap, the view after conversion
     */
    public static Bitmap loadBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }
}
