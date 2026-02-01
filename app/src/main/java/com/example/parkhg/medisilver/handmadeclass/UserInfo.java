package com.example.parkhg.medisilver.handmadeclass;

import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ParkHG on 2017-06-22.
 */

public class UserInfo implements Serializable{
    private String name;
    private Date birth;
    private String protect_phone;
    private double height;
    private double weight;
    private double liver;
    private boolean diabetes;
    private char bloodpressure;
    private int certifycount;

    public UserInfo(String in_name, Date in_birth, String in_pro_phone, double in_height, double in_weight, double in_liver, boolean in_dia, char in_blood, int in_certify){
        try {
            name = in_name;
            birth = in_birth;
            protect_phone = in_pro_phone;
            height = in_height;
            weight = in_weight;
            liver = in_liver;
            diabetes = in_dia;
            bloodpressure = in_blood;
            certifycount = in_certify;
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }

    public UserInfo(String in_name, String in_birth, String in_pro_phone, double in_height, double in_weight, double in_liver, boolean in_dia, char in_blood, int in_certify){
        try {
            name = in_name;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            birth = sdf.parse(in_birth);
            protect_phone = in_pro_phone;
            height = in_height;
            weight = in_weight;
            liver = in_liver;
            diabetes = in_dia;
            bloodpressure = in_blood;
            certifycount = in_certify;
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }

    public String getName(){ return name;}
    public Date getBirth(){ return birth;}
    public String getStrBirth(){
        SimpleDateFormat trans = new SimpleDateFormat("yyyy");
        return trans.format(birth);
    }
    public String getProtectPhone(){ return protect_phone; }
    public double getHeight(){return height;}
    public double getWeight(){return weight;}
    public double getLiver(){return liver;}
    public boolean isDiabetes(){return diabetes;}
    public char getBloodpressure(){return bloodpressure;}
    public int getCertifycount(){return certifycount;}

    public void show(){
        Log.i("UserInfo : ", name+", "+birth+", "+protect_phone+", height: "+height+", weight: "+weight+", liver: "+liver+", diabetes: "+diabetes+", blood: "+bloodpressure+" certify: "+certifycount);
    }
}
