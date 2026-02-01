package com.example.parkhg.medisilver.activity;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.parkhg.medisilver.R;
import com.example.parkhg.medisilver.database.InDBAdapter;
import com.example.parkhg.medisilver.database.InnerDataBase;
import com.example.parkhg.medisilver.handmadeclass.Data;
import com.example.parkhg.medisilver.handmadeclass.ListViewBtnAdapter;
import com.example.parkhg.medisilver.handmadeclass.ListViewItems;
import com.example.parkhg.medisilver.handmadeclass.SMS;
import com.example.parkhg.medisilver.handmadeclass.Treatment;
import com.example.parkhg.medisilver.handmadeclass.UserInfo;
import com.hci.mcalendar.EventHandler.MCalendarEventHandler;
import com.hci.mcalendar.FrgCalendar;
import com.hci.mcalendar.MCalendar;
import com.hci.mcalendar.MCalendarView;
import com.hci.mcalendar.adapter.AdapterFrgCalendar;
import com.hci.mcalendar.adapter.AdapterRcvSimple;
import com.hci.mcalendar.datas.DataObject;
import com.hci.mcalendar.interfaces.IInitializer;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.example.parkhg.medisilver.R.id.Main;
import static com.example.parkhg.medisilver.R.id.wrap_content;
import static java.lang.Math.toIntExact;

public class MainActivity extends AppCompatActivity implements IInitializer,FrgCalendar.OnFragmentListener,View.OnClickListener
{
    private UserInfo userInfo;
    private ToggleButton tb1, tb2, tb3;
    private Button addButton;
    private Intent intent;
    private ArrayList<Treatment> treatments;
    private InnerDataBase indb;
    private Date currentDate;
    private ArrayList<ListViewItems> items;
    private ListView listview;
    private ListViewBtnAdapter adapter;
    private MenuActivity CustomDialog;
    private static final int COUNT_PAGE = 12;
    private AdapterFrgCalendar fadapter;
    private ViewPager viewPager;
    private TextView curtitle;
    private long btnpresstime;
    private int age,day;

    private ArrayList<ArrayList<String>> certify = null;
    private ArrayList<ArrayList<String>> hospital = null;
    private ArrayList<ArrayList<ArrayList<String>>> medicine = null;
    private ArrayList<ArrayList<ArrayList<String>>> takingcheck = null;
    private ArrayList<ArrayList<String>> mediitems = null;
    private ArrayList<ArrayList<String>> takecheckitems = null;
    private ArrayList<String> mediitem = null;

    final static int AC_QR = 0;
    final static int AC_EDIT = 1;
    final static int AC_PRES = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String name = "";
        Date birth = null;
        String protect_phone = "";
        int h =0 , w= 0, l= 0;
        boolean diabetes =false;
        char bloodpressure ='n';
        int certifycount = 0;

        System.out.println("MainActivity :: onCreate - user load check!");
        intent = getIntent();
        if(intent.getIntExtra("checkuserflag",0)==1){
            System.out.println("MainActivity :: onCreate - user is coming from loadingActivity");
            indb = new InnerDataBase(getApplicationContext(), "userinfo.db", null, 1);

            String result = indb.getResult("select * from userinfo;");
            System.out.println(result);
            String[] fsplt = result.split("%");
            System.out.println("fsplt: ");
            System.out.println(Arrays.toString(fsplt));


            String[] ssplt = fsplt[0].split("&");
            System.out.println("\nssplt: ");
            System.out.println(Arrays.toString(ssplt));
            System.out.println("\n");

            name = ssplt[0];
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy");
                birth = format.parse(ssplt[1]);
            }catch (Exception e){
                e.printStackTrace();
            }
            protect_phone = ssplt[2];
            h = Integer.parseInt(ssplt[3]);
            w = Integer.parseInt(ssplt[4]);
            l = Integer.parseInt(ssplt[5]);
            Integer intinput = Integer.parseInt(ssplt[6]);
            diabetes = (intinput == 0 ? false: true);
            bloodpressure = ssplt[7].charAt(0);
            certifycount = Integer.parseInt(ssplt[8]);

