package com.example.parkhg.medisilver.handmadeclass;

/**
 * Created by w on 2017-07-22.
 */

public class PrescriptionListItem {
    public PrescriptionListItem(String id ,int icolor, String stextHospital, String stextDate, String sstate, UserInfo userInfo)
    {
        certifyId = id;
        color = icolor;
        textHospital = stextHospital;
        textDate = stextDate;
        state = sstate;
        user = userInfo;
    }
    private int color;
    private String certifyId;
    private String textHospital;
    private String textDate;
    private String state;
    private UserInfo user;

    public int getColor() {
        return color;
    }

    public String getTextDate() {
        return textDate;
    }

    public String getState() {
        return state;
    }

    public String getTextHospital() {
        return textHospital;
    }

    public String getCertifyId() {
        return certifyId;
    }

    public UserInfo getUser() {
        return user;
    }
}
