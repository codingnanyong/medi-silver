package com.example.parkhg.medisilver.handmadeclass;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parkhg.medisilver.R;
import com.example.parkhg.medisilver.activity.MainActivity;
import com.example.parkhg.medisilver.database.InDBAdapter;
import com.example.parkhg.medisilver.database.InnerDataBase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by w on 2017-07-22.
 */

public class PrescriptionAdapter extends BaseAdapter
{
    private InnerDataBase indb;
    private Date currentDate;
    private String[] items = {"복용 중","복용 중단","상담 요망","취소"};
    Context maincon;
    LayoutInflater inflater;
    ArrayList<PrescriptionListItem> arSrc;
    int layout;
    public PrescriptionAdapter(Context context, int alayout, ArrayList<PrescriptionListItem> aarSrc)
    {
        maincon = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arSrc = aarSrc;
        layout = alayout ;
    }

    @Override
    public int getCount() {
        return arSrc.size();
    }
    public String getItem(int position)
    {
        return arSrc.get(position).getTextHospital();
    }
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }
        ImageView img = (ImageView)convertView.findViewById(R.id.imageColor);
        img.setBackgroundColor(arSrc.get(position).getColor());

        TextView tx = (TextView)convertView.findViewById(R.id.textHospital);
        tx.setText(arSrc.get(position).getTextHospital());

        TextView tx2 = (TextView)convertView.findViewById(R.id.textDate);
        tx2.setText(arSrc.get(position).getTextDate());

        final Button btn = (Button)convertView.findViewById(R.id.btnstate);

        btn.setText(arSrc.get(position).getState());
        btn.setBackgroundResource(R.drawable.btn_selector);
        btn.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(maincon)
                        .setTitle("복용 상태 변경")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,final int which) {
                                if(items[which].equals("취소"))
                                {

                                }
                                else
                                {
                                    new AlertDialog.Builder(maincon)
                                            .setTitle("알림")
                                            .setMessage("복용상태를 "+ items[which] +"로 변경 시 보호자에게 알림이 갑니다.\n변경하시겠습니까??")
                                            .setCancelable(false)
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which1) {
                                                    SimpleDateFormat sdf = new SimpleDateFormat("yy년 MM월 dd일 E요일 a hh:mm");
                                                    SMS sms = new SMS((Activity) maincon);
                                                    currentDate = new Date(System.currentTimeMillis());
                                                    /*sms.SendSMS(arSrc.get(position).getUser().getProtectPhone(),"[메디실버 알림]\n"+arSrc.get(position).getUser().getName()+
                                                            "환자 분이 "+sdf.format(currentDate)+"에 복용상태를 "+ items[which] +"로 변경하였습니다.");*/
                                                    InDBAdapter indbadter = new InDBAdapter(arSrc.get(position).getCertifyId());
                                                    indb = new InnerDataBase(maincon, "certifytable.db", null, 1);
                                                    indb.update(indbadter.updateDoseStateQuery(items[which]));
                                                    indb.close();
                                                    btn.setText(items[which]);
                                                }
                                            })
                                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .show();
                                }
                            }
                        })
                        .show();
            }
        });
        return convertView;
    }
}
