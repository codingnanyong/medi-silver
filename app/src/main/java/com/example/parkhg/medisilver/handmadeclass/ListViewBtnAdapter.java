package com.example.parkhg.medisilver.handmadeclass;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.parkhg.medisilver.R;
import com.example.parkhg.medisilver.activity.MainActivity;
import com.example.parkhg.medisilver.activity.QRActivity;
import com.example.parkhg.medisilver.database.InDBAdapter;
import com.example.parkhg.medisilver.database.InnerDataBase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static java.security.AccessController.getContext;

/**
 * Created by ParkHG on 2017-06-19.
 */

public class ListViewBtnAdapter extends BaseAdapter {
    // 생성자로부터 전달된 resource id 값을 저장.
    int resourceId ;
    private InnerDataBase indb;
    Context maincon;
    LayoutInflater inflater;
    ArrayList<ListViewItems> arSrc;
    Date currentDate = new Date(System.currentTimeMillis());
    // 생성자로부터 전달된 ListBtnClickListener  저장.

    // ListViewBtnAdapter 생성자. 마지막에 ListBtnClickListener 추가.
    public ListViewBtnAdapter(Context context, int resource, ArrayList<ListViewItems> list) {
        maincon = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arSrc = list;
        resourceId = resource ;
    }
    public int getCount()
    {
        return arSrc.size();
    }
    public String getItem(int position)
    {
        return arSrc.get(position).getText1();
    }
    public long getItemId(int position)
    {
        return position;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final int pos = position;
        if (convertView == null) {
            convertView = inflater.inflate(resourceId, parent, false);
        }
        // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
        final ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1);
        final TextView textTextView1 = (TextView) convertView.findViewById(R.id.textName);
        final TextView textTextView2 = (TextView) convertView.findViewById(R.id.textDate);
        final Switch slidebutton = (Switch) convertView.findViewById(R.id.btnDose);

        View linear = inflater.inflate(R.layout.activity_main,null);

        final ToggleButton tb1 = (ToggleButton)linear.findViewById(R.id.toggleButton);
        final ToggleButton tb2 = (ToggleButton)linear.findViewById(R.id.toggleButton2);
        final ToggleButton tb3 = (ToggleButton)linear.findViewById(R.id.toggleButton3);
        slidebutton.setChecked(arSrc.get(position).isButtonItem());
        SimpleDateFormat trans = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setBackgroundColor(arSrc.get(position).getIcon());
        textTextView1.setText(arSrc.get(position).getText1());
        textTextView2.setText(trans.format(arSrc.get(position).getDate()));

        // button1 클릭 시 TextView(textView1)의 내용 변경.
        slidebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slidebutton.isChecked()==true)
                {
                    System.out.println("======================ListViewBtnAdapter.button1.onClick===================");
                    String eattime=arSrc.get(position).getToggle();
                    System.out.println("eatTime : "+eattime);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.KOREAN);
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTime(arSrc.get(position).getDate());
                    cal.add(cal.DATE, arSrc.get(position).getTreatment().takedays);
                    if(arSrc.get(position).getTreatment().hospital_name.toString().equals(textTextView1.getText().toString()) &&
                            (sdf.format(arSrc.get(position).getTreatment().regist_day).equals(textTextView2.getText().toString())) &&
                            (arSrc.get(position).getTreatment().regist_day.getTime()<=currentDate.getTime() && currentDate.getTime()<= cal.getTime().getTime())) {
                        try {
                            slidebutton.setClickable(false);
                            InDBAdapter indba = new InDBAdapter(arSrc.get(position).getTreatment());
                            indb = new InnerDataBase(maincon, "takingchecktable.db", null, 1);
                            indb.update(indba.updateTakingcheckQeury(eattime, true));
                            indb.close();
                            new AlertDialog.Builder(maincon)
                                    .setTitle("알림")
                                    .setMessage("복용이 완료되었습니다.\n복용 간 특이사항이 있으셨습니까?")
                                    .setCancelable(false)
                                    .setPositiveButton("특이사항 없음", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SimpleDateFormat sdf = new SimpleDateFormat("yy년 MM월 dd일 E요일 a hh:mm");
                                            SMS sms = new SMS((Activity) maincon);
                                            sms.SendSMS(arSrc.get(position).getUserInfo().getProtectPhone(),"[메디실버 알림]\n"+arSrc.get(position).getUserInfo().getName()+"환자 분이 "+sdf.format(currentDate)+"에 약을 복용하셨습니다");
                                        }
                                    })
                                    .setNegativeButton("의사와 상담 요망", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        slidebutton.setChecked(false);
                        Toast.makeText(maincon,"약을 먹는 날짜가 아닙니다. 날짜확인 부탁드립니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    slidebutton.setChecked(true);
                }
            }
        });
        return convertView;
    }
}
