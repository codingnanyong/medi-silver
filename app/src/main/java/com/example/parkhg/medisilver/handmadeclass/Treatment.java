package com.example.parkhg.medisilver.handmadeclass;

import android.graphics.Color;
import android.graphics.Paint;

import com.hci.mcalendar.datas.DataObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by ParkHG on 2017-06-22.
 */

public class Treatment extends DataObject implements Serializable {
    public String certificate_id;                       //진단서 id
    public String hospital_id;                          //병원 id
    public String hospital_name;                        //병원 이름
    public Date regist_day;                             //등록일
    public int takedays;                                //복용 일수
    public String department;                           //진료과
    //public String dianosis;                             //진단 -> 이거 필요할까?
    public int medicinescount;                         //약 갯수
    public ArrayList<ArrayList<String>> medicines;      //약 정보 [0]: 약 id, [1]: 약이름, [2]:약모양, [3]:병코드
    public int color;                                    //달력에 출력될 색 저장
    public boolean takingcheck[][];                     //고객이 약 먹은 정보 저장
    public String dosestate;

    private int index = 1;
    public Treatment(){
        try {
            Random random = new Random();
            certificate_id = null;
            hospital_id = null;
            hospital_name = null;
            regist_day = new Date(System.currentTimeMillis());
            takedays = 3;
            department = null;
            //dianosis = null;
            medicinescount = 0;
            medicines = new ArrayList<ArrayList<String>>();
            color = 0;
            takingcheck = new boolean[takedays][3];
            dosestate = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Treatment(String in_certify, String in_hospital, String in_hos_name, Date in_register, int in_take, String in_depart, int in_mcount, ArrayList<ArrayList<String>> in_medi, int in_color, boolean in_check[][]){
        try {
            certificate_id = in_certify;
            hospital_id = in_hospital;
            hospital_name = in_hos_name;
            regist_day = in_register;
            takedays = in_take;
            department = in_depart;
            //dianosis = in_dianosis;
            medicinescount = in_mcount;
            medicines = (ArrayList<ArrayList<String>>)in_medi.clone();
            color = in_color;
            takingcheck = in_check.clone();
            dosestate = "dosing";
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Treatment(String in_certify, String in_hospital, String in_hos_name, String in_register, int in_take, String in_depart, int in_mcount, ArrayList<ArrayList<String>> in_medi, int in_color, boolean in_check[][]){
        try {
            certificate_id = in_certify;
            hospital_id = in_hospital;
            hospital_name = in_hos_name;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.KOREAN);
            regist_day = sdf.parse(in_register);
            takedays = in_take;
            department = in_depart;
            //dianosis = in_dianosis;
            medicinescount = in_mcount;
            medicines = (ArrayList<ArrayList<String>>)in_medi.clone();
            color = in_color;
            takingcheck = in_check.clone();
            dosestate = "dosing";
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Treatment(Treatment input){
        certificate_id = input.certificate_id;
        hospital_id = input.hospital_id;
        hospital_name = input.hospital_name;
        regist_day = input.regist_day;
        takedays = input.takedays;
        department = input.department;
        medicinescount = input.medicinescount;
        medicines = (ArrayList<ArrayList<String>>)input.medicines.clone();
        color = input.color;
        takingcheck = input.takingcheck.clone();
        dosestate = input.dosestate;
    }

    public String getInfo() {
        String temp = certificate_id +"\n"+ hospital_id+"\n"+hospital_name+"\n"+regist_day+"\n"+takedays+"\n"+department+"\n"+medicinescount+"\n"+ color;
        for(ArrayList<String> str : medicines){
            for(String strs : str)
                temp += strs + " ";
            temp +="\n";
        }
        return temp;
    }
    public long getFirstDate() {
        return regist_day.getTime();
    }

    @Override
    public long getLastDate() {
        return regist_day.getTime() + (1000*60*60*24)*takedays;
    }

    @Override
    public int getIndex() {
        return index;
    }
    public void setIndex(int i){
        index = i;
    }
    @Override
    public int getColor() {
        return color;
    }

    @Override
    public Paint getPaint() {
        Paint paint = new Paint();
        paint.setColor(getColor());
        return paint;
    }
}
