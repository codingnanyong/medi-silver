package com.hci.mcalendar.datas;

import android.graphics.Paint;
import android.support.annotation.ColorInt;

/**
 * Created by pdi on 2017-07-23.
 */

public class LineViewData extends DataObject{
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mIndex = 0;

    public LineViewData() {
        super();
    }

    public void setColor(@ColorInt int color){
        mPaint.setColor(color);
    }

    public Paint getPaint(){
        return mPaint;
    }

    public int getmIndex(){
        return mIndex;
    }

    @Override
    public long getFirstDate() {
        return 0;
    }

    @Override
    public int getIndex() {
        return 0;
    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public long getLastDate() {
        return 0;
    }
}
