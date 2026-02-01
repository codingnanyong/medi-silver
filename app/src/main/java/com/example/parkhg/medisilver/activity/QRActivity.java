package com.example.parkhg.medisilver.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.JsonReader;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkhg.medisilver.R;
import com.example.parkhg.medisilver.database.GetMedicineInfo;
import com.example.parkhg.medisilver.handmadeclass.Treatment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * Created by ParkHG on 2017-06-22.
 */

public class QRActivity extends Activity {
    private TextView view_certify, view_hid,view_hname, view_regist, view_takeday, view_depart, view_medicines_count;
    private IntentIntegrator qrScan;
    private LinearLayout layout;
    private GetMedicineInfo task;
    private Intent intent;

    private Treatment treatment;
    private ArrayList<ArrayList<String>> medicinesinfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        view_certify = (TextView) findViewById(R.id.textViewCertifyId);
        view_hid = (TextView)findViewById(R.id.textViewHospitalId);
        view_hname = (TextView)findViewById(R.id.textViewHospitalName);
        view_regist = (TextView)findViewById(R.id.textViewRegistDay);
        view_takeday = (TextView)findViewById(R.id.textViewTakeDay);
        view_depart = (TextView)findViewById(R.id.textViewDepartment);
        view_medicines_count = (TextView)findViewById(R.id.textViewMedicinesCount);

        medicinesinfo = new ArrayList<ArrayList<String>>();
        intent = getIntent();

        //Qr 코드 카메라 띄우기
        qrScan = new IntentIntegrator(this);
        qrScan.setPrompt("");
        qrScan.setOrientationLocked(false);
        qrScan.initiateScan();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.buttonCancle:
                    setResult(RESULT_CANCELED);
                    finish();
                    break;
                case R.id.buttonSave:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("treatment", treatment);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK,intent);
                    finish();
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("QRActivity - onActivityResult");
        try {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            Random random = new Random();
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(QRActivity.this, "취소!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    //qrcode 결과가 있으면
                    Toast.makeText(QRActivity.this, "스캔완료!", Toast.LENGTH_SHORT).show();
                    try {
                        //data를 json으로 변환
                        new AlertDialog.Builder(this)
                                .setTitle("QR 정보 출력")
                                .setMessage(result.getContents())
                                .setCancelable(false)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .show();
                        JSONObject obj = new JSONObject(result.getContents());
                        view_certify.setText(obj.getString("certify"));
                        view_hid.setText(obj.getString("hid"));
                        view_hname.setText(obj.getString("hname"));
                        view_regist.setText(obj.getString("registday"));
                        view_takeday.setText(obj.getString("takeday"));
                        view_depart.setText(obj.getString("department"));
                        view_medicines_count.setText(obj.getString("midcount"));

                        int count = Integer.parseInt(view_medicines_count.getText().toString());
                        createMedicinesTextViews(count, obj);

                        boolean eatcheck[][] = new boolean[obj.getInt("takeday")][3];
                        System.out.println("takeday : "+obj.getInt("takeday"));
                        for(int i = 0; i<obj.getInt("takeday"); i++)
                        {
                            for(int j = 0; j<3; j++)
                            {
                                eatcheck[i][j] = false;
                            }
                        }
                        System.out.println("view_regist.getText().toString() value - "+ view_regist.getText().toString());
                        treatment = new Treatment(view_certify.getText().toString(), view_hid.getText().toString(), view_hname.getText().toString()
                                ,view_regist.getText().toString(), Integer.parseInt(view_takeday.getText().toString())
                                , view_depart.getText().toString(), Integer.parseInt(view_medicines_count.getText().toString()), medicinesinfo, Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256)), eatcheck);
                        System.out.println(treatment.getInfo().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(QRActivity.this, "서버 Warning!", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    setResult(1, data);
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void createMedicinesTextViews(int in_count, JSONObject input) throws JSONException, Exception{
        layout = (LinearLayout)findViewById(R.id.LinearLayoutTextViews);

        //서버와 연결
        task = new GetMedicineInfo();
        String sum_mid  = new String();

        for(int i=0; i<in_count; i++){
            ArrayList<String> medicine = new ArrayList<String>();

            TextView medinum = new TextView(QRActivity.this);
            medinum.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            medinum.setGravity(Gravity.CENTER);
            medinum.setText("medicine"+(i+1));

            TextView mediidenty = new TextView(QRActivity.this);
            mediidenty.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            mediidenty.setGravity(Gravity.CENTER);
            String mid = input.getString(new String("mid"+(i+1)));  //약 품목 번호 가져오기
            medicine.add(mid);      //약 id를 저장
            mediidenty.setText(mid/*+" : "+result*/);
            sum_mid += mid;
            if(i < (in_count-1)) sum_mid += "/";
            //mediidenty.setTextAppearance(this, style.TextAppearance_AppCompat_Large);

            layout.addView(medinum);
            layout.addView(mediidenty);
            medicinesinfo.add(medicine);
        }

        String result = task.execute(sum_mid).get();    //데이터베이스에 저장되어있는 데이터 불러오기.
        if (task.getStatus() == GetMedicineInfo.Status.RUNNING) { task.cancel(true); }
        System.out.println("======================================================================");
        System.out.println(result);

        //믿지마 나중에 파싱 공부하면 다시 건드려 볼것.
        String str1[] = result.split("<body>");
        String str2[] = str1[1].split("</body>");
        String str3[] = str2[0].split("#");
        for(int i = 1; i <= in_count; i++) {
            String[] info = str3[i].split("/");
            for (int j = 0; j < 3; j++) {
                TextView resultViews = new TextView(QRActivity.this);
                resultViews.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                medicinesinfo.get(i - 1).add(info[j]);
                resultViews.setText(info[j]);
                layout.addView(resultViews);
            }
        }
    }
}
