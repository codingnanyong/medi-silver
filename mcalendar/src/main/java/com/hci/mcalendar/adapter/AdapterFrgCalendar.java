package com.hci.mcalendar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.hci.mcalendar.EventHandler.MCalendarEventHandler;
import com.hci.mcalendar.FrgCalendar;
import com.hci.mcalendar.datas.DataObject;
import com.hci.mcalendar.interfaces.IData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by pdi on 2017-07-23.
 */

public class AdapterFrgCalendar extends FragmentStatePagerAdapter {
    private HashMap<Integer, FrgCalendar> frgMap;
    private ArrayList<Long> listMonthByMillis = new ArrayList<>();
    private int numOfMonth;
    private FrgCalendar.OnFragmentListener onFragmentListener;
    private ArrayList<DataObject> contents;
    private MCalendarEventHandler handler;

    public AdapterFrgCalendar(FragmentManager fm) {
        super(fm);
        clearPrevFragments(fm);
        frgMap = new HashMap<Integer, FrgCalendar>();
        contents = new ArrayList<>();
    }

    private void clearPrevFragments(FragmentManager fm) {
        List<Fragment> listFragment = fm.getFragments();

        if (listFragment != null) {
            FragmentTransaction ft = fm.beginTransaction();

            for (Fragment f : listFragment) {
                if (f instanceof FrgCalendar) {
                    ft.remove(f);
                }
            }
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public Fragment getItem(int position) {
        FrgCalendar frg = null;
        if (frgMap.size() > 0) {
            frg = frgMap.get(position);
        }
        if (frg == null) {
            frg = FrgCalendar.newInstance(position);

            Log.d("MTEST", "" + position);
            frg.setOnFragmentListener(onFragmentListener);
            frgMap.put(position, frg);
        }
        frg.setTimeByMillis(listMonthByMillis.get(position));
        if(contents.size() > 0){
            long millis = listMonthByMillis.get(position);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(listMonthByMillis.get(position));

            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);

            long mount_min =  calendar1.getTimeInMillis();
            calendar1.add(Calendar.MONTH, 1);
            long mount_max =  calendar1.getTimeInMillis() - 1;

            for(DataObject data : contents) {
                if (data.getFirstDate() < mount_max && data.getLastDate() > mount_min)
                    Log.d("aaa", "add");
                    frg.addContent(data);
            }
        }
        if(handler != null)
            frg.setHandler(handler);

        return frg;
    }

    @Override
    public int getCount() {
        return listMonthByMillis.size();
    }

    public void setNumOfMonth(int numOfMonth) {
        this.numOfMonth = numOfMonth;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -numOfMonth);
        calendar.set(Calendar.DATE, 1);

        for (int i = 0; i < numOfMonth * 2 + 1; i++) {
            listMonthByMillis.add(calendar.getTimeInMillis());
            calendar.add(Calendar.MONTH, 1);
        }

        notifyDataSetChanged();
    }

    public void addNext() {
        long lastMonthMillis = listMonthByMillis.get(listMonthByMillis.size() - 1);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastMonthMillis);
        for (int i = 0; i < numOfMonth; i++) {
            calendar.add(Calendar.MONTH, 1);
            listMonthByMillis.add(calendar.getTimeInMillis());
        }
        notifyDataSetChanged();
    }

    public void addPrev() {
        long lastMonthMillis = listMonthByMillis.get(0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastMonthMillis);
        calendar.set(Calendar.DATE, 1);
        for (int i = numOfMonth; i > 0; i--) {
            calendar.add(Calendar.MONTH, -1);

            listMonthByMillis.add(0, calendar.getTimeInMillis());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public String getMonthDisplayed(int position) {
        Calendar calendar = Calendar.getInstance();
        int yyyy = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(listMonthByMillis.get(position));
        if (yyyy != calendar.get(Calendar.YEAR)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM");
            Date date = new Date();
            date.setTime(listMonthByMillis.get(position));

            return sdf.format(date);
        } else {
            return calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        }
    }

    public void setOnFragmentListener(FrgCalendar.OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }

    public void setContents(ArrayList<DataObject> datas){
        contents = datas;
    }

    public void addContent(DataObject data){
        contents.add(data);
        Log.d("addContent", "aaa");
    }

    public void setHandler(MCalendarEventHandler handler){
        this.handler = handler;
    }
}
