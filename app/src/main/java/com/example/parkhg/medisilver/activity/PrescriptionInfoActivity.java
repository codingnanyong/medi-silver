package com.example.parkhg.medisilver.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.parkhg.medisilver.R;
import com.example.parkhg.medisilver.handmadeclass.PreListViewAdapter;
import com.example.parkhg.medisilver.handmadeclass.PreListViewItem;

import java.util.ArrayList;

public class PrescriptionInfoActivity extends AppCompatActivity {
    private Intent intent;
    private ArrayList<String> certify = null;
    private ArrayList<String> hospital = null;
    private ArrayList<ArrayList<String>> medicine = null;
    private ArrayList<ArrayList<String>> takingcheck = null;
    private TextView tx,tx2,tx3;
    private ImageView iv;
    private ArrayList<PreListViewItem> arItem;
    private PreListViewItem pl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_info);

        intent = getIntent();
        certify = (ArrayList<String>)intent.getSerializableExtra("Certify");
        hospital = (ArrayList<String>)intent.getSerializableExtra("Hospital");
        medicine = (ArrayList<ArrayList<String>>)intent.getSerializableExtra("Medicine");
        takingcheck = (ArrayList<ArrayList<String>>)intent.getSerializableExtra("Takingcheck");
        iv = (ImageView)findViewById(R.id.imageColor); iv.setBackgroundColor(intent.getIntExtra("Color",0));
        tx = (TextView)findViewById(R.id.textHospital); tx.setText(intent.getStringExtra("HospitalName"));
        tx2 = (TextView)findViewById(R.id.textDate); tx2.setText(intent.getStringExtra("Date"));
        tx3 = (TextView)findViewById(R.id.textState); tx3.setText(intent.getStringExtra("State"));

        arItem = new ArrayList<PreListViewItem>();
        int i = 0;
        for(ArrayList<String> first : medicine)
        {
            pl = new PreListViewItem(first.get(2),"","",takingcheck.get(0).get(2)+"\n~"+takingcheck.get(medicine.size()-1).get(2));
            arItem.add(pl);
            i++;
        }
        PreListViewAdapter adapter = new PreListViewAdapter(this,R.layout.listview_presc_item,arItem);
        ListView List;
        List = (ListView)findViewById(R.id.list);
        List.setAdapter(adapter);
        List.setOnItemClickListener(ItemClickListener);
    }
    private AdapterView.OnItemClickListener ItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            intent = new Intent(getBaseContext(),DetailActivity.class);
            intent.putExtra("Medicine",medicine.get(position));
            intent.putExtra("Takingcheck",takingcheck);
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
