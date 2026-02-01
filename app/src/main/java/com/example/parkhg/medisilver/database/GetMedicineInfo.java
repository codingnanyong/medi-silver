package com.example.parkhg.medisilver.database;

import android.os.AsyncTask;
import android.util.Log;
import android.util.MalformedJsonException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ParkHG on 2017-06-19.
 *
 * 성훈이형이 만들어 놓은 클래스. 문의는 형에게.
 */

public class GetMedicineInfo extends AsyncTask<String, Void, String> {
    String sendMsg, receiveMsg;
    @Override
    protected String doInBackground(String[] strings) {
        try {
            String str;
            URL url = new URL("http://203.241.246.181/medicine_server/data.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            sendMsg = "medicine_code="+strings[0];
            osw.write(sendMsg);
            osw.flush();
            if(conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
            } else {
                Log.i("통신 결과", conn.getResponseCode()+"에러");

            }
        } catch (MalformedJsonException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receiveMsg;
    }
}
