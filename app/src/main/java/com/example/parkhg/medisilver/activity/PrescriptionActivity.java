package com.example.parkhg.medisilver.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkhg.medisilver.R;
import com.example.parkhg.medisilver.handmadeclass.PrescriptionAdapter;
import com.example.parkhg.medisilver.handmadeclass.PrescriptionListItem;
import com.example.parkhg.medisilver.handmadeclass.UserInfo;

import java.util.ArrayList;

public class PrescriptionActivity extends AppCompatActivity {
    private ArrayList<ArrayList<String>> certify = null;
    private ArrayList<ArrayList<String>> hospital = null;
    private ArrayList<ArrayList<ArrayList<String>>> medicine = null;
    private ArrayList<ArrayList<ArrayList<String>>> takingcheck = null;
    private ArrayList<ArrayList<String>> mediitems = null;
    private ArrayList<String> mediitem = null;
    private UserInfo userInfo;
    private Intent intent;
    private Button btn;
    private ArrayList<PrescriptionListItem> arItem;
    private TextView tx,tx1;
    private PrescriptionListItem pl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        intent = getIntent();
        tx = (TextView)findViewById(R.id.Textname); tx.setText(intent.getStringExtra("Name"));
        tx1 = (TextView)findViewById(R.id.Textage); tx1.setText("("+intent.getIntExtra("Age",0)+"세)");
        userInfo = (UserInfo)intent.getSerializableExtra("User") ;
        certify = (ArrayList<ArrayList<String>>)intent.getSerializableExtra("Certify");
        hospital = (ArrayList<ArrayList<String>>)intent.getSerializableExtra("Hospital");
        medicine = (ArrayList<ArrayList<ArrayList<String>>>)intent.getSerializableExtra("Medicine");
        mediitems = (ArrayList<ArrayList<String>>)intent.getSerializableExtra("Mediitems");
        takingcheck = (ArrayList<ArrayList<ArrayList<String>>>)intent.getSerializableExtra("Takingcheck");

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        RelativeLayout rel = (RelativeLayout)inflater.inflate(R.layout.listview_text_item,null);

        arItem = new ArrayList<PrescriptionListItem>();
        int i = 0;
        for (ArrayList<String> first : certify) {
            String state = null;
            if(first.get(8).equals("dosing"))
            {
                state = "복용 중";
            }
            else if(first.get(8).equals("notdose"))
            {
                state = "복용 중단";
            }
            else if(first.get(8).equals("counseling"))
            {
                state = "상담 요망";
            }
            pl = new PrescriptionListItem(first.get(1),Integer.parseInt(first.get(5)), hospital.get(i).get(2), " ("+first.get(6)+")", state,userInfo);
            arItem.add(pl);
            i++;
        }

        PrescriptionAdapter adapter = new PrescriptionAdapter(this,R.layout.listview_text_item,arItem);

        ListView List;
        List = (ListView)findViewById(R.id.list);
        List.setAdapter(adapter);
        List.setOnItemClickListener(ItemClickListener);

        btn = (Button)rel.findViewById(R.id.btnstate);
    }
    private AdapterView.OnItemClickListener ItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            intent = new Intent(getBaseContext(),PrescriptionInfoActivity.class);
            intent.putExtra("Certify",certify.get(position));
            intent.putExtra("Hospital",hospital.get(position));
            intent.putExtra("Medicine",medicine.get(position));
            intent.putExtra("Takingcheck",takingcheck.get(position));
            intent.putExtra("Color",arItem.get(position).getColor());
            intent.putExtra("HospitalName",arItem.get(position).getTextHospital());
            intent.putExtra("Date",arItem.get(position).getTextDate());
            intent.putExtra("State",arItem.get(position).getState());
            startActivity(intent);
        }
    };
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
                intent = new Intent();
                setResult(RESULT_OK,intent);
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