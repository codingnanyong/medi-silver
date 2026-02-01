package com.example.parkhg.medisilver.database;

import android.icu.text.SimpleDateFormat;
import android.text.TextUtils;
import android.text.format.DateFormat;

import com.example.parkhg.medisilver.handmadeclass.Treatment;
import com.example.parkhg.medisilver.handmadeclass.UserInfo;

import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by ParkHG on 2017-06-22.
 */

public class InDBAdapter {
    Treatment treatment;
    String CertifyId;
    Date currentDate = new Date(System.currentTimeMillis());

    public InDBAdapter() {}

    public InDBAdapter(Treatment t){
        treatment = t;
    }

    public InDBAdapter(String Certify) { CertifyId = Certify; }

    public String insertCertifyQuery(){
        String query = "";
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        query +="insert into certifytable values(1,'"+ treatment.certificate_id + "','"
                + treatment.hospital_id +"', '"+ treatment.regist_day+"', '"+treatment.takedays +"', '"+treatment.color+"', '"+format.format(treatment.regist_day)+"', "
                + treatment.medicinescount+", '"+ treatment.dosestate +"');";
        return query;
    }

    public String updateDoseStateQuery(String input){
        String query = "";
        String data = "";

        if(input.equals("복용 중"))  data = "dosing";
        else if(input.equals("복용 중단")) data = "notdose";
        else if(input.equals("상담 요망")) data = "counseling";

        query = "update certifytable set dosestate = '" + data + "' where certifyid = '"+ CertifyId +"'";

        return query;
    }

    public String insertHospitalQuery(){
        String query = "";

        query +="insert into hospitaltable values('"+ treatment.hospital_id + "','"
                + treatment.certificate_id +"', '"+ treatment.hospital_name+"','"+treatment.department +"');";

        return query;
    }

    public String[] insertMedicineQuery(){
        String query[] = new String[treatment.medicines.size()];
        int i = 0;
        for( ArrayList<String> strarr : treatment.medicines) {
            query[i] = "insert into medicinetable values('" + treatment.certificate_id + "','"
                    + strarr.get(0) + "', '" + strarr.get(1) + "','"
                    + strarr.get(2) + "','" + strarr.get(3) + "');";
            i++;
        }
        //System.out.println("medicineQuery : "+query);
        return query;
    }


     public String[] insertTakingcheckQeury() // 서버와의 통신이 완성되면 구현하기.
     {
         String query[] = new String[treatment.takedays];
         GregorianCalendar cal = new GregorianCalendar();
         Date day = treatment.regist_day;

         for( ArrayList<String> strarr : treatment.medicines) {
             cal.setTime(day);
             for(int i=0; i<treatment.takedays; i++) {
                 query[i] = ("insert into takingchecktable values('" + treatment.certificate_id + "','"
                         + strarr.get(0) + "', '" + DateFormat.format("yyyy-MM-dd",cal.getTime()) + "',0,0,0);");
                 cal.add(cal.DATE, 1);
             }
         }
         System.out.println("InAdapter = insert Taking Check Qeury Value: ");
         System.out.println(query);
         return query;
     }


    public static  String insertUserinfoQuery(UserInfo info){
        String query = "";
        query = "insert into userinfo values('"+ info.getName()+"', '"+ info.getStrBirth()+"', '"+ info.getProtectPhone()+"', "+ (int)info.getHeight()+", "+ (int)info.getWeight()+
                ", "+ (int)info.getLiver()+", "+(info.isDiabetes() ? 1: 0)+", '"+info.getBloodpressure()+"', "+ info.getCertifycount()+");";

        return query;
    }

    //사용할때 주의 treatment 객체는 삭제 하지 않으므로 db와 정보 차이가 날수있음.
    public String deleteQuery(String certifyid){
        String query = "";

        query += "delete from takingchecktable where certifyid = '"+treatment.certificate_id+"';";
        query += "delete from medicinetable where certifyid = '"+treatment.certificate_id+"';";
        query += "delete from hospitaltable where certifyid = '"+treatment.certificate_id+"';";
        query += "delete from certifytable where certifyid = '"+ treatment.certificate_id +"';";

        return query;
    }

    public String[] InitTable()
    {
        String query[] = new String[4];

        query[0] = "truncate table certifytable;";
        query[1] = "truncate table hospitaltable;";
        query[2] = "truncate table medicinetable;";
        query[3] = "truncate table takingchecktable;";

        System.out.println(query);

        return query;
    }

    public String updateTakingcheckQeury(String input, boolean flag){
        String query = "";

        if(input.equals("아침"))  query = "moningcheck";
        else if(input.equals("점심")) query = "lunchcheck";
        else if(input.equals("저녁")) query = "eveningcheck";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);

        query = "update takingchecktable set "+ query + " = " + (flag ? 1 : 0) + " where certifyid = '" + treatment.certificate_id + "' and day = '" + sdf.format(currentDate)+"'";
        return query;
    }
}
