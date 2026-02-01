package com.example.parkhg.medisilver.handmadeclass;

import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.Switch;

import java.util.Date;

/**
 * Created by ParkHG on 2017-06-19.
 */

public class ListViewItems {
    private int icon;
    private String textStr1,Toggle;
    private Date date;
    private Treatment treatment;
    private boolean buttonItem;
    private UserInfo userInfo;
    public ListViewItems(int inticon, String text1,Date date1, boolean button, Treatment treatmentt ,UserInfo user, String getTb)
    {
        icon = inticon;
        textStr1 = text1;
        date = date1;
        buttonItem = button;
        treatment = treatmentt;
        userInfo = user;
        Toggle = getTb;
    }
    public int getIcon()
    {
        return icon;
    }
    public String getText1()
    {
        return textStr1;
    }
    public Date getDate()
    {
        return date;
    }
    public boolean isButtonItem()
    {
        return buttonItem;
    }
    public Treatment getTreatment() {
        return treatment;
    }
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getToggle() {
        return Toggle;
    }
}
