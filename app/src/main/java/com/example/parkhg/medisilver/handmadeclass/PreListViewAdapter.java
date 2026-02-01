package com.example.parkhg.medisilver.handmadeclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.parkhg.medisilver.R;

import java.util.ArrayList;

/**
 * Created by w on 2017-07-22.
 */

public class PreListViewAdapter extends BaseAdapter{
    Context maincon;
    LayoutInflater inflater;
    ArrayList<PreListViewItem> arSrc;
    int layout;

    public PreListViewAdapter(Context context, int alayout, ArrayList<PreListViewItem> aarSrc)
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
    public String getItem(int position) {
        return arSrc.get(position).getTextMedicine();
    }
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }
        TextView tx = (TextView)convertView.findViewById(R.id.textMedicine);
        tx.setText(arSrc.get(position).getTextMedicine());

        TextView tx2 = (TextView)convertView.findViewById(R.id.textMedicine2);
        tx2.setText(arSrc.get(position).getTextMedicine2());

        TextView tx3 = (TextView)convertView.findViewById(R.id.textDisease);
        tx3.setText(arSrc.get(position).getTextDisease());

        TextView tx4 = (TextView)convertView.findViewById(R.id.textPeriod);
        tx4.setText(arSrc.get(position).getTextPeriod());

        ToggleButton toggleButton = (ToggleButton)convertView.findViewById(R.id.toggleOnOff);
        toggleButton.setOnClickListener(new ToggleButton.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
}
