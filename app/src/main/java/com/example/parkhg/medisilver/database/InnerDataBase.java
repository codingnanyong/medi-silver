package com.example.parkhg.medisilver.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.parkhg.medisilver.handmadeclass.ListViewItems;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ParkHG on 2017-06-22.
 */

public class InnerDataBase extends SQLiteOpenHelper {
    SQLiteDatabase database;
    ListViewItems items;
    public InnerDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version); 

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        database = db;
        database.execSQL("CREATE TABLE certifytable (" +
                "  `activityflag` TINYINT NULL," +          // 진단서 완료 여부. true - 복용 중, false - 복용완료 진단서.
                "  `certifyid` VARCHAR(80) NOT NULL," +     // 진단서 id
                "  `hospitalid` VARCHAR(100) NULL," +       // 병원 id
                "  `registday` DATE NULL," +                // 진단서 받은 일자. -> String형으로 바꾸되. 시간역시 저장하능하도록 형식을 만들어서 바꾸기. 나중에.
                "  `takedays` VARCHAR(45) NULL," +          // 복용 기간
                "  'certifycolor' INT NULL," +              // 진단서 색깔
                "  'startday' VARCHAR(20) NULL," +          // 진단서 받은 일자 String
                "  'medicinecount' INT NULL," +             // 약 갯수.
                "  'dosestate' VARCHAR(10)," +              // 복용 상태
                "  PRIMARY KEY (`certifyid`));");

        database.execSQL("CREATE TABLE hospitaltable (" +
                "  `hospitalid` VARCHAR(100) NULL," +       // 병원 id
                "  `certifyid` VARCHAR(80) NOT NULL," +     // 진단서 id
                "  `hos_name` VARCHAR(20) NULL," +          // 병원 이름
                "  `department` VARCHAR(80) NULL," +        // 병원에 있는 '과'이름
                "  PRIMARY KEY (`certifyid`));");

        database.execSQL("CREATE TABLE medicinetable (" +
                "  `certifyid` VARCHAR(80) NOT NULL," +             // 진단서 id
                "  `medicineid` VARCHAR(100) NULL," +               // 약 id
                "  `medicinename` VARCHAR(300) NULL," +             // 약 이름
                "  `medicinename_sum` VARCHAR(300) NULL," +         // 약 별명
                "  `medicininfo` VARCHAR(1000) NULL);");            // 약 설명

        database.execSQL("CREATE TABLE takingchecktable (" +
                "  `certifyid` VARCHAR(80) NOT NULL," +             //진단서 id
                "  `medicineid` VARCHAR(100) NULL," +               //약 id
                "  `day` DATE NULL," +                              //약 복용 일자. ex) 3일 복용>> 2017-07-25, 2017-07-26, 2017-07-27.... 이런식으로 저장.
                "  `moningcheck` TINYINT NULL," +                   // 아침 시간 복용 여부
                "  `lunchcheck` TINYINT NULL," +                    // 점심 시간 복용 여부
                "  `eveningcheck` TINYINT NULL);");                 // 저녁 시간 복용 여부

        database.execSQL("CREATE TABLE userinfo (" +
                "  `name` VARCHAR(20) NOT NULL," +
                "  `birthday` DATE NULL," +
                "  `protectorphone` VARCHAR(45) NULL," +
                "  `height` INT NULL," +
                "  `weight` INT NULL," +
                "  `liver` INT NULL," +
                "  `diabetes` TINYINT NULL," +
                "  `bloodpressure` VARCHAR(2) NULL," +
                "  `certifycount` INT NULL);");
    }

    public boolean insert(String query) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db = getWritableDatabase();
            db.execSQL(query);
        }catch (Exception e){
            db.close();
            e.printStackTrace();
            return false;
        }
        db.close();
        return true;
    }
    public boolean insert(String[] query) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db = getWritableDatabase();
            for(int i = 0; i<query.length; i++)
            {
                db.execSQL(query[i]);
            }
        }catch (Exception e){
            db.close();
            e.printStackTrace();
            return false;
        }
        db.close();
        return true;
    }

    public boolean update(String query){
        SQLiteDatabase db = getWritableDatabase();
        try {
            db = getWritableDatabase();
            db.execSQL(query);
        }catch (Exception e){
            db.close();
            e.printStackTrace();
            return false;
        }
        db.close();

        return true;
    }
    public boolean delete(String query){
        SQLiteDatabase db = getWritableDatabase();
        try {
            db = getWritableDatabase();
            db.execSQL(query);
        }catch (Exception e){
            db.close();
            e.printStackTrace();
            return false;
        }
        db.close();
        return true;
    }
    public boolean isEmpty(String query)
    {
        SQLiteDatabase db = getReadableDatabase();
        boolean result = false;

        try
        {
            Cursor cs = db.rawQuery(query,null);
            if(cs.moveToFirst())
            {
                result = false;
                return result;
            }
        }
        catch (Exception e)
        {
            result = true;
            db.close();
            e.printStackTrace();
            return result;
        }
        db.close();
        return result;
    }
    public String getResult(String query){
        SQLiteDatabase db = getWritableDatabase();
        String result = "";

        try {
            db= getReadableDatabase();

            Cursor cs = db.rawQuery(query,null);
            while(cs.moveToNext()){
                for(int i= 0; i<cs.getColumnCount(); i++){
                    result += (cs.getString(i)+"&");
                }
                result += "#";
            }
        }catch (Exception e){
            db.close();
            e.printStackTrace();
            return "";
        }
        db.close();

        return result;
    }

    public boolean dropTables(){
        try{
            database = getWritableDatabase();
            database.execSQL("drop table "+super.getDatabaseName()+";");
        }
        catch (Exception e){
            database.close();
            e.printStackTrace();
            return false;
        }
        database.close();
        return true;
    }
}
