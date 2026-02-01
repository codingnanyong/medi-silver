package com.hci.mcalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hci.mcalendar.EventHandler.MCalendarEventHandler;
import com.hci.mcalendar.datas.DataObject;
import com.hci.mcalendar.datas.LineViewData;
import com.hci.mcalendar.interfaces.IData;
import com.hci.mcalendar.utils.Common;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by pdi on 2017-07-23.
 */

public class MCalendarItemView extends View {
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintTextWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintTextRed = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundToday = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundEvent = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int dayOfWeek = -1;
    private boolean isStaticText = false;
    private long millis;
    private Rect rect;
    private boolean isTouchMode;
    private int dp11;
    private int dp16;
    private int dp5;
    private int dp3;
    private boolean hasEvent = false;
    private int[] mColorEvents;
    private final float RADIUS = 100f;

    private ArrayList<DataObject> mContents;
    private boolean hasContents = false;
    private MCalendarEventHandler handler = null;

    public MCalendarItemView(Context context) {
        super(context);
        initialize();
    }

    public MCalendarItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        //dataObject
        mContents = new ArrayList<>();

        dp3 = (int) Common.dp2px(getContext(),3);
        dp5 = (int) Common.dp2px(getContext(),5);
        dp11 = (int) Common.dp2px(getContext(),11);
        dp16 = (int) Common.dp2px(getContext(),16);