            userInfo = new UserInfo(name, birth, protect_phone, h, w, l, diabetes, bloodpressure, certifycount);
        }
        else{
            System.out.println("MainActivity :: onCreate - user is coming from JoinActivity");
            userInfo = (UserInfo)intent.getSerializableExtra("userinfo");
        }
        currentDate = new Date(System.currentTimeMillis());

        userInfo.show();
        System.out.println("MainActivity :: onCreate - user load check - DONE!!");
        treatments = new ArrayList<Treatment>();
        layoutViewInits();
        if(currentDate.getHours() >= 6 && currentDate.getHours() < 12)
        {
            tb1.setChecked(true);
            tb2.setChecked(false);
            tb3.setChecked(false);
            tb1.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_green_border));
            tb2.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_gray_border));
            tb3.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_gray_border));
            setParam(4,1,1);
        }
        else if(currentDate.getHours() >= 12 && currentDate.getHours() < 18)
        {
            tb1.setChecked(false);
            tb2.setChecked(true);
            tb3.setChecked(false);
            tb1.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_gray_border));
            tb2.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_green_border));
            tb3.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_gray_border));
            setParam(1,4,1);
        }
        else if((currentDate.getHours() >= 18 && currentDate.getHours() < 24 )|| currentDate.getHours() < 6)
        {
            tb1.setChecked(false);
            tb2.setChecked(false);
            tb3.setChecked(true);
            tb1.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_gray_border));
            tb2.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_gray_border));
            tb3.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_green_border));
            setParam(1,1,4);
        }

        getUserTreatmentInfoFromInnerDB();

        //ListLoad(items);
        intent = new Intent();
    }

    @Override
    protected void onStart() { super.onStart(); }


    //메인 페이지의 병원 리스트 관련 함수.
    public boolean loadListItems(ArrayList<ListViewItems> list) {
        ListViewItems item ;
        boolean setButton;
        String getToggle;
        if (list == null) {  list = new ArrayList<ListViewItems>(); }

        if(treatments != null)
        {
            list.clear();
            for(Treatment treat: treatments) {
                day = (int)(currentDate.getTime() - treat.regist_day.getTime());
                System.out.println("reday : "+currentDate.getTime()+"/"+treat.regist_day.getTime());
                day = day / (24*60*60*1000);
                setButton = false;
                getToggle = "";
                System.out.println("dayday : "+day);
                System.out.println("isCheck : "+ -treat.takedays);
                if(day < treat.takedays && day>=0) {
                    if (tb1.isChecked()) {
                        setButton = treat.takingcheck[day][0];
                        getToggle = tb1.getText().toString();
                    }
                    if (tb2.isChecked()) {
                        setButton = treat.takingcheck[day][1];
                        getToggle = tb2.getText().toString();
                    }
                    if (tb3.isChecked()) {
                        setButton = treat.takingcheck[day][2];
                        getToggle = tb3.getText().toString();
                    }
                    for (int i = 0; i < day; i++) {
                        for (int j = 0; j < 3; j++) {
                            System.out.println("takingday : " + treat.takingcheck[i][j]);
                        }
                    }
                    item = new ListViewItems(treat.color, treat.hospital_name, treat.regist_day, setButton, treat, userInfo,getToggle);
                    list.add(item);
                    adapter.notifyDataSetChanged();
                }
                else if(day < 0) {
                    if (tb1.isChecked()) {
                        getToggle = tb1.getText().toString();
                    }
                    if (tb2.isChecked()) {
                        getToggle = tb2.getText().toString();
                    }
                    if (tb3.isChecked()) {
                        getToggle = tb3.getText().toString();
                    }
                    setButton = false;
                    for (int i = 0; i < day; i++) {
                        for (int j = 0; j < 3; j++) {
                            System.out.println("takingday : " + treat.takingcheck[i][j]);
                        }
                    }
                    item = new ListViewItems(treat.color, treat.hospital_name, treat.regist_day, setButton, treat, userInfo,getToggle);
                    list.add(item);
                    adapter.notifyDataSetChanged();
                }
                else
                {

                }
            }
        }
        return true ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.custom_actionbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        return true;
    }
    public int getAgeFromBirthday(Date birthday) {
        Calendar birth = new GregorianCalendar();
        Calendar today = new GregorianCalendar();

        birth.setTime(birthday);
        today.setTime(new Date());

        int factor = 1;
        if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            factor = -1;
        }
        return today.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + factor;
    }
    public void mOnClick(View v)
    {
        try {
            switch (v.getId()) {
                case R.id.btnmenu:
                    age = getAgeFromBirthday(userInfo.getBirth());
                    CustomDialog = new MenuActivity(this,userInfo.getName()+"("+age+"세)",upListener,centerListener,downListener);
                    CustomDialog.show();
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private View.OnClickListener upListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(getBaseContext(),HealthInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("userinfo", userInfo);
            intent.putExtras(bundle);
            startActivityForResult(intent,AC_EDIT);

        }
    };
    private View.OnClickListener centerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getUserTreatmentInfoFromInnerDB();
            if(certify != null) {
                try {
                    getDataInfoFromInnerDB("certifytable", "select * from certifytable where activityflag = 1;", certify);
                    intent = new Intent(getBaseContext(), PrescriptionActivity.class);
                    intent.putExtra("User",userInfo);
                    intent.putExtra("Name", userInfo.getName());
                    intent.putExtra("Age", age);
                    intent.putExtra("Certify", certify);
                    intent.putExtra("Hospital", hospital);
                    intent.putExtra("Medicine", medicine);
                    intent.putExtra("Mediitems", mediitems);
                    intent.putExtra("Takingcheck", takingcheck);
                    startActivityForResult(intent, AC_PRES);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(MainActivity.this,"처방전 정보가 없습니다.",Toast.LENGTH_SHORT).show();
            }
        }
    };
    private View.OnClickListener downListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InDBAdapter indbadter = new InDBAdapter();
            indb = new InnerDataBase(getApplicationContext(), "certifytable.db", null, 1);
            indb.insert(indbadter.InitTable());
            indb.close();
            treatments.clear();
            items.clear();
            adapter.notifyDataSetChanged();
            initialize();
        }
    };

    //아침, 점심, 저녁 및 메인 엑티비티에 존재하는 버튼을 관리하는 이벤트.
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId())
            {
                case R.id.toggleButton:
                    if(tb1.isChecked())
                    {
                        tb2.setChecked(false);
                        tb3.setChecked(false);
                        tb1.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_green_border));
                        tb2.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_gray_border));
                        tb3.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_gray_border));
                        setParam(3,1,1);
                    }
                    tb1.setChecked(true);
                    break;
                case R.id.toggleButton2:
                    if(tb2.isChecked())
                    {
                        tb1.setChecked(false);
                        tb3.setChecked(false);
                        tb1.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_gray_border));
                        tb2.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_green_border));
                        tb3.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_gray_border));
                        setParam(1,3,1);
                    }
                    tb2.setChecked(true);
                    break;
                case R.id.toggleButton3:
                    if(tb3.isChecked())
                    {
                        tb1.setChecked(false);
                        tb2.setChecked(false);
                        tb1.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_gray_border));
                        tb2.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_gray_border));
                        tb3.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_green_border));
                        setParam(1,1,3);
                    }
                    tb3.setChecked(true);
                    break;
                case R.id.buttonAdd:
                    intent = new Intent(this, QRActivity.class);
                    intent.putExtra("SCAN_MODE", "ALL");
                    startActivityForResult(intent, AC_QR);
                    break;

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    protected void layoutViewInits(){
        System.out.println("=========================layoutViewInits=================================");
        try {
            addButton = (Button) findViewById(R.id.buttonAdd);
            addButton.setOnClickListener(this);
            tb1 = (ToggleButton) findViewById(R.id.toggleButton);
            tb1.setOnClickListener(this);
            tb2 = (ToggleButton) findViewById(R.id.toggleButton2);
            tb2.setOnClickListener(this);
            tb3 = (ToggleButton) findViewById(R.id.toggleButton3);
            tb3.setOnClickListener(this);

            initialize();

            items = new ArrayList<ListViewItems>();
            // items 로드.
            loadListItems(items);

            // Adapter 생성
            adapter = new ListViewBtnAdapter(this, R.layout.listview_btn_item, items);

            // 리스트뷰 참조 및 Adapter달기
            listview = (ListView) findViewById(R.id.listView);
            listview.setAdapter(adapter);
        }
        catch (Exception e){
            System.out.println("===================Main Inits Error=========================");
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("MainActivity = onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        boolean a = true;
        try {
            switch (requestCode) {
                case AC_QR:
                    if(data != null || resultCode == RESULT_OK) {
                        Treatment trtmt = (Treatment) data.getSerializableExtra("treatment");

                        System.out.println("Qr 확인 버튼 클릭.");
                        System.out.println(trtmt.getInfo());
                        day = (int) (currentDate.getTime()-trtmt.regist_day.getTime());

                        day = day / (24*60*60*1000);

                        if(day>=trtmt.takedays)
                        {
                            Toast.makeText(MainActivity.this,"복용 기간이 지났습니다",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        System.out.println("takeday : "+trtmt.takedays);
                        System.out.println("day : "+day);
                        if(treatments.size()>0)
                        {
                            for (int i=0; i<treatments.size(); i++)
                            {
                                if((trtmt.getFirstDate() >= treatments.get(i).getFirstDate()&&trtmt.getLastDate() <= treatments.get(i).getLastDate())||
                                        (trtmt.getFirstDate() <= treatments.get(i).getLastDate()&&trtmt.getFirstDate() >= treatments.get(i).getFirstDate())||
                                        (trtmt.getLastDate() >= treatments.get(i).getFirstDate()&&trtmt.getLastDate() >= treatments.get(i).getLastDate()))
                                {
                                    trtmt.setIndex(treatments.get(i).getIndex()+1);
                                }
                            }
                        }
                        for (int i = 0; i < treatments.size(); i++)
                            if (trtmt.certificate_id.equals(treatments.get(i).certificate_id)) {
                                a = false;
                                Toast.makeText(MainActivity.this, "중복된 QR코드 입니다.", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        if (a)
                            treatments.add(trtmt);

                        initialize();
                        loadListItems(items);
                        //데이터 베이스에 바로 저장
                        InDBAdapter indbadter = new InDBAdapter(trtmt);
                        indb = new InnerDataBase(getApplicationContext(), "certifytable.db", null, 1);
                        indb.insert(indbadter.insertCertifyQuery());
                        indb.close();

                        indb = new InnerDataBase(getApplicationContext(), "hospitaltable.db", null, 1);
                        indb.insert(indbadter.insertHospitalQuery());
                        indb.close();

                        System.out.println(trtmt.getInfo());
                        indb = new InnerDataBase(getApplicationContext(), "medicinetable.db", null, 1);
                        indb.insert(indbadter.insertMedicineQuery());
                        System.out.println("==============="+trtmt.medicines);
                        indb.close();

                        indb = new InnerDataBase(getApplicationContext(), "takingchecktable.db", null, 1);
                        indb.insert(indbadter.insertTakingcheckQeury());
                        indb.close();

                        System.out.println("MainActivity get intent");
                        for (Treatment arr : treatments)
                            System.out.println(arr.getInfo());
                    }
                    break;
                case AC_EDIT:
                    if(data != null || resultCode == RESULT_OK)
                    {
                        userInfo = (UserInfo)data.getSerializableExtra("userinfo");
                        userInfo.show();
                        age = getAgeFromBirthday(userInfo.getBirth());
                        TextView tx=(TextView)CustomDialog.findViewById(R.id.textNameAge);
                        tx.setText(userInfo.getName()+"("+age+"세)");
                        System.out.println("유저 정보 변경");
                    }
                    else
                    {
                        System.out.println("유저 정보 변경 실패");
                    }
                    break;
                case AC_PRES:
                    if(resultCode == RESULT_OK || data != null) {
                        try {

                            getUserTreatmentInfoFromInnerDB();
                            System.out.println("처방전 변경");
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        System.out.println("처방전 정보 받기 실패");
                    }
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }

    protected void getUserTreatmentInfoFromInnerDB(){
        System.out.println("==========================================================");
        System.out.println("getUserTreatmentInfoFromInnerDB");
        try {
            indb = new InnerDataBase(getApplicationContext(), "certifytable.db", null, 1);
            if(!indb.isEmpty("select * from certifytable where activityflag = 1;")&& indb.getResult("select * from certifytable where activityflag = 1;")!="") {
                //디비에 있는 정보 배열에 담기.HG
                certify = new ArrayList<ArrayList<String>>();
                getDataInfoFromInnerDB("certifytable", "select * from certifytable where activityflag = 1 and dosestate = 'dosing';", certify);
                System.out.println("certify : "+certify.get(0).get(0).toString());

                hospital = new ArrayList<ArrayList<String>>();
                getDataInfoFromInnerDB("hospitaltable", "select * from hospitaltable;", hospital);

                medicine = new ArrayList<ArrayList<ArrayList<String>>>();
                for (int i = 0; i < certify.size(); i++) {
                    mediitems = new ArrayList<ArrayList<String>>();
                    getDataInfoFromInnerDB("medicinetable", "select * from medicinetable where certifyid = " + certify.get(i).get(1) + ";", mediitems);
                    medicine.add(mediitems);
                    System.out.println(medicine);
                }

                takingcheck = new ArrayList<ArrayList<ArrayList<String>>>();
                for (int i = 0; i < certify.size(); i++) {
                    takecheckitems = new ArrayList<ArrayList<String>>();
                    getDataInfoFromInnerDB("takingchecktable", "select * from takingchecktable where certifyid = " + certify.get(i).get(1) + ";", takecheckitems);
                    takingcheck.add(takecheckitems);
                    System.out.println(takingcheck);
                }
                System.out.println(certify);
                System.out.println(hospital);
                System.out.println(medicine);
                System.out.println(takingcheck);

                if (treatments != null) treatments.clear();

                int i = 0;
                int k = 0;

                for (ArrayList<String> first : certify) {
                    System.out.println("asdfasdfasdf");
                    mediitems = new ArrayList<ArrayList<String>>();
                    boolean[][] takeflags = new boolean[3][3];

                    System.out.println("=====");
                    for (int j = 0; j < medicine.get(k).size(); j++) {
                        mediitem = new ArrayList<String>();
                        mediitem.add(medicine.get(k).get(j).get(1));
                        mediitem.add(medicine.get(k).get(j).get(2));
                        mediitem.add(medicine.get(k).get(j).get(3));
                        mediitem.add(medicine.get(k).get(j).get(4));
                        mediitems.add(mediitem);
                    }
                    for(int j = 0; j < 3; j++)
                    {
                        takeflags[j][0] = ((takingcheck.get(k).get(j).get(3).equals("1")) ? true : false);
                        takeflags[j][1] = ((takingcheck.get(k).get(j).get(4).equals("1")) ? true : false);
                        takeflags[j][2] = ((takingcheck.get(k).get(j).get(5).equals("1")) ? true : false);
                    }

                    Treatment titem = new Treatment(first.get(1), first.get(2), hospital.get(i).get(2), first.get(6),
                            Integer.parseInt(first.get(4)), hospital.get(i).get(3), medicine.size(), mediitems, Integer.parseInt(first.get(5)), takeflags);
                    System.out.println("QQ : "+takeflags);
                    System.out.println("내부디비에서 가져온 데이터로 만든 treatment" + (i + 1));
                    System.out.println(titem.getInfo());
                    if(treatments.size()>0)
                    {
                        for (i=0; i<treatments.size(); i++)
                        {
                            if((titem.getFirstDate() > treatments.get(i).getFirstDate()&&titem.getLastDate() < treatments.get(i).getLastDate())||
                                    (titem.getFirstDate() < treatments.get(i).getLastDate()&&titem.getFirstDate() > treatments.get(i).getFirstDate())||
                                    (titem.getLastDate() > treatments.get(i).getFirstDate())&&titem.getFirstDate() < treatments.get(i).getLastDate())
                            {
                                titem.setIndex(treatments.get(i).getIndex()+1);
                            }
                        }
                    }
                    treatments.add(titem);
                    initControl();
                    loadListItems(items);
                    i++;
                    k++;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("==========================================================");
    }
    //디비에 있는 정보를 배열에 담아 주는 함수 HG
    protected void getDataInfoFromInnerDB(String dbname, String query, ArrayList<ArrayList<String>> array) throws Exception{
        String[] fstStrs = null;
        array.clear();

        indb = new InnerDataBase(getApplicationContext(), dbname+".db", null, 1);
        String temp =indb.getResult(query);
        indb.close();
        System.out.println("split before : "+temp);
        fstStrs = temp.split("#");
        for(String input : fstStrs){
            ArrayList<String> arr = new ArrayList<String>(Arrays.asList(input.split("&")));
            array.add(arr);
        }
        System.out.println("array : "+array);
    }
    void setParam(int left, int middle, int right) { //종재 8/31 버튼배율
        LinearLayout.LayoutParams lparam = (LinearLayout.LayoutParams)tb1.getLayoutParams();
        lparam.weight = left;
        tb1.setLayoutParams(lparam);

        LinearLayout.LayoutParams mparam = (LinearLayout.LayoutParams)tb2.getLayoutParams();
        mparam.weight = middle;
        tb2.setLayoutParams(mparam);

        LinearLayout.LayoutParams rparam = (LinearLayout.LayoutParams)tb3.getLayoutParams();
        rparam.weight = right;
        tb3.setLayoutParams(rparam);
    }
    protected void initialize() {
        initData();
        initView();
        initControl();
    }
    public void initData() {
    }
    public void initView() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        curtitle = (TextView)findViewById(R.id.CurMouth);
    }
    public void initControl() {
        fadapter = new AdapterFrgCalendar(getSupportFragmentManager());
        if(treatments != null)
        {
            for (Treatment treat : treatments)
            {
                System.out.println("calDay : "+day);
                if(day >= 0 && day <= treat.takedays) {
                    fadapter.addContent(treat);
                }
                else
                {
                    break;
                }
            }
        }
        fadapter.setHandler(new MCalendarEventHandler() {
            @Override
            public void handler(long millis, ArrayList<DataObject> contents) {
                if (System.currentTimeMillis() > btnpresstime + 1000) {
                    btnpresstime = System.currentTimeMillis();
                    Toast.makeText(getApplicationContext(), "한번 더 터치하면 오늘 날짜로 돌아갑니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (System.currentTimeMillis() <= btnpresstime + 1000) {
                    viewPager.setCurrentItem(COUNT_PAGE);
                    System.out.println("touchtouch");
                }
                Log.d("onClick", "aaa - " + millis + " " + contents.size());
            }
        });
        viewPager.setAdapter(fadapter);
        fadapter.setOnFragmentListener(this);
        fadapter.setNumOfMonth(COUNT_PAGE);
        viewPager.setCurrentItem(COUNT_PAGE);
        String title = fadapter.getMonthDisplayed(COUNT_PAGE);
        curtitle.setText(title);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String title = fadapter.getMonthDisplayed(position);
                curtitle.setText(title);

                if (position == 0) {
                    fadapter.addPrev();
                    viewPager.setCurrentItem(COUNT_PAGE, false);
                } else if (position == fadapter.getCount() - 1) {
                    fadapter.addNext();
                    viewPager.setCurrentItem(fadapter.getCount() - (COUNT_PAGE + 1), false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void onFragmentListener(View view) {
        resizeHeight(view);
    }
    public void resizeHeight(View mRootView) {

        if (mRootView.getHeight() < 1) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
        if (layoutParams.height <= 0) {
            layoutParams.height = mRootView.getHeight();
            viewPager.setLayoutParams(layoutParams);
            return;
        }
        ValueAnimator anim = ValueAnimator.ofInt(viewPager.getLayoutParams().height, mRootView.getHeight());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
                layoutParams.height = val;
                viewPager.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(200);
        anim.start();
    }
}
