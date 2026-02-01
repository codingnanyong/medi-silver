package com.example.parkhg.medisilver.handmadeclass;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.example.parkhg.medisilver.activity.MainActivity;

import java.util.ArrayList;

/**
 * Created by w on 2017-09-04.
 */

public class SMS
{
    private Activity activity;
    public SMS(Activity activity1)
    {
        activity = activity1;
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.SEND_SMS},1);
    }
    public void SendSMS(String phonenumber, String message) {

        SmsManager smsManager = SmsManager.getDefault();

        String sendTo = phonenumber;

        ArrayList<String> partMessage = smsManager.divideMessage(message);

        smsManager.sendMultipartTextMessage(sendTo, null, partMessage, null, null);

        Toast.makeText(activity, "전송되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
