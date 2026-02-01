package com.hci.mcalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hci.mcalendar.EventHandler.MCalendarEventHandler;
import com.hci.mcalendar.datas.DataObject;
import com.hci.mcalendar.interfaces.IData;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.ButterKnife;

/**
 * Created by pdi on 2017-07-23.
 */

public class FrgCalendar extends Fragment {
    private int position;
    MCalendarView calendarView;
    private long timeByMillis;
    private OnFragmentListener onFragmentListener;
    private View mRootView;

    private ArrayList<DataObject> contents;
    private MCalendarEventHandler handler = null;

    public void setOnFragmentListener(OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }

    public interface OnFragmentListener {
        public void onFragmentListener(View view);
    }

    public static FrgCalendar newInstance(int position) {
        FrgCalendar frg = new FrgCalendar();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        frg.setArguments(bundle);
        return frg;
    }

    public FrgCalendar() {
        contents = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("poisition");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_calendar, null);
        ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    protected void initView() {
        calendarView = (MCalendarView) mRootView.findViewById(R.id.calendarview);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeByMillis);
        calendar.set(Calendar.DATE, 1);
        // 1일의 요일
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //이달의 마지막 날
        int maxDateOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendarView.initCalendar(dayOfWeek, maxDateOfMonth);
        for (int i = 0; i < maxDateOfMonth + 7; i++) {
            MCalendarItemView child = new MCalendarItemView(getActivity().getApplicationContext());
//            if (i == 20) {
//                child.setEvent(R.color.colorPrimaryDark);
//            }
            child.setDate(calendar.getTimeInMillis());

            if (i < 7) {
                child.setDayOfWeek(i);
            } else {
                if(handler != null)
                    child.setHandler(handler);

                for(DataObject data : contents){
                    if(calendar.getTimeInMillis() >= data.getFirstDate() && calendar.getTimeInMillis() <= data.getLastDate()) {
                        child.addContent(data);
                    }
                }

                calendar.add(Calendar.DATE, 1);
            }
            calendarView.addView(child);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && onFragmentListener != null && mRootView != null) {
            onFragmentListener.onFragmentListener(mRootView);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {

            mRootView.post(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    onFragmentListener.onFragmentListener(mRootView);
                }
            });

        }
    }

    public void setTimeByMillis(long timeByMillis) {
        this.timeByMillis = timeByMillis;
    }

    public void setContents(ArrayList<DataObject> datas){
        contents = datas;
    }

    public void addContent(DataObject data){
        contents.add(data);
    }

    public void setHandler(MCalendarEventHandler handler){
        this.handler = handler;
    }
}
