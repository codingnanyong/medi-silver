package com.hci.mcalendar.utils;

import android.content.Context;

/**
 * Created by pdi on 2017-07-23.
 */

public class Common {
    public static float dp2px(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
