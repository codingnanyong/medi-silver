package com.example.parkhg.medisilver.handmadeclass;

import android.graphics.Color;
import android.graphics.Paint;

import com.hci.mcalendar.datas.DataObject;

/**
 * Created by w on 2017-07-24.
 */

public class Data extends DataObject {
    public long getFirstDate() {
        return 1500805019118L;
    }

    @Override
    public long getLastDate() {
        return 1500805019118L + 1000*60*60*24*1;
    }

    @Override
    public int getIndex() {
        return 1;
    }

    @Override
    public int getColor() {
        return Color.YELLOW;
    }

    @Override
    public Paint getPaint() {
        Paint paint = new Paint();
        paint.setColor(getColor());
        return paint;
    }
}