        mPaint.setColor(Color.DKGRAY);
        mPaint.setTextSize(dp11);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaintTextWhite.setColor(Color.WHITE);
        mPaintTextWhite.setTextSize(dp11);
        mPaintTextWhite.setTextAlign(Paint.Align.CENTER);
        mPaintTextRed.setColor(Color.RED);
        mPaintTextRed.setTextSize(dp11);
        mPaintTextRed.setTextAlign(Paint.Align.CENTER);
        mPaintBackground.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        mPaintBackgroundToday.setColor(ContextCompat.getColor(getContext(), R.color.today));
        mPaintBackgroundEvent.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        setClickable(true);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                Log.d("hatti.onTouchEvent", event.getAction() + "");
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
//                        setBackgroundResource(R.drawable.round_default_select);
                        rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                        isTouchMode = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isTouchMode) {
                            //((MCalendarView) getParent()).setCurrentSelectedView(v);
                            if(handler != null)
                                handler.handler(millis, mContents);
                            isTouchMode = false;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        isTouchMode = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            isTouchMode = false;
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
        setPadding(30, 0, 30, 0);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
        int yPos2 = (int) ((canvas.getHeight() / 3) - ((mPaint.descent() + mPaint.ascent()) / 2));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        MCalendarView calendarView = (MCalendarView) getParent();
        if (calendarView.getParent() instanceof ViewPager) {
            ViewGroup parent = (ViewPager) calendarView.getParent();
            MCalendarItemView tagView = (MCalendarItemView) parent.getTag();

            if (!isStaticText && tagView != null && tagView.getTag() != null && tagView.getTag() instanceof Long) {
                long millis = (long) tagView.getTag();
                if (isSameDay(millis, this.millis)) {
                    RectF rectF = new RectF(xPos - dp16, getHeight() / 2 - dp16, xPos + dp16, getHeight() / 2 + dp16);
                    canvas.drawRoundRect(rectF, RADIUS, RADIUS, mPaintBackground);
                }
            }
        }

//        if (!isStaticText && isToday(millis)) {
//            RectF rectF = new RectF(xPos - dp16, getHeight() / 2 - dp16, xPos + dp16, getHeight() / 2 + dp16);
//            canvas.drawRoundRect(rectF, RADIUS, RADIUS, mPaintBackgroundToday);
//        }

        if (isStaticText) {
            // 요일 표시
            canvas.drawText(MCalendarView.DAY_OF_WEEK[dayOfWeek], xPos, yPos, mPaint);
        } else {
            // 날짜 표시
            if (isToday(millis)) {
                canvas.drawText(calendar.get(Calendar.DATE) + "", xPos, yPos2, mPaintTextRed);
            } else {
                canvas.drawText(calendar.get(Calendar.DATE) + "", xPos, yPos2, mPaint);
            }
        }

//        if (hasEvent) {
//            mPaintBackgroundEvent.setColor(getResources().getColor(mColorEvents[0]));
//            RectF rectF = new RectF(xPos - 5, getHeight() / 2 + 20, xPos + 5, getHeight() / 2 + 30);
//            canvas.drawRoundRect(rectF, RADIUS, RADIUS, mPaintBackgroundToday);
//        }

//        Paint aPaint = new Paint();
//        aPaint.setColor(Color.YELLOW);
//        int top = yPos2 + dp3 * (0 + 1) + dp3 * 0;
//        RectF rectF = new RectF(0, top, getWidth(), top + dp3);
//        canvas.drawRect(rectF, aPaint);
//
//        top = yPos2 + dp3 * (1+ 1) + dp3 * 1;
//        rectF = new RectF(0, top, getWidth(), top + dp3);
//        canvas.drawRect(rectF, aPaint);
//
//        top = yPos2 + dp3 * (2+ 1) + dp3 * 2;
//        rectF = new RectF(0, top, getWidth(), top + dp3);
//        canvas.drawRect(rectF, aPaint);

        if(hasContents && !isStaticText){
//            Paint aPaint = new Paint();
//            aPaint.setColor(Color.YELLOW);
//            int top = yPos2 + dp3 * (0+ 1) + dp3 * 0;
//            RectF rectF = new RectF(0, top, getWidth(), top + dp5);
//            canvas.drawRect(rectF, aPaint);

            for(DataObject object : mContents){
                int top = yPos2 + dp3 * (object.getIndex()+ 1) + object.getIndex() * 1;
                RectF rectF = new RectF(0, top, getWidth(), top + dp5);

                canvas.drawRect(rectF, object.getPaint());
            }
        }

//        rectF =  new RectF(0, yPos2 + 25, getWidth(), yPos2 + 35);
//        canvas.drawRect(rectF, aPaint);
//
//        rectF =  new RectF(0, yPos2 + 40, getWidth(), yPos2 + 50);
//        canvas.drawRect(rectF, aPaint);

//        if(hasContents){
//            Paint aPaint = new Paint();
//            aPaint.setColor(Color.YELLOW);
//            RectF rectF =  new RectF(0, getHeight() / 2 + 20, getWidth(), getHeight() / 2 + 30);
//            canvas.drawRect(rectF, ((LineViewData)content).getPaint());
//
//            RectF rectF =  new RectF(0, getHeight() / 2 + 20, getWidth(), getHeight() / 2 + 30);
//            canvas.drawRect(rectF, ((LineViewData)content).getPaint());
//
//        }

    }

    private boolean isToday(long millis) {
        Calendar cal1 = Calendar.getInstance();
        return isSameDay(cal1.getTimeInMillis(), millis);

    }

    public void setDate(long millis) {
        this.millis = millis;
        setTag(millis);
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        isStaticText = true;
    }

    public void setEvent(int... resid) {
        hasEvent = true;
        mColorEvents = resid;
    }
    public boolean isSameDay(long millis1, long millis2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(millis1);
        cal2.setTimeInMillis(millis2);
        Log.d("hatti.calendar", "" + cal1.get(Calendar.YEAR) + "" + cal1.get(Calendar.MONTH) + "" + cal1.get(Calendar.DATE) + " VS " +
                cal2.get(Calendar.YEAR) + "" + cal2.get(Calendar.MONTH) + "" + cal2.get(Calendar.DATE));
        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE));
    }

    public boolean isStaticText() {
        return isStaticText;
    }

    public void addContent(DataObject content){
        if(!hasContents)
            hasContents = true;
        mContents.add(content);
    }

    public void setHandler(MCalendarEventHandler handler){
        this.handler = handler;
    }
}
