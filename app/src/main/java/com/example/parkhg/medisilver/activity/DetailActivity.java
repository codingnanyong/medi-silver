package com.example.parkhg.medisilver.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.parkhg.medisilver.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private DonutProgress donutProgress;
    private Intent intent;
    private ArrayList<String> medicine = null;
    private ArrayList<ArrayList<String>> takingcheck = null;
    private TextView textmedicine,textmedicine2,textdisease,textinfo,textdosing;
    private ToggleButton toggleButton;
    private float truecnt = 0,totalcnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        intent = getIntent();
        medicine = (ArrayList<String>) intent.getSerializableExtra("Medicine");
        takingcheck = (ArrayList<ArrayList<String>>)intent.getSerializableExtra("Takingcheck");

        textmedicine = (TextView)findViewById(R.id.textMedicine); textmedicine.setText(medicine.get(2));
        textmedicine2 = (TextView)findViewById(R.id.textMedicine2);
        textdisease = (TextView)findViewById(R.id.textDisease); textdisease.setText(takingcheck.get(0).get(2)+"\n~"+takingcheck.get(takingcheck.size()-1).get(2));
        textdosing = (TextView)findViewById(R.id.textdosing);
        textinfo = (TextView)findViewById(R.id.MedicineInfo); textinfo.setText(medicine.get(3));
        toggleButton = (ToggleButton)findViewById(R.id.toggleOnOff);
        donutProgress = (DonutProgress)findViewById(R.id.donut_progress);

        for(int i = 0; i<takingcheck.size(); i++)
        {
            for(int j = 3; j<takingcheck.get(i).size(); j++)
            {
                if(takingcheck.get(i).get(j).equals("1"))
                {
                    truecnt++;
                }
                totalcnt++;
            }
        }
        donutProgress.setUnfinishedStrokeColor(Color.WHITE);
        donutProgress.setFinishedStrokeColor(Color.GREEN);
        donutProgress.setTextColor(Color.BLACK);
        donutProgress.setText((int)truecnt+"회/"+(int)totalcnt+"회");
        donutProgress.setProgress((truecnt/totalcnt)*100);
        textdosing.setText((int)donutProgress.getProgress()+"%\n복용완료");
        System.out.println("per : "+(truecnt/totalcnt));
        System.out.println("persent : "+(truecnt/totalcnt)*100);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.custom_actionbar, null);
        ImageButton imageButton = (ImageButton)actionbar.findViewById(R.id.btnmenu);
        imageButton.setImageResource(R.drawable.previous);
        TextView tx = (TextView)actionbar.findViewById(R.id.title);
        tx.setText("처방전 관리");

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        return true;
    }
}
