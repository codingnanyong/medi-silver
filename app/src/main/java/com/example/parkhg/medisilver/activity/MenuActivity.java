package com.example.parkhg.medisilver.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkhg.medisilver.R;
import com.example.parkhg.medisilver.handmadeclass.UserInfo;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.app.Activity.RESULT_OK;

public class MenuActivity extends Dialog {

    private TextView TextNameAge;
    private TextView TextTitle;
    private ImageButton img;
    private Button ButtonHealth;
    private Button ButtonPreScription;
    private Button ButtonSetting;
    private String sNameAge;
    private View v;

    private View.OnClickListener mUpClickListener;
    private View.OnClickListener mCenterClickListener;
    private View.OnClickListener mDownClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_menu);

        TextNameAge = (TextView)findViewById(R.id.textNameAge);
        TextTitle = (TextView)findViewById(R.id.texttitle);
        img = (ImageButton)findViewById(R.id.buttonmenu);
        ButtonHealth = (Button)findViewById(R.id.btnhealthinfo);
        ButtonPreScription = (Button)findViewById(R.id.btnprescription);
        ButtonSetting = (Button)findViewById(R.id.btnsetting);

        TextNameAge.setText(sNameAge);
        TextTitle.setText("메뉴");

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ButtonHealth.setOnClickListener(mUpClickListener);
        ButtonPreScription.setOnClickListener(mCenterClickListener);
        ButtonSetting.setOnClickListener(mDownClickListener);
    }
    public MenuActivity (Context context, String NameAge, View.OnClickListener upListener,
                         View.OnClickListener centerListener, View.OnClickListener downListener)
    {
        super(context,android.R.style.Theme_Translucent_NoTitleBar);
        this.sNameAge = NameAge;
        this.mUpClickListener = upListener;
        this.mCenterClickListener = centerListener;
        this.mDownClickListener = downListener;
    }
}
