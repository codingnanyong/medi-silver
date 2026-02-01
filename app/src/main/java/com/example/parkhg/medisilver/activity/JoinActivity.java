package com.example.parkhg.medisilver.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.parkhg.medisilver.R;
import com.example.parkhg.medisilver.database.InDBAdapter;
import com.example.parkhg.medisilver.database.InnerDataBase;
import com.example.parkhg.medisilver.handmadeclass.UserInfo;

public class JoinActivity extends AppCompatActivity {
    private Spinner sYear;
    private TextView phNumber;
    private EditText eName,eNumber,eNumber2,eCm,eKg,eLiver;
    private ToggleButton toYes,toNo,toHigh,toNormal,toLow;
    private ArrayAdapter<CharSequence> adspin,adspin2;
    private String strYear,strNumber;
    private char aChar;
    private UserInfo userInfo;
    private boolean isKorean(String str){
        return str.matches("^[가-힣]*$");
    }//17.8.31 태현(유저 이름 한글 유효성)
    //디비관련 변수 선언 HG
    InnerDataBase indb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        eName = (EditText)findViewById(R.id.editName);
        eNumber = (EditText)findViewById(R.id.editNumber);
        eNumber2 = (EditText)findViewById(R.id.editNumber2);
        eCm = (EditText)findViewById(R.id.editcm);
        eKg = (EditText)findViewById(R.id.editkg);
        eLiver = (EditText)findViewById(R.id.editliver);
        sYear = (Spinner)findViewById(R.id.spinnerYear);
        phNumber = (TextView) findViewById(R.id.phNumber);

        toYes = (ToggleButton)findViewById(R.id.toggleYes);
        toNo = (ToggleButton)findViewById(R.id.toggleNo);
        toHigh = (ToggleButton)findViewById(R.id.toggleHigh);
        toNormal = (ToggleButton)findViewById(R.id.toggleNormal);
        toLow = (ToggleButton)findViewById(R.id.togglelow);

        toNo.setChecked(true); //종재 수정 8/31 토글초기버튼 당뇨없음
        toNormal.setChecked(true); //종재 수정 8/31 토글초기버튼 혈압정상

        toYes.setBackgroundColor(Color.LTGRAY); //종재 수정 8/31 토글버튼색
        toNo.setBackgroundColor(Color.GREEN); //종재 수정 8/31 토글버튼색
        toHigh.setBackgroundColor(Color.LTGRAY); //종재 수정 8/31 토글버튼색
        toNormal.setBackgroundColor(Color.GREEN); //종재 수정 8/31 토글버튼색
        toLow.setBackgroundColor(Color.LTGRAY); //종재 수정 8/31 토글버튼색

        adspin = ArrayAdapter.createFromResource(this,R.array.year,android.R.layout.simple_spinner_item);
        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sYear.setAdapter(adspin);


        aChar = 'n';

        indb = null;


        sYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strYear = adspin.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        imageButton.setVisibility(View.INVISIBLE);


        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        return true;
    }
    public void mOnClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnsave:
                if(eName.getText().toString().isEmpty() || eNumber.getText().toString().isEmpty() || eNumber2.getText().toString().isEmpty() || eCm.getText().toString().isEmpty()
                        ||eKg.getText().toString().isEmpty() || eLiver.getText().toString().isEmpty())
                {
                    Toast.makeText(JoinActivity.this,"빈칸을 전부 채워주세요", Toast.LENGTH_SHORT).show();
                }
                else if(eName.getText().toString().length()<=1)
                {
                    Toast.makeText(JoinActivity.this,"이름은 2글자 이상으로 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(Double.parseDouble(eCm.getText().toString())<100||Double.parseDouble(eCm.getText().toString())>200)
                {
                    Toast.makeText(JoinActivity.this,"키를 100이상 200이하로 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(Double.parseDouble(eKg.getText().toString())<40||Double.parseDouble(eKg.getText().toString())>100)
                {
                    Toast.makeText(JoinActivity.this,"몸무게는 40이상 100이하로 입력해 주세요..", Toast.LENGTH_SHORT).show();
                }
                else if(!isKorean(eName.getText().toString())){
                    Toast.makeText(JoinActivity.this,"정확한 이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } //17.8.31 태현(유저 이름 한글 유효성)

                else if(eNumber.getText().toString().length() != 4 || eNumber2.getText().toString().length() !=4){
                    Toast.makeText(JoinActivity.this,"휴대전화 번호를 정확히 입력하세요",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new AlertDialog.Builder(JoinActivity.this)
                            .setTitle("알림")
                            .setMessage("저장하시겠습니까?\n이 정보는 [메뉴] - [건강정보관리]에서\n수정하실 수 있습니다.")
                            .setPositiveButton("저장",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getBaseContext(),MainActivity.class);
                                    userInfo = new UserInfo(eName.getText().toString(),strYear,phNumber.getText().toString()+" "+eNumber.getText().toString()+" "+eNumber2.getText().toString()
                                            ,Double.parseDouble(eCm.getText().toString()), Double.parseDouble(eKg.getText().toString()),
                                            Double.parseDouble(eLiver.getText().toString()),toYes.isChecked(),aChar,0);

                                    /*
                                    유저 정보를 디비에 저장. 일단은 유효성검사 없이 저장. 수정필요. HG
                                    */
                                    indb = new InnerDataBase(getApplicationContext(), "userinfo.db", null, 1);
                                    indb.insert(InDBAdapter.insertUserinfoQuery(userInfo));
                                    indb.close();
                                    //

                                    //디비 저장 확인 HG
                                    System.out.println("JoinActivity :: mOnClick - user db value check!");
                                    indb = new InnerDataBase(getApplicationContext(), "userinfo.db", null, 1);
                                    System.out.println(indb.getResult("select * from userinfo"));
                                    System.out.println("JoinActivity :: mOnClick - user db value check DONE!!");

                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("userinfo", userInfo);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("취소",null)
                            .show();
                }
                break;
            //17.9.2 태현(010만 입력 확인)
            case R.id.phNumber:
                new AlertDialog.Builder(JoinActivity.this)
                        .setTitle("알림")
                        .setMessage("'010' 이외의 번호는 지원하지 않습니다.")
                        .setPositiveButton("확인",null)
                        .show();
            case R.id.toggleYes:
                if(toYes.isChecked())
                {
                    toYes.setBackgroundColor(Color.GREEN); //종재 수정 8/31 토글버튼색
                    toNo.setBackgroundColor(Color.LTGRAY); //종재 수정 8/31 토글버튼색
                    toNo.setChecked(false);
                }
                break;
            case R.id.toggleNo:
                if(toNo.isChecked())
                {
                    toYes.setBackgroundColor(Color.LTGRAY); //종재 수정 8/31 토글버튼색
                    toNo.setBackgroundColor(Color.GREEN);   //종재 수정 8/31 토글버튼색
                    toYes.setChecked(false);
                }
                break;
            case R.id.toggleHigh:
                if(toHigh.isChecked())
                {
                    aChar = 'h';
                    toHigh.setBackgroundColor(Color.GREEN); //종재 수정 8/31 토글버튼색
                    toNormal.setBackgroundColor(Color.LTGRAY); //종재 수정 8/31 토글버튼색
                    toLow.setBackgroundColor(Color.LTGRAY); //종재 수정 8/31 토글버튼색
                    toNormal.setChecked(false);
                    toLow.setChecked(false);
                }
                break;
            case R.id.toggleNormal:
                if(toNormal.isChecked())
                {
                    aChar = 'n';
                    toHigh.setBackgroundColor(Color.LTGRAY); //종재 수정 8/31 토글버튼색
                    toNormal.setBackgroundColor(Color.GREEN); //종재 수정 8/31 토글버튼색
                    toLow.setBackgroundColor(Color.LTGRAY); //종재 수정 8/31 토글버튼색
                    toHigh.setChecked(false);
                    toLow.setChecked(false);
                }
                break;
            case R.id.togglelow:
                if(toLow.isChecked())
                {
                    aChar = 'l';
                    toHigh.setBackgroundColor(Color.LTGRAY); //종재 수정 8/31 토글버튼색
                    toNormal.setBackgroundColor(Color.LTGRAY); //종재 수정 8/31 토글버튼색
                    toLow.setBackgroundColor(Color.GREEN); //종재 수정 8/31 토글버튼색
                    toNormal.setChecked(false);
                    toHigh.setChecked(false);
                }
                break;
        }
    }
}
