package com.example.parkhg.medisilver.handmadeclass;

/**
 * Created by w on 2017-07-22.
 */

public class PreListViewItem {
    public PreListViewItem(String stextMedicine,String stextMedicine2,String stextDisease,String stextPeriod)
    {
        textMedicine = stextMedicine;
        textMedicine2 = stextMedicine2;
        textDisease = stextDisease;
        textPeriod = stextPeriod;
    }
    private String textMedicine,textMedicine2;
    private String textDisease;
    private String textPeriod;
    private boolean toggleOnOff;

    public String getTextMedicine() {
        return textMedicine;
    }
    public String getTextMedicine2() {
        return textMedicine2;
    }
    public String getTextDisease() {
        return textDisease;
    }
    public String getTextPeriod() {
        return textPeriod;
    }
    public boolean isToggleOnOff() {
        return toggleOnOff;
    }
}
