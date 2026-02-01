package com.example.parkhg.medisilver.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.parkhg.medisilver.R;
import com.example.parkhg.medisilver.database.InDBAdapter;
import com.example.parkhg.medisilver.database.InnerDataBase;

/**
 * Created by ParkHG on 2017-06-19.
 */

public class LoadingActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        startLoading();
    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isCheckAppFirstExe())
                {
                    Intent intent = new Intent(getBaseContext(), JoinActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.putExtra("checkuserflag", 1);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
    private boolean isCheckAppFirstExe()
    {
        InnerDataBase indb = new InnerDataBase(getApplicationContext(), "userinfo.db", null, 1);
        String check = indb.getResult("select * from userinfo;");

        return check.isEmpty();
    }
}
